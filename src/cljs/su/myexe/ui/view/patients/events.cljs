(ns su.myexe.ui.view.patients.events
  (:require [clojure.string :as string]
            [medley.core :as medley]
            [re-frame.core :as rf]
            [su.myexe.invoke :as invoke]
            [su.myexe.kit.core :refer [<sub >evt] :as kit]
            [su.myexe.ui.view.patients.ds :refer [ds]]))

(rf/reg-event-fx ::load
  (fn [{:keys [db]} _]
    (let [filters (->> (kit/get-from-state db :filters)
                       (medley/remove-vals string/blank?))]
      {:dispatch (-> (invoke/invoke :list :patient nil filters)
                     (invoke/set-ds ds))})))

(rf/reg-event-fx ::add-filters
  (fn [{:keys [db]} [_ new-filters reload?]]
    (let [filters (-> (kit/get-from-state db :filters)
                      (merge new-filters))]
      (cond-> {}
              (some? new-filters) (merge {:db (kit/set-to-state db :filters filters)})
              reload? (merge {:dispatch [::load]})))))

(rf/reg-event-fx ::delete
  (fn [_ [_ id]]
    {:dispatch (-> (invoke/invoke :delete :patient id)
                   (invoke/on-success #(>evt [::load])))}))