FROM maven:3.6.3-jdk-8 AS build
VOLUME /tmp
RUN mkdir -p /opt/app
COPY ./ /opt/app
RUN mvn -f /opt/app/pom.xml -Dmaven.test.skip=true clean package -B

FROM adoptopenjdk/openjdk8
RUN mkdir -p /opt/app
COPY --from=build /opt/app/target/bieber-tweets.jar /opt/app

ENTRYPOINT java -Xmx4096m -jar -Dspring.profiles.active=local /opt/app/bieber-tweets.jar