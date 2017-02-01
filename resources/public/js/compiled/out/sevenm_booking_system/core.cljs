(ns sevenm-booking-system.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [devtools.core :as devtools]
              [sevenm-booking-system.handlers]
              [sevenm-booking-system.subs]
              [sevenm-booking-system.routes :as routes]
              [sevenm-booking-system.views :as views]
              [sevenm-booking-system.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")
    (devtools/install!)))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))

(init)
