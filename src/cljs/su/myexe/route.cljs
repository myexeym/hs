(ns su.myexe.route
  (:require [reitit.frontend :as rf]
            [su.myexe.kit.core :refer [>evt]]
            [su.myexe.ui.view.patient.core :as patient]
            [su.myexe.ui.view.patients.core :as patients]))

(defn- controller
  [init-event]
  [(merge {:identity (fn [match]
                       {:view-name (-> match :data :name)
                        :route-params (:route-params match)
                        :query-params (:query-params match)})}
          (when init-event
            {:start (fn [match]
                      (>evt [init-event
                             (merge (:query-params match)
                                    (:route-params match))]))}))])

(def routes
  (rf/router
    ["/"
     [""
      {:name ::patients/view
       :view patients/view
       :controllers (controller ::patients/init)}]
     ["patient"
      {:name ::patient/view
       :view patient/view
       :controllers (controller ::patient/init)}]]))