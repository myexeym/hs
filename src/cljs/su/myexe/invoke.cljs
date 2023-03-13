(ns su.myexe.invoke
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [go <!]]
            [re-frame.core :as rf]))

(def BASE_URL "http://localhost:3000/")

(def action->method
  {:list :get
   :get :get
   :create :put
   :update :put
   :delete :delete})

(def route->uri
  {:patient "patient"})

(defn normalise-uri
  [uri id]
  (if id
    (str (route->uri uri) "/" id)
    (route->uri uri)))

(defn- request
  [{:keys [method
           uri
           query-params
           on-success
           on-failure
           on-done]}]
  (go (let [url (str BASE_URL uri)
            response (<! (http/request {:method method
                                        :url url
                                        :query-params query-params
                                        :with-credentials? false}))]
        (if (= 200 (:status response))
          (when on-success (on-success (:body response)))
          (when on-failure (on-failure response)))
        (when on-done (on-done response)))))

(rf/reg-fx ::request
  (fn [params]
    (request params)))

(rf/reg-event-fx ::invoke
  (fn [_ [_ {:keys [ds on-success] :as params}]]
    (let [on-success (fn [body]
                       (when ds
                         (rf/dispatch [:kit.ds/set-data ds body]))
                       (when on-success
                         (on-success body)))]
      {::request (assoc params :on-success on-success)})))

(defn invoke
  ([action route]
   (invoke action route nil))
  ([action route id]
   (invoke action route id nil))
  ([action route id query-params]
   [::invoke {:method (action->method action)
              :uri (normalise-uri route id)
              :query-params query-params}]))

(defn set-ds
  [[event args] ds]
  [event (assoc args :ds ds)])

(defn on-success
  [[event args] on-success]
  [event (assoc args :on-success on-success)])

(defn on-failure
  [[event args] on-failure]
  [event (assoc args :on-failure on-failure)])

(defn on-done
  [[event args] on-done]
  [event (assoc args :on-done on-done)])