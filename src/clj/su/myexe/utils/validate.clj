(ns su.myexe.utils.validate
  (:require [clojure.string :as string]
            [java-time.api :as jt]
            [su.myexe.const :as const])
  (:use [slingshot.slingshot :only [throw+]]))

(defn- check-require
  [entity validation-map]
  (->> validation-map
       (filter (fn [[_ v]]
                 (contains? (set v)
                            :require)))
       (map first)
       (reduce (fn [acc require-field]
                 (let [value (get entity require-field)]
                   (if (or (nil? value)
                           (and (string? value)
                                (string/blank? value)))
                     (conj acc {:type :error
                                :id (:id entity)
                                :field require-field
                                :message :validation/field-require
                                :value value})
                     acc)))
               [])))

(defn- check-field
  [value check-fns]
  (reduce (fn [acc check-fn]
            (into acc (check-fn value)))
          []
          check-fns))

(defn- validate
  ""
  [entity validation-map]
  (let [require-errors (check-require entity validation-map)
        fields-error (reduce-kv (fn [acc k v]
                                  (let [errors (check-field v (get validation-map k))]
                                    (->> errors
                                         (map (fn [error]
                                                {:type (:type error)
                                                 :id (:id entity)
                                                 :field k
                                                 :message (:message error)
                                                 :value v}))
                                         (into acc))))
                                []
                                entity)]
    (-> []
        (into require-errors)
        (into fields-error))))

(defmulti validate-entity
  (fn [x _]
    x))

(defmethod validate-entity :patient
  [_ data]
  (let [check-policy-number (fn [v]
                              (when (not= 16 (count v))
                                [{:type :error
                                  :message :validation/incorrect-policy}]))
        check-birthday (fn [v]
                         (when (jt/after? (jt/plus (jt/local-date const/date-format v)
                                                   (jt/years 18))
                                          (jt/local-date))
                           [{:type :error
                             :message :validation/too-young}]))
        errors (validate data
                                         {:full_name [:require]
                                          :gender [:require]
                                          :birthday [:require check-birthday]
                                          :address [:require]
                                          :policy_number [:require check-policy-number]})]
    (when (seq errors)
      (throw+ {:type :validation-error
               :errors errors}))))

