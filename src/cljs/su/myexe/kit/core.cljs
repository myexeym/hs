(ns su.myexe.kit.core
  (:require [re-frame.core :as rf]))

(defn <sub
  "Alias for @(rf/subscribe [sub])"
  [sub & args]
  (let [sub (cond-> sub
              (not (vector? sub)) vector
              (seq args) (into args))]
    @(rf/subscribe sub)))

(defn >evt
  "Alias for (rf/dispatch [event])"
  [evt & args]
  (when (some? evt)
    (let [evt (cond-> evt
                (not (vector? evt)) vector
                (seq args) (into args))]
      (rf/dispatch evt))))

(defn set-to-state
  "Adds pair (name, value) to state."
  [db name value]
  (assoc-in db [:state name] value))

(defn get-from-state
  "Returns value by name from state."
  [db name]
  (get-in db [:state name]))

(rf/reg-event-fx :kit.ds/set-to-state
  (fn [{:keys [db]} [_ name value]]
    {:db (set-to-state db name value)}))

(rf/reg-sub :kit/get-from-state
  (fn [db [_ name]]
    (get-from-state db name)))