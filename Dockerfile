FROM openjdk:11.0.9-jdk-slim

COPY docker/run.sh /srv/gateway/run.sh
COPY build/libs/reactor-experiments-*.jar /srv/gateway/application.jar

# Add group and user. Set the ownership of the gateway.
RUN groupadd gateway-group && \
    useradd -G gateway-group gateway-user && \
    chown gateway-user:gateway-group -R /srv/gateway

# Now switch to the new user. ENTRYPOINT will be executed by this user rather than root.
USER gateway-user

# This is mainly for documentation, to let users of the image know what port(s) the app will be listening on.
# https://docs.docker.com/engine/reference/builder/#expose
EXPOSE 8080 5005

# The script that the container will run on startup. This script is responsible for executing our java application.
ENTRYPOINT ["/srv/gateway/run.sh"]
