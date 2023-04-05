(ns su.myexe.handler
  (:require [compojure.core :as cc]
            [compojure.route :as cr]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.x-headers :refer [wrap-xss-protection]]
            [su.myexe.route.patient :as patient]))

(cc/defroutes routes
  patient/routes
  (cr/not-found "Error, page not found!"))

(defn wrap-app-params
  "Add `app-params` to request."
  [handler app-params]
  (fn [request]
    (handler (assoc request :app-params app-params))))

(defn app
  [app-params]
  (-> (wrap-reload #'routes)
      (wrap-xss-protection true)
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :put :post :delete :options])
      (wrap-app-params app-params)
      (wrap-restful-format :formats [:edn])
      wrap-keyword-params
      wrap-params))