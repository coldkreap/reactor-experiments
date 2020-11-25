#!/bin/sh

# Setting this causes the script to exit if a command fails (which will prevent the docker container from starting).
set -o errexit

if [ "${ENABLE_DEBUG}" = "true" ]; then
  # FUN FACT: To debug startup, you can set 'suspend=y' (in the line below).
  #           This means the spring boot app will not start up until a debugger has been attached.
  # WARN: The 'address' part is different when using java 11 vs java 8.
  export JAVA_OPTS="${JAVA_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
fi

exec java ${JAVA_OPTS} \
          -jar /srv/gateway/application.jar \
          --spring.config.additional-location=file:/srv/gateway/config/ # This allows for adding config without packaging it into the JAR.
