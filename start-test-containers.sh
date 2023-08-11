#!/bin/sh

docker-compose -f test-containers-docker-compose.yml up -d

# Setup Kafka:
sleep 5s
# Create topics:
docker exec booking-service-sample_kafka_1 /usr/bin/kafka-topics --create --topic booking_history --partitions 1 --replication-factor 1 --if-not-exists --bootstrap-server kafka:9092
