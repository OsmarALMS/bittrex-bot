FROM openjdk:8

VOLUME /tmp

ADD build/libs/bittex-bot.jar bittex-bot.jar

RUN sh -c 'touch /bittex-bot.jar'
ENTRYPOINT [ "java", "-jar", "/bittex-bot.jar" ]

EXPOSE 8080