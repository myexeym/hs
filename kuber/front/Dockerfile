FROM clojure:temurin-17-lein-alpine

ENV HS_API_URL=http://localhost:3001/

RUN mkdir -p /app
WORKDIR /app
COPY . /app
RUN lein run -m shadow.cljs.devtools.cli release app

ENTRYPOINT "lein" "run" "-m" "shadow.cljs.devtools.cli" "server"