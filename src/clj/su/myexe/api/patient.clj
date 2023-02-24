(ns su.myexe.api.patient
  (:require [clojure.java.jdbc :as jdbc]
            [medley.core :as medley]))

(defn list-patient
  [{:keys [db]}]
  (jdbc/query db
              ["select * from patient"]))

(defn get-patient
  [{:keys [db id]}]
  (prn "get-patient -" id))

(defn create-patient
  [{:keys [db data] :as params}]
  (jdbc/insert! db
                :patient
                {})
  (prn "create-patient -" params))

(defn update-patient
  [{:keys [db data] :as params}]
  (prn "update-patient -" params))

(defn delete-patient
  [{:keys [db id] :as params}]
  (prn "delete-patient -" params))

(defn validate-patient
  [{:keys [db id] :as params}]
  (prn "validate-patient -" params))