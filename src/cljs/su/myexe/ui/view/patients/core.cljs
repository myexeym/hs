(ns su.myexe.ui.view.patients.core
  (:require-macros [reagent-mui.util :refer [with-unchanged-js-props]])
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [su.myexe.kit.core :refer [>evt <sub]]
            [su.myexe.ui.view.patients.ds :refer [ds]]
            [su.myexe.invoke :as invoke]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.icons.delete :refer [delete]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.x.data-grid :refer [data-grid]]
            [reagent-mui.util :refer [js->clj' clj->js' wrap-clj-function]]
            [su.myexe.ui.view.patients.events :as events]
            ))

(rf/reg-event-fx ::init
  (fn [_ _]
    {:dispatch [::events/load]}))

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
               :width 450}
              {:field :actions
               :width 50
               :type :actions
               :renderCell (wrap-clj-function
                             (fn [{:keys [row-node]}]
                               (r/as-element [delete {:on-click #(>evt [::events/delete (:id row-node)])}])))}])

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
                    :on-change (fn [e]
                                 (>evt [::events/add-filters {:by-all (.. e -target -value)} true]))
                    }]
       [button {:variant :outlined
                :color :success
                :on-click (fn []
                            (>evt [:kit/navigate-to :patient]))}
        "Add"]]
      [data-grid {:rows rows
                  :columns columns
                  :autoWidth true
                  :autoHeight true
                  :pageSizeOptions [10]
                  :disableRowSelectionOnClick true
                  :disableColumnSelector true
                  :onRowClick (fn [row]
                                (>evt [:kit/navigate-to
                                       :patient
                                       nil
                                       {:id (-> row
                                                js->clj'
                                                :id)}]))
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
                                           (>evt [::events/add-filters clj-filters true])))}]]]))