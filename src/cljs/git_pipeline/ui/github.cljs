(ns git-pipeline.ui.github
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [git-pipeline.routes :as routes]
            [git-pipeline.data.github :as data]))

(defn- repo-link [repo]
  (let [repo-url (.-html_url repo)
        repo-user (.. repo -owner -login)
        repo-name (.-name repo)
        href (routes/component->path :repo {:user repo-user :name repo-name})]
    (dom/li {}
      (dom/a {:href href} repo-url))))

(defn- handle-repos-mount [e data store]
  (data/load-repos store))

(defn- handle-repo-mount [e data store]
  (let [[user repo-name] (vals (select-keys (:params data) [:user :name]))]
    (data/load-repo store user repo-name)))

(q/defcomponent Repos
  :name "Repos"
  :on-mount handle-repos-mount
  [data store]
  (dom/div {}
    (dom/h1 {} "Repositories")
    (dom/ul {} (map repo-link (:repos data)))))

(q/defcomponent Repo
  :name "Repo"
  :on-mount handle-repo-mount
  [data store]
  (println (:params data))
  (dom/div {}
    (dom/h1 {} (vals (:params data)))
    (dom/ul {} (map #(dom/li {} %) (:branches data)))
    (dom/a {:href (routes/component->path :repos)} "Back to repositories")))
