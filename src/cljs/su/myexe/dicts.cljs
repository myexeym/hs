(ns su.myexe.dicts
  (:require [tongue.core :as tongue]))

(def dicts
  {:en {:date-format "dd.MM.yyyy"
        :su.myexe.ui.view.patient.core {:full-name "Full Name"
                                        :gender "Gender"
                                        :birthday "Birthday"
                                        :address "Address"
                                        :policy-number "Policy Number"
                                        :woman "Woman"
                                        :man "Man"
                                        :save "Save"
                                        :validate "Validate"}
        :su.myexe.ui.view.patients.core {:full-name "Full Name"
                                         :gender "Gender"
                                         :birthday "Birthday"
                                         :address "Address"
                                         :policy-number "Policy Number"
                                         :woman "Woman"
                                         :man "Man"
                                         :search "Search"
                                         :add "Add"}

        :validation {:field-require "Required"
                     :too-young "Minimum age must be 18 years old"
                     :incorrect-policy "Must be of 16 digits"}}
   :tongue/fallback :en})

(def translate
  (tongue/build-translate dicts))