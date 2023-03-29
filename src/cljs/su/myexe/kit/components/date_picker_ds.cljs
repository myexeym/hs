(ns su.myexe.kit.components.date-picker-ds
  (:require [clojure.string :as string]
            [cljs-time.core :as ct]
            [cljs-time.format :as ctf]
            [reagent-mui.cljs-time-adapter :refer [cljs-time-adapter]]
            [reagent-mui.x.localization-provider :refer [localization-provider]]
            [su.myexe.kit.core :refer [<sub >evt translate]]
            [reagent-mui.x.date-picker :refer [date-picker]]))

(defn date-picker-ds
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
                      :maxDate (ct/now)
                      :slotProps {:textField {:variant :standard
                                              :required true
                                              :error (not (empty? errors))
                                              :helperText message}}
                      :variant :standard
                      :required true
                      :format (translate :date-format)
                      :error (not (empty? errors))
                      :helperText message
                      :value (some->> (<sub [:kit.ds/value ds locator field])
                                      (ctf/parse (ctf/formatter (translate :date-format))))
                      :on-change (fn [e]
                                   (>evt [:kit.ds/set-errors-for-field ds locator field nil])
                                   (>evt [:kit.ds/set-value
                                          ds
                                          locator
                                          field
                                          (ctf/unparse (ctf/formatter (translate :date-format)) e)]))})]
    [localization-provider {:date-adapter cljs-time-adapter}
     (into [date-picker props] children)]))