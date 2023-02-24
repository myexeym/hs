(ns su.myexe.utils.route
  (:require [clojure.tools.logging :as log]
            [medley.core :as medley]
            [ring.util.response :as rr]))

(defn call-api
  "Calls api method. Adds logging, merges params and app-params, parses `id` to long.
  Returns response of method."
  [method {:keys [app-params params]}]
  (log/info "->api->" (str method) params)
  (let [result (method (-> params
                           (merge app-params)
                           (medley/update-existing :id parse-long)))]
    (log/info "<-api<-" (str method) (rr/response result))
    (rr/response result)))
