DROP FUNCTION IF EXISTS truncate_date;

CREATE FUNCTION truncate_date(d DATETIME)
RETURNS DATETIME
  RETURN  date_format(d, '%Y-%m-%d %H:00:00');