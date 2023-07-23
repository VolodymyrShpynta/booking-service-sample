CREATE TABLE apartments
(
    apartment_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Unique apartment id',
    apartment_number INT UNSIGNED    NOT NULL COMMENT 'Apartment number',
    apartment_name   VARCHAR(255)    NOT NULL COMMENT 'Apartment name',

    PRIMARY KEY (apartment_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8;
