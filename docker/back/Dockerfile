FROM clojure:temurin-17-lein-alpine

RUN mkdir -p /app
WORKDIR /app
COPY . /app

ENTRYPOINT "lein" "dev"