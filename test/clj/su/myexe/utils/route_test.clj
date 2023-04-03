(ns su.myexe.utils.route-test
  (:require [clojure.test :refer :all]
            [su.myexe.utils.route :as sut])
  (:use [slingshot.slingshot :only [throw+]]))

(deftest call-api-test
  (let [params {:app-params {:db {:dbtype :pg}}
                :params {:id "1"}}]
    (testing "Success"
      (let [method (fn [params]
                     params)
            expected {:body {:db {:dbtype :pg}
                             :id 1}
                      :headers {}
                      :status 200}]
        (is (= expected (sut/call-api method params)))))
    (testing "Validation Error"
      (let [method (fn [_]
                     (throw+ {:type :validation-error
                              :errors [{:type :error
                                        :id 1
                                        :field :name
                                        :message :validation/field-require
                                        :value nil}]}))
            expected {:body {:errors [{:field :name
                                       :id 1
                                       :message :validation/field-require
                                       :type :error
                                       :value nil}]
                             :type :validation-error}
                      :headers {}
                      :status 200}]
        (is (= expected (sut/call-api method params)))))))
