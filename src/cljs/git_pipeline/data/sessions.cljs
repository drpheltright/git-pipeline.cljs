(ns git-pipeline.data.sessions
  (:require [cljsjs.oauthio]))

(js/OAuth.initialize  "MrqjIqW8jg_SNqpOvvdWvZ4P_w8")

(defn- set-token [store token]
  (swap! store assoc :token token))

(defn github-oauth-login [store handle-success handle-error]
  (-> (js/OAuth.popup "github")
      (.done (fn [result]
               (set-token store (.-access_token result))
               (handle-success)))
      (.fail handle-error)))

(defn github-oauth-logout [store]
  (swap! store assoc :token nil))
