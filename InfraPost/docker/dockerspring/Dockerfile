FROM openjdk:8
RUN apt-get update
RUN apt-get install -y npm
RUN npm install -g n
RUN n 12.18.3
ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
COPY ${JAR_FILE} app/app.jar
ENTRYPOINT ["nohup","java","-jar","app/app.jar","&"]