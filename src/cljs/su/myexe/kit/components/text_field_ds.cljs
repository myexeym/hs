(ns su.myexe.kit.components.text-field-ds
  (:require [clojure.string :as string]
            [reagent-mui.material.text-field :refer [text-field]]
            [su.myexe.kit.core :refer [<sub >evt translate]]))

(defn text-field-ds
  [{:keys [ds
           locator
           field]
    :as props} & children]
  (let [errors (<sub [:kit.ds/get-errors-for-field ds locator field])
        message (->> errors
                     (map :message)
                     (map translate)
                     (string/join ", "))
        props (merge props
                     {:InputLabelProps {:shrink true}
                      :variant :standard
                      :required true
                      :error (not (empty? errors))
                      :value (or (<sub [:kit.ds/value ds locator field])
                                 "")
                      :helperText message
                      :on-change (fn [v]
                                   (>evt [:kit.ds/set-errors-for-field ds locator field nil])
                                   (>evt [:kit.ds/set-value ds locator field (.. v -target -value)]))})]
    (into [text-field props] children)))