(ns git-pipeline.ui.app
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [git-pipeline.routes :as routes]))

(defn- logout [store e]
  (swap! store assoc :token nil)
  (set! (.-location js/window) (routes/component->path :sign-in)))

(q/defcomponent App
  :name "App"
  [data page-component store]
  (dom/div {:className "app"}
    (dom/nav {:className "app__nav"}
      (if (empty? (:token data))
        (dom/a {:href (routes/component->path :login)} "sign in")
        (dom/a {:onClick (partial logout store)} "sign out")))
    (page-component data store)))
