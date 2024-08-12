FROM  openjdk:17-jdk-slim

WORKDIR /app

COPY ./target/linknoteResource-0.0.1-SNAPSHOT.jar .
COPY application.properties .

#ENV DB_HOST=${DB_HOST}
#ENV DB_PORT=${DB_PORT}
#ENV DB_USER=${DB_USER}
#ENV MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
#ENV DB_NAME=${DB_NAME}
#ENV JWT_SECRET_KEY=${JWT_SECRET_KEY}
#ENV AUTHENTICATION_HOST=${AUTHENTICATION_HOST}

CMD [ "nohup", "java", "-jar", "/app/linknoteResource-0.0.1-SNAPSHOT.jar", "&" ]

EXPOSE 8081