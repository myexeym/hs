(ns su.myexe.ui.view.patients.core
  (:require [re-frame.core :as rf]
            [su.myexe.kit.core :refer [>evt <sub]]
            [su.myexe.invoke :as invoke]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.x.data-grid :refer [data-grid]]
            [reagent-mui.util :refer [clj->js' wrap-clj-function]]))

(rf/reg-event-fx ::init
  (fn [_ [_ query-params]]
    {:dispatch (-> (invoke/invoke :list :patient)
                   (invoke/on-success #(prn "success" %))
                   (invoke/on-failure #(prn "failure" %)))}))

(def columns [{:field :id
               :headerName "FIO"
               :width 90}
              {:field :first-name
               :headerName "Sex"
               :width 150
               :editable true}
              {:field :last-name
               :headerName "Birthday"
               :width 150
               :editable true}
              {:field :age
               :headerName "Address"
               :type :number
               :width 110
               :editable true}
              {:field :full-name
               :headerName "Policy number"
               :description "This column has a value getter and is not sortable."
               :sortable false
               :width 160
               :valueGetter (wrap-clj-function
                              (fn [params]
                                (str (get-in params [:row :first-name] "") " " (get-in params [:row :last-name] ""))))}])

(def rows [{:id 1 :last-name "Snow" :first-name "Jon hd ksdfhkj sdhfkj sdhfk sdhfklj shdflk hsdfk" :age 35}
           {:id 2 :last-name "Lannister" :first-name "Cersei" :age 42}
           {:id 3 :last-name "Lannister" :first-name "Jaime" :age 45}
           {:id 4 :last-name "Stark" :first-name "Arya" :age 16}
           {:id 5 :last-name "Targaryen" :first-name "Daenerys" :age nil}
           {:id 6 :last-name "Melisandre" :first-name nil :age 150}
           {:id 7 :last-name "Clifford" :first-name "Ferrara" :age 44}
           {:id 8 :last-name "Frances" :first-name "Rossini" :age 36}
           {:id 9 :last-name "Roxie" :first-name "Harvey" :age 65}])

(defn view
  []
  (let [
        ;rows (<sub [:patients-rows])
        ]
    [container
     [data-grid {:rows rows
                 :columns columns
                 :autoHeight true
                 :page-size-options [5]
                 :checkbox-selection true
                 :disable-row-selection-on-click true}]])
  #_(form-grid/form-grid {}))