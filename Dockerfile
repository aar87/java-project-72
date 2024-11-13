FROM azul/zulu-openjdk:23.0.1-jdk

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app
