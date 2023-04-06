(ns su.myexe.api.patient-test
  (:require [clojure.test :refer :all]
            [su.myexe.api.patient :as sut]))

(deftest gen-list-sql-query-test
  (let [gen-list-sql-query #'sut/gen-list-sql-query]
    (testing "Without filters"
      (let [expected ["select * from patient"]]
        (is (= expected (gen-list-sql-query {})))))
    (testing "With all filters"
      (let [expected ["select * from patient where (full_name like ?
                                           or gender like ?
                                           or to_char(birthday, 'dd.mm.yyyy') like ?
                                           or address like ?
                                           or policy_number like ?) and full_name like ? and gender = ? and to_char(birthday, 'dd.mm.yyyy') like ? and policy_number like ? and address like ?"
                      "%123%"
                      "123"
                      "%123%"
                      "%123%"
                      "%123%"
                      "%456%"
                      "man"
                      "%09.05%"
                      "%1234567890%"
                      "%qwerty%"]]
        (is (= expected (gen-list-sql-query {:by-all "123"
                                             :full_name "456"
                                             :gender "man"
                                             :birthday "09.05"
                                             :policy_number "1234567890"
                                             :address "qwerty"})))))))
