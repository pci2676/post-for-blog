FROM openjdk:8-alpine
ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILES_ACTIVE=prod
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
RUN echo ">> active profiles : $SPRING_PROFILES_ACTIVE"
COPY ${JAR_FILE} app/app.jar
ENTRYPOINT ["nohup","java","-jar","app/app.jar","&"]