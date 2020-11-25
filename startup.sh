#!/bin/sh

./gradlew clean build

# Build the docker image (in the current directory) and tag it "reactor-experiments".
# Run 'docker images' to see the built image.
docker build -t reactor-experiments .

# Reference: https://docs.docker.com/engine/reference/run/
# Starts up a docker container using the "reactor experiments" image.
# -it - TODO: Why is this needed...it's how I learned and have always done it.
# -p  - Maps a local port to the container port. NOTE: the left of the ':' could be any open port on your system.
# -v  - Maps the my-config/ directory to the containers /srv/gateway/config. NOTE: The directory on the host machine must be a complete (non-relative) path.
# -e  - Sets an environment variable in the container.
# --name - Sets a name on the container, so it can be referenced via name instead of the random container ID.
# --rm - Removes the container on shutdown. Without it a 'docker ps -a', you can still see that the container exists.
# finally tell docker the name of the image, 'reactor-experiments' in our case.
docker run -it \
          -p 8080:8080 \
          -p 5005:5005 \
          -v "${PWD}/my-config/:/srv/gateway/config/" \
          -e ENABLE_DEBUG=true \
          --name reactor-experiments \
          --rm \
          reactor-experiments