(ns su.myexe.route.patient
  (:require [compojure.core :as cc]
            [su.myexe.api.patient :as patient]
            [su.myexe.utils.route :as r]))

(cc/defroutes routes
  (cc/context "/patient" []
    (cc/GET "/" params (r/call-api patient/list-patient params))
    (cc/GET "/:id" params (r/call-api patient/get-patient params))
    (cc/PUT "/" params (r/call-api patient/create-patient params))
    (cc/PUT "/:id" params (r/call-api patient/update-patient params))
    (cc/DELETE "/:id" params (r/call-api patient/delete-patient params))
    (cc/POST "/:id/validate" params (r/call-api patient/validate-patient params))))