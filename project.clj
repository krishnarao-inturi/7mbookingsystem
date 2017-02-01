(defproject sevenm-booking-system "0.1.0-SNAPSHOT"
  :dependencies
  [[org.clojure/clojure        "1.8.0"]
   [org.clojure/core.async "0.1.346.0-17112a-alpha"]
   [org.clojure/clojurescript  "1.9.89"]
   [reagent "0.6.0-rc"]
   [binaryage/devtools "0.6.1"]
   [re-frame "0.8.0"]
   [secretary "1.2.3"]
   [bidi "2.0.10"]
   [kibu/pushy "0.3.6"]
   ;; [cljsjs/chosen "1.4.2-1"]
   [bouncer "1.0.0"]
   [cljsjs/react-bootstrap "0.30.2-0"]
   [cljs-ajax "0.5.8"]
   [alandipert/storage-atom "1.2.4"]
   [cljsjs/react-select "1.0.0-rc.1"]
   [clj-http "2.3.0"]
   [cheshire "5.6.3"]
   [clj-oauth "1.5.5"]
   [migratus "0.8.6"]
   [org.postgresql/postgresql "9.4-1203-jdbc42"]
   [org.clojure/java.jdbc "0.4.2"]
   [ring "1.5.0"]
   [yesql "0.5.1"]
   [prismatic/dommy "1.1.0"]
   [com.cognitect/transit-cljs "0.8.225"]
   [ring/ring-defaults "0.2.1"]
   [ring/ring-jetty-adapter "1.4.0"]
   [compojure "1.5.1"]
   [ring/ring-json "0.4.0"]
   [ring-cors "0.1.7"]
   [clj-time "0.11.0"]
   [cc.qbits/jet "0.7.1"]
   [clj-time "0.11.0"]
   [crypto-password "0.1.3"]
   [clojurewerkz/money "1.9.0"]
   [com.andrewmcveigh/cljs-time "0.4.0"]
   [hodgepodge "0.1.3"]
   [clj-pdf "2.2.0"]
   [com.draines/postal "1.11.3"]
   [org.eclipse.jetty/jetty-server "9.3.3.v20150827"]
   [buddy/buddy-hashers "0.4.0"]
   [buddy/buddy-auth "0.4.0"]
   [crypto-random "1.2.0"]
   [hodgepodge "0.1.3"]
   [jarohen/chime "0.1.9"]
   [dk.ative/docjure "1.11.0"]]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-ring "0.9.7"]
            [migratus-lein "0.1.7"]
            [lein-figwheel "0.5.4-3"]]

  :min-lein-version "2.5.3"
  :source-paths ["src/clj"]
  :main sevenm-booking-system.core
  :aot [sevenm-booking-system.core]
  :ring {:handler sevenm-booking-system.handler/app}
  :migratus {:store :database
             :migration-dir "migrations/"
             :db {:classname "org.postgresql.Driver"
                  :subprotocol "postgresql"
                  :subname "//tours7m.csxtllrc3qzc.us-west-2.rds.amazonaws.com:5432/tours7m"
                  :user "venkat"
                  ;; :subname "//localhost:5432/7mtours"
                  ;; :user "postgres"
                  :password "Design_20"}}

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles {:dev
             {:dependencies [[javax.servlet/servlet-api "2.5"]
                             [ring-mock "0.1.5"]]
              :plugins      [[lein-figwheel "0.5.4-3"]]}}
  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "sevenm-booking-system.core/mount-root"}
     :compiler     {:main                 sevenm-booking-system.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :foreign-libs
                    [{:file "resources/public/js/bootstrap-datepicker.min.js"
                      :provides ["vendor.datepicker"]}
                     {:file "resources/public/js/bootstrap-timepicker.min.js"
                      :provides ["vendor.timepicker"]}
                     {:file "resources/public/js/star-rating.js"
                      :provides ["vendor.star-rating"]}
                     {:file "resources/public/js/bootstrap-notify.min.js"
                      :provides ["vendor.notify"]}
                     #_{:file "resources/public/js/chosen.min.js"
                        :provides ["vendor.chosen"]}]
                    :externs ["resources/public/vendor/my-externs.js"]
                    }}
    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            sevenm-booking-system.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :foreign-libs
                    [{:file "resources/public/js/bootstrap-datepicker.min.js"
                      :provides ["vendor.datepicker"]}
                     {:file "resources/public/js/bootstrap-timepicker.min.js"
                      :provides ["vendor.timepicker"]}
                     {:file "resources/public/js/star-rating.js"
                      :provides ["vendor.star-rating"]}
                     {:file "resources/public/js/bootstrap-notify.min.js"
                      :provides ["vendor.notify"]}
                     #_{:file "resources/public/js/chosen.min.js"
                        :provides ["vendor.chosen"]}]
                    :externs ["resources/public/vendor/my-externs.js"]
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}]})
