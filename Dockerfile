FROM gradle:8.8-jdk-21-and-22-alpine

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app
