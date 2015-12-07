(ns git-pipeline.ui.session
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [cljsjs.oauthio]
            [git-pipeline.routes :as routes]))

; OAuth.popup('facebook')
;     .done(function(result) {})
;       //use result.access_token in your API request
;       //or use result.get|post|put|del|patch|me methods (see below)
;
;     .fail(function (err) {})
;       //handle error with err
; ;
(js/OAuth.initialize  "MrqjIqW8jg_SNqpOvvdWvZ4P_w8")

(defn- handle-sign-in [store result]
  (swap! store assoc :token (.-access_token result))
  (set! (.-location js/window) (routes/component->path :repos)))

(defn- handle-error [error] (println error))

(defn- handle-form-submit [e store]
  (.preventDefault e)
  (println "logging in")
  (-> (js/OAuth.popup "github")
      (.done (partial handle-sign-in store))
      (.fail handle-error)))

(q/defcomponent SignIn
  :name "SignIn"
  [data store]
  (dom/form {:onSubmit #(handle-form-submit % store)}
    (dom/button {} "Sign in with GitHub")))
