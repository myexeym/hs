(ns su.myexe.api.patient
  (:require [clojure.java.jdbc :as jdbc]
            [java-time.api :as jt]
            [medley.core :as medley]))

(defn list-patient
  [{:keys [db]}]
  (jdbc/query db
              ["select * from patient"]))

(defn get-patient
  [{:keys [db id]}]
  (-> db
      (jdbc/query ["select * from patient where id = ?" id])
      first))

(defn create-patient
  [{:keys [db data] :as params}]
  (jdbc/insert! db
                :patient
                (update data :birthday jt/instant->sql-timestamp))
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