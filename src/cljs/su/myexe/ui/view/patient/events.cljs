(ns su.myexe.ui.view.patient.events
  (:require [re-frame.core :as rf]
            [su.myexe.invoke :as invoke]
            [su.myexe.kit.core :refer [<sub >evt] :as kit]
            [su.myexe.kit.ds.core :as kit.ds]
            [su.myexe.ui.view.patient.ds :refer [ds]]))

(rf/reg-event-fx ::save
  (fn [{:keys [db]} _]
    (let [id (kit/get-from-state db :id)
          patient (kit.ds/get-record db ds id)]
      (prn "->" id patient)
      (prn "->>" (invoke/invoke :create :patient id patient))
      {:dispatch (if id
                   (-> (invoke/invoke :update :patient id {:data patient})
                       (invoke/on-success #(prn "-> update success" %))
                       (invoke/on-failure #(prn "-> update failure" %)))
                   (-> (invoke/invoke :create :patient id {:data patient})
                       (invoke/on-success #(prn "-> create success" %))
                       (invoke/on-failure #(prn "-> create failure" %))))})))