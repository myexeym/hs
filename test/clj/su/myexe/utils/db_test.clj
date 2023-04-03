(ns su.myexe.utils.db-test
  (:require [clojure.test :refer :all]
            [java-time.api :as jt]
            [su.myexe.utils.db :as sut]))

(deftest string->date-test
  (let [str "01.02.1234"
        expected '(1 2 1234)]
    (is (= expected (-> (sut/string->date str)
                        (jt/as :day-of-month :month-of-year :year))))))

(deftest date->string-test
  (let [date (jt/local-date 1234 2 1)
        expected "01.02.1234"]
    (is (= expected (sut/date->string date)))))
