(ns git-pipeline.data.github)

(defn load-repos [store]
  (let [data @store]
    (-> (js/Github. #js{:token (:token data) :auth "oauth"})
        (.getUser)
        (.repos {} #(swap! store assoc :repos %2)))))

(defn load-repo [store user repo-name]
  (let [data @store]
    (-> (js/Github. #js{:token (:token data) :auth "oauth"})
        (.getRepo user repo-name)
        (.listBranches #(swap! store assoc :branches %2)))))
