(ns su.myexe.invoke
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [go <!]]
            [re-frame.core :as rf]))

;; FIXME Move to config
(def BASE_URL "http://localhost:3000/")

(def action->method
  {:list :get
   :get :get
   :create :put
   :update :put
   :delete :delete
   :validate :post})

(def route->uri
  {:patient "patient"
   :patient/validate "patient/validate"})

(defn normalise-uri
  [uri id]
  (if id
    (str (route->uri uri) "/" id)
    (route->uri uri)))

(defn- request
  [{:keys [method
           uri
           query-params
           params
           on-success
           on-failure
           on-done]}]
  (go (let [url (str BASE_URL uri)
            response (<! (http/request {:method method
                                        :url url
                                        :query-params query-params
                                        :edn-params params
                                        :with-credentials? false}))]
        (if (= 200 (:status response))
          (when on-success (on-success (:body response)))
          (when on-failure (on-failure response)))
        (when on-done (on-done response)))))

(rf/reg-fx ::request
           (fn [params]
             (request params)))

(rf/reg-event-fx ::invoke
  (fn [_ [_ {:keys [ds error-ds on-success] :as params}]]
    (let [on-success (fn [body]
                       (when ds
                         (if-let [errors (:errors body)]
                           (rf/dispatch [:kit.ds/set-errors ds (reduce (fn [acc v]
                                                                         (assoc-in acc [(:id v) (:field v)] v))
                                                                       {}
                                                                       errors)])
                           (if (sequential? body)
                             (rf/dispatch [:kit.ds/set-data ds (reduce (fn [acc v]
                                                                         (assoc acc (:id v) v))
                                                                       {}
                                                                       body)])
                             (rf/dispatch [:kit.ds/set-record ds (:id body) body]))))
                       (when error-ds
                         (if-let [errors (:errors body)]
                           (rf/dispatch [:kit.ds/set-errors error-ds (reduce (fn [acc v]
                                                                               (update-in acc [(:id v) (:field v)] conj v))
                                                                             {}
                                                                             errors)])
                           (rf/dispatch [:kit.ds/set-errors error-ds nil])))
                       (when on-success
                         (on-success body)))]
      {::request (assoc params :on-success on-success)})))

(defn invoke
  ([action route]
   (invoke action route nil))
  ([action route id]
   (invoke action route id nil))
  ([action route id params]
   (let [method (action->method action)
         params-kwd (if (= :get method)
                      :query-params
                      :params)]
     [::invoke {:method method
                :uri (normalise-uri route id)
                params-kwd params}])))

(defn set-ds
  [[event args] ds]
  [event (assoc args :ds ds)])

(defn set-error-to-ds
  [[event args] ds]
  [event (assoc args :error-ds ds)])

(defn on-success
  [[event args] on-success]
  [event (assoc args :on-success on-success)])

(defn on-failure
  [[event args] on-failure]
  [event (assoc args :on-failure on-failure)])

(defn on-done
  [[event args] on-done]
  [event (assoc args :on-done on-done)])