(ns su.myexe.kit.ds.core
  (:require [su.myexe.kit.ds.DS :refer [->DS]]
            [su.myexe.kit.ds.IDS :as IDS]
            [re-frame.core :as rf]))

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

(defn clear
  "Clears ds."
  [db ds]
  (IDS/clear ds db))


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

(rf/reg-event-fx :kit.ds/clear
  (fn [{:keys [db]} [_ ds]]
    {:db (clear db ds)}))

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
