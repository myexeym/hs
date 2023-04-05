(ns su.myexe.api.patient
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]
            [medley.core :as medley]
            [su.myexe.utils.db :as u-db]
            [su.myexe.utils.validate :as u-validate]))

;;
;; Helpers
;;

(defn- gen-list-sql-query
  "Returns query for list patients filtered by fields."
  [{:keys [by-all full_name gender birthday policy_number address]}]
  (let [sql-where-parts (cond-> []
                                by-all (conj "(full_name like ?
                                           or gender like ?
                                           or to_char(birthday, 'dd.mm.yyyy') like ?
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
    (into [sql] params)))

;;
;; Api
;;

(defn list-patient
  "Returns list of patients with filter by parameters."
  [{:keys [db] :as params}]
  (->> (gen-list-sql-query params)
       (jdbc/query db)
       (map #(update % :birthday u-db/date->string))))

(defn get-patient
  "Returns patient by id."
  [{:keys [db id]}]
  (->> (jdbc/query db ["select * from patient where id = ?" id])
       (map #(update % :birthday u-db/date->string))
       first))

(defn create-patient
  "Creates new patient."
  [{:keys [db data]}]
  (u-validate/validate-entity :patient data)
  (jdbc/insert! db
                :patient
                (medley/update-existing data :birthday u-db/string->date)))

(defn update-patient
  "Updates patient"
  [{:keys [db id data]}]
  (u-validate/validate-entity :patient data)
  (jdbc/update! db
                :patient
                (-> data
                    (dissoc :id)
                    (medley/update-existing :birthday u-db/string->date))
                ["id = ?" id]))

(defn delete-patient
  "Deletes patient"
  [{:keys [db id]}]
  (jdbc/delete! db :patient ["id = ?" id]))

(defn validate-patient
  "Validate patient"
  [{:keys [data]}]
  (u-validate/validate-entity :patient data))
