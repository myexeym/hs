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
