#!/usr/bin/env sh

# Exit on failures
set -o errexit

# Build the spring cloud gateway
./gradlew clean build

# Build the Docker image.
docker-compose build

# Run the Docker image
docker-compose up
