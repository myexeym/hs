(ns su.myexe.kit.ds.core
  (:require [re-frame.core :as rf]
            [su.myexe.kit.ds.DS :refer [->DS]]
            [su.myexe.kit.ds.IDS :as IDS]))

;;
;; Functions
;;

(defn make-ds
  "Makes datasource with `path`."
  [path]
  (->DS path))

(defn get-data
  "Returns data from ds."
  [db ds]
  (IDS/get-data ds db))

(defn get-records
  "Returns collection of records."
  [db ds]
  (->> (IDS/get-data ds db)
       vals
       vec))

(defn set-data
  "Sets data to ds."
  [db ds data]
  (IDS/set-data ds db data))

(defn get-record
  "Returns record."
  [db ds id]
  (IDS/get-data ds db id))

(defn set-record
  "Sets record."
  [db ds id record]
  (IDS/set-data ds db id record))

(defn get-value
  "Returns record value."
  [db ds id field]
  (IDS/get-data ds db id field))

(defn set-value
  "Sets record value."
  [db ds id field value]
  (IDS/set-data ds db id field value))

(defn clear-data
  "Clears ds data."
  [db ds]
  (IDS/clear-data ds db))

(defn get-errors
  "Returns errors from ds."
  [db ds]
  (IDS/get-errors ds db))

(defn set-errors
  "Sets errors to ds."
  [db ds errors]
  (IDS/set-errors ds db errors))

(defn get-errors-for-record
  "Returns errors for record."
  [db ds id]
  (IDS/get-errors ds db id))

(defn set-errors-for-record
  "Sets errors for record."
  [db ds id record]
  (IDS/set-errors ds db id record))

(defn get-errors-for-field
  "Returns errors for field."
  [db ds id field]
  (IDS/get-errors ds db id field))

(defn set-errors-for-field
  "Sets errors for field."
  [db ds id field value]
  (IDS/set-errors ds db id field value))

(defn clear-errors
  "Clears ds errors."
  [db ds]
  (IDS/clear-errors ds db))


;;
;; Events
;;

(rf/reg-event-fx :kit.ds/set-data
  (fn [{:keys [db]} [_ ds data]]
    {:db (set-data db ds data)}))

(rf/reg-event-fx :kit.ds/set-record
  (fn [{:keys [db]} [_ ds id record]]
    {:db (set-record db ds id record)}))

(rf/reg-event-fx :kit.ds/set-value
  (fn [{:keys [db]} [_ ds id field value]]
    {:db (set-value db ds id field value)}))

(rf/reg-event-fx :kit.ds/clear-data
  (fn [{:keys [db]} [_ ds]]
    {:db (clear-data db ds)}))

(rf/reg-event-fx :kit.ds/set-errors
  (fn [{:keys [db]} [_ ds errors]]
    {:db (set-errors db ds errors)}))

(rf/reg-event-fx :kit.ds/set-errors-for-field
  (fn [{:keys [db]} [_ ds id field value]]
    {:db (set-errors-for-field db ds id field value)}))

;;
;; Subs
;;

(rf/reg-sub :kit.ds/data
  (fn [db [_ ds]]
    (get-data db ds)))

(rf/reg-sub :kit.ds/records
  (fn [db [_ ds]]
    (get-records db ds)))

(rf/reg-sub :kit.ds/record
  (fn [db [_ ds id]]
    (get-record db ds id)))

(rf/reg-sub :kit.ds/value
  (fn [db [_ ds id field]]
    (get-value db ds id field)))

(rf/reg-sub :kit.ds/get-errors-for-field
  (fn [db [_ ds id field]]
    (get-errors-for-field db ds id field)))
