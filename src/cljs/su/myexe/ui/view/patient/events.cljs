(ns su.myexe.ui.view.patient.events
  (:require [re-frame.core :as rf]
            [su.myexe.invoke :as invoke]
            [su.myexe.kit.core :refer [<sub >evt] :as kit]
            [su.myexe.kit.ds.core :as kit.ds]
            [su.myexe.ui.view.patient.ds :refer [ds]]))

(rf/reg-event-fx ::save
  (fn [{:keys [db]} _]
    (let [id (kit/get-from-state db :id)
          patient (kit.ds/get-record db ds id)
          method (if id
                   :update
                   :create)]
      {:dispatch (-> (invoke/invoke method :patient id {:data patient})
                     (invoke/set-error-to-ds ds)
                     (invoke/on-success #(when (empty? (:errors %))
                                           (>evt [:kit/navigate-to :patients]))))})))

(rf/reg-event-fx ::validate
  (fn [{:keys [db]} _]
    (let [id (kit/get-from-state db :id)
          patient (kit.ds/get-record db ds id)]
      {:dispatch (-> (invoke/invoke :validate :patient/validate nil {:data patient})
                     (invoke/set-error-to-ds ds))})))