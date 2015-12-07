(ns git-pipeline.routes
  (:require [bidi.bidi :as bidi]))

(def routes
  ["/" {"" :sign-in
        "/repos" :repos}])

(defn path->component [path]
  (:handler (bidi/match-route routes path)))

(defn component->path [component]
  (str "/#" (bidi/path-for routes component)))
