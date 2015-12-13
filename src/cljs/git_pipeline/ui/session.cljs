(ns git-pipeline.ui.session
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [git-pipeline.routes :as routes]
            [git-pipeline.data.sessions :as data]))

(defn- handle-sign-in []
  (set! (.-location js/window) (routes/component->path :repos)))

(defn- handle-error [error]
  (println error))

(defn- handle-form-submit [e store]
  (.preventDefault e)
  (println "logging in")
  (data/github-oauth-login store handle-sign-in handle-error))

(q/defcomponent SignIn
  :name "SignIn"
  [data store]
  (dom/form {:onSubmit #(handle-form-submit % store)}
    (dom/button {} "Sign in with GitHub")))
