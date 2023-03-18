(ns su.myexe.api.patient
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]
            [java-time.api :as jt]
            [medley.core :as medley]))

(defn list-patient
  "Returns list of partners from db with filter by parameters."
  [{:keys [db by-all full_name gender birthday policy_number address]}]
  (let [sql-where-parts (cond-> []
                                by-all (conj "(full_name like ?
                                           or gender like ?
                                           or address like ?
                                           or policy_number like ?)")
                                full_name (conj "full_name like ?")
                                gender (conj "gender = ?")
                                birthday (conj "birthday = ?")
                                policy_number (conj "policy_number like ?")
                                address (conj "address like ?"))
        sql (cond-> "select * from patient"
                    (seq sql-where-parts) (str " where " (string/join " and " sql-where-parts)))
        params (cond-> []
                       by-all (conj (str "%" by-all "%")
                                    by-all
                                    (str "%" by-all "%")
                                    (str "%" by-all "%"))
                       full_name (conj (str "%" full_name "%"))
                       gender (conj gender)
                       birthday (conj birthday)
                       policy_number (conj (str "%" policy_number "%"))
                       address (conj (str "%" address "%")))]
    (jdbc/query db
                (into [sql] params))))

(defn get-patient
  [{:keys [db id]}]
  (-> db
      (jdbc/query ["select * from patient where id = ?" id])
      first))

(defn create-patient
  [{:keys [db data]}]
  (jdbc/insert! db
                :patient
                (update data :birthday jt/instant->sql-timestamp)))

(defn update-patient
  [{:keys [db id data] :as params}]
  (jdbc/update! db
                :patient
                (-> data
                    (dissoc :id)
                    (medley/update-existing :birthday jt/instant->sql-timestamp))
                ["id = ?" id]))

(defn delete-patient
  [{:keys [db id]}]
  (jdbc/delete! db :patient ["id = ?" id]))

(defn validate-patient
  [{:keys [db id] :as params}]
  (prn "validate-patient -" params))