/*
 * usage : select type, property, datavalue
 * from stats_values_forecast
 * where city=? and delay=? and and property = ? inserted_at between(?,?)
 */
CREATE OR REPLACE VIEW stats_values_forecast as
  (select
     if(proust_db.DELAY.delay_value = 0,truncate_date(proust_db.WEATHERDATA.inserted_at), adddate(truncate_date(proust_db.WEATHERDATA.inserted_at),INTERVAL proust_db.DELAY.delay_value HOUR)) as inserted_at,
     proust_db.API.api_name AS type,
     proust_db.PROPERTY.property_unit AS unit,
     proust_db.DATAVALUE.value_dec AS values_forecast,
     proust_db.LOCATION.city AS city,
     proust_db.DELAY.delay_value as delay,
     proust_db.PROPERTY.property_name as property
   from proust_db.WEATHERDATA
     left join proust_db.DELAY on proust_db.WEATHERDATA.data_delay_id = proust_db.DELAY.id
     left join proust_db.API on proust_db.WEATHERDATA.data_api_id = proust_db.API.id
     left join proust_db.LOCATION on proust_db.WEATHERDATA.data_location_id = proust_db.LOCATION.id
     left join proust_db.DATAVALUE on proust_db.WEATHERDATA.id = proust_db.DATAVALUE.weather_data_id
     left join proust_db.PROPERTY on proust_db.DATAVALUE.property_id = proust_db.PROPERTY.id
   order by proust_db.WEATHERDATA.inserted_at, proust_db.WEATHERDATA.data_api_id, proust_db.WEATHERDATA.data_location_id,proust_db.DELAY.delay_value
  )
