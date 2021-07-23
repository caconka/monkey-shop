FROM openjdk:11-jdk-slim

ENV VERTICLE_FILE *-fat.jar

ENV VERTICLE_HOME /usr/verticles

COPY build/libs/$VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]
