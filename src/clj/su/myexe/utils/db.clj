(ns su.myexe.utils.db
  (:require [java-time.api :as jt]
            [su.myexe.const :as const]))

(defn string->date
  "Converts string to date by date-format."
  [str]
  (jt/local-date const/date-format str))

(defn date->string
  "Converts date to string by date-format."
  [date]
  (jt/format const/date-format (jt/local-date date)))
