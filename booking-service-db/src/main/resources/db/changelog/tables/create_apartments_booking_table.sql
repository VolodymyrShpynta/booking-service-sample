CREATE TABLE apartments_booking
(
    booking_id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Unique apartment booking id',
    apartment_id BIGINT UNSIGNED NOT NULL COMMENT 'Unique apartment id',
    user_id      VARCHAR(128)    NOT NULL COMMENT 'Unique user id',
    start_date   TIMESTAMP(3)    NOT NULL COMMENT 'Indicates from which day the apartment will be booked',
    end_date     TIMESTAMP(3)    NOT NULL COMMENT 'Indicates till which day the apartment will be booked',

    PRIMARY KEY (booking_id),
    CONSTRAINT fk_apartments_booking__apartments FOREIGN KEY (apartment_id) REFERENCES apartments (apartment_id),
    KEY idx_apartment__start_date_end_date (apartment_id, start_date, end_date)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  DEFAULT CHARSET = utf8;
