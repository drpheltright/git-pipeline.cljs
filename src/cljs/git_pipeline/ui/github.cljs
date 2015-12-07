(ns git-pipeline.ui.github
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]))

(defn- load-repos [_ data store]
  (-> (js/Github. #js{:token (:token data) :auth "oauth"})
      (.getUser)
      (.repos {} #(swap! store assoc :repos %2))))

(q/defcomponent Repos
  :name "Repos"
  :on-mount load-repos
  [data store]
  (let [repo-urls (map #(.-html_url %) (:repos data))]
    (dom/ul {} (map #(dom/li {} %) repo-urls))))
