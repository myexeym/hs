(ns su.myexe.kit.ds.DS
  (:require [medley.core :as medley]
            [su.myexe.kit.ds.IDS :refer [IDS]]))

(defn get-data-path
  ([path]
   [:ds path :data])
  ([path record-key]
   [:ds path :data record-key])
  ([path record-key value-key]
   [:ds path :data record-key value-key]))

(defn get-errors-path
  ([path]
   [:ds path :error])
  ([path record-key]
   [:ds path :error record-key])
  ([path record-key value-key]
   [:ds path :error record-key value-key]))

(defrecord DS [path]
  IDS

  (get-data [_ db]
    (get-in db (get-data-path path)))

  (get-data [_ db record-key]
    (get-in db (get-data-path path record-key)))

  (get-data [_ db record-key value-key]
    (get-in db (get-data-path path record-key value-key)))

  (set-data [_ db map-of-records]
    (assoc-in db (get-data-path path) map-of-records))

  (set-data [_ db record-key record]
    (assoc-in db (get-data-path path record-key) record))

  (set-data [_ db record-key value-key value]
    (assoc-in db (get-data-path path record-key value-key) value))

  (clear-data [_ db]
    (medley/dissoc-in db (get-data-path path)))


  (get-errors [_ db]
    (get-in db (get-errors-path path)))

  (get-errors [_ db record-key]
    (get-in db (get-errors-path path record-key)))

  (get-errors [_ db record-key value-key]
    (get-in db (get-errors-path path record-key value-key)))

  (set-errors [_ db errors]
    (assoc-in db (get-errors-path path) errors))

  (set-errors [_ db record-key errors]
    (assoc-in db (get-errors-path path record-key) errors))

  (set-errors [_ db record-key value-key errors]
    (assoc-in db (get-errors-path path record-key value-key) errors))

  (clear-errors [_ db]
    (medley/dissoc-in db (get-errors-path path))))