# -------- Stage 1: build (maven + java) --------
FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /app

COPY pom.xml .

RUN mvn -q -e -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package

# -------- Stage 2: runtime (wildfly) --------
FROM jboss/wildfly:8.2.1.Final

ENV WILDFLY_HOME=/opt/jboss/wildfly

COPY --from=build /app/target/*.war ${WILDFLY_HOME}/standalone/deployments/mini-mundo.war

USER root

RUN mkdir -p ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main

COPY docker/wildfly/modules/org/postgresql/main/module.xml \
  ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main/module.xml

COPY docker/wildfly/modules/org/postgresql/main/postgresql-9.2-1003-jdbc3.jar \
  ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main/postgresql-9.2-1003-jdbc3.jar

COPY docker/wildfly/init.cli ${WILDFLY_HOME}/init.cli
COPY docker/wildfly/standalone.xml ${WILDFLY_HOME}/standalone/configuration/standalone.xml
COPY docker/wildfly/entrypoint.sh ${WILDFLY_HOME}/entrypoint.sh

# Permissions
RUN chmod +x ${WILDFLY_HOME}/entrypoint.sh \
 && chown -R jboss:jboss \
      ${WILDFLY_HOME}/entrypoint.sh \
      ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main

USER jboss

EXPOSE 8080 9990

ENV DB_HOST=db \
    DB_PORT=5432 \
    DB_NAME=mini_mundo \
    DB_USER=mini_mundo \
    DB_PASSWORD=mini_mundo \
    JAVA_OPTS="-Djboss.node.name=mini-mundo-node -Dcom.arjuna.ats.arjuna.xa.nodeIdentifier=mini-mundo-tx"

CMD ["/bin/bash", "/opt/jboss/wildfly/entrypoint.sh"]
