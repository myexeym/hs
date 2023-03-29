(ns su.myexe.kit.core
  (:require [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]
            [su.myexe.dicts :as dicts]))

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

(defn translate
  ([key]
   (dicts/translate nil key))
  ([locale key]
   (dicts/translate locale key))
  ([locale key x]
   (dicts/translate locale key x))
  ([locale key x & args]
   (apply dicts/translate locale key x args)))

(defn set-to-state
  "Adds pair (name, value) to state."
  [db name value]
  (assoc-in db [:state name] value))

(defn get-from-state
  "Returns value by name from state."
  [db name]
  (get-in db [:state name]))

;;
;; Events
;;

(rf/reg-event-fx :kit/set-to-state
  (fn [{:keys [db]} [_ name value]]
    {:db (set-to-state db name value)}))

(rf/reg-event-fx :kit/navigate-to
  (fn [_ [_ name path-params query-params replace?]]
    (if replace?
      (rfe/replace-state name path-params query-params)
      (rfe/push-state name path-params query-params))))

;;
;; Subs
;;

(rf/reg-sub :kit/get-from-state
  (fn [db [_ name]]
    (get-from-state db name)))