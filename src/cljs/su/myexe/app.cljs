(ns su.myexe.app
  (:require [clojure.browser.dom :as cd]
            [reagent.core :as r]
            [reagent.dom :as r-dom]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]
            [re-frame.core :as rf]
            [su.myexe.invoke]
            [su.myexe.kit.core :refer [<sub >evt]]
            [su.myexe.route :as route]))

(rf/reg-event-fx ::set-view
  (fn [{:keys [db]} [_ view]]
    {:db (assoc db ::view view)}))

(rf/reg-sub ::view
  (fn [db _]
    (::view db)))

(defn root
  "Root view."
  []
  (let [view (<sub [::view])]
    (when view
      [view])))

(defonce match (r/atom nil))

(defn ^:dev/after-load init!
  []
  (rfe/start! route/routes
              (fn [new-match]
                (swap! match (fn [{:keys [controllers]}]
                               (when new-match
                                 (assoc new-match :controllers (rfc/apply-controllers controllers new-match)))))
                (if new-match
                  (>evt [::set-view (:view (:data new-match))])
                  (rfe/replace-state :su.myexe.ui.view.error-404/view nil nil)))
              {:use-fragment false})
  (r-dom/render
    [root]
    (cd/get-element "root")))