(ns sevenm-booking-system.dbmigrations
  (:require [migratus.core :as migratus]))

(def config {:store :database
             :migration-dir "migrations/"
             :db {:classname "org.postgresql.Driver"
                  :subprotocol "postgresql"
                  :subname "//tours7m.csxtllrc3qzc.us-west-2.rds.amazonaws.com:5432/tours7m"
                  ;; :subname "//localhost:5432/7mtours"
                  :user "venkat"
                  :password "Design_20"}})
