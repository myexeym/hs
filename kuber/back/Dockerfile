FROM clojure:temurin-17-lein-alpine

RUN mkdir -p /app
WORKDIR /app
COPY . /app
RUN lein deps

ENV PG_HOST=hs-svc-pg.default.svc.cluster.local

ENTRYPOINT "lein" "dev"