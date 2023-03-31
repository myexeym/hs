(ns su.myexe.utils.validate-test
  (:require [clojure.test :refer :all]
            [slingshot.test]
            [su.myexe.utils.validate :as sut]))

;;
;; Helpers
;;

(defn check-count5 [v]
  (when (not= 5 (count v))
    [{:type :error
      :message :validation/incorrect-count-5}]))

(defn check-count6 [v]
  (when (not= 6 (count v))
    [{:type :warning
      :message :validation/incorrect-count-6}]))

;;
;; Tests
;;

(deftest check-require-test
  (let [check-require #'sut/check-require
        validation-map {:first-name [:require :q]
                        :last-name [:require :w]
                        :middle-name [:require :e]}]
    (testing "All fields require"
      (let [entity {:id 1
                    :first-name "First Name"
                    :last-name "Last Name"
                    :middle-name "Middle Name"}
            expected []]
        (is (= expected (check-require entity validation-map)))))
    (testing "One field empty"
      (let [entity {:id 1
                    :first-name "First Name"
                    :middle-name "Middle Name"}
            expected [{:field :last-name
                       :id 1
                       :message :validation/field-require
                       :type :error
                       :value nil}]]
        (is (= expected (check-require entity validation-map))))
      (let [entity {:id 1
                    :first-name "First Name"
                    :last-name "   "
                    :middle-name "Middle Name"}
            expected [{:field :last-name
                       :id 1
                       :message :validation/field-require
                       :type :error
                       :value "   "}]]
        (is (= expected (check-require entity validation-map)))))))

(deftest check-field-test
  (let [check-field #'sut/check-field]
    (testing "Success"
      (let [value "12345"
            check-fns [check-count5]
            expected []]
        (is (= expected (check-field value check-fns)))))
    (testing "Error"
      (let [value "1234"
            check-fns [check-count5]
            expected [{:type :error
                       :message :validation/incorrect-count-5}]]
        (is (= expected (check-field value check-fns))))
      (let [value "12345"
            check-fns [check-count5 check-count6]
            expected [{:type :warning
                       :message :validation/incorrect-count-6}]]
        (is (= expected (check-field value check-fns))))
      (let [value "1234"
            check-fns [check-count5 check-count6]
            expected [{:type :error
                       :message :validation/incorrect-count-5}
                      {:type :warning
                       :message :validation/incorrect-count-6}]]
        (is (= expected (check-field value check-fns)))))))

(deftest validate-test
  (let [validate #'sut/validate
        validation-map {:first-name [:require]
                        :last-name [:require]
                        :middle-name [:require check-count5]}]
    (testing "Success"
      (let [entity {:id 1
                    :first-name "First Name"
                    :last-name "Last Name"
                    :middle-name "12345"}
            expected []]
        (is (= expected (validate entity validation-map)))))
    (testing "Errors"
      (let [entity {:id 1
                    :first-name "First Name"
                    :middle-name "1234"}
            expected [{:field :last-name
                       :id 1
                       :message :validation/field-require
                       :type :error
                       :value nil}
                      {:field :middle-name
                       :id 1
                       :message :validation/incorrect-count-5
                       :type :error
                       :value "1234"}]]
        (is (= expected (validate entity validation-map)))))))

(deftest validate-entity-test
  (testing "Success"
    (let [patient {:id 8
                    :full_name "111"
                    :gender "woman"
                    :birthday "11.11.2000"
                    :address "123131"
                    :policy_number "1234567890123456"}]
      (is (nil? (sut/validate-entity :patient patient)))))
  (testing "Errors"
    (let [patient {:id 8
                   :full_name "111"
                   :birthday "11.11.2019"
                   :address "123131"
                   :policy_number "123456789012345"}
          expected {:type :validation-error
                    :errors [{:type :error
                              :id 8
                              :field :gender
                              :message :validation/field-require
                              :value nil}
                             {:type :error
                              :id 8
                              :field :birthday
                              :message :validation/too-young
                              :value "11.11.2019"}
                             {:type :error
                              :id 8
                              :field :policy_number
                              :message :validation/incorrect-policy
                              :value "123456789012345"}]}]
      (is (thrown+? #(= expected %)
                    (sut/validate-entity :patient patient))))))
