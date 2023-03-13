(ns su.myexe.ui.view.patient.core
  (:require [re-frame.core :as rf]
            [su.myexe.invoke :as invoke]
            [su.myexe.kit.ds.core :as kit.ds]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.x.date-field :refer [date-field]]
            [reagent-mui.cljs-time-adapter :refer [cljs-time-adapter]]
            [reagent-mui.x.localization-provider :refer [localization-provider]]
            [reagent-mui.material.menu-item :refer [menu-item]]))

(def ds (kit.ds/make-ds ::patient-ds))

(rf/reg-event-fx ::init
  (fn [_ [_ {:keys [id]}]]
    (if id
      {:dispatch (-> (invoke/invoke :get :patient id)
                     (invoke/on-success #(prn "success" %))
                     (invoke/on-failure #(prn "failure" %)))})))

(defn event-value
  [e]
  (.. e -target -value))

(defn view
  []
  [container {:maxWidth :xs
              :fixed true}
   [stack {:sx {:width 500
                :mt 5}
           :spacing 3}
    [text-field {:id "fio"
                 :label "FIO"
                 :required true}]
    [text-field {:id "gender"
                 :label "Gender"
                 :defaultValue ""
                 :required true
                 :select true}
     [menu-item {:value 1}
      "Woman"]
     [menu-item {:value 2}
      "Man"]]
    [localization-provider {:date-adapter cljs-time-adapter}
     [date-field {:label "Birthday"
                  :required true
                  :format "dd.MM.yyyy"}]]
    [text-field {:id "address"
                 :label "Address"
                 :multiline true
                 :required true}]
    [text-field {:id "policy-number"
                 :label "Policy Number"
                 :required true}]
    [stack {:direction :row
            :justifyContent :space-between}
     [button {:variant :outlined
              :color :success}
      "Save"]
     [button {:variant :outlined
              :color :error}
      "Reset"]]]])
