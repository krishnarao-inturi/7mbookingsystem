(ns sevenm-booking-system.db.core
  (:require
   [yesql.core :refer [defqueries]]))

(def conn
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//tours7m.csxtllrc3qzc.us-west-2.rds.amazonaws.com:5432/tours7m"
   ;; :subname "//localhost:5432/7mtours"
   :user "venkat"
   :password "Design_20"})

(defqueries "sql/queries.sql" {:connection conn})
