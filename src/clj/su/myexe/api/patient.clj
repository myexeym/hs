(ns su.myexe.api.patient
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]
            [java-time.api :as jt]
            [medley.core :as medley]
            [su.myexe.utils.validate :as validate]
            [su.myexe.const :as const])
  (:use [slingshot.slingshot :only [throw+]]))

;;
;; Api
;;

(defn list-patient
  "Returns list of partners from db with filter by parameters."
  [{:keys [db by-all full_name gender birthday policy_number address]}]
  (let [sql-where-parts (cond-> []
                                by-all (conj "(full_name like ?
                                           or gender like ?
                                           or to_char(birthday, 'dd.mm.yyyy') LIKE ?
                                           or address like ?
                                           or policy_number like ?)")
                                full_name (conj "full_name like ?")
                                gender (conj "gender = ?")
                                birthday (conj "to_char(birthday, 'dd.mm.yyyy') like ?")
                                policy_number (conj "policy_number like ?")
                                address (conj "address like ?"))
        sql (cond-> "select * from patient"
                    (seq sql-where-parts) (str " where " (string/join " and " sql-where-parts)))
        params (cond-> []
                       by-all (conj (str "%" by-all "%")
                                    by-all
                                    (str "%" by-all "%")
                                    (str "%" by-all "%")
                                    (str "%" by-all "%"))
                       full_name (conj (str "%" full_name "%"))
                       gender (conj gender)
                       birthday (conj (str "%" birthday "%"))
                       policy_number (conj (str "%" policy_number "%"))
                       address (conj (str "%" address "%")))]
    (->> (jdbc/query db
                     (into [sql] params))
         (map #(update % :birthday (fn [v]
                                     (jt/format const/date-format (jt/local-date v))))))))

(defn get-patient
  [{:keys [db id]}]
  (->> (jdbc/query db ["select * from patient where id = ?" id])
       (map #(update % :birthday (fn [v]
                                   (jt/format const/date-format (jt/local-date v)))))
       first))

(defn create-patient
  [{:keys [db data]}]
  (validate/validate-entity :patient data)
  (jdbc/insert! db
                :patient
                (medley/update-existing data :birthday #(jt/local-date const/date-format %))))

(defn update-patient
  [{:keys [db id data]}]
  (validate/validate-entity :patient data)
  (jdbc/update! db
                :patient
                (-> data
                    (dissoc :id)
                    (medley/update-existing :birthday #(jt/local-date const/date-format %)))
                ["id = ?" id]))

(defn delete-patient
  [{:keys [db id]}]
  (jdbc/delete! db :patient ["id = ?" id]))

(defn validate-patient
  [{:keys [data]}]
  (validate/validate-entity :patient data))