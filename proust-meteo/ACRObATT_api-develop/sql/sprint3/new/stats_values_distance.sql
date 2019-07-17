DROP TABLE IF EXISTS stats_values_distance;

/*
 * on doit insérer les stats toutes les 6h au compute
 * + mettre à jour pour le backup
 */
CREATE TABLE IF NOT EXISTS stats_values_distance(
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  date_req DATETIME NOT NULL,
  api VARCHAR(255) NOT NULL,
  prop VARCHAR(255) NOT NULL,
  location VARCHAR(255) NOT NULL,
  delay BIGINT(20) NOT NULL,
  value_fcst FLOAT NOT NULL,
  value_rt FLOAT NOT NULL,
  distance FLOAT NOT NULL,

  PRIMARY KEY (id)/*,
  CONSTRAINT fk_vm_dist_api_id FOREIGN KEY (api_id) REFERENCES api(id),
  CONSTRAINT fk_vm_dist_city_id FOREIGN KEY (location_id) REFERENCES location(id),
  CONSTRAINT fk_vm_dist_prop_id FOREIGN KEY (prop_id) REFERENCES property(id)*/
) ENGINE=MyISAM;
