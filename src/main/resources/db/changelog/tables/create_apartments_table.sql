CREATE TABLE apartments
(
    apartment_id     BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Unique apartment id',
    apartment_number INT          NOT NULL COMMENT 'Apartment number',
    apartment_name   VARCHAR(255) NOT NULL COMMENT 'Apartment name'
);
