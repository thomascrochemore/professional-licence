DROP TABLE IF EXISTS jump_all_rt;
/**
 * "jump" table to use with scoring calculation
 */
CREATE TABLE IF NOT EXISTS jump_all_rt(
  id BIGINT(20) NOT NULL,
  inserted_at DATETIME NULL,
  is_forecast BIT NULL,
  data_api_id BIGINT(20) NULL,
  data_delay_id BIGINT(20) NULL,
  data_location_id BIGINT(20) NULL,

  PRIMARY KEY (id),
  CONSTRAINT fk_jmprt_loc FOREIGN KEY (data_location_id) REFERENCES LOCATION(id),
  CONSTRAINT fk_jmprt_del FOREIGN KEY (data_delay_id) REFERENCES DELAY(id),
  CONSTRAINT fk_jmprt_api FOREIGN KEY (data_api_id) REFERENCES LOCATION(id)

) ENGINE=MyISAM;

INSERT INTO jump_all_rt
  SELECT DISTINCT WEATHERDATA.*
  FROM WEATHERDATA
    LEFT JOIN API ON WEATHERDATA.data_api_id = API.id
    LEFT JOIN DELAY ON WEATHERDATA.data_delay_id = DELAY.id
    LEFT JOIN LOCATION ON WEATHERDATA.data_location_id = LOCATION.id
    LEFT JOIN DATAVALUE ON WEATHERDATA.id = DATAVALUE.weather_data_id
    LEFT JOIN PROPERTY ON DATAVALUE.property_id = PROPERTY.id
  WHERE WEATHERDATA.is_forecast = 0;