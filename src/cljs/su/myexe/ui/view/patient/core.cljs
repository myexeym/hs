(ns su.myexe.ui.view.patient.core
  (:require [re-frame.core :as rf]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.menu-item :refer [menu-item]]
            [reagent-mui.material.stack :refer [stack]]
            [su.myexe.invoke :as invoke]
            [su.myexe.kit.components.date-picker-ds :refer [date-picker-ds]]
            [su.myexe.kit.components.text-field-ds :refer [text-field-ds]]
            [su.myexe.kit.core :refer [<sub >evt translate] :as kit]
            [su.myexe.kit.ds.core :as kit.ds]
            [su.myexe.ui.view.patient.ds :refer [ds]]
            [su.myexe.ui.view.patient.events :as events]))

(rf/reg-event-fx ::init
  (fn [{:keys [db]} [_ {:keys [id]}]]
    (if id
      {:db (kit/set-to-state db :id (js/parseInt id))
       :dispatch (-> (invoke/invoke :get :patient (js/parseInt id))
                     (invoke/set-ds ds))}
      {:db (-> db
               (kit/set-to-state :id nil)
               (kit.ds/clear-data ds))})))

(defn view
  []
  (let [id (<sub [:kit/get-from-state :id])]
    [container {:maxWidth :xs
                :fixed true}
     [stack {:sx {:width 500
                  :mt 5}
             :spacing 3}
      [text-field-ds {:id :full-name
                      :label (translate ::full-name)
                      :ds ds
                      :locator id
                      :field :full_name}]
      [text-field-ds {:id :gender
                      :label (translate ::gender)
                      :ds ds
                      :locator id
                      :field :gender
                      :select true}
       [menu-item {:value :woman}
        (translate ::woman)]
       [menu-item {:value :man}
        (translate ::man)]]
      [date-picker-ds {:id :birthday
                      :label (translate ::birthday)
                      :ds ds
                      :locator id
                      :field :birthday}]
      [text-field-ds {:id :policy-number
                      :label (translate ::policy-number)
                      :ds ds
                      :locator id
                      :field :policy_number}]
      [text-field-ds {:id :address
                      :label (translate ::address)
                      :ds ds
                      :locator id
                      :field :address
                      :multiline true}]
      [stack {:direction :row
              :justifyContent :space-between}
       [button {:variant :outlined
                :color :success
                :on-click (fn []
                            (>evt [::events/save]))}
        (translate ::save)]
       [button {:variant :outlined
                :color :success
                :on-click (fn []
                            (>evt [::events/validate]))}
        (translate ::validate)]]]]))

