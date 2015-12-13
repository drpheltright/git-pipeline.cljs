(ns git-pipeline.ui.app
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [git-pipeline.routes :as routes]
            [git-pipeline.data.sessions :as data]))

(q/defcomponent App
  :name "App"
  [data page-component store]
  (dom/div {:className "app"}
    (dom/nav {:className "app__nav"}
      (if (empty? (:token data))
        (dom/a {:href (routes/component->path :login)} "sign in")
        (dom/a {:onClick #(data/github-oauth-logout store)} "sign out")))
    (page-component data store)))
