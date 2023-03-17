(ns su.myexe.ui.view.patients.core
  (:require [re-frame.core :as rf]
            [su.myexe.kit.core :refer [>evt <sub]]
            [su.myexe.ui.view.patients.ds :refer [ds]]
            [su.myexe.invoke :as invoke]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.x.data-grid :refer [data-grid]]
            [reagent-mui.util :refer [use-effect use-ref use-state js->clj']]))

(rf/reg-event-fx ::init
  (fn [_ [_ query-params]]
    {:dispatch (-> (invoke/invoke :list :patient)
                   (invoke/set-ds ds)
                   (invoke/on-failure #(prn "failure" %)))}))

(def columns [{:field :full_name
               :headerName "Full Name"
               :width 200}
              {:field :gender
               :headerName "Gender"
               :type :singleSelect
               :valueOptions [{:value :woman
                               :label "Woman"}
                              {:value :man
                               :label "Man"}]
               :width 150}
              {:field :birthday
               :headerName "Birthday"
               :type :date
               :width 150}
              {:field :policy_number
               :headerName "Policy number"
               :description "This column has a value getter and is not sortable."
               :sortable false
               :width 150}
              {:field :address
               :headerName "Address"
               :width 500}])

(defn view
  []
  (let [rows (<sub [:kit.ds/records ds])]
    [container {:sx {:mt 5}}
     [stack {:spacing 1}
      [stack {:direction :row
              :justifyContent :space-between}
       [text-field {:id :filter
                    :sx {:width 500}
                    :placeholder "Search"
                    :InputLabelProps {:shrink true}
                    :variant :standard
                    :required true
                    :fullWidth true
                    ;:value (<sub [:kit.ds/value ds id :full_name])
                    ;:on-change #(>evt [:kit.ds/set-value ds id :full_name (.. % -target -value)])
                    }]
       [button {:variant :outlined
                :color :success
                ;:on-click (fn []
                ;            (>evt [::events/save]))
                }
        "Add"]]
      [data-grid {:rows rows
                  :columns columns
                  :autoWidth true
                  :autoHeight true
                  :pageSizeOptions [10]
                  :disableColumnSelector true
                  :filterMode :server
                  :componentsProps {:filterPanel
                                    {:filterFormProps
                                     {:columnInputProps {:disabled true
                                                         :sx {:display "none"}}
                                      :operatorInputProps {:disabled true
                                                           :sx {:display "none"}}}}}
                  :onFilterModelChange (fn [filters]
                                         (let [clj-filters (->> filters
                                                                js->clj'
                                                                :items
                                                                (reduce (fn [acc filter]
                                                                          (assoc acc (:field filter)
                                                                                     (:value filter)))
                                                                        {}))]
                                           (prn "->>>" clj-filters)))}]]]))