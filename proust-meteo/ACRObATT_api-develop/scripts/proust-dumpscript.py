#!/usr/bin/env python2.7
# Crontab (user s51): 0 0,6,12,18 * * *

from pymongo import MongoClient
from pymongo import errors
import datetime
import requests
import json
import logging
import sys

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def keySwitch(keys, lastIndex):
  if lastIndex < len(keys)-1: lastIndex += 1
  else: lastIndex = 0
  return lastIndex

def build_json(obj, city, name, delay):
  storedDate = str(datetime.datetime.now().isoformat())
  new = '{ "api":"'+ name +'", "city": "'+ city +'", "storedDate": "'+ storedDate + '", "delay": '+str(delay)+', "data": '+obj+' }'
  return new
  
# Dirty URL checking (Wunderground)
def urlstrategy(value, keyparam, key):
  if keyparam != "-1" and key is not None:
    url = value + '&' + keyparam + '=' + key
  else:
    url = value
  logger.debug('Using URL: '+url)
  return url

def main():
  logger.info('Starting script...')
  try:
    # Create client
    logger.info('Connecting to database')
    client = MongoClient(host='localhost',
                       port=27017,
                       username='prstdump',
                       password='prstpwd',
                       authSource='proust-datadump',
                       authMechanism='SCRAM-SHA-1')

    # Get database and collections
    db = client['proust-datadump']
    dumpCol = db.dumpcol
    confscript = db.configScript
    logger.info('Fetched collections ['+dumpCol.name+','+confscript.name+'] from '+db.name)

    # Get API config
    logger.info('Fetching API data...')
    for config in confscript.find():
      if config["active"] == True:
        logger.info('Fetching for: '+config["nameAPI"])

        try:
          keyparam = str(config["keyparam"])
          keys = config["keys"]
          last = config["lastUsedKey"]
          lastkey = keys[last]
        except:
          logger.warn('Could not fetch API key info. Using an unusual URL ?')

        # Loop for every delay
        for key,value in config["url-realTime"].items():
          if value is not None and value != "":
            res = requests.get(urlstrategy(value, keyparam, lastkey))
            tosave = build_json(res.text, key, config["nameAPI"], 0)
            dumpcol.insert_one(json.loads(tosave))
            # logger.info('Saved'+tosave)
            
        for key,value in config["url-6h"].items():
          if value is not None and value != "":
            res = requests.get(urlstrategy(value, keyparam, lastkey))
            tosave = build_json(res.text, key, config["nameAPI"], 6)
            dumpCol.insert_one(json.loads(tosave))
            # logger.info('Saved '+tosave)
			
		for key,value in config["url-12h"].items():
          if value is not None and value != "":
            res = requests.get(urlstrategy(value, keyparam, lastkey))
            tosave = build_json(res.text, key, config["nameAPI"], 12)
            dumpCol.insert_one(json.loads(tosave))
            # logger.info('Saved '+tosave)

        for key,value in config["url-1d"].items():
          if value is not None and value != "":
            res = requests.get(urlstrategy(value, keyparam, lastkey))
            tosave = build_json(res.text, key, config["nameAPI"], 24)
            dumpCol.insert_one(json.loads(tosave))
            # logger.info('Saved '+tosave)

        for key,value in config["url-3d"].items():
          if value is not None and value != "":
            res = requests.get(urlstrategy(value, keyparam, lastkey))
            tosave = build_json(res.text, key, config["nameAPI"], 72)
            dumpCol.insert_one(json.loads(tosave))
            # logger.info('Saved '+tosave)

        # Switch the keys
        logger.info('Switching keys...')
        nextid = keySwitch(keys, last)
        confscript.update_one(
          { "_id": config["_id"] },
          { "$set": { "lastUsedKey": nextid }}
        )
    logger.info('Done. Exiting...')

  except KeyboardInterrupt:
    logger.debug('Received interrupt signal. Exiting...')
  except errors.ConnectionFailure:
    logger.exception('Failed to connect to the database')
  except errors.PyMongoError:
    logger.exception('Unexpected PyMongo error')
  except Exception as e:
    logger.exception('Unexpected exception')


if __name__ == "__main__":
    main()