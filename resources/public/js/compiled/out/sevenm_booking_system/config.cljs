(ns sevenm-booking-system.config
  (:require [alandipert.storage-atom :refer [local-storage]]))

(def debug? ^boolean js/goog.DEBUG)


(def session (local-storage (atom {}) :session))

(def server "http://localhost:8001/")

;; (def server "http://103.5.16.114:8002/")
;; (def server "http://52.38.182.97:81/")
;; (def server "http://costing.7mtours.com:81/")
