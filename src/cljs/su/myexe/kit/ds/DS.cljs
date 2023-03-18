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

  (clear [_ db]
    (medley/dissoc-in db (get-data-path path))))