(ns git-pipeline.routes
  (:require [bidi.bidi :as bidi]))

(def routes
  ["/" {"" :sign-in
        "repos/" {"" :repos
                  [:user "/" :name] :repo}}])

(defn path->component [path]
  (:handler (bidi/match-route routes path)))

(defn path->params [path]
  (:route-params (bidi/match-route routes path)))

(defn component->path
  ([component] (str "/#" (bidi/path-for routes component)))
  ([component params] (str "/#" (component->path component params true)))
  ([component params without-prefix] (apply (partial bidi/path-for routes component)
                                       (flatten (into [] params)))))
