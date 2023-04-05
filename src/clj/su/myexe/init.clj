(ns su.myexe.init
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]
            [integrant.core :as ig]
            [migratus.core :as migratus]
            [ring.adapter.jetty :as jetty]
            [su.myexe.handler :as handler])
  (:gen-class))

(def config
  (-> "ig-config.edn"
      io/resource
      slurp
      ig/read-string))

(defmethod ig/init-key :adapter/jetty
  [_ {:keys [handler port]}]
  (jetty/run-jetty handler
                   {:port port
                    :join? true}))

(defmethod ig/init-key :handler/run-app
  [_ {:keys [db]}]
  (handler/app {:db db}))

(defmethod ig/init-key :pg/init
  [_ {:keys [db migratus-cfg]}]
  (let [cfg (assoc migratus-cfg :db db)]
    (migratus/init cfg)
    (migratus/migrate cfg))
  db)

(defmethod ig/init-key :pg/db
  [_ {:keys [db]}]
  (let [env-host (env :pg-host)]
    (cond-> db
            env-host (assoc :host env-host))))

(defmethod ig/halt-key! :adapter/jetty
  [_ server]
  (.stop server))

(defn -main []
  (ig/init config))