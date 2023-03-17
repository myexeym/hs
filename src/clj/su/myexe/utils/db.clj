(ns su.myexe.utils.db
  (:require [clojure.java.jdbc :as jdbc]
            [java-time.api :as jt]
            [medley.core :as medley]))

(defn map->db-data
  [data]
  (-> (medley/map-keys #(keyword (name %)) data)
      (update :birthday jt/instant->sql-timestamp)))

(defn db-data->map
  [data]
  (-> (medley/map-keys #(keyword (name %)) data)
      (update :birthday jt/instant->sql-timestamp)))
