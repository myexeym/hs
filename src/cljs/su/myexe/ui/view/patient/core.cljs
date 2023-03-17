(ns su.myexe.ui.view.patient.core
  (:require [re-frame.core :as rf]
            [su.myexe.invoke :as invoke]
            [su.myexe.kit.core :refer [<sub >evt] :as kit]
            [su.myexe.ui.view.patient.ds :refer [ds]]
            [su.myexe.ui.view.patient.events :as events]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.x.date-field :refer [date-field]]
            [reagent-mui.cljs-time-adapter :refer [cljs-time-adapter]]
            [reagent-mui.x.localization-provider :refer [localization-provider]]
            [reagent-mui.material.menu-item :refer [menu-item]]))

(rf/reg-event-fx ::init
  (fn [{:keys [db]} [_ {:keys [id]}]]
    (when id
      {:db (kit/set-to-state db :id (js/parseInt id))
       :dispatch (-> (invoke/invoke :get :patient (js/parseInt id))
                     (invoke/set-ds ds))})))

(defn view
  []
  (let [id (<sub [:kit/get-from-state :id])
        entity (<sub [:kit.ds/record ds id])]
    (prn "->" id)
    (prn "->>" entity)
    (prn "->>>" (<sub [:kit.ds/data ds]))
    (prn "+>" (<sub [:kit.ds/value ds id :gender]))
    [container {:maxWidth :xs
                :fixed true}
     [stack {:sx {:width 500
                  :mt 5}
             :spacing 3}
      [text-field {:id "fio"
                   :label "FIO"
                   :InputLabelProps {:shrink true}
                   :variant :standard
                   :required true
                   :value (<sub [:kit.ds/value ds id :full_name])
                   :on-change #(>evt [:kit.ds/set-value ds id :full_name (.. % -target -value)])}]
      [text-field {:id "gender"
                   :label "Gender"
                   :InputLabelProps {:shrink true}
                   :variant :standard
                   :defaultValue ""
                   :required true
                   :select true
                   :value (<sub [:kit.ds/value ds id :gender])
                   :on-change #(>evt [:kit.ds/set-value ds id :gender (.. % -target -value)])}
       [menu-item {:value :woman}
        "Woman"]
       [menu-item {:value :man}
        "Man"]]
      [localization-provider {:date-adapter cljs-time-adapter}
       [date-field {:label "Birthday"
                    :InputLabelProps {:shrink true}
                    :variant :standard
                    :required true
                    :format "dd.MM.yyyy"
                    ;:value (js/Date. (<sub [:kit.ds/value ds id :birthday]))
                    :on-change (fn [e]
                                 (prn "------" (type e))
                                 (js/console.log (js/Date. e))
                                 (>evt [:kit.ds/set-value ds id :birthday (js/Date. e)]))}]]
      [text-field {:id "address"
                   :label "Address"
                   :InputLabelProps {:shrink true}
                   :variant :standard
                   :multiline true
                   :required true
                   :value (<sub [:kit.ds/value ds id :address])
                   :on-change #(>evt [:kit.ds/set-value ds id :address (.. % -target -value)])}]
      [text-field {:id "policy-number"
                   :label "Policy Number"
                   :InputLabelProps {:shrink true}
                   :variant :standard
                   :required true
                   :value (<sub [:kit.ds/value ds id :policy_number])
                   :on-change #(>evt [:kit.ds/set-value ds id :policy_number (.. % -target -value)])}]
      [stack {:direction :row
              :justifyContent :space-between}
       [button {:variant :outlined
                :color :success
                :on-click (fn []
                            (>evt [::events/save]))}
        "Save"]
       [button {:variant :outlined
                :color :error}
        "Reset"]]]]))

