(ns git-pipeline.ui.github
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [git_pipeline.routes :as routes]))

(defn- load-repos [_ data store]
  (-> (js/Github. #js{:token (:token data) :auth "oauth"})
      (.getUser)
      (.repos {} #(swap! store assoc :repos %2))))

(defn- load-repo [_ data store]
  (-> (js/Github. #js{:token (:token data) :auth "oauth"})
      (.getRepo (-> data :params :user)
                (-> data :params :name))
      (.listBranches #(swap! store assoc :branches %2))))

(defn- repo-link [repo]
  (let [repo-url (.-html_url repo)
        repo-user (.. repo -owner -login)
        repo-name (.-name repo)
        href (routes/component->path :repo {:user repo-user :name repo-name})]
    (dom/li {}
      (dom/a {:href href} repo-url))))

(q/defcomponent Repos
  :name "Repos"
  :on-mount load-repos
  [data store]
  (dom/ul {} (map repo-link (:repos data))))

(q/defcomponent Repo
  :name "Repo"
  :on-mount load-repo
  [data store]
  (println (:params data))
  (dom/div {}
    (dom/h1 {} (vals (:params data)))
    (dom/ul {} (map #(dom/li {} %) (:branches data)))))
