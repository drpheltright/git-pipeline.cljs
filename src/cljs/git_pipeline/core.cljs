(ns git-pipeline.core
  (:require [goog.events :as events]
            [bidi.bidi :as bidi]
            [quiescent.core :as q]
            [goog.dom :as gdom]
            [git-pipeline.ui.app :as app]
            [git-pipeline.ui.session :as session]
            [git-pipeline.ui.github :as github]
            [git-pipeline.routes :as routes])
  (:import [goog History]
           [goog.history EventType]))


(defn init
  "Initialises application"
  []

  ;; Output println to console
  ;;
  (enable-console-print!)

  (println "Initialising application")

  ;; Default application state
  ;;
  (def store (atom {:pages {:sign-in session/SignIn
                            :repos github/Repos}
                    :page nil
                    :path nil
                    :token nil}))


  ;; Just for logging application state changes
  ;;
  ;; (add-watch store :path-logger
  ;;   (fn [key store state next-state]
  ;;     (println next-state)))

  ;; Rebuilds app everytime state changes provided it can find a page
  ;; component to render.
  ;;
  (add-watch store :re-renderer
    (fn [key store state next-state]
      (if-let [page-component (:page next-state)]
        (do
          (println "Rendering application")
          (q/render (app/App @store page-component store) (gdom/getElement "app"))))))

  ;; Everytime application state :path changes we dispatch secretary.
  ;; Secretary will then update application state :page-component
  ;; which represents main page component for that URL.
  ;;
  (add-watch store :path-dispatcher
    (fn [key store state next-state]
      (if (not= (:path state) (:path next-state))
        (if-let [page-component (get (:pages next-state) (routes/path->component (:path next-state)))]
          (swap! store assoc :page page-component)))))

  ;; History will update application state :path everytime URL changes
  ;;
  (let [history (History.)]
    (events/listen history EventType.NAVIGATE
      (fn [e]
        (let [token (.-token e)]
          (swap! store assoc :path (if (empty? token) "/" token)))))
    (.setEnabled history true)))

(defn reload
  "Forces reload via changing application state"
  []
  (let [now (js/Date.)]
    (println "Reloading application" (str now))
    (swap! store assoc :reload-time now)))
