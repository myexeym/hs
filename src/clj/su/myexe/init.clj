(ns su.myexe.init
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [clojure.java.io :as io]
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
  [_ {:keys [db]}]
  ;; TODO: Need add migration.
  db)

(defmethod ig/halt-key! :adapter/jetty
  [_ server]
  (.stop server))

(defn -main []
  (ig/init config))