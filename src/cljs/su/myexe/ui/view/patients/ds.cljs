(ns su.myexe.ui.view.patients.ds
  (:require [su.myexe.kit.ds.core :as kit.ds]))

(def ds (kit.ds/make-ds ::patients))
