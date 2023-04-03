FROM clojure:temurin-17-lein-alpine

RUN mkdir -p /app
WORKDIR /app
COPY . /app
RUN lein run -m shadow.cljs.devtools.cli release app

ENTRYPOINT "lein" "run" "-m" "shadow.cljs.devtools.cli" "server"