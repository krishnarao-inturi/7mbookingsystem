(ns sevenm-booking-system.core
  (:require [qbits.jet.server :refer [run-jetty]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [sevenm-booking-system.db.core :as db]
            [compojure.response :refer [render]]
            [clojure.java.io :as io]
            [ring.util.io :refer :all]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [ring.middleware.json :as ring-json]
            [cheshire.core :refer [parse-string ]]
            [ring.util.response	:as rr]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.local :as l]
            [ring.middleware.file :as fi]
            [postal.core :refer :all]
            [buddy.core.nonce :as nonce]
            [buddy.core.codecs :as codecs]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.token :refer [token-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [buddy.hashers :as hashers]
            [sevenm-booking-system.auth :refer [make-token!]]
            [clojure.string :as str :refer [split]]
            [clojurewerkz.money.amounts :as ma]
            [clojurewerkz.money.currencies :as mc]
            [clojurewerkz.money.format :as mf]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clojure.set :as rk]
            [clojure.string :as str]
            [chime :refer [chime-at]]
            [clj-time.periodic :refer [periodic-seq]]
            [sevenm-booking-system.quick-books-api
             :refer [post-restaurent-vendor
                     post-tour-guide-vendor
                     post-transportportation-vendor]]
            [cheshire.core :refer [parse-string]]
            [compojure.coercions :refer [as-int]]
            [dk.ative.docjure.spreadsheet :refer [create-workbook
                                                  select-sheet
                                                  set-row-style!
                                                  create-cell-style!
                                                  save-workbook!
                                                  row-seq]]
            [clojure.java.jdbc :as j])
  (:import [org.eclipse.jetty.server.handler.gzip GzipHandler])
  (:import java.util.Locale)
  (:import [org.joda.time DateTimeZone])
  (:use clj-pdf.core))

(defmulti parse-int type)
(defmethod parse-int java.lang.Integer [n] n)
(defmethod parse-int java.lang.String [s]  (Integer/parseInt s))

(def content-type  "application/json; charset=utf-8")

(defn map-to-db-data [keys body]
  (into {} (map (fn [s] (vector (keyword s) (get body s)))
                keys)))
(defn get-token-from-request [headers]
  (last (split (headers "authorization") #" ")))


;; Simple function that works as controller
;; It should return a proper response. In our
;; case it returns a content of static index.html.

(defn home [req]
  (render (io/resource "public/index.html") req))

(defn create [user]
  (let [usr (assoc user :password (hashers/encrypt (:password user)))]
    (db/register usr)
    (dissoc usr :password)))

(defn validate-password [credentials]
  (let [usr (first (db/get-user {:email (:email credentials)}))]
    (if (and usr (hashers/check (:password credentials) (:password usr)))
      (hash-map
       :status 200,
       :body {:user (dissoc usr :password)
              :token (make-token! (:id usr))})
      (hash-map :status 401,
                :body {:message "invalid credentials"}))))

(defn my-authfn
  [req token]
  (when-let [user (first (db/validate-token
                          {:token token}))]
    user))

(def auth-backend
  (token-backend {:authfn my-authfn}))

(defn users-search [index pagesize type text]
  (fn [request]
    (if type
      (case type
        "username" (if-not (authenticated? request)
                     (throw-unauthorized)
                     (rr/content-type
                      (rr/response {:totalusers
                                    (:totalusers
                                     (first  (db/get-total-users-by-email
                                              {:text text
                                               })))
                                    :data (db/get-all-users-by-index-pagesize-by-email
                                           {:index (parse-int index)
                                            :pagesize (parse-int pagesize)
                                            :text text
                                            })}) content-type)))
      (if-not (authenticated? request)
        (throw-unauthorized)
        (let [res (db/get-all-users-by-index-pagesize
                   {:index (parse-int index)
                    :pagesize (parse-int pagesize)})]
          (rr/content-type
           (rr/response
            {:totalusers
             (:totalusers
              (first
               (db/get-total-users)))
             :data res})
           content-type))))))

(defn user-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-user body)))
       content-type))))

(defn bookings [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        {:totalbookings (:totalbookings (first (db/get-total-bookings)))
         :data (db/get-all-bookings-by-index-pagesize
                {:index (parse-int index)
                 :pagesize (parse-int pagesize)
                 })})
       content-type))))

(defn fit-bookings [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        {:totalbookings (:totalbookings (first (db/get-fit-total-bookings)))
         :data (db/get-all-fit-bookings-by-index-pagesize
                {:index (parse-int index)
                 :pagesize (parse-int pagesize)
                 })})
       content-type))))

(defn fit-bookings-search [index pagesize type text]
  (fn [request]
    (let [user_id (:user_id (:identity request))
          user (if (nil? user_id) {} (first
                                      (db/get-user-details
                                       {:user_id user_id})))]
      (if type
        (case type
          "agentname" (if-not (authenticated? request)
                        (throw-unauthorized)
                        (rr/content-type
                         (rr/response
                          {:totalbookings
                           (:totalbookings
                            (first (if (= (:role user) "bookadmin")
                                     (db/get-all-fit-bookings-count-by-agentname
                                      {:text text})
                                     (db/get-all-fit-bookings-count-by-agentname-by-user
                                      {:text text
                                       :userid (:id user)}))))
                           :data (if (= (:role user) "bookadmin")
                                   (db/get-all-fit-bookings-by-index-pagesize-agentname
                                    {:index (parse-int index)
                                     :pagesize (parse-int pagesize)
                                     :text text})
                                   (db/get-all-fit-bookings-by-index-pagesize-agentname-user
                                    {:index (parse-int index)
                                     :pagesize (parse-int pagesize)
                                     :text text
                                     :userid (:id user)}))}) content-type))
          "quotationnumber" (if-not (authenticated? request)
                              (throw-unauthorized)
                              (rr/content-type
                               (rr/response
                                {:totalbookings
                                 (:totalbookings
                                  (first (if (= (:role user) "bookadmin")
                                           (db/get-all-fit-bookings-count-by-quotationnumber
                                            {:text text})
                                           (db/get-all-fit-bookings-count-by-quotationnumber-user
                                            {:text text
                                             :userid (:id user)}))))
                                 :data (if (= (:role user) "bookadmin")
                                         (db/get-all-fit-bookings-by-index-pagesize-quotationnumber
                                          {:index (parse-int index)
                                           :pagesize (parse-int pagesize)
                                           :text text})
                                         (db/get-all-fit-bookings-by-index-pagesize-quotationnumber-user
                                          {:index (parse-int index)
                                           :pagesize (parse-int pagesize)
                                           :text text
                                           :userid (:id user)}))}) content-type))
          "bookingnumber" (if-not (authenticated? request)
                            (throw-unauthorized)
                            (rr/content-type
                             (rr/response
                              {:totalbookings
                               (:totalbookings
                                (first (if (= (:role user) "bookadmin")
                                         (db/get-all-fit-bookings-count-by-bookingnumber
                                          {:text text})
                                         (db/get-all-fit-bookings-count-by-bookingnumber-user
                                          {:text text
                                           :userid (:id user)}))))
                               :data (if (= (:role user) "bookadmin")
                                       (db/get-all-fit-bookings-by-index-pagesize-bookingnumber
                                        {:index (parse-int index)
                                         :pagesize (parse-int pagesize)
                                         :text text})
                                       (db/get-all-fit-bookings-by-index-pagesize-bookingnumber-user
                                        {:index (parse-int index)
                                         :pagesize (parse-int pagesize)
                                         :text text
                                         :userid (:id user)}))}) content-type)))
        (if-not (authenticated? request)
          (throw-unauthorized)
          (rr/content-type
           (rr/response
            {:totalbookings (:totalbookings
                             (first (if (= (:role user) "bookadmin")
                                      (db/get-fit-total-bookings)
                                      (db/get-fit-total-bookings-by-user
                                       {:userid (:id user)}))))
             :data (if (= (:role user) "bookadmin")
                     (db/get-all-fit-bookings-by-index-pagesize
                      {:index (parse-int index)
                       :pagesize (parse-int pagesize)})
                     (db/get-all-fit-bookings-by-index-pagesize-user
                      {:index (parse-int index)
                       :pagesize (parse-int pagesize)
                       :userid (:id user)}))})
           content-type))))))

(defn groups-bookings [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        {:totalbookings (:totalbookings (first (db/get-groups-total-bookings)))
         :data (db/get-all-groups-bookings-by-index-pagesize
                {:index (parse-int index)
                 :pagesize (parse-int pagesize)
                 })})
       content-type))))

(defn groups-bookings-search [index pagesize type text]
  (fn [request]
    (let [user_id (:user_id (:identity request))
          user (if (nil? user_id) {} (first
                                      (db/get-user-details
                                       {:user_id user_id})))]
      (if type
        (case type
          "agentname" (if-not (authenticated? request)
                        (throw-unauthorized)
                        (rr/content-type
                         (rr/response
                          {:totalbookings
                           (:totalbookings
                            (first (if (= (:role user) "bookadmin")
                                     (db/get-all-group-bookings-count-by-agentname
                                      {:text text})
                                     (db/get-all-group-bookings-count-by-agentname-user
                                      {:text text :user_id user_id}))))
                           :data (if (= (:role user) "bookadmin")
                                   (db/get-all-group-bookings-by-index-pagesize-agentname
                                    {:index (parse-int index)
                                     :pagesize (parse-int pagesize)
                                     :text text})
                                   (db/get-all-group-bookings-by-index-pagesize-agentname-user
                                    {:index (parse-int index)
                                     :user_id user_id
                                     :pagesize (parse-int pagesize)
                                     :text text}))}) content-type))
          "quotationnumber" (if-not (authenticated? request)
                              (throw-unauthorized)
                              (rr/content-type
                               (rr/response
                                {:totalbookings
                                 (:totalbookings
                                  (first (if (= (:role user) "bookadmin")
                                           (db/get-all-group-bookings-count-by-quotationnumber
                                            {:text text})
                                           (db/get-all-group-bookings-count-by-quotationnumber-user
                                            {:text text :user_id user_id }))))
                                 :data (if (= (:role user) "bookadmin")
                                         (db/get-all-group-bookings-by-index-pagesize-quotationnumber
                                          {:index (parse-int index)
                                           :pagesize (parse-int pagesize)
                                           :text text})
                                         (db/get-all-group-bookings-by-index-pagesize-quotationnumber-user
                                          {:index (parse-int index)
                                           :pagesize (parse-int pagesize)
                                           :text text
                                           :user_id user_id}))}) content-type))
          "bookingnumber" (if-not (authenticated? request)
                            (throw-unauthorized)
                            (rr/content-type
                             (rr/response
                              {:totalbookings
                               (:totalbookings
                                (first (if (= (:role user) "bookadmin")
                                         (db/get-all-group-bookings-count-by-bookingnumber
                                          {:text text})
                                         (db/get-all-group-bookings-count-by-bookingnumber-user
                                          {:text text :user_id user_id}))))
                               :data (if (= (:role user) "bookadmin")
                                       (db/get-all-group-bookings-by-index-pagesize-bookingnumber
                                        {:index (parse-int index)
                                         :pagesize (parse-int pagesize)
                                         :text text})
                                       (db/get-all-group-bookings-by-index-pagesize-bookingnumber-user
                                        {:index (parse-int index)
                                         :pagesize (parse-int pagesize)
                                         :user_id user_id
                                         :text text}))}) content-type)))
        (if-not (authenticated? request)
          (throw-unauthorized)
          (rr/content-type
           (rr/response
            {:totalbookings (:totalbookings
                             (first (if (= (:role user) "bookadmin")
                                      (db/get-groups-total-bookings)
                                      (db/get-groups-total-bookings-user
                                       {:user_id user_id}))))
             :data (if (= (:role user) "bookadmin")
                     (db/get-all-groups-bookings-by-index-pagesize
                      {:index (parse-int index)
                       :pagesize (parse-int pagesize)})
                     (db/get-all-groups-bookings-by-index-pagesize-user
                      {:index (parse-int index)
                       :pagesize (parse-int pagesize)
                       :user_id user_id}))})
           content-type))))))


(defn packages [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        {:totalpackages (:totalpackages (first (db/get-total-packages)))
         :data (db/get-all-packages-by-index-pagesize
                {:index (parse-int index)
                 :pagesize (parse-int pagesize)
                 })})
       content-type))))

(defn packages1 [index pagesize name]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        {:totalpackages (:totalpackages (first (db/get-total-packages-by-name {:name name})))
         :data (db/get-all-packages-by-index-pagesize-name
                {:index (parse-int index)
                 :pagesize (parse-int pagesize)
                 :name name
                 })})
       content-type))))

(def built-in-formatter (f/formatters :basic-date-time))


(defn stringToDate [x]
  (.toDate (f/parse (f/formatter "dd-MMM-yyyy") x)))

(defn date-to-string [date]
  (.format (java.text.SimpleDateFormat. "dd-MMM-yyyy") date))

(defn date-to-string-report [date]
  (.format (java.text.SimpleDateFormat. "dd-MMM") date))


(def multi-parser (f/formatter (t/default-time-zone) "YYYY-MM-dd" "dd-MMM-yyyy"))

(defn pdf-cell1 [x]
  [:cell {:min-height 20
          :align :center
          :valign :middle
          :background-color  [252, 255, 255]
          :border false }
   x]
  )

(defn pdf-cell2 [x]
  [:cell {:min-height 5
          :align :center
          :valign :middle
          :background-color  [252, 255, 255]
          :border false }
   x]
  )

(defn get-citiname [x]
  (first(db/get-cities-by-id {:id x})))

(defn get-hotelname [x]
  (first (db/get-hotels-by-id {:id x})))

(defn get-items-description [x]
  (first (db/get-items-by-id {:id x})))

(defn get-vendors-name [x]
  (println x)
  (first (db/get-vendors-view {:text x})))


(defn get-vendors-name-by-category [vendorid category]
  (let [low-case-cat (str/lower-case category)
        id (parse-int vendorid)]
    (cond (= low-case-cat "attractions")(db/get-events-name-by-id
                                         {:id id})
          (= low-case-cat "guidedcitytours")(db/get-tour-guides-name-by-id
                                             {:id id})
          (= low-case-cat "meals")(db/get-restaurents-name-by-id
                                   {:id id})
          (= low-case-cat "hotel")(db/get-hotels-name-by-id
                                   {:id id})
          (= low-case-cat "transportation") (db/get-transportation-name-by-id
                                             {:id id})
          :else [])))


(defn get-events-description [x]
  (first (db/get-events-by-id {:id x})))

(defn get-restaurants-description [x]
  (first (db/get-restaurants-by-id {:id x})))

(defn get-tourguides-description [x]
  (first (db/get-tourguides-by-id {:id x})))

(defn get-transportation-description [x]
  (first (db/get-transportation-by-id {:id x})))

(defn get-logos-filename [x]
  (first (db/get-logos-by-id {:id x})))

(defn get-description [item-str]
  (let [item-str-id (str/split item-str #"_")
        item-type (first item-str-id)
        id (parse-int (last item-str-id))]
    (case item-type
      "E" (get-events-description id)
      "R" (get-restaurants-description id)
      "TG"  (get-tourguides-description id)
      "TP"  (get-transportation-description id))))


(defn table-cell1 [x]
  [:cell { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9 }  x]
  #_[:cell {:size 14 :family :arial} x]
  )
(defn table-cell2 [x]
  #_[:cell {:ttf-name "resources/public/fonts/OpenSans-Bold.ttf"  :size 12} x]
  [:cell {:ttf-name "resources/public/fonts/OpenSans-Regular.ttf" :style :bold :size 11} x]
  #_[:cell {:size 16 :family :arial :style :bold} x]
  )


(defn table-cell3 [x]
  [:cell { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9 :align :center }  x]
  #_[:cell {:size 14 :family :arial :align :center} x]
  )

(defn table-cell4 [x]
  #_[:cell {:ttf-name "resources/public/fonts/OpenSans-Bold.ttf"  :size 12} x]
  [:cell {:ttf-name "resources/public/fonts/OpenSans-Regular.ttf" :style :bold :size 11 :colspan 3 :align :center} x]
  #_[:cell {:size 16 :family :arial :style :bold} x]
  )

(defn table-cell5 [x]
  [:cell {:ttf-name "resources/public/fonts/OpenSans-Regular.ttf" :style :bold :size 9 :align  :center } x]
  #_[:cell {:size 16 :family :arial :style :bold} x]
  )


(defn paragraph-cell1 [x]
  #_[:paragraph {:ttf-name "resources/public/fonts/OpenSans-Bold.ttf"  :size 12} x]
  [:paragraph {:ttf-name "resources/public/fonts/OpenSans-Regular.ttf" :style :bold :size 11} x]
  #_[:paragraph {:size 16 :family :arial :style :bold} x]
  )

(defn paragraph-cell2 [x]
  [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9 }  x]
  #_[:paragraph {:size 14 :family :arial} x]
  )

(defn group-pdf-cell1 [x]
  [:pdf-cell {:size 11  :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" } x]
  #_[:pdf-cell {:size 16 :family :arial} x]
  )

(defn group-pdf-cell2 [x]
  #_ [:pdf-cell { :size 11  :ttf-name "resources/public/fonts/OpenSans-Bold.ttf" } x]
  [:pdf-cell {:ttf-name "resources/public/fonts/OpenSans-Regular.ttf" :style :bold :size 11} x]
  #_[:pdf-cell {:size 16 :family :arial :style :bold} x]
  )

(defn groups-pdf-cell3 [x]
  [:pdf-cell {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :indent 30 :size 11  :color [72, 141, 69] } x]
  )

(defn groups-pdf-cell4 [x]
  [:pdf-cell {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"  :size 11  :color [72, 141, 69] } x]
  )


#_(defn itinerary-table [itinerarydetails]
    (let [dt (date-to-string (:date
                              (first itinerarydetails)))
          rdt (for [x itinerarydetails]
                (vector (str "7M/VCH/" (:id x)) (:cityname (get-citiname (:cityid x))) (:date x) (:time x)
                        (if (= "0"  (:itemid x))
                          (str  (:manualdescription x)
                                (when-not (zero? (:numberofpeoples x) )
                                  (str " - " (:numberofpeoples x))))
                          (str (:description (get-items-description (:itemid x)))
                               (when-not (zero? (:numberofpeoples x) )
                                 (str " - " (:numberofpeoples x)))))))]

      (into
       [:table {:header [[:paragraph {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 12 } dt]]
                :widths [1 1 1 1 3]
                :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                :size 11
                :min-height 20
                :align :center
                ;;:valign :middle
                :background-color [255 0 0]

                }
        ;; [(pdf-cell2 dt) (pdf-cell2 " ") (pdf-cell2 "") (pdf-cell2 " ")]
        ["     Voucher" "       City"  "       Date" "       Time"       "                          Description" ]
        ]
       (cell-data rdt)
       )
      )
    )

;;Old PDF
#_(defn cell-data [x]
    #_ (for [i x]
         (list [:cell {:style :bold, :size 11, :family :arial, :colspan 1, :rowspan 1, :align :center, :background-color [235 235 231]} (get (vec i) 0)]
               [:cell {:style :bold, :size 11, :family :arial, :colspan 1, :rowspan 1, :align :center, :background-color [235 235 231]} (get (vec i) 1)]
               [:cell {:style :bold, :size 11, :family :arial, :colspan 1, :rowspan 1, :align :center, :background-color [235 235 231]} (get (vec i) 2)]
               [:cell {:style :bold, :size 11, :family :arial, :colspan 1, :rowspan 1, :align :center, :background-color [235 235 231]} (get (vec i) 3)]))

    (map (partial map (fn [element]
                        [:pdf-cell {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                                    ;;:size 9
                                    :min-height 25
                                    :align :center
                                    ;; :valign :middle
                                    ;;:set-border [:right]
                                    :background-color [255, 255, 255]
                                    :set-border  [:top :bottom]
                                    :border-color  [255, 255, 255]
                                    } element]

                        )) x))

;;New PDF
#_(defn cell-data [x]

    (for [i x]
      (list
       #_[:cell { :colspan 1} ["table" { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 11, :family :arial, :colspan 1  :align :left, :background-color [235 235 231] :widths [1 1 2]}   [ [:image "resources/public/dist/img/flight.jpg"]  (get (vec i) 0) [:cell (get (vec i) 3)]]]]

       [:cell ["table" { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 11, :family :arial, :colspan 1, :rowspan 1, :align :left, :background-color [235 235 231]}   [[:cell [:image {:xscale 0.5 :yscale 0.5 :align  :center :width 10 :height 10}  "resources/public/dist/img/circle.png"]] [:cell  [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9} (get (vec i) 0)] [:paragraph { :ttf-name "resources/public/fonts/OpenSans-ExtraBold.ttf" :size 12} (get (vec i) 1)] [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9} (get (vec i) 2)]]]]]
       [:cell ["table" { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 11 , :colspan 1, :rowspan 1, :align :left, :background-color [235 235 231]}  [[:cell (get (vec i) 3)]]]]

       ))
    )
;;New PDF 2
#_(defn cell-data [x]
    (for [i x]
      #_[:cell [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 12 :style :bold}
                (get (first i) 1)] [:paragraph ""]]
      #_(println (get (first i) 1))


      (for [m i]
        [:cell [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 12 :style :bold}
                (get (vec m) 2) "  "  (get (vec m) 5)]
         [:paragraph { :ttf-name "resources/public/fonts/OpenSans-ExtraBold.ttf" :size 12} (get (vec m) 3)]]
        ))
    )

#_(defn cell-data [x]
    #_(println (get (first (first x)) 1))
    ;;(println x)
    (for [i x]
      #_(println (get (first i) 1))
      [[:cell
        (into
         #_["table" {:header[ [:image "resources/public/dist/img/iti.png" ]
                             #_[:image {:width 100 :height 22 :pad-left 0 :margin-left 0} "resources/public/dist/img/itinerary1.png"]
                             #_[:paragraph {:size 12 :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"  :style :bold  :align :left} [:image "resources/public/dist/img/iti.png" ]  " Itinerary - "(get (first i) 1) "-" (get (first i) 6) ]]
                     :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 11, :family :arial,  :align :left, :background-color [255 255 255]}]
         ["table"
          [[:cell
            [:image {:width 100 :height 22 :pad-left 0 :margin-left 0} "resources/public/dist/img/itinerary.png"]
            [:paragraph {:size 12 :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"  :style :bold  :align :left} (get (first i) 1) " - " (get (first i) 6) ]
            ]]]
         (for [m i]
           [[:cell
             [:image {:float :left :width 10 :height 10}  "resources/public/dist/img/circle.png" [[:paragraph "Circle Tag"]]]
             [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :style :bold  :size 9} (get (vec m) 2) " | " (get (vec m) 1) " - " (get (vec m) 7)]
             #_[:paragraph { :ttf-name "resources/public/fonts/OpenSans-ExtraBold.ttf" :size 12} (get (vec m) 5)]
             [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9} (get (vec m) 3)]]]))
        [:line {:dotted true}]
        ]]
      #_(for [m i]
          [:cell
           ["table" {:header[#_[:image {:width 100 :height 22 :pad-left 0 :margin-left 0} "resources/public/dist/img/itinerary1.png"]
                             #_[:paragraph {:size 12 :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"  :style :bold  :align :left} " Itinerary - "(get (vec m) 1) "-" (get (vec m) 6) ]]
                     :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 11, :family :arial,  :align :left, :background-color [255 255 255]}
            [[:cell
              [:image {:width 100 :height 22 :pad-left 0 :margin-left 0} "resources/public/dist/img/itinerary1.png" "Itergvgsdfgsdfg"]
              [:paragraph  {:size 12 :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"  :style :bold } (get (vec m) 1) " - " (get (vec m) 6) ]
              [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9} (get (vec m) 2)]
              [:paragraph { :ttf-name "resources/public/fonts/OpenSans-ExtraBold.ttf" :size 12} (get (vec m) 5)]
              [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9} (get (vec m) 3)]]]]
           [:line {:dotted true}]]
          ))
    )

(defn get-image [m]
  (cond (= (get (vec m) 8) "meals") "resources/public/dist/img/meals11.png"
        (= (get (vec m) 8) "hotel") "resources/public/dist/img/hotel11.png"
        (= (get (vec m) 8) "transportation") "resources/public/dist/img/transportation11.png"
        (= (get (vec m) 8) "attractions") "resources/public/dist/img/attractions11.png"
        (= (get (vec m) 8) "guidedcitytours") "resources/public/dist/img/guidedcitytours11.png"
        :else "resources/public/dist/img/meals11.png")
  )

(defn get-color [m]
  (cond (= (get (vec m) 8) "meals") [60 84 164]
        (= (get (vec m) 8) "hotel") [244 164 4]
        (= (get (vec m) 8) "transportation") [252 108 147]
        (= (get (vec m) 8) "attractions") [35 124 36]
        (= (get (vec m) 8) "guidedcitytours") [140 56 132]
        :else [192 0 0]
        )
  )

(defn cell-data [x]
  (for [i x]
    [[:cell
      (into
       ["table"  {:colspan 1, :rowspan 1, :background-color [255 255 255]  :widths [3 37]}
        [[:cell  [:image {:float :left  :width 20 :height 20}  "resources/public/dist/img/itinerary11.png"]]
         [:cell
          [:paragraph {:size 15 :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :color [192 0 0]  :style :bold  :align :left} "Itinerary - " (get (first i) 1) " - " (get (first i) 6) ]
          ]]]
       (for [m i]
         [[:cell [:image {:float :right :width 17 :height 17}
                  (get-image m)]]
          [:cell
           [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"  :style :bold  :size 12 :color (get-color m)} (get (vec m) 2) " | " (get (vec m) 1) " - " (get (vec m) 7)]
           [:paragraph { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 10} (get (vec m) 3)]
           ]]))
      [:line {:dotted true}]
      ]]
    )
  )

;;Old PDF
#_(defn flight-data [x]
    (map (partial map (fn [element] [:pdf-cell {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                                                ;; :size 13
                                                :min-height 25
                                                :align :center
                                                ;; :valign :middle
                                                :background-color [232 237 255]
                                                :set-border [:top :bottom]
                                                :border-color [255 255 255]

                                                } element])) x)
    )

(defn filght-image [x]
  (for  [i x]
    (cond (= (get (vec i) 0) "Arrive Flight") "Flight"
          (= (get (vec i) 0) "Depart Flight") "Flight"
          (= (get (vec i) 0) "Arrive Cruise") "Cruise"
          (= (get (vec i) 0) "Depart Cruise") "Cruise"
          (= (get (vec i) 0) "Arrive Surface") "Surface"
          (= (get (vec i) 0) "Depart Surface") "Surface"
          :else "Flight"))
  )

;;New PDF
(defn flight-data [x]
  (for [i x]
    (list
     #_[:cell  ["table" [[:cell [:image {:xscale 0 :yscale 0 :align  :left :width 100 :height 22 :pad-left  0 :pad-right 0}
                                 (cond (= (get (vec i) 0) "Arrive Flight") "resources/public/dist/img/transportation/flightarrival.png"
                                       (= (get (vec i) 0) "Depart Flight") "resources/public/dist/img/transportation/flightdeparture.png"
                                       (= (get (vec i) 0) "Arrive Cruise") "resources/public/dist/img/transportation/cruisearrival.png"
                                       (= (get (vec i) 0) "Depart Cruise") "resources/public/dist/img/transportation/cruisedepature.png"
                                       (= (get (vec i) 0) "Arrive Surface") "resources/public/dist/img/transportation/surafacearrival.png"
                                       (= (get (vec i) 0) "Depart Surface") "resources/public/dist/img/transportation/suraface.png"
                                       :else "resources/public/dist/img/transportation/flightarrival.png")
                                 #_(if (= (get (vec i) 0) "Arrive Flight") "resources/public/dist/img/depart.png" "resources/public/dist/img/clock.png" ) ]]]
                ]]
     [:cell ["table" {:colspan 1, :rowspan 1, :align :left, :background-color [255, 255, 255]} [ (table-cell1 "Journey Type")
                                                                                                (table-cell1 (cond (= (get (vec i) 0) "Arrive Flight") "Flight #"
                                                                                                                   (= (get (vec i) 0) "Depart Flight") "Flight #"
                                                                                                                   (= (get (vec i) 0) "Arrive Cruise") "Cruise #"
                                                                                                                   (= (get (vec i) 0) "Depart Cruise") "Cruise #"
                                                                                                                   (= (get (vec i) 0) "Arrive Surface") "Surface #"
                                                                                                                   (= (get (vec i) 0) "Depart Surface") "Surface #"
                                                                                                                   :else "Flight #"))
                                                                                                (table-cell1 "Date")] [ (table-cell2 (get (vec i) 0)) (table-cell2 (get (vec i) 1)) (table-cell2 (get (vec i) 2))]]]
     [:cell ["table" {:colspan 1 :rowspan 1  :align :left, :background-color [255 255 255] :widths [1 2 1]} [ (table-cell3 "Depart") [:cell ""] (table-cell3 "Arrive")] [[:cell {:align :center} (paragraph-cell1 (get (vec i) 3)) (paragraph-cell2 (get (vec i) 5))] [:cell [:image {:xscale 0.5 :yscale 0.5 :align  :center :width 89 :height 27 :pad-left  0 :pad-right 0}
                                                                                                                                                                                                                                                                              (cond (= (get (vec i) 0) "Arrive Flight") "resources/public/dist/img/flight11.png"
                                                                                                                                                                                                                                                                                    (= (get (vec i) 0) "Depart Flight") "resources/public/dist/img/flight12.png"
                                                                                                                                                                                                                                                                                    (= (get (vec i) 0) "Arrive Cruise") "resources/public/dist/img/cruise11.png"
                                                                                                                                                                                                                                                                                    (= (get (vec i) 0) "Depart Cruise") "resources/public/dist/img/cruise12.png"
                                                                                                                                                                                                                                                                                    (= (get (vec i) 0) "Arrive Surface") "resources/public/dist/img/surface11.png"
                                                                                                                                                                                                                                                                                    (= (get (vec i) 0) "Depart Surface") "resources/public/dist/img/surface12.png"
                                                                                                                                                                                                                                                                                    :else "resources/public/dist/img/flight11.png")
                                                                                                                                                                                                                                                                              #_(if (= (get (vec i) 0) "Arrive Flight") "resources/public/dist/img/depart.png" "resources/public/dist/img/clock.png" ) ]] [:cell {:align :center} (paragraph-cell1 (get (vec i) 4)) (paragraph-cell2 (get (vec i) 6))] ]] ]

     ))

  )

;;Old PDF
#_(defn hotelinformation-data [x]
    (map (partial map (fn [element] [:pdf-cell {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                                                ;;:size 9
                                                :min-height 25
                                                :align :center
                                                ;; :valign :middle
                                                :background-color [232 237 255]
                                                :set-border [:top :bottom]
                                                :border-color [255 255 255]

                                                } element])) x)
    )
;; New PDF
(defn hotelinformation-data [x]
  (for [i x]
    (list
     [:cell ["table" {:colspan 1, :rowspan 1, :align :left, :background-color [255, 255, 255]} [(table-cell1 "Booked For") (table-cell1 "Rooms") (table-cell1 "Confirm #") (table-cell1 "Breakfast")] [ (table-cell2 (get (vec i) 7)) (table-cell2 (get (vec i) 6)) (table-cell2 (get (vec i) 3)) (table-cell2 (get (vec i) 8))]]]
     [:cell ["table" {:colspan 1, :rowspan 1, :align :left, :background-color [255 255 255] :widths [15 10 15] }
             [(table-cell4 (get (vec i) 0) )]
             [ (table-cell5 "Checkin" ) [:cell ""] (table-cell5 "Checkout")] [[:cell { :ttf-name "resources/public/fonts/OpenSans-ExtraBold.ttf" :size 12 :align  :center}  (get (vec i) 4)] [:cell [:image {:align  :center :width 30 :height 30 } "resources/public/dist/img/clock.png"]] [:cell { :ttf-name "resources/public/fonts/OpenSans-ExtraBold.ttf" :size 12 :align  :center }  (get (vec i) 5)]]
             [[:cell  [:image {:align :center :width 20 :height 20} "resources/public/dist/img/location.png"] ] [:cell { :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 9 :colspan 2 :align :left} (get (vec i) 1) ]]
             ]
      ]
     ))
  )

;; Old PDF
#_(defn itinerary-city-table [bookings itinerarydetails]
    ;;(println (:filename (get-logos-filename (:logoid bookings))))
    (let [ite (filterv #(contains? % :itemid) itinerarydetails)
          hot (filterv #(contains? % :hotelid) itinerarydetails)
          fli (filterv #(contains? % :journeytype) itinerarydetails)
          cid  (:cityid
                (first ite))
          rdt (for [x ite]
                (vector (str "7M/VCH/" (:id x)) #_(:cityname (get-citiname (:cityid x))) (:date x) (:time x)
                        (if (= 0  (:itemid x))
                          (str  (:manualdescription x)
                                (when-not (zero? (:numberofpeoples x) )
                                  (str " - " (:numberofpeoples x))))
                          (str (:description (get-items-description (:itemid x)))
                               (when-not (zero? (:numberofpeoples x) )
                                 (str " - " (:numberofpeoples x)))))  (:confirmationnumber x)))

          ff  (vec (for [x fli]
                     (vector (:journeytype x) (:flightnumber x) (:date x)  (:depart x) (:arrive x) (:departtime x) (:arrivetime x))))
          hh  (vec (for [x hot]
                     (vector  (:hotelname (get-hotelname (:hotelid x)) "") (:address x) (:telephonenumber x)  (:confirmationnumber x)
                              (:checkin x) (:checkout x)  (str (:numerofrooms x)) (:bookedundername x)
                              (if (= true (:isbreakfastincluded x)) (str "YES") (str "NO")))))

          ]
      [:paragraph
       [:heading  [:image {;; "xscale":0.1
                           ;; "yscale":0.1
                           ;; :width 1251
                           ;; :height 178
                           ;; "align" :center
                           } (str  "resources/public/dist/img/logos/"  (:filename (get-logos-filename (:logoid bookings))))
                   ]]
       [:table {:header [[:paragraph {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :underline true  :size 12
                                      :color [0, 112, 192]
                                      }  (:cityname (get-citiname cid))]]
                :border false
                :cell-border false}
        [""]
        ]
       [:pdf-table {:border false :cell-border false }
        [30 60]
        [(group-pdf-cell1 "Booking Number") (group-pdf-cell2 (:bookingnumber bookings)) ]
        ;; [(group-pdf-cell1  "Email") (group-pdf-cell2 (:email bookings)) ]
        ;; [(group-pdf-cell1  "Agent Name") (group-pdf-cell2  (:agentname bookings)) ]
        [(group-pdf-cell1  "Guest Name") (group-pdf-cell2  (:guestname bookings)) ]
        ]
       [:spacer 0]
       (into
        [:pdf-table {
                     ;;:header [[:paragraph { :style :bold :size 12  :family :arial} "Flight Information" ]]
                     ;;:header [[:image "resources/public/dist/img/flight.jpg"]]
                     ;;:widths [ 1 1 1 1 1 1 1]
                     :bounding-box [70 100]
                     :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                     ;;:size 9
                     :min-height 28
                     :align :center
                     ;;:valign :middle
                     ;; :background-color [185 201 254]
                     :background-color [232 237 255]
                     :set-border  [:top :bottom]
                     :border-color  [255, 255, 255]
                     :border false :cell-border false

                     }
         [ 10 10 10 10 10 10 10]
         [[:pdf-cell {:rowspan 1 :colspan 7   :set-border  [:top :bottom]
                      :border-color  [255, 255, 255]} [:image "resources/public/dist/img/flight.jpg"]]]
         [[:pdf-cell " Journey Type"] [:pdf-cell " Transport No."]  [:pdf-cell   " Date"] [:pdf-cell " Depart"] [:pdf-cell " Arrive"] [:pdf-cell " Depart Time"] [:pdf-cell " Arrive Time"]]
         ]
        (flight-data ff)
        )
       [:spacer 0]
       (into
        [:pdf-table {;;:header [[:paragraph { :style :bold :size 12  :family :arial} "Hotel Information" ]]
                     ;; :header [[:image {;; :width 515
                     ;;                   ;; :height 20
                     ;;                   }  "resources/public/dist/img/hotel.jpg"]]
                     ;;:widths [1 1 1 1 1 1 1 1 1]
                     :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                     :bounding-box [72 100]
                     ;; :size 9
                     :min-height 28
                     :align :center
                     ;; :background-color [185 201 254]
                     :background-color [232 237 255]
                     :set-border  [:top :bottom]
                     :border-color  [255, 255, 255]
                     :border false :cell-border false
                     }
         [8 8 8 8 8 8 8 8 8]
         [[:pdf-cell {:rowspan 1 :colspan 9   :set-border  [:top :bottom]
                      :border-color  [255, 255, 255]} [:image "resources/public/dist/img/hotel.jpg"]]]
         [[:pdf-cell "Name"] [:pdf-cell "Address"]   [:pdf-cell "Telephone"] [:pdf-cell "Confirm #"] [:pdf-cell " Checkin"] [:pdf-cell " Checkout"] [:pdf-cell " Rooms"] [:pdf-cell "Booked For"] [:pdf-cell " Breakfast"]]
         ]
        (hotelinformation-data hh)
        )
       [:spacer 0]
       (into
        [:pdf-table {;;:header [[:paragraph {:style :bold :size 12  :family :arial} "Itinerary" ]]
                     ;; :header [[:image {;; :width 515
                     ;;                   ;; :height 20
                     ;;                   }  "resources/public/dist/img/itinerary.jpg"]]
                     :bounding-box [50 100]
                     ;;:widths [1 1 1 3 1]
                     :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                     ;;:size 9
                     :min-height 28
                     :align :center
                     ;; :valign :middle
                     ;;:background-color [185 201 254]
                     :background-color [232 237 255]
                     :set-border  [:top :bottom]
                     :border-color  [255, 255, 255]
                     :border false :cell-border false
                     }
         [8 8 8 18 8]
         [[:pdf-cell {:rowspan 1 :colspan 5   :set-border  [:top :bottom]
                      :border-color  [255, 255, 255]} [:image "resources/public/dist/img/itinerary.jpg"]]]
         [[:pdf-cell {:align :center} "Voucher"]  [:pdf-cell "Date "] [:pdf-cell "Time"]  [:pdf-cell   "Description"]  [:pdf-cell   "Comments"]  ]
         ]
        (cell-data rdt)
        )
       ;;[:heading [:image "resources/public/dist/img/reportfotter.png"]]
       ]
      )
    )
;; New Pdf
#_(defn itinerary-city-table [bookings itinerarydetails]
    ;;(println (:filename (get-logos-filename (:logoid bookings))))
    (let [ite (filterv #(contains? % :itemid) itinerarydetails)
          hot (filterv #(contains? % :hotelid) itinerarydetails)
          fli (filterv #(contains? % :journeytype) itinerarydetails)
          cid  (:cityid
                (first ite))
          rdt (for [x ite]
                (vector (str "7M/VCH/" (:id x)) #_(:cityname (get-citiname (:cityid x))) (:date x) (:time x)
                        (if (= 0  (:itemid x))
                          (str  (:manualdescription x)
                                (when-not (zero? (:numberofpeoples x) )
                                  (str " - " (:numberofpeoples x))))
                          (str (:description (get-items-description (:itemid x)))
                               (when-not (zero? (:numberofpeoples x) )
                                 (str " - " (:numberofpeoples x)))))  (:confirmationnumber x)))

          ff  (vec (for [x fli]
                     (vector (:journeytype x) (:flightnumber x) (:date x)  (:depart x) (:arrive x) (:departtime x) (:arrivetime x))))
          hh  (vec (for [x hot]
                     (vector  (:hotelname (get-hotelname (:hotelid x)) "") (:address x) (:telephonenumber x)  (:confirmationnumber x)
                              (:checkin x) (:checkout x)  (str (:numerofrooms x)) (:bookedundername x)
                              (if (= true (:isbreakfastincluded x)) (str "Yes") (str "No")))))

          ]
      [:paragraph
       [:heading  [:image {;; "xscale":0.1
                           ;; "yscale":0.1
                           ;; :width 1251
                           ;; :height 178
                           ;; "align" :center
                           } (str  "resources/public/dist/img/logos/"  (:filename (get-logos-filename (:logoid bookings))))
                   ]]
       [:paragraph {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :underline true  :size 12
                    :color [192, 0, 0]
                    :align :right
                    } "Booking / "  (:cityname (get-citiname cid))]
       [:pdf-table {:border false :cell-border false }
        [30 60]
        [(group-pdf-cell1 "Booking Number") (group-pdf-cell2 (:bookingnumber bookings)) ]
        ;; [(group-pdf-cell1  "Email") (group-pdf-cell2 (:email bookings)) ]
        ;; [(group-pdf-cell1  "Agent Name") (group-pdf-cell2  (:agentname bookings)) ]
        [(group-pdf-cell1  "Guest Name") (group-pdf-cell2  (:guestname bookings)) ]
        ]
       [:line {:dotted true}]
       [:spacer 0]
       (if (not-empty ff)
         (into
          [:table {
                   ;;:header [[:paragraph {:size 12   :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "Flight Information" ]]
                   :header [[:image {:align  :left :width 101 :height 22 :pad-left 5} "resources/public/dist/img/flight1.png"] [:paragraph {:size 12   :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "" ]]
                   ;;:widths [ 1 1 1 1 1 1 1]
                   ;;:bounding-box [50 50]
                   ;; :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                   ;;:size 9
                   :min-height 28
                   :align :left
                   ;;:widths [3 1]
                   ;;:valign :middle
                   :background-color [255, 255, 255]
                   ;;:background-color [235 235 231]
                   ;;:set-border  [:top :bottom]
                   ;;:border-color  [255, 255, 255]
                   :border false :cell-border false
                   }
           #_[[:cell " Journey Type"] [:cell " Transport No."]  [:cell   " Date"] [:cell " "]]
           ]
          (flight-data ff)
          )
         )
       [:line {:dotted true}]
       [:spacer 0]
       (if (not-empty hh)
         (into
          [:table {;;:header [[:paragraph { :style :bold :size 12  :family :arial} "Hotel Information" ]]
                   ;; :header [[:image {;; :width 515
                   ;;                   ;; :height 20
                   ;;                   }  "resources/public/dist/img/hotel.jpg"]]
                   ;;:widths [1 1 1 1 1 1 1 1 1]
                   :header [[:image {:align  :left :width 101 :height 22 :pad-left 5} "resources/public/dist/img/hotel1.png"] [:paragraph {:size 12 :align :left  :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "" ]]
                   :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"

                   ;; :size 9
                   :min-height 28
                   :align :left
                   :background-color [255, 255, 255]
                   ;; :background-color [235 235 231]
                   :border false :cell-border false
                   ;;:set-border  [:right] :border-color  [235, 235, 231]
                   }
           ]
          (hotelinformation-data hh)
          ))
       [:line {:dotted true}]
       [:spacer 0]
       (into
        #_[:table {;;:header [[:paragraph { :style :bold :size 12  :family :arial} "Hotel Information" ]]
                   ;; :header [[:image {;; :width 515
                   ;;                   ;; :height 20
                   ;;                   }  "resources/public/dist/img/hotel.jpg"]]
                   ;;:widths [1 1 1 1 1 1 1 1 1]
                   :header [[:image {:align  :left :width 101 :height 22 :pad-left 5} "resources/public/dist/img/itinerary1.png"] [:paragraph {:size 12 :align :left  :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "" ]]
                   :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"

                   ;; :size 9
                   :min-height 28
                   :align :left
                   ;; :background-color [185 201 254]
                   :background-color [235 235 231]
                   :border false :cell-border false
                   :widths [1 3]
                   :watermark {:image "resources/public/dist/img/booking-header1.jpg"}
                   ;;:set-border  [:right] :border-color  [235, 235, 231]
                   :horizontal-align :right


                   }
           ]

        [:pdf-table {;;:header [[:paragraph {:style :bold :size 12  :family :arial} "Itinerary" ]]
                     ;; :header [[:image {;; :width 515
                     ;;                   ;; :height 20
                     ;;                   }  "resources/public/dist/img/itinerary.jpg"]]
                     :bounding-box [50 100]
                     ;;:widths [1 1 1 3 1]
                     :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                     ;;:size 9
                     :min-height 28
                     :align :center
                     ;; :valign :middle
                     :background-color [255, 255, 255]
                     ;; :background-color [235 235 231]
                     :set-border  [:top :bottom]
                     :border-color  [255, 255, 255]
                     :border false :cell-border false
                     }
         [8 8 8 18 8]
         [[:pdf-cell {:rowspan 1 :colspan 5   :set-border  [:top :bottom]
                      :border-color  [255, 255, 255]} [:image {:align  :left :width 101 :height 22 :pad-left 5}  "resources/public/dist/img/itinerary1.png"]]]
         [[:pdf-cell {:align :center} "Voucher"]  [:pdf-cell "Date "] [:pdf-cell "Time"]  [:pdf-cell   "Description"]  [:pdf-cell   "Comments"]  ]
         ]
        (cell-data rdt)
        )
       ]
      )
    )
(defn group-by-date1 [x]
  (let [gd (group-by #(:date %) x)]
    (println gd)
    )
  )
;; New Pdf 2
(defn itinerary-city-table [bookings itinerarydetails]
  ;;(println (:filename (get-logos-filename (:logoid bookings))))
  (let [ite (filterv #(contains? % :itemid) itinerarydetails)
        hot (filterv #(contains? % :hotelid) itinerarydetails)
        fli (filterv #(contains? % :journeytype) itinerarydetails)
        cid  (:cityid
              (first ite))
        rdt (for [x ite]
              (vector (str "7M/VCH/" (:id x)) #_(:cityname (get-citiname (:cityid x))) (:date x) (:time x)
                      (if (= 0  (:itemid x))
                        (str  (:manualdescription x)
                              (when-not (zero? (:numberofpeoples x) )
                                (str " - " (:numberofpeoples x))))
                        (str (:description (get-items-description (:itemid x)))
                             (when-not (zero? (:numberofpeoples x) )
                               (str " - " (:numberofpeoples x)))))  (:confirmationnumber x) (:name (first (get-vendors-name-by-category (:vendorid1 x) (:category x)))) (:cityname (get-citiname (:cityid x))) (:activitytitle x) ))

        rdt1 (group-by #(:date %) ite)




        itdate        (:date
                       (second rdt1))

        rdt2 (for [x (flatten (vals rdt1))]
               (vector (str "7M/VCH/" (:id x)) #_(:cityname (get-citiname (:cityid x))) (:date x) (:time x)
                       (if (= 0  (:itemid x))
                         (str  (:manualdescription x)
                               (when-not (zero? (:numberofpeoples x) )
                                 (str " - " (:numberofpeoples x))))
                         (str (:description (get-items-description (:itemid x)))
                              (when-not (zero? (:numberofpeoples x) )
                                (str " - " (:numberofpeoples x)))))  (:confirmationnumber x) (:name (get-vendors-name-by-category (:vendorid1 x) (:category x)))))
        ;; rdt3  (map #(list (last %)) rdt1)

        rdt4  (doall (for [i (vals rdt1)]
                       (for [x i]
                         (vector (str "7M/VCH/" (:id x)) #_(:cityname (get-citiname (:cityid x))) (date-to-string-report (stringToDate (:date x))) (:time x)
                                 #_(if (= 0  (:itemid x))
                                     (str  (:manualdescription x)
                                           (when-not (zero? (:numberofpeoples x) )
                                             (str " - " (:numberofpeoples x))))
                                     (str (:description (get-items-description (:itemid x)))
                                          (when-not (zero? (:numberofpeoples x) )
                                            (str " - " (:numberofpeoples x)))))
                                 (str  (:manualdescription x)
                                       (when-not (zero? (:numberofpeoples x) )
                                         (str " - " (:numberofpeoples x))))
                                 (:confirmationnumber x)
                                 (:name (first (get-vendors-name-by-category (:vendorid1 x) (:category x)))) (:cityname (get-citiname (:cityid x))) (:activitytitle x) (:category x)))))

        ff  (vec (for [x fli]
                   (vector (:journeytype x) (:flightnumber x) (:date x)  (:depart x) (:arrive x) (:departtime x) (:arrivetime x))))
        hh  (vec (for [x hot]
                   (vector  (:hotelname (get-hotelname (:hotelid x)) "") (:address x) (:telephonenumber x)  (:confirmationnumber x)
                            (:checkin x) (:checkout x)  (str (:numerofrooms x)) (:bookedundername x)
                            (if (= true (:isbreakfastincluded x)) (str "Yes") (str "No")))))

        ]
    ;;(println (get (vec (first ff)) 0))
    [:paragraph
     [:heading  [:image {;; "xscale":0.1
                         ;; "yscale":0.1
                         ;; :width 1251
                         ;; :height 178
                         ;; "align" :center
                         } (str  "resources/public/dist/img/logos/"  (:filename (get-logos-filename (:logoid bookings))))
                 ]]
     [:paragraph {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :underline true  :size 15
                  :color [192, 0, 0]
                  :align :right
                  }  (:cityname (get-citiname cid))]
     [:pdf-table {:border false :cell-border false }
      [30 60]
      [(group-pdf-cell1 "Booking Number") (group-pdf-cell2 (:bookingnumber bookings)) ]
      ;; [(group-pdf-cell1  "Email") (group-pdf-cell2 (:email bookings)) ]
      ;; [(group-pdf-cell1  "Agent Name") (group-pdf-cell2  (:agentname bookings)) ]
      [(group-pdf-cell1  "Guest Name") (group-pdf-cell2  (:guestname bookings)) ]
      ]
     [:line {:dotted true}]
     [:spacer 0]
     (if (not-empty ff)
       (into
        [:table {
                 ;;:header [[:paragraph {:size 12   :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "Flight Information" ]]
                 :header [[:image {:align  :left :width 101 :height 22 :pad-left 5} "resources/public/dist/img/Transport.png"] [:paragraph {:size 12   :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "" ]]
                 ;;:widths [ 1 1 1 1 1 1 1]
                 ;;:bounding-box [50 50]
                 ;; :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                 ;;:size 9
                 :min-height 28
                 :align :left
                 ;;:widths [3 1]
                 ;;:valign :middle
                 :background-color [255, 255, 255]
                 ;;:background-color [235 235 231]
                 ;;:set-border  [:top :bottom]
                 ;;:border-color  [255, 255, 255]
                 :border false :cell-border false
                 }
         #_[[:cell " Journey Type"] [:cell " Transport No."]  [:cell   " Date"] [:cell " "]]
         ]
        (flight-data ff)
        )
       )
     [:line {:dotted true}]
     [:spacer 0]
     (if (not-empty hh)
       (into
        [:table {;;:header [[:paragraph { :style :bold :size 12  :family :arial} "Hotel Information" ]]
                 ;; :header [[:image {;; :width 515
                 ;;                   ;; :height 20
                 ;;                   }  "resources/public/dist/img/hotel.jpg"]]
                 ;;:widths [1 1 1 1 1 1 1 1 1]
                 :header [[:image {:align  :left :width 101 :height 22 :pad-left 5} "resources/public/dist/img/Hotel.png"] [:paragraph {:size 12 :align :left  :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "" ]]
                 :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"

                 ;; :size 9
                 :min-height 28
                 :align :left
                 :background-color [255, 255, 255]
                 ;; :background-color [235 235 231]
                 :border false :cell-border false
                 ;;:set-border  [:right] :border-color  [235, 235, 231]
                 }
         ]
        (hotelinformation-data hh)
        ))
     #_[:line {:dotted true}]
     #_[:spacer 0]

     [:line {:dotted true}]
     #_[:spacer 0]
     (into
      [:table {;;:header [[:paragraph { :style :bold :size 12  :family :arial} "Hotel Information" ]]
               ;; :header [[:image {;; :width 515
               ;;                   ;; :height 20
               ;;                   }  "resources/public/dist/img/hotel.jpg"]]
               ;;:widths [1 1 1 1 1 1 1 1 1]
               ;;:header [[:image {:width 101 :height 22 :pad-left 0 :margin-left 0} "resources/public/dist/img/itinerary1.png"]]
               :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"

               ;; :size 9
               :min-height 28
               :align :left
               ;; :background-color [185 201 254]
               :background-color [255 255 255]
               :border false :cell-border false
               ;;:set-border  [:right] :border-color  [235, 235, 231]
               ;; :horizontal-align :right


               }
       ]
      (cell-data rdt4)
      )
     ]
    )
  )


;; Group-By-Date
(defn itinerary-date-table [bookings itinerarydetails]
  ;;(println (:filename (get-logos-filename (:logoid bookings))))
  (println itinerarydetails)
  (let [ite (filterv #(contains? % :itemid) itinerarydetails)
        hot (filterv #(contains? % :hotelid) itinerarydetails)
        fli (filterv #(contains? % :journeytype) itinerarydetails)
        fdate  (:date
                (first ite))
        rdt (for [x ite]
              (vector (str "7M/VCH/" (:id x)) #_(:cityname (get-citiname (:cityid x))) (:date x) (:time x)
                      (if (= 0  (:itemid x))
                        (str  (:manualdescription x)
                              (when-not (zero? (:numberofpeoples x) )
                                (str " - " (:numberofpeoples x))))
                        (str (:description (get-items-description (:itemid x)))
                             (when-not (zero? (:numberofpeoples x) )
                               (str " - " (:numberofpeoples x)))))  (:confirmationnumber x)))

        ff  (vec (for [x fli]
                   (vector (:journeytype x) (:flightnumber x) (:date x)  (:depart x) (:arrive x) (:departtime x) (:arrivetime x))))
        hh  (vec (for [x hot]
                   (vector  (:hotelname (get-hotelname (:hotelid x)) "") (:address x) (:telephonenumber x)  (:confirmationnumber x)
                            (:date x) (:checkout x)  (str (:numerofrooms x)) (:bookedundername x)
                            (if (= true (:isbreakfastincluded x)) (str "Yes") (str "No")))))

        ]
    [:paragraph
     [:heading  [:image  (str  "resources/public/dist/img/logos/"  (:filename (get-logos-filename (:logoid bookings))))
                 ]]
     [:paragraph {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :underline true  :size 12
                  :color [192, 0, 0]
                  :align :right
                  }  fdate]
     [:pdf-table {:border false :cell-border false }
      [30 60]
      [(group-pdf-cell1 "Booking Number") (group-pdf-cell2 (:bookingnumber bookings)) ]
      ;; [(group-pdf-cell1  "Email") (group-pdf-cell2 (:email bookings)) ]
      ;; [(group-pdf-cell1  "Agent Name") (group-pdf-cell2  (:agentname bookings)) ]
      [(group-pdf-cell1  "Guest Name") (group-pdf-cell2  (:guestname bookings)) ]
      ]
     [:spacer 0]
     (into
      [:table {
               :header [[:image {:align  :left :width 101 :height 22 :pad-left 5} "resources/public/dist/img/flight1.png"] [:paragraph {:size 12   :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "" ]]
               :min-height 28
               :align :left
               :background-color [235 235 231]
               :border false :cell-border false
               }
       ]
      (flight-data ff)
      )
     (into
      [:table {:header [[:image {:align  :left :width 101 :height 22 :pad-left 5} "resources/public/dist/img/hotel1.png"] [:paragraph {:size 12 :align :left  :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "" ]]
               :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"

               ;; :size 9
               :min-height 28
               :align :left
               ;; :background-color [185 201 254]
               :background-color [235 235 231]
               :border false :cell-border false
               ;;:set-border  [:right] :border-color  [235, 235, 231]
               }
       ]
      (hotelinformation-data hh)
      )
     (into

      [:pdf-table {;;:header [[:paragraph {:style :bold :size 12  :family :arial} "Itinerary" ]]
                   ;; :header [[:image {;; :width 515
                   ;;                   ;; :height 20
                   ;;                   }  "resources/public/dist/img/itinerary.jpg"]]
                   :bounding-box [50 100]
                   ;;:widths [1 1 1 3 1]
                   :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                   ;;:size 9
                   :min-height 28
                   :align :center
                   ;; :valign :middle
                   ;;:background-color [185 201 254]
                   :background-color [235 235 231]
                   :set-border  [:top :bottom]
                   :border-color  [255, 255, 255]
                   :border false :cell-border false
                   }
       [8 8 8 18 8]
       [[:pdf-cell {:rowspan 1 :colspan 5   :set-border  [:top :bottom]
                    :border-color  [255, 255, 255]} [:image {:align  :left :width 101 :height 22 :pad-left 5}  "resources/public/dist/img/itinerary1.png"]]]
       [[:pdf-cell {:align :center} "Voucher"]  [:pdf-cell "Date "] [:pdf-cell "Time"]  [:pdf-cell   "Description"]  [:pdf-cell   "Comments"]  ]
       ]
      (cell-data rdt)
      )
     ]
    )
  )

#_(defn flightinformation-table [flightinformationdata]
    (let [rdt (vec (for [x flightinformationdata]
                     (vector (str (:cityid x)) (:journeytype x) (:flightnumber x) (:date x)  (:depart x) (:arrive x) (:departtime x) (:arrivetime x))))
          ]
      (into
       [:table {:header [[:paragraph {:ttf-name "resources/public/fonts/OpenSans-Semibold.ttf" :size 12 } "Flight Information" ]]
                :widths [1 1 1 1 1 1 1 1]
                :size 9
                :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                :min-height 20
                :align :center
                ;;:valign :middle
                :background-color [235 235 231]

                }
        ;; [5 5 5 5 5 5 5 5]
        [ " City Name" "Journey Type"  "FlightNumber"    "   Date"  "    Depart" " Arrive"  " Depart Time" " Arrive Time"]
        ]
       (flight-data rdt)
       )))
#_(defn hotelinformation-table [hotelinformation]
    (let [rdt (vec (for [x hotelinformation]
                     (vector (str (:cityid x)) (str (:hotelid x)) (:address x) (:telephonenumber x)  (:faxnumber x) (:checkin x) (:checkout x)  (str (:numerofrooms x)))))
          ]
      (into
       [:table {:header [[:paragraph {:size 12  :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"} "Hotel Information" ]]
                :widths [1 1 1 1 1 1 1 1]
                :size 9
                :min-height 20
                :align :center
                :background-color [235 235 231]

                }
        [ " City Name" "Hotel Name"  "Address"    "   Telephone"  "    Fax" " Checkin"  " Checkout" "NumerofRooms"]
        ]
       (hotelinformation-data rdt)
       )))

(defn formatter-pdf [x]
  (mf/format (ma/amount-of mc/USD  x  (java.math.RoundingMode/HALF_DOWN)) Locale/US )
  )


(defn send-mail-bookings [name toid bkno]
  (send-message {:host "smtp.gmail.com"
                 :user "fit@7mtours.com"
                 :pass "queen7m1"
                 :ssl :yes!!!11}
                {:from "FIT 7M<fit@7mtours.com>"
                 :to   (clojure.string/split toid #"\,+")
                 :subject (str " Your booking reference:" bkno)
                 :body [{:type "text/html"
                         :content (str  "Dear " name " .<br/> <br/> <b>Greetings from the 7M Tours team from Orlando USA! </b> <br/> <br/>
                           Thank you for your submitting your FIT Query to 7M Tours. Please check the attached PDF, which contains  your quotation request. We appreciate it very much the trust that you have put in us. We are here to assist your travel needs 24/7. The quotation that has been attached to you is from our best resources in the USA for this specifically designed itinerary and it is our honest effort to give you the best value for money that you will be spending. <br/> <br/>  Please do not hesitate in contacting us. Once you have confirmed this booking with us, one of our booking managers will assist you and will be in full support until the client returns from their trip. <br/> <br/> Best Regards, <br>" "<b>" name "</b> <br/>" "F.I.T. Costing Department <br/>  Office: 407-250-5800 | Fax: 407-420-7964  <br/> <img src='http://www.7mtours.com/main/sig/sig-new1.png'>") }
                        {:type :attachment
                         :content (java.io.File. (str  bkno ".pdf"))}
                        ]}))




(defn get-bookings-pdf [bookingnumber]
  (rr/content-type
   (rr/response (io/input-stream (str "bookings/"  bookingnumber ".pdf")))
   "application/pdf"))

(defn get-fit-pdf [qrnumber]
  (rr/content-type
   (rr/response (io/input-stream (str "/home/inturi/quotations/fit/" qrnumber ".pdf")))
   "application/pdf"))

(defn get-groups-pdf [qrnumber]
  (rr/content-type
   (rr/response (io/input-stream (str "/home/inturi/quotations/groups/" qrnumber ".pdf")))
   "application/pdf"))

(def res-type {"png" "image/png"
               "jpg" "image/jpg"
               "pdf" "application/pdf"
               "jpeg" "image/jpeg"
               "docx" "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
               "doc" "application/msword"})

(defn get-hotels-contractfile [filename]
  (let [extention (last (str/split filename #"\."))]
    (rr/content-type
     (rr/response (io/input-stream (str "hotelcontracts/" filename)))
     (get res-type extention "application/pdf"))))

(defn get-logos-logofile [filename]
  (let [extention (last (str/split filename #"\."))]
    (rr/content-type
     (rr/response (io/input-stream (str "resources/public/dist/img/logos/" filename)))
     (get res-type extention "application/pdf"))))

(def custom-formatter (f/formatter "MMM dd, yyyy"))
(defn group-by-cityid [b c f h]
  (let [op (concat  c f h)
        gc  (group-by #(:cityid %) op)
        ]
    (interpose [:pagebreak] (for [[_ v] gc]
                              (itinerary-city-table b v)
                              )))
  )

(defn group-by-date [b c f h]
  (let [hh (map #(rk/rename-keys % {:checkin :date} ) h)
        op (concat  c f hh)
        gc  (group-by #(:date %) op)
        ]
    (interpose [:pagebreak] (for [[_ v] gc]
                              (itinerary-date-table b v))))
  )


#_(defn group-flight-by-cityid [b c f h]
    (let [gf (group-by #(:cityid %) f)]
      (interpose [:pagebreak] (for [[_ fl] gf]
                                (flightinformation-table fl))))
    )

#_(defn group-hotel-by-cityid [b c f h]
    (let [gh (group-by #(:cityid %) h)]
      (interpose [:pagebreak] (for [[_ hl] gh]
                                (hotelinformation-table hl))))
    )

#_(defn group-by-date [d]
    (let [md (map #(assoc % :date (stringToDate (:date %)))
                  d)
          gd  (group-by #(:date %) md)]
      (interpose [:pagebreak] (for [[_ v] gd]
                                (itinerary-table v))))
    )

;;Booking PDF
#_(defn gen-booking-pdf [bookings  itinerarydetails flightinformation hotelinformation]
    (pdf
     [
      {;;:header (str "Bookings")
       ;; :header (str "Booking Number : " (str (:bookingnumber bookings)))
       ;; :header    (:heading [:image "resources/public/dist/img/header.png"])
       :footer  {:text (str "Emergency No: " (:emergencynumbers bookings) ",  Pg.No: ")
                 :footer-separator "dadssd"
                 :align :right
                 :page-numbers true}}
      ;; [:heading [:image "resources/public/dist/img/reportheader.jpg"]]
      ;;(group-flight-by-cityid bookings  itinerarydetails flightinformation  hotelinformation)
      #_(group-hotel-by-cityid bookings  itinerarydetails flightinformation  hotelinformation)
      (group-by-cityid bookings  itinerarydetails flightinformation  hotelinformation)
      [:pagebreak]
      [:paragraph {:size 12 :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                   :color [0, 112, 192]
                   }  "Terms and Conditions :"]
      [:paragraph {:size 8 } (:termsandconditions bookings)]
      #_(group-by-date itinerarydetails)
      ;; [:image "resources/public/dist/img/reportfotter.png"]
      ]
     (str "bookings/"
          (clojure.string/replace (:bookingnumber bookings) #"/" "")
          ".pdf") ))

;;new pdf
(defn gen-booking-pdf [bookings  itinerarydetails flightinformation hotelinformation]
  (pdf
   [
    {:left-margin   8
     :right-margin  5
     :top-margin    0
     :bottom-margin 10
     ;;:header (str "Bookings")
     ;;:header (str "Booking Number : " (str (:bookingnumber bookings)))
     ;;:header    (:heading [:image "resources/public/dist/img/header.png"])
     ;;:header    (:heading  [[:image "resources/public/dist/img/reportheader.jpg"]])
     ;;:header
     #_{:x 20
        :y 50
        :table
        [:pdf-table
         {:border false}
         [100]
         [[:image "resources/public/dist/img/header.png"]]]}
     :footer  {:text (str "Emergency No: " (:emergencynumbers bookings) ",  Pg.No: ")
               :footer-separator "dadssd"
               :align :right
               :page-numbers true}
     #_:watermark
     #_{:image "resources/public/dist/img/header.png"
        ;; :image and :render keys are exclusive, :render is preferred
        :render (fn [g2d] (.drawString g2d "DRAFT COPY" 0 0))
        :translate [100 200]
        :rotate 45
        :scale 8}
     }
    ;;[:heading [:image "resources/public/dist/img/reportheader.jpg"]]
    ;;(group-flight-by-cityid bookings  itinerarydetails flightinformation  hotelinformation)
    #_(group-hotel-by-cityid bookings  itinerarydetails flightinformation  hotelinformation)

    (group-by-cityid bookings  itinerarydetails flightinformation  hotelinformation)
    #_ [:table {:header [{:background-color [100 100 100]} "Transport"] :spacing 2}
        ["Journey Type"  ""]
        ["Arrive Flight" [:cell ["table" ["Inner table Col1" "Inner table Col2" "Inner table Col3"]]]]
        ]
    #_[:table {:header [{:background-color [100 100 100]} "FOO"] :spacing 2}
       ["foo"
        [:cell
         [:phrase
          {:style "italic" :size 18 :family "helvetica" :color [200 55 221]}
          "Hello Clojure!"]]
        "baz"]
       ["foo1" [:cell {:background-color [100 10 200]} "bar1"] "baz1"]
       ["foo2" "bar2" [:cell ["table" ["Inner table Col1" "Inner table Col2" "Inner table Col3"]]]]]
    [:pagebreak]
    [:paragraph {:size 12 :ttf-name "resources/public/fonts/OpenSans-Semibold.ttf"
                 :color [0, 112, 192]
                 }  "Terms and Conditions :"]
    [:paragraph {:size 8 } (:termsandconditions bookings)]
    #_(group-by-date itinerarydetails)
    ;; [:image "resources/public/dist/img/reportfotter.png"]
    ]
   (str "bookings/"
        (clojure.string/replace (:bookingnumber bookings) #"/" "")
        ".pdf") ))

(defn send-bookings-pdf-email [body]
  (rr/content-type
   (rr/response
    (do (send-mail-bookings
         (:agentname body)(:emailid body)
         (str "bookings/"   (clojure.string/replace (:bookingnumber body) #"/" "")))))
   content-type))


(defn post-bookings [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [booking-data (map-to-db-data
                          (filter (fn [k] (and (not= k "itinerarydetails")
                                               (not= k "flightinformation")
                                               (not= k "hotelinformation")))
                                  (keys body)) body)
            user_id (:user_id (:identity request))
            bid  (first (db/insert-bookings
                         (assoc
                          booking-data
                          :travelsid (if (clojure.string/starts-with?
                                          (booking-data :qrnumber) "7M/T") (booking-data :quotationid))
                          :groupsid (if (clojure.string/starts-with?
                                         (booking-data :qrnumber) "7M/G") (booking-data :quotationid))
                          :createdoperatorid user_id
                          :modifiedoperatorid user_id
                          :bookingagents (if (empty? (:bookingagents booking-data)) nil
                                             (:bookingagents booking-data)))))
            itno (first (db/get-bookings-number-by-id {:id (:id bid)}))
            itdetails
            (map (fn [mo]
                   (assoc
                    mo
                    :id
                    (:id (first
                          (db/insert-bookingsdetails
                           {:bookingid (:id bid)
                            :cityid (:cityid mo)
                            :vendorid1 (:vendorid1 mo)
                            :itemid (:itemid mo)
                            :date  (if (seq (:date mo))
                                     (c/to-sql-time
                                      (f/unparse multi-parser
                                                 (f/parse multi-parser (:date mo))))
                                     nil)
                            :time (:time mo)
                            :amount (:amount mo)
                            :paymentmode (:paymentmode mo)
                            :manualdescription (:manualdescription mo)
                            :remarks (:remarks mo)
                            :numberofpeoples (:numberofpeoples mo)
                            :confirmationnumber (:confirmationnumber mo)
                            :createdoperatorid user_id
                            :modifiedoperatorid user_id
                            :activitytitle (:activitytitle mo)
                            :category (:category mo)
                            :subcategory (:subcategory mo)})))))
                 (:itinerarydetails body))]
        (doall (map (fn [mo] (db/insert-flightinfromation
                              {:bookingid (:id bid)
                               :cityid (:cityid mo)
                               :journeytype (:journeytype mo)
                               :flightnumber (:flightnumber mo)
                               :date  (if (seq (:date mo))
                                        (c/to-sql-time
                                         (f/unparse multi-parser
                                                    (f/parse multi-parser (:date mo))))
                                        nil)
                               :depart (:depart mo)
                               :arrive (:arrive mo)
                               :departtime (:departtime mo)
                               :arrivetime (:arrivetime mo)
                               :createdoperatorid user_id
                               :modifiedoperatorid user_id
                               :noofpax (:noofpax mo)
                               :flightname (:flightname mo)}))
                    (:flightinformation body)))
        (doall (map (fn [mo] (db/insert-hotelinfromation
                              {:bookingid (:id bid)
                               :cityid (:cityid mo)
                               :hotelid (:hotelid mo)
                               :address (:address mo)
                               :telephonenumber (:telephonenumber mo)
                               :faxnumber (:faxnumber mo)
                               :checkin  (if (seq (:checkin mo))
                                           (c/to-sql-time
                                            (f/unparse multi-parser
                                                       (f/parse multi-parser (:checkin mo))))
                                           nil)
                               :checkout  (if (seq (:checkout mo))
                                            (c/to-sql-time
                                             (f/unparse multi-parser
                                                        (f/parse multi-parser
                                                                 (:checkout mo))))
                                            nil)
                               :numerofrooms (:numerofrooms mo)
                               :numberofpeople (:numberofpeoples mo)
                               :isbreakfastincluded (:isbreakfastincluded mo)
                               :confirmationnumber (:confirmationnumber mo)
                               :bookedundername (:bookedundername mo)
                               :amount (:amount mo)
                               :paymentmode (:paymentmode mo)
                               :vendorid1 (:vendorid1 mo)
                               :createdoperatorid user_id
                               :modifiedoperatorid user_id}))
                    (:hotelinformation body)))



        (rr/content-type
         (rr/response (do
                        (gen-booking-pdf
                         (assoc booking-data
                                :bookingnumber
                                (:bookingnumber itno))
                         itdetails (:flightinformation body) (:hotelinformation body))
                        bid))
         content-type)))))


(defn insert-booking [mo bid userid]
  (:id (first
        (db/insert-bookingsdetails
         {:bookingid bid
          :cityid (:cityid mo)
          :vendorid1 (:vendorid1 mo)
          :itemid (:itemid mo)
          :date  (if (seq (:date mo))
                   (c/to-sql-time
                    (f/unparse multi-parser
                               (f/parse multi-parser (:date mo))))
                   nil)
          :time (:time mo)
          :amount (:amount mo)
          :paymentmode (:paymentmode mo)
          :manualdescription (:manualdescription mo)
          :remarks (:remarks mo)
          :numberofpeoples (:numberofpeoples mo)
          :confirmationnumber (:confirmationnumber mo)
          :createdoperatorid userid
          :modifiedoperatorid userid
          :activitytitle (:activitytitle mo)
          :category (:category mo)
          :subcategory (:subcategory mo)
          }))))

(defn update-bookings [mo bid userid]
  (:id (first
        (db/update-bookingsdetails-by-id
         {:id (:id mo)
          :bookingid bid
          :cityid (:cityid mo)
          :vendorid1 (:vendorid1 mo)
          :itemid (:itemid mo)
          :date  (if (seq (:date mo))
                   (c/to-sql-time
                    (f/unparse multi-parser
                               (f/parse multi-parser (:date mo))))
                   nil)
          :time (:time mo)
          :amount (:amount mo)
          :paymentmode (:paymentmode mo)
          :manualdescription (:manualdescription mo)
          :remarks (:remarks mo)
          :numberofpeoples (:numberofpeoples mo)
          :confirmationnumber (:confirmationnumber mo)
          :modifiedoperatorid userid
          :activitytitle (:activitytitle mo)
          :category (:category mo)
          :subcategory (:subcategory mo)
          }
         ))))

(defn insert-flight [mo bid userid]
  (:id (first
        (db/insert-flightinfromation
         {:bookingid bid
          :cityid (:cityid mo)
          :journeytype (:journeytype mo)
          :flightnumber (:flightnumber mo)
          :date  (if (seq (:date mo))
                   (c/to-sql-time
                    (f/unparse
                     multi-parser
                     (f/parse multi-parser (:date mo))))
                   nil)
          :depart (:depart mo)
          :arrive (:arrive mo)
          :departtime (:departtime mo)
          :arrivetime (:arrivetime mo)
          :createdoperatorid userid
          :modifiedoperatorid userid
          :noofpax (:noofpax mo)
          :flightname (:flightname mo)})))
  )

(defn update-flight [mo bid userid]
  (:id (first
        (db/update-flightinfromation-by-id
         {:id (:id mo)
          :bookingid bid
          :cityid (:cityid mo)
          :journeytype (:journeytype mo)
          :flightnumber (:flightnumber mo)
          :date  (if (seq (:date mo))
                   (c/to-sql-time
                    (f/unparse
                     multi-parser
                     (f/parse multi-parser (:date mo))))
                   nil)
          :depart (:depart mo)
          :arrive (:arrive mo)
          :departtime (:departtime mo)
          :arrivetime (:arrivetime mo)
          :modifiedoperatorid userid
          :noofpax (:noofpax mo)
          :flightname (:flightname mo)})))
  )

(defn insert-hotel [mo bid userid]
  (:id (first
        (db/insert-hotelinfromation
         {:bookingid bid
          :cityid (:cityid mo)
          :hotelid (:hotelid mo)
          :address (:address mo)
          :telephonenumber (:telephonenumber mo)
          :faxnumber (:faxnumber mo)
          :checkin  (if (seq (:checkin mo))
                      (c/to-sql-time
                       (f/unparse
                        multi-parser
                        (f/parse multi-parser (:checkin mo))))
                      nil)
          :checkout  (if (seq (:checkout mo))
                       (c/to-sql-time
                        (f/unparse
                         multi-parser
                         (f/parse multi-parser (:checkout mo))))
                       nil)
          :numerofrooms (:numerofrooms mo)
          :numberofpeople (:numberofpeoples mo)
          :isbreakfastincluded (:isbreakfastincluded mo)
          :confirmationnumber (:confirmationnumber mo)
          :bookedundername (:bookedundername mo)
          :amount (:amount mo)
          :paymentmode (:paymentmode mo)
          :vendorid1 (:vendorid1 mo)
          :createdoperatorid userid
          :modifiedoperatorid userid}))))

(defn update-hotel [mo bid userid]
  (:id (first
        (db/update-hotelinfromation-by-id
         {:id (:id mo)
          :bookingid bid
          :cityid (:cityid mo)
          :hotelid (:hotelid mo)
          :address (:address mo)
          :telephonenumber (:telephonenumber mo)
          :faxnumber (:faxnumber mo)
          :checkin  (if (seq (:checkin mo))
                      (c/to-sql-time
                       (f/unparse
                        multi-parser
                        (f/parse multi-parser (:checkin mo))))
                      nil)
          :checkout  (if (seq (:checkout mo))
                       (c/to-sql-time
                        (f/unparse
                         multi-parser
                         (f/parse multi-parser (:checkout mo))))
                       nil)
          :numerofrooms (:numerofrooms mo)
          :numberofpeople (:numberofpeoples mo)
          :isbreakfastincluded (:isbreakfastincluded mo)
          :confirmationnumber (:confirmationnumber mo)
          :bookedundername (:bookedundername mo)
          :amount (:amount mo)
          :paymentmode (:paymentmode mo)
          :vendorid1 (:vendorid1 mo)
          :modifiedoperatorid userid}))))

(defn put-bookings [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [booking-data (map-to-db-data (filter (fn [k] (and (not= k "itinerarydetails")
                                                              (not= k "flightinformation")
                                                              (not= k "hotelinformation")))
                                                 (keys body)) body)
            user_id (:user_id (:identity request))
            bkng  (first (db/update-bookings-by-id
                          (assoc booking-data
                                 :travelsid (if (clojure.string/starts-with?
                                                 (booking-data :qrnumber) "7M/T")
                                              (booking-data :quotationid))
                                 :groupsid (if (clojure.string/starts-with?
                                                (booking-data :qrnumber) "7M/G")
                                             (booking-data :quotationid))
                                 :createdoperatorid user_id
                                 :modifiedoperatorid user_id
                                 :bookingagents (if (empty? (:bookingagents booking-data)) nil
                                                    (:bookingagents booking-data)))))

            bid (if (nil? (:id bkng))
                  (:id bkng)
                  (:id bkng))
            itno (first (db/get-bookings-number-by-id {:id (:id bid)}))]


        (doall (map (fn [mo] (if (nil? (:id mo))
                               (assoc mo :id (insert-booking mo bid user_id))
                               (assoc mo :id (update-bookings mo bid user_id))))
                    (:itinerarydetails body)))
        #_(doall  (db/delete-bookingdetails-by-bookingid
                   {:bookingid bid}))
        #_ (doall (map (fn [mo] (insert-booking mo bid user_id))
                       (:itinerarydetails body)))

        (doall (map (fn [mo] (if (nil? (:id mo))
                               (assoc mo :id (insert-flight mo bid user_id))
                               (assoc mo :id (update-flight mo  bid user_id))) )
                    (:flightinformation body)))
        #_(doall  (db/delete-flightinformation-by-bookingid
                   {:bookingid bid}))

        #_(doall (map (fn [mo]  (insert-flight mo bid user_id))
                      (:flightinformation body)))
        (doall (map (fn [mo] (if (nil? (:id mo))
                               (assoc mo :id (insert-hotel mo bid user_id))
                               (assoc mo :id (update-hotel mo bid user_id))))
                    (:hotelinformation body)
                    ))
        #_(doall  (db/delete-hotelinformation-by-bookingid
                   {:bookingid bid}))
        #_(doall (map (fn [mo] (insert-hotel mo bid user_id))
                      (:hotelinformation body)))

        (rr/content-type
         (rr/response (do
                        (gen-booking-pdf
                         booking-data
                         (:itinerarydetails body)
                         (:flightinformation body)
                         (:hotelinformation body))
                        {:id  (:id  booking-data)}))
         content-type)))))


(defn post-activitylog [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [userid (:user_id (:identity request))]
        (rr/content-type
         (rr/response
          (first (db/insert-activitylog {:userid userid
                                         :bookingid (:bookingid body)
                                        ; :cityid (:cityid body)
                                         :vendorid1 (:vendorid body)
                                         :bookingdetailid (:activityid body)
                                         :activitytext (:activitytext body)
                                         :type (:type body)
                                         :category (:category body)
                                         :createdoperatorid userid
                                         :modifiedoperatorid userid
                                         }
                                        )))
         content-type)))))

(defn logout [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/logout
                 {:id (body :id)
                  :token (get-token-from-request
                          (:headers request))})]
        (rr/content-type
         (rr/status
          {:body (if (pos? (count res))
                   {:message "successfully logged out"}
                   {:message "unAuthorized"})}
          (:status (if (pos? (count res)) 200
                       400)))
         content-type)))))

(defn fit-quotations-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-fit-quotations))
       content-type))))

(defn fit-agents-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-fit-agents))
       content-type))))

(defn groups-quotations-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-groups-quotations))
       content-type))))

(defn groups-agents-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-group-agents))
       content-type))))


(defn fit-quotations [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-fit-quotations-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalquotations
           (:count
            (first
             (db/get-active-fit-quotations-count)))
           :data res })
         content-type)))))

(defn groups-quotations [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-groups-quotations-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalquotations
           (:count
            (first
             (db/get-active-groups-quotations-count)))
           :data res })
         content-type)))))

(defn items [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-items-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalitems
           (:count
            (first
             (db/get-active-items-count)))
           :data res })
         content-type)))))


(defn items-search [index pagesize type text]
  (fn [request]
    (if type
      (case type
        "itemname" (if-not (authenticated? request)
                     (throw-unauthorized)
                     (rr/content-type
                      (rr/response {:totalitems
                                    (:totalitems
                                     (first  (db/get-items-count-by-itemname
                                              {:text text
                                               })))
                                    :data (db/get-all-items-by-index-pagesize-itemname
                                           {:index (parse-int index)
                                            :pagesize (parse-int pagesize)
                                            :text text
                                            })}) content-type))
        "city" (if-not (authenticated? request)
                 (throw-unauthorized)
                 (rr/content-type
                  (rr/response {:totalitems
                                (:totalitems
                                 (first  (db/get-items-count-by-cityname
                                          {:text text})))
                                :data (db/get-all-items-by-index-pagesize-cityname
                                       {:index (parse-int index)
                                        :pagesize (parse-int pagesize)
                                        :text text})}) content-type))
        "category" (if-not (authenticated? request)
                     (throw-unauthorized)
                     (rr/content-type
                      (rr/response {:totalitems
                                    (:totalitems
                                     (first (db/get-items-count-by-category
                                             {:text text})))
                                    :data (db/get-all-items-by-index-pagesize-category
                                           {:index (parse-int index)
                                            :pagesize (parse-int pagesize)
                                            :text text})}) content-type))
        "vendorname" (if-not (authenticated? request)
                       (throw-unauthorized)
                       (rr/content-type
                        (rr/response {:totalitems
                                      (:totalitems
                                       (first (db/get-items-count-by-vendorname
                                               {:text text})))
                                      :data (db/get-all-items-by-index-pagesize-vendorname
                                             {:index (parse-int index)
                                              :pagesize (parse-int pagesize)
                                              :text text})}) content-type))
        "description" (if-not (authenticated? request)
                        (throw-unauthorized)
                        (rr/content-type
                         (rr/response {:totalitems
                                       (:totalitems
                                        (first (db/get-items-count-by-description
                                                {:text text})))
                                       :data (db/get-all-items-by-index-pagesize-description
                                              {:index (parse-int index)
                                               :pagesize (parse-int pagesize)
                                               :text text})}) content-type)))
      (if-not (authenticated? request)
        (throw-unauthorized)
        (let [res (db/get-all-items-by-index-pagesize
                   {:index (parse-int index)
                    :pagesize (parse-int pagesize)})]
          (rr/content-type
           (rr/response
            {:totalitems
             (:count
              (first
               (db/get-active-items-count)))
             :data res })
           content-type))))))

(defn item-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/insert-item
                body)))
       content-type))))

(defn item-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-items body)))
       content-type))))

(defn items-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-items))
       content-type))))

(defn vendors [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-vendors-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalvendors
           (:count
            (first
             (db/get-active-vendors-count)))
           :data res })
         content-type)))))

(defn vendor-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/insert-vendor
                body)))
       content-type))))

(defn vendors-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first
         (db/update-vendors body)))
       content-type))))

(defn vendors-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-vendors))
       content-type))))


(defn cities [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-cities-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalcities
           (:count
            (first
             (db/get-active-cities-count)))
           :data res })
         content-type)))))

(defn cities-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/insert-cities
                body)))
       content-type))))

(defn cities-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-cities))
       content-type))))

(defn hotels [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-hotels-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalhotels
           (:count
            (first
             (db/get-active-hotels-count)))
           :data res })
         content-type)))))

(defn hotels-search [index pagesize type text]
  (fn [request]
    (if type
      (case type
        "hotelname" (if-not (authenticated? request)
                      (throw-unauthorized)
                      (rr/content-type
                       (rr/response {:totalhotels
                                     (:totalhotels
                                      (first  (db/get-hotels-count-by-hotelname
                                               {:text text
                                                })))
                                     :data (db/get-all-hotels-by-index-pagesize-hotelname
                                            {:index (parse-int index)
                                             :pagesize (parse-int pagesize)
                                             :text text
                                             })}) content-type))
        "city" (if-not (authenticated? request)
                 (throw-unauthorized)
                 (rr/content-type
                  (rr/response {:totalhotels
                                (:totalhotels
                                 (first  (db/get-hotels-count-by-cityname
                                          {:text text})))
                                :data (db/get-all-hotels-by-index-pagesize-cityname
                                       {:index (parse-int index)
                                        :pagesize (parse-int pagesize)
                                        :text text})}) content-type))
        "category" (if-not (authenticated? request)
                     (throw-unauthorized)
                     (rr/content-type
                      (rr/response {:totalhotels
                                    (:totalhotels
                                     (first (db/get-hotels-count-by-category
                                             {:text text})))
                                    :data (db/get-all-hotels-by-index-pagesize-category
                                           {:index (parse-int index)
                                            :pagesize (parse-int pagesize)
                                            :text text})}) content-type)))
      (if-not (authenticated? request)
        (throw-unauthorized)
        (let [res (db/get-all-hotels-by-index-pagesize
                   {:index (parse-int index)
                    :pagesize (parse-int pagesize)})]
          (rr/content-type
           (rr/response
            {:totalhotels
             (:count
              (first
               (db/get-active-hotels-count)))
             :data res})
           content-type))))))

(defn hotels-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/insert-hotels
                (assoc body
                       :estimatedroomrate
                       (if (empty? (:estimatedroomrate body)) nil
                           (read-string
                            (:estimatedroomrate body)))))))
       content-type))))

(defn hotels-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-hotels
                (assoc body
                       :estimatedroomrate
                       (if (empty? (:estimatedroomrate body)) nil
                           (read-string
                            (:estimatedroomrate body)))))))
       content-type))))

(defn hotels-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-hotels))
       content-type))))

(defn hotels-with-cityname []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-hotels-cityname))
       content-type))))

(defn tourguides [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-tourguides-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totaltourguides
           (:count
            (first
             (db/get-active-tourguides-count)))
           :data res })
         content-type)))))

(defn tourguides-search [index pagesize type text]
  (fn [request]
    (if type
      (case type
        "tourguidename" (if-not (authenticated? request)
                          (throw-unauthorized)
                          (rr/content-type
                           (rr/response {:totaltourguides
                                         (:totaltourguides
                                          (first  (db/get-tourguides-count-by-tourguidename
                                                   {:text text
                                                    })))
                                         :data (db/get-all-tourguides-by-index-pagesize-tourguidename
                                                {:index (parse-int index)
                                                 :pagesize (parse-int pagesize)
                                                 :text text
                                                 })}) content-type))
        "city" (if-not (authenticated? request)
                 (throw-unauthorized)
                 (rr/content-type
                  (rr/response {:totaltourguides
                                (:totaltourguides
                                 (first  (db/get-tourguides-count-by-cityname
                                          {:text text})))
                                :data (db/get-all-tourguides-by-index-pagesize-cityname
                                       {:index (parse-int index)
                                        :pagesize (parse-int pagesize)
                                        :text text})}) content-type)))
      (if-not (authenticated? request)
        (throw-unauthorized)
        (let [res (db/get-all-tourguides-by-index-pagesize
                   {:index (parse-int index)
                    :pagesize (parse-int pagesize)})]
          (rr/content-type
           (rr/response
            {:totaltourguides
             (:count (first (db/get-active-tourguides-count)))
             :data res})
           content-type))))))

(defn tourguides-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [data (first(db/insert-tourguides
                        (assoc body
                               :bankorpaymentinfo
                               (if (empty? (:bankorpaymentinfo body)) nil
                                   (:bankorpaymentinfo body)))))
            city (first (db/get-city-name-by-id
                         {:cityid (body :cityid)}))
            quick-data (post-tour-guide-vendor
                        (assoc body
                               :tourguidename (str (:tourguidename body) "-" (:id data))
                               :city (city :cityname)
                               ))
            quick-book-vendor ((parse-string
                                (quick-data :body)
                                (fn [k] (keyword k)))
                               :Vendor)]
        (do
          (when (= (quick-data :status) 200)
            (db/update-tour-guide-quickbook-id
             {:id (data :id)
              :quickbookid  (quick-book-vendor
                             :Id)}))
          (rr/content-type
           (rr/response
            data)
           content-type))))))

(defn tourguides-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-tourguides body)))
       content-type))))

(defn tourguides-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-tourguides))
       content-type))))


(defn restaurants [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-restaurants-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalrestaurants
           (:count
            (first
             (db/get-active-restaurants-count)))
           :data res })
         content-type)))))

(defn restaurants-search [index pagesize type text]
  (fn [request]
    (if type
      (case type
        "restaurantname" (if-not (authenticated? request)
                           (throw-unauthorized)
                           (rr/content-type
                            (rr/response
                             {:totalrestaurants
                              (:totalrestaurants
                               (first  (db/get-restaurants-count-by-restaurantname
                                        {:text text})))
                              :data (db/get-all-restaurants-by-index-pagesize-restaurantname
                                     {:index (parse-int index)
                                      :pagesize (parse-int pagesize)
                                      :text text})}) content-type))
        "city" (if-not (authenticated? request)
                 (throw-unauthorized)
                 (rr/content-type
                  (rr/response {:totalrestaurants
                                (:totalrestaurants
                                 (first  (db/get-restaurants-count-by-cityname
                                          {:text text})))
                                :data (db/get-all-restaurants-by-index-pagesize-cityname
                                       {:index (parse-int index)
                                        :pagesize (parse-int pagesize)
                                        :text text})}) content-type))
        "area" (if-not (authenticated? request)
                 (throw-unauthorized)
                 (rr/content-type
                  (rr/response {:totalrestaurants
                                (:totalrestaurants
                                 (first (db/get-restaurants-count-by-area
                                         {:text text})))
                                :data (db/get-all-restaurants-by-index-pagesize-area
                                       {:index (parse-int index)
                                        :pagesize (parse-int pagesize)
                                        :text text})}) content-type)))
      (if-not (authenticated? request)
        (throw-unauthorized)
        (let [res (db/get-all-restaurants-by-index-pagesize
                   {:index (parse-int index)
                    :pagesize (parse-int pagesize)})]
          (rr/content-type
           (rr/response
            {:totalrestaurants (:count (first (db/get-active-restaurants-count)))
             :data res }) content-type))))))

(defn restaurants-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [data (first (db/insert-restaurants
                         body))
            city (first (db/get-city-name-by-id
                         {:cityid (body :cityid)}))
            quick-data (post-restaurent-vendor
                        (assoc body
                               :restaurantname (str (:restaurantname body)
                                                    "-"
                                                    (:id data))
                               :city (city :cityname)))
            quick-book-vendor ((parse-string
                                (quick-data :body)
                                (fn [k] (keyword k)))
                               :Vendor)]
        (do
          (when (= (quick-data :status) 200)
            (db/update-restaurant-quickbook-id
             {:id (data :id)
              :quickbookid  (quick-book-vendor
                             :Id)}))
          (rr/content-type
           (rr/response
            data)
           content-type))))))

(defn restaurants-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-restaurants body)))
       content-type))))

(defn restaurants-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-restaurants))
       content-type))))

(defn transportation [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-transportation-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totaltransportations
           (:count
            (first
             (db/get-active-transportation-count)))
           :data res })
         content-type)))))


(defn transportations-search [index pagesize type text]
  (fn [request]
    (if type
      (case type
        "transportationname" (if-not (authenticated? request)
                               (throw-unauthorized)
                               (rr/content-type
                                (rr/response
                                 {:totaltransportations
                                  (:totaltransportations
                                   (first  (db/get-transportations-count-by-transportationname
                                            {:text text})))
                                  :data (db/get-all-transportations-by-index-pagesize-transportationname
                                         {:index (parse-int index)
                                          :pagesize (parse-int pagesize)
                                          :text text
                                          })}) content-type))
        "city" (if-not (authenticated? request)
                 (throw-unauthorized)
                 (rr/content-type
                  (rr/response {:totaltransportations
                                (:totaltransportations
                                 (first  (db/get-transportations-count-by-cityname
                                          {:text text})))
                                :data (db/get-all-transportations-by-index-pagesize-cityname
                                       {:index (parse-int index)
                                        :pagesize (parse-int pagesize)
                                        :text text})}) content-type)))
      (if-not (authenticated? request)
        (throw-unauthorized)
        (let [res (db/get-all-transportation-by-index-pagesize
                   {:index (parse-int index)
                    :pagesize (parse-int pagesize)})]
          (rr/content-type
           (rr/response
            {:totaltransportations
             (:count
              (first
               (db/get-active-transportation-count)))
             :data res })
           content-type))))))

(defn transportation-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [data (first  (db/insert-transportation
                          body))
            city (first (db/get-city-name-by-id
                         {:cityid (body :cityid)}))
            quick-data (post-transportportation-vendor
                        (assoc body
                               :transportationname (str (:transportationname body)
                                                        "-" (:id data))
                               :city (city :cityname)))
            quick-book-vendor ((parse-string
                                (quick-data :body)
                                (fn [k] (keyword k)))
                               :Vendor)]
        (do
          (when (= (quick-data :status) 200)
            (db/update-transportation-quickbook-id
             {:id (data :id)
              :quickbookid  (quick-book-vendor
                             :Id)}))
          (rr/content-type
           (rr/response
            data)
           content-type))))))

(defn transportation-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-transportation body)))
       content-type))))

(defn transportation-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-transportation))
       content-type))))

(defn bookingfits [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-bookingfits-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalbookingfits
           (:count
            (first
             (db/get-active-bookingfits-count)))
           :data res })
         content-type)))))

(defn bookingfits-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/insert-bookingfits
                body)))
       content-type))))

(defn bookingfits-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-bookingfits body)))
       content-type))))

(defn events [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-events-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totalbookingfits
           (:count
            (first
             (db/get-active-events-count)))
           :data res })
         content-type)))))

(defn events-search [index pagesize type text]
  (fn [request]
    (if type
      (case type
        "eventname" (if-not (authenticated? request)
                      (throw-unauthorized)
                      (rr/content-type
                       (rr/response
                        {:totalevents
                         (:totalevents
                          (first  (db/get-events-count-by-eventname
                                   {:text text})))
                         :data (db/get-all-events-by-index-pagesize-eventname
                                {:index (parse-int index)
                                 :pagesize (parse-int pagesize)
                                 :text text})}) content-type))
        "city" (if-not (authenticated? request)
                 (throw-unauthorized)
                 (rr/content-type
                  (rr/response {:totalevents
                                (:totalevents
                                 (first  (db/get-events-count-by-cityname
                                          {:text text})))
                                :data (db/get-all-events-by-index-pagesize-cityname
                                       {:index (parse-int index)
                                        :pagesize (parse-int pagesize)
                                        :text text})}) content-type)))
      (if-not (authenticated? request)
        (throw-unauthorized)
        (let [res (db/get-all-events-by-index-pagesize
                   {:index (parse-int index)
                    :pagesize (parse-int pagesize)})]
          (rr/content-type
           (rr/response
            {:totalevents
             (:count
              (first
               (db/get-active-events-count)))
             :data res })
           content-type))))))

(defn events-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/insert-events
                body)))
       content-type))))

(defn events-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-events body)))
       content-type))))


(defn register-user [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response (create (map-to-db-data (keys body) body)))
       content-type))))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn upload-file-for-hotels [tempfile filename]
  (let [ui-filename (str (uuid) "-" filename)]
    (io/copy tempfile (io/file "hotelcontracts" ui-filename))
    (rr/content-type
     (rr/response
      {:message "success"
       :filename ui-filename})
     content-type)))

(defn upload-file-for-logos [tempfile filename]
  (let [ui-filename (str (uuid) "-" filename)]
    (io/copy tempfile (io/file "resources/public/dist/img/logos" ui-filename))
    (rr/content-type
     (rr/response
      {:message "success"
       :filename ui-filename})
     content-type)))

(defn logos [index pagesize]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [res (db/get-all-logos-by-index-pagesize
                 {:index index
                  :pagesize pagesize})]
        (rr/content-type
         (rr/response
          {:totallogos
           (:count
            (first
             (db/get-active-logos-count)))
           :data res })
         content-type)))))

(defn logos-search [index pagesize type text]
  (fn [request]
    (if type
      (case type
        "logoname" (if-not (authenticated? request)
                     (throw-unauthorized)
                     (rr/content-type
                      (rr/response
                       {:totallogos
                        (:totallogos
                         (first  (db/get-logo-count-by-logoname
                                  {:text text})))
                        :data (db/get-all-logo-by-index-pagesize-logoname
                               {:index (parse-int index)
                                :pagesize (parse-int pagesize)
                                :text text
                                })}) content-type)))
      (if-not (authenticated? request)
        (throw-unauthorized)
        (let [res (db/get-all-logos-by-index-pagesize
                   {:index (parse-int index)
                    :pagesize (parse-int pagesize)})]
          (rr/content-type
           (rr/response
            {:totallogos
             (:count
              (first
               (db/get-active-logos-count)))
             :data res })
           content-type))))))

(defn logos-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/insert-logos
                body)))
       content-type))))

(defn logos-put [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (first (db/update-logos-by-id body)))
       content-type))))

(defn logos-all []
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (db/get-logos))
       content-type))))

(defn get-vendors-by-category-and-city [cityid category]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [low-case-cat(str/lower-case category)
            city-id (read-string cityid)]
        (rr/content-type
         (rr/response
          (cond (= low-case-cat "attractions") (db/get-events-by-city
                                                {:cityid city-id})
                (= low-case-cat "guidedcitytours")(db/get-tour-guides-by-city
                                                   {:cityid city-id})
                (= low-case-cat "meals")(db/get-restaurents-by-city
                                         {:cityid city-id})
                (= low-case-cat "hotel")(db/get-hotels-by-city
                                         {:cityid city-id})
                (= low-case-cat "transportation") (db/get-transportation-by-city
                                                   {:cityid city-id})
                :else []))
         content-type)))))

(defn get-fit-activities-by-category-and-city [cityid category]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [low-case-cat(str/lower-case category)
            city-id (read-string cityid)]
        (rr/content-type
         (rr/response
          (db/get-fit-activities-by-city-category
           {:category category
            :cityid city-id}))
         content-type)))))

(defn get-group-activities-by-category-and-city [cityid category]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [low-case-cat(str/lower-case category)
            city-id (read-string cityid)]
        (rr/content-type
         (rr/response
          (db/get-group-activities-by-city-category
           {:category category
            :cityid city-id}))
         content-type)))))

(defn payment-log-post [body]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [userid (parse-int
                    (:user_id (:identity request)))]
        (rr/content-type
         (rr/response
          (first (db/insert-payment-log
                  (assoc body
                         :createdoperatorid userid
                         :modifiedoperatorid userid))))
         content-type)))))

(defn delete-hotel-information [id]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (let [userid (parse-int
                    (:user_id (:identity request)))]
        (rr/content-type
         (rr/response
          (first (db/delete-hotel-information
                  {:id (parse-int id)
                   :modifiedoperatorid userid})))
         content-type)))))

(defn get-payment-logs
  [pagesize index paymentid bookingid]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (cond (and (nil? paymentid) (nil? bookingid)){:records ((first (db/get-payment-logs)) :count)
                                                      :data (db/get-payment-logs-index-page-size
                                                             {:index index
                                                              :pagesize pagesize})}
              (nil? bookingid){:records ((first (db/get-payment-logs-by-payment-id-count
                                                 {:paymentid (read-string paymentid)}))
                                         :count)
                               :data (db/get-payment-logs-by-payment-id-index-page-size
                                      {:paymentid (read-string paymentid)
                                       :index index
                                       :pagesize pagesize})}
              (nil? paymentid){:records ((first (db/get-payment-logs-by-booking-id-count
                                                 {:bookingid (read-string bookingid)}))
                                         :count)
                               :data (db/get-payment-logs-by-booking-id-index-page-size
                                      {:bookingid (read-string bookingid)
                                       :index index
                                       :pagesize pagesize})}
              :else {:records ((first (db/get-payment-logs-by-payment-id-booking-id-count
                                       {:bookingid (read-string bookingid)
                                        :paymentid (read-string paymentid)}))
                               :count)
                     :data (db/get-payment-logs-by-payment-id-booking-id-index-page-size
                            {:paymentid (read-string paymentid)
                             :bookingid (read-string bookingid)
                             :index index
                             :pagesize pagesize})}))
       content-type))))


(defn get-activity-logs
  [pagesize index activityid bookingid]
  (fn [request]
    (if-not (authenticated? request)
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (cond (and (nil? activityid) (nil? bookingid)){:records ((first (db/get-activity-logs)) :count)
                                                       :data (db/get-activity-logs-index-page-size
                                                              {:index index
                                                               :pagesize pagesize})}
              (nil? bookingid){:records ((first (db/get-activity-logs-by-activity-id-count
                                                 {:activityid (read-string activityid)}))
                                         :count)
                               :data (db/get-activity-logs-by-activity-id-index-page-size
                                      {:activityid (read-string activityid)
                                       :index index
                                       :pagesize pagesize})}
              (nil? activityid){:records ((first (db/get-activity-logs-by-booking-id-count
                                                  {:bookingid (read-string bookingid)}))
                                          :count)
                                :data (db/get-activity-logs-by-booking-id-index-page-size
                                       {:bookingid (read-string bookingid)
                                        :index index
                                        :pagesize pagesize})}
              :else {:records ((first (db/get-activity-logs-by-activity-id-booking-id-count
                                       {:bookingid (read-string bookingid)
                                        :activityid (read-string activityid)}))
                               :count)
                     :data (db/get-activity-logs-by-activity-id-booking-id-index-page-size
                            {:activityid (read-string activityid)
                             :bookingid (read-string bookingid)
                             :index index
                             :pagesize pagesize})}))
       content-type))))

(defn excel-restaurent-in-order [h]
  [(:cityname h) (:restaurantname h) (:rating h) (:area h) (:address h)
   (:cuisine h)(:remarks h) (:contactperson h) (:contactnumber h)
   (:cellnumber h) (:faxnumber h) (:email h) (:lunch h) (:dinner h)
   (:kids h)])


(defn create-excel-file-restaurents []
  (let [db-data (db/get-restaurents-excel-data)
        y (reduce (fn [acc o](merge acc (excel-restaurent-in-order o)))
                  [["City Name" "Restaurant Name" "7M Rating" "Area"
                    "Address" "Cuisine" "Remarks" "Contact Person"
                    "Contact Number" "Cell Number" "Fax Number"
                    "Email" "Lunch" "Dinner" "Child/Student Cost"]]
                  db-data)
        wb (create-workbook "Restaurent Names" y)
        head-keys (first (row-seq (select-sheet "Restaurent Names" wb)))]
    (do
      (set-row-style! head-keys (create-cell-style! wb {:background :yellow,
                                                        :font {:bold true}}))
      (save-workbook! "Restaurents.xlsx" wb))))


(defn excel-hotel-in-order [h]
  [(:hotelname h) (:cityname h) (:rating h) (:type h) (:cost h) (:address h)
   (:phonenumber h) (:faxnumber h) (:contactpersonname h)
   (:contactpersonphonenumber h) (:contactpersonemail h)
   (:estimatedroomrate h) (:remarks h) (:cancellationpolicy h)
   (:description h)])


(defn create-excel-file-hotels []
  (let [db-data (db/get-hotels-excel-data)
        y (reduce (fn [acc o](merge acc (excel-hotel-in-order o)))
                  [["Hotel Name" "City Name" "7M Rating" "Contract Type" "Cost"
                    "Address" "Phone Number" "Fax Number" "Contact Person Name"
                    "Contact Person Phone #" "Contact Person Email"
                    "Estimated Room Rates" "Remarks" "Cancellation Policy"
                    "Description"]]
                  db-data)
        wb (create-workbook "Hotels" y)
        head-keys (first (row-seq (select-sheet "Hotels" wb)))]
    (do
      (set-row-style! head-keys (create-cell-style! wb {:background :yellow,
                                                        :font {:bold true}}))
      (save-workbook! "Hotels.xlsx" wb))))

(defn excel-tour-guide-in-order [tg]
  [(:cityname tg) (:tourguidename tg) (:rating tg) (:address tg)
   (:contactnumber tg) (:cellnumber tg) (:email tg)
   (:bankorpaymentinfo tg) (:amount tg)])

(defn create-excel-file-tourguides []
  (let [db-data (db/get-tourguides-excel-data)
        y (reduce (fn [acc o](merge acc (excel-tour-guide-in-order o)))
                  [["City Name" "Tour Guide Name" "7M Rating" "Address"
                    "Contact Number" "Cell Number" "Email" "Bank/Payment Info"
                    "Tour Guide Rate per hr"]] db-data)
        wb (create-workbook "Tour Guides" y)
        head-keys (first (row-seq (select-sheet "Tour Guides" wb)))]
    (do
      (set-row-style! head-keys (create-cell-style! wb {:background :yellow,
                                                        :font {:bold true}}))
      (save-workbook! "Tour Guides.xlsx" wb))))

(defn excel-transportation-in-order [t]
  [(:cityname t) (:transportationname t) (:rating t) (:type t)
   (:address t) (:contactperson t) (:contactnumber t)
   (:faxnumber t) (:email t) (:bankinformation t)])

(defn create-excel-file-transportation []
  (let [db-data (db/get-transportation-excel-data)
        y (reduce (fn [acc o](merge acc (excel-transportation-in-order o)))
                  [["City Name" "Transportation Name" "7M Rating" "Type"
                    "Address" "Contact Person" "Contact Number" "Fax Number"
                    "Email" "Bank/Payment Info"]] db-data)
        wb (create-workbook "Transportation" y)
        head-keys (first (row-seq (select-sheet "Transportation" wb)))]
    (do
      (set-row-style! head-keys (create-cell-style! wb {:background :yellow,
                                                        :font {:bold true}}))
      (save-workbook! "Transportation.xlsx" wb))))

(defn excel-event-in-order [e]
  [(:cityname e) (:eventname e) (:rating e) (:contract e)
   (:meal e) (:actual e) (:remarks e) (:bookingguide e)
   (:contactnumber e) (:phonenumber e) (:faxnumber e)
   (:website e) (:email e) (:adultcost e) (:childcost e)
   (:st e) (:cp e)])


(defn create-excel-file-events []
  (let [db-data (db/get-events-excel-data)
        y (reduce (fn [acc o](merge acc (excel-event-in-order o)))
                  [["City Name" "Event Name" "7M Rating" "Contract Type"
                    "Meal Inclusion" "Actual Price" "Remarks" "Booking Guide"
                    "Contact Number" "Phone Number" "Fax Number" "Website"
                    "Email" "Adult Cost" "Child Cost" "Student Cost"
                    "Com Policy"]] db-data)
        wb (create-workbook "Events" y)
        head-keys (first (row-seq (select-sheet "Events" wb)))]
    (do
      (set-row-style! head-keys (create-cell-style! wb {:background :yellow,
                                                        :font {:bold true}}))
      (save-workbook! "Events.xlsx" wb))))

(defn excel-activities-in-order [e]
  [(:cityname e) (:category e) (:vendorname e) (:type e)
   (:itemname e) (:activitytitle e) (:description e)])

(defn create-excel-file-activities []
  (let [db-data (db/get-activities-excel-data)
        y (reduce (fn [acc o](merge acc (excel-activities-in-order o)))
                  [["City" "Category" "Vendor" "Type" "Activity Name"
                    "Activity Title" "Activity Description"]] db-data)
        wb (create-workbook "Activities" y)
        head-keys (first (row-seq (select-sheet "Activities" wb)))]
    (do
      (set-row-style! head-keys (create-cell-style! wb {:background :yellow,
                                                        :font {:bold true}}))
      (save-workbook! "Activities.xlsx" wb))))

(defn get-restaurents-excel [token]
  (fn [request]
    (if (empty? (db/validate-token
                 {:token token}))
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (do
          (create-excel-file-restaurents)
          (io/file "Restaurents.xlsx")))
       "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;Content-Disposition:attachment;
         filename:restaurents.xlsx"))))

(defn get-hotels-excel [token]
  (fn [request]
    (if (empty? (db/validate-token
                 {:token token}))
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (do
          (create-excel-file-hotels)
          (io/file "Hotels.xlsx")))
       "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;Content-Disposition:attachment;
         filename:hotels.xlsx"))))

(defn get-tourguides-excel [token]
  (fn [request]
    (if (empty? (db/validate-token
                 {:token token}))
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (do
          (create-excel-file-tourguides)
          (io/file "Tour Guides.xlsx")))
       "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;Content-Disposition:attachment;
         filename:tourguides.xlsx"))))

(defn get-transportation-excel [token]
  (fn [request]
    (if (empty? (db/validate-token
                 {:token token}))
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (do
          (create-excel-file-transportation)
          (io/file "Transportation.xlsx")))
       "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;Content-Disposition:attachment;
         filename:transportation.xlsx"))))

(defn get-events-excel [token]
  (fn [request]
    (if (empty? (db/validate-token
                 {:token token}))
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (do
          (create-excel-file-events)
          (io/file "Events.xlsx")))
       "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;Content-Disposition:attachment;
         filename:events.xlsx"))))

(defn get-activities-excel [token]
  (fn [request]
    (if (empty? (db/validate-token
                 {:token token}))
      (throw-unauthorized)
      (rr/content-type
       (rr/response
        (do
          (create-excel-file-activities)
          (io/file "Activities.xlsx")))
       "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;Content-Disposition:attachment;
         filename:Activities.xlsx"))))

(defn get-booking-agents [row] (if (nil? (:bookingagents row))
                                 row
                                 (assoc row :bookingagents
                                        (seq (.getArray
                                              (:bookingagents row))))))

;; Routes definition
;; ------------------------------------
(defroutes app-routes
  (GET "/" [] home)

  (POST "/register" {body :body}
        (register-user body))

  (POST "/login" {body :body}
        (let [res (validate-password (map-to-db-data
                                      (keys body) body))]
          (rr/content-type
           (rr/status {:body (:body res)} (:status res))
           content-type)))
  (GET "/users/search"
       [index pagesize type text :as {params :params}]
       (users-search index pagesize type text))

  (GET "/users/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-users-by-id
                 {:id (parse-int id)})))
        content-type))
  (PUT "/users/:id" {body :body}
       (user-put body))
  (DELETE
   "/users/:id" [id]
   (db/delete-user-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))
  (GET "/fit/pdf/:qrnumber" [qrnumber]
       (get-fit-pdf qrnumber))

  (GET "/groups/pdf/:qrnumber" [qrnumber]
       (get-groups-pdf qrnumber))

  (GET "/bookings/pdf/:bookingnumber" [bookingnumber]
       (get-bookings-pdf bookingnumber))

  (POST "/bookings/pdf/email" {body :body}
        (send-bookings-pdf-email body))

  (GET "/fit/quotations" []
       (fit-quotations-all))

  (GET "/fitagents" []
       (fit-agents-all))

  (GET "/groups/quotations" []
       (groups-quotations-all))

  (GET "/groupsagents" []
       (groups-agents-all))

  (POST "/bookings" {body :body}
        (post-bookings body))
  (GET "/bookings/:index/:pagesize" [index pagesize]
       (bookings index pagesize))

  (GET "/fit/bookings/:index/:pagesize" [index pagesize]
       (fit-bookings index pagesize))

  (GET "/fit/bookings/search"
       [index pagesize type text :as {params :params}]
       (fit-bookings-search index pagesize type text))

  (GET "/groups/bookings/:index/:pagesize" [index pagesize]
       (groups-bookings index pagesize))

  (GET "/groups/bookings/search"
       [index pagesize type text :as {params :params}]
       (groups-bookings-search index pagesize type text))

  (GET "/bookings/:id" [id]
       (let [booking (j/query db/conn ["SELECT id, (SELECT  id as quotationid  FROM Travels where id = travelsid  UNION ALL SELECT  id as quotationid FROM groups where id = groupsid), (SELECT  qrnumber FROM Travels where id = travelsid  UNION ALL SELECT  qrnumber FROM groups where id = groupsid), bookingnumber, isactive, email, agentname, guestname, isfinalbooking, emergencynumbers, termsandconditions, logoid,noofpax,nameoftourmanager,remarks,bookingagents FROM bookings where isactive = TRUE and id =?" (parse-int id)]
                              :row-fn get-booking-agents)]
         (do
           (rr/content-type
            (rr/response
             {:booking booking
              :itinerarydetails (db/get-booking-details-by-booking-id
                                 {:bookingid (parse-int id)})
              :flightinformation (db/get-flightinformation-by-booking-id
                                  {:bookingid (parse-int id)})
              :hotelinformation  (db/get-hotelinformation-by-booking-id
                                  {:bookingid (parse-int id)})}) content-type))))

  (POST "/logout" {body :body}
        (logout body))

  (PUT "/bookings/:id" {body :body}
       (put-bookings body))

  (DELETE
   "/bookings/:id" [id]
   (db/delete-bookings-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (DELETE
   "/bookings/:bookingid/:cityid" [bookingid cityid]
   (fn [request]
     (if-not (authenticated? request)
       (throw-unauthorized)
       (let [userid (:user_id (:identity request))]
         (rr/content-type
          (rr/response
           {:bookingdetailid (vals (first (db/delete-bookingdetails-by-cityid {:bookingid (parse-int bookingid)
                                                                               :cityid (parse-int cityid)
                                                                               :modifiedoperatorid (parse-int userid)
                                                                               }
                                                                              )))
            :flightid (vals (first (db/delete-flightinformation-by-cityid {:bookingid (parse-int bookingid)
                                                                           :cityid (parse-int cityid)
                                                                           :modifiedoperatorid (parse-int userid)
                                                                           }
                                                                          )))
            :hotelid  (vals (first (db/delete-hotelinformation-by-cityid {:bookingid (parse-int bookingid)
                                                                          :cityid (parse-int cityid)
                                                                          :modifiedoperatorid (parse-int userid)
                                                                          }
                                                                         )))})
          content-type)))))
  (DELETE
   "/bookingdetails/:bookingdetailid" [bookingdetailid]
   (fn [request]
     (if-not (authenticated? request)
       (throw-unauthorized)
       (let [userid (:user_id (:identity request))]
         (rr/content-type
          (rr/response
           (first (db/delete-bookingdetails-by-bookingdetailid {:bookingdetailid (parse-int bookingdetailid)
                                                                :modifiedoperatorid (parse-int userid)
                                                                }
                                                               )))
          content-type)))))

  (DELETE
   "/flightinformation/:flightid" [flightid]
   (fn [request]
     (if-not (authenticated? request)
       (throw-unauthorized)
       (let [userid (:user_id (:identity request))]
         (rr/content-type
          (rr/response
           (first (db/delete-flightinformation-by-flightid {:flightid (parse-int flightid)
                                                            :modifiedoperatorid (parse-int userid)
                                                            }
                                                           )))
          content-type)))))

  (POST "/activitylog" {body :body}
        (post-activitylog body))

  (GET "/activitylog/itinerarydetails/:bookingdetailid" [bookingdetailid]
       (rr/content-type
        (rr/response (db/get-activitylog-by-bookingdetail-id {:bookingdetailid (parse-int bookingdetailid)}))
        content-type))

  (GET "/activitylog/flightinformation/:bookingid/:cityid" [bookingid cityid]
       (rr/content-type
        (rr/response (db/get-activitylog-by-flightinformation {:bookingid (parse-int bookingid)
                                                               :cityid (parse-int cityid)}))
        content-type))

  (GET "/activitylog/hotelinformation/:bookingid/:cityid" [bookingid cityid]
       (rr/content-type
        (rr/response (db/get-activitylog-by-hotelinformation  {:bookingid (parse-int bookingid)
                                                               :cityid (parse-int cityid)}))
        content-type))

  (GET "/fit/quotations/:index/:pagesize" [index pagesize]
       (fit-quotations (parse-int index)
                       (parse-int  pagesize)))
  (GET "/groups/quotations/:index/:pagesize" [index pagesize]
       (groups-quotations (parse-int index)
                          (parse-int  pagesize)))

  (GET "/packages/all" []
       (rr/content-type
        (rr/response (db/get-all-packages))
        content-type))

  (GET "/packages/all/groups" []
       (rr/content-type
        (rr/response (db/get-all-packages1))
        content-type))

  (GET "/packages/:index/:pagesize" [index pagesize]
       (packages index pagesize))

  (GET "/packages/search" [index page-size name
                           :as {params :params}]
       (packages1 index page-size name))

  (GET "/package/between/:total" [total]
       (rr/content-type
        (rr/response
         (db/get-all-packages-between
          {:total (parse-int total)}))
        content-type))

  (GET "/packages/:id" [id]
       (rr/content-type
        (rr/response
         (db/get-packages-by-id
          {:id (parse-int id)}))
        content-type))

  (GET "/logos/search"
       [index pagesize type text :as {params :params}]
       (logos-search index pagesize type text))

  (POST "/logos/upload"
        {{{tempfile :tempfile filename :filename} :file } :params :as params}
        (upload-file-for-logos tempfile filename))

  (GET "/logosfilename/:filename" [filename]
       (get-logos-logofile  filename))

  (POST "/logos" {body :body}
        (logos-post body))

  (GET "/logos" []
       (logos-all))

  (GET "/logos/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-logos-by-id
                 {:id (parse-int id)})))
        content-type))

  (PUT "/logos/:id" {body :body}
       (logos-put body))

  (DELETE
   "/logos/:id" [id]
   (db/delete-logos-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (GET "/items/:index/:pagesize" [index pagesize]
       (items (parse-int index)
              (parse-int  pagesize)))

  (GET "/items/search"
       [index pagesize type text :as {params :params}]
       (items-search index pagesize type text))

  (GET "/items" []
       (items-all))

  (POST "/items" {body :body}
        (item-post body))

  (GET "/items/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-items-by-id
                 {:id (parse-int id)})))
        content-type))

  (GET "/itemsbyfitcityid/:cityid" [cityid]
       (rr/content-type
        (rr/response
         (db/get-items-by-fit-cityid
          {:cityid (parse-int cityid)}))
        content-type))

  (GET "/itemsbygroupcityid/:cityid" [cityid]
       (rr/content-type
        (rr/response
         (db/get-items-by-group-cityid
          {:cityid (parse-int cityid)}))
        content-type))


  (PUT "/items/:id" {body :body}
       (item-put body))

  (DELETE
   "/items/:id" [id]
   (db/delete-items-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))


  (GET "/vendors/:index/:pagesize" [index pagesize]
       (vendors (parse-int index)
                (parse-int  pagesize)))

  (GET "/vendors" []
       (vendors-all))

  (POST "/vendors" {body :body}
        (vendor-post body))

  (GET "/vendors/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-vendors-by-id
                 {:id (parse-int id)})))
        content-type))

  (GET "/vendorsbycityid/:cityid" [cityid]
       (rr/content-type
        (rr/response
         (db/get-vendors-vv-by-cityid
          {:cityid (parse-int cityid)}))
        content-type))

  (GET "/city/:cityid/category/:category/vendors" [cityid category]
       (get-vendors-by-category-and-city
        cityid category))

  (GET "/vendorsview" [id]
       (rr/content-type
        (rr/response
         (db/get-vendors-view
          {:id id }))
        content-type))

  (PUT "/vendors/:id" {body :body}
       (vendors-put body))

  (DELETE
   "/vendors/:id" [id]
   (db/delete-vendors-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (GET "/cities/:index/:pagesize" [index pagesize]
       (cities (parse-int index)
               (parse-int  pagesize)))

  (GET "/cities" []
       (cities-all))

  (POST "/cities" {body :body}
        (cities-post body))

  (GET "/cities/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-cities-by-id
                 {:id (parse-int id)})))
        content-type))

  (PUT "/cities/:id"
       {body :body}
       (fn [request]
         (if-not (authenticated? request)
           (throw-unauthorized)
           (rr/content-type
            (rr/response
             (db/update-cities
              {:id (parse-int (:id body))
               :cityname (:cityname body)
               }))
            content-type))))

  (DELETE
   "/cities/:id" [id]
   (db/delete-cities-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))


  (GET "/hotels/:index/:pagesize" [index pagesize]
       (hotels (parse-int index)
               (parse-int  pagesize)))

  (GET "/hotels/search"
       [index pagesize type text :as {params :params}]
       (hotels-search index pagesize type text))

  (GET "/hotels" []
       (hotels-all))

  (GET "/hotelscitynames" []
       (hotels-with-cityname))

  (POST "/hotels/upload"
        {{{tempfile :tempfile filename :filename} :file } :params :as params}
        (upload-file-for-hotels tempfile filename))

  (GET "/hotelcontractfiles/:filename" [filename]
       (get-hotels-contractfile filename))

  (POST "/hotels" {body :body}
        (hotels-post body))

  (PUT "/hotels/:id"  {body :body}
       (hotels-put body))

  (GET "/hotels/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-hotels-by-id
                 {:id (parse-int id)})))
        content-type))

  (GET "/hotelsbycityid/:cityid" [cityid]
       (rr/content-type
        (rr/response
         (db/get-hotels-by-cityid
          {:cityid (parse-int cityid)}))
        content-type))

  (DELETE
   "/hotels/:id" [id]
   (db/delete-hotels-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (GET "/tourguides/:index/:pagesize" [index pagesize]
       (tourguides (parse-int index)
                   (parse-int  pagesize)))

  (GET "/tourguides/search"
       [index pagesize type text :as {params :params}]
       (tourguides-search index pagesize type text))

  (GET "/tourguides" []
       (tourguides-all))

  (POST "/tourguides" {body :body}
        (tourguides-post body))

  (GET "/tourguides/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-tourguides-by-id
                 {:id (parse-int id)})))
        content-type))

  (PUT "/tourguides/:id"  {body :body}
       (tourguides-put body))

  (DELETE
   "/tourguides/:id" [id]
   (db/delete-tourguides-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (GET "/restaurants/:index/:pagesize" [index pagesize]
       (restaurants (parse-int index)
                    (parse-int  pagesize)))

  (GET "/restaurants/search"
       [index pagesize type text :as {params :params}]
       (restaurants-search index pagesize type text))

  (GET "/restaurants" []
       (tourguides-all))

  (POST "/restaurants" {body :body}
        (restaurants-post body))

  (GET "/restaurants/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-restaurants-by-id
                 {:id (parse-int id)})))
        content-type))

  (PUT "/restaurants/:id"  {body :body}
       (restaurants-put body))

  (DELETE
   "/restaurants/:id" [id]
   (db/delete-restaurants-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (GET "/transportations/:index/:pagesize" [index pagesize]
       (transportation (parse-int index)
                       (parse-int  pagesize)))

  (GET "/transportations/search"
       [index pagesize type text :as {params :params}]
       (transportations-search index pagesize type text))

  (GET "/transportations" []
       (transportation-all))

  (POST "/transportations" {body :body}
        (transportation-post body))

  (GET "/transportations/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-transportation-by-id
                 {:id (parse-int id)})))
        content-type))

  (PUT "/transportations/:id"  {body :body}
       (transportation-put body))

  (DELETE
   "/transportations/:id" [id]
   (db/delete-transportation-by-id
    {:id (parse-int id)})4
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (GET "/bookingfits/:index/:pagesize" [index pagesize]
       (bookingfits (parse-int index)
                    (parse-int  pagesize)))

  (POST "/bookingfits" {body :body}
        (bookingfits-post body))

  (GET "/bookingfits/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-bookingfits-by-id
                 {:id (parse-int id)})))
        content-type))

  (PUT "/bookingfits/:id"  {body :body}
       (bookingfits-put body))

  (DELETE
   "/bookingfits/:id" [id]
   (db/delete-bookingfits-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (GET "/events/:index/:pagesize" [index pagesize]
       (events (parse-int index)
               (parse-int  pagesize)))

  (GET "/events/search"
       [index pagesize type text :as {params :params}]
       (events-search index pagesize type text))


  (POST "/events" {body :body}
        (events-post body))

  (GET "/events/:id" [id]
       (rr/content-type
        (rr/response
         (first (db/get-events-by-id
                 {:id (parse-int id)})))
        content-type))

  (PUT "/events/:id"  {body :body}
       (events-put body))

  (DELETE
   "/events/:id" [id]
   (db/delete-events-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (POST "/packages" {body :body}
        ;; (fn [request]
        ;;   (if-not (authenticated? request)
        ;;     (throw-unauthorized)
        (db/insert-packages
         {:name (:name body)
          :adultamount (:adultamount body)
          :childamount (:childamount body)
          :categories (:categories body)
          :description (:description body)
          :isgroup (:isgroup body)
          :min (:min body)
          :max (:max body)})
        (rr/content-type
         (rr/response "")
         content-type))

  (PUT "/packages/:id"
       {body :body}
       ;; (fn [request]
       ;;   (if-not (authenticated? request)
       ;;     (throw-unauthorized)
       (rr/content-type
        (rr/response
         (db/update-packages
          {:id (parse-int (:id body))
           :name (:name body)
           :adultamount (:adultamount body)
           :childamount (:childamount body)
           :categories (:categories body)
           :description (:description body)
           :isgroup (:isgroup body)
           :min (:min body)
           :max (:max body)
           }))
        content-type))

  (POST "/items" {body :body}
        (item-post body))

  (DELETE
   "/packages/:id" [id]
   (db/delete-packages-by-id
    {:id (parse-int id)})
   (rr/content-type
    (rr/response "Deleted")
    content-type))

  (GET "/city/:cityid/category/:category/fitactivities" [cityid category]
       (get-fit-activities-by-category-and-city cityid category))

  (GET "/city/:cityid/category/:category/groupactivities" [cityid category]
       (get-group-activities-by-category-and-city cityid category))

  (POST "/paymentlog" {body :body}
        (payment-log-post body))

  (DELETE "/hotelinformation/:id" [id]
          (delete-hotel-information id))

  (GET "/paymentlog" [pagesize :<< as-int
                      index :<< as-int
                      paymentid
                      bookingid
                      :as {params :params}]

       (get-payment-logs pagesize
                         index
                         paymentid
                         bookingid))
  (GET "/activitylog" [pagesize :<< as-int
                       index :<< as-int
                       activityid
                       bookingid
                       :as {params :params}]

       (get-activity-logs pagesize
                          index
                          activityid
                          bookingid))

  (GET "/excel/restaurents.xlsx" [token :as {params :params}]
       (get-restaurents-excel token))

  (GET "/excel/hotels.xlsx" [token :as {params :params}]
       (get-hotels-excel token))

  (GET "/excel/tourguides.xlsx" [token :as {params :params}]
       (get-tourguides-excel token))

  (GET "/excel/transportation.xlsx" [token :as {params :params}]
       (get-transportation-excel token))

  (GET "/excel/events.xlsx" [token :as {params :params}]
       (get-events-excel token))

  (GET "/excel/activities.xlsx" [token :as {params :params}]
       (get-activities-excel token))

  ;; Client Side routes
  ;; ----------------------------------------------
  (GET "/" [] home)
  ;; (GET "/register" [] home)
  (GET "/fitquotations-list" [] home)
  (GET "/groupquotations-list" [] home)
  (GET "/fitbookings" [] home)
  (GET "/fitbookings/add-fitbooking" [] home)
  (GET "/fitbookings/:id/edit-fitbooking" [id] home)
  (GET "/fitbookings/:id/fit-activitylog" [id] home)
  (GET "/fitbookings/:id/fit-paymentlog" [id] home)
  (GET "/groupbookings" [] home)
  (GET "/groupbookings/add-groupbooking" [] home)
  (GET "/groupbookings/:id/edit-groupbooking" [id] home)
  (GET "/groupbookings/:id/group-activitylog" [id] home)
  (GET "/groupbookings/:id/group-paymentlog" [id] home)
  (GET "/cities-list" [] home)
  (GET "/cities-list/add-city" [] home)
  (GET "/cities-list/:id/edit-city" [id] home)
  (GET "/items-list" [] home)
  (GET "/items-list/add-item" [] home)
  (GET "/items-list/:id/edit-item" [id] home)
  (GET "/items-list/:id/view-item" [id] home)
  (GET "/all-vendors/hotels-list" [] home)
  (GET "/all-vendors/hotels-list/add-hotel" [] home)
  (GET "/all-vendors/hotels-list/:id/edit-hotel" [id] home)
  (GET "/all-vendors/hotels-list/:id/view-hotel" [id] home)
  (GET "/all-vendors/restaurants-list" [] home)
  (GET "/all-vendors/restaurants-list/add-restaurant" [] home)
  (GET "/all-vendors/restaurants-list/:id/edit-restaurant" [id] home)
  (GET "/all-vendors/restaurants-list/:id/view-restaurant" [id] home)
  (GET "/all-vendors/tourguides-list" [] home)
  (GET "/all-vendors/tourguides-list/add-tourguide" [] home)
  (GET "/all-vendors/tourguides-list/:id/edit-tourguide" [id] home)
  (GET "/all-vendors/tourguides-list/:id/view-tourguide" [id] home)
  (GET "/all-vendors/transportations-list" [] home)
  (GET "/all-vendors/transportations-list/add-transportation" [] home)
  (GET "/all-vendors/transportations-list/:id/edit-transportation" [id] home)
  (GET "/all-vendors/transportations-list/:id/view-transportation" [id] home)
  (GET "/all-vendors/events-list" [] home)
  (GET "/all-vendors/events-list/add-event" [] home)
  (GET "/all-vendors/events-list/:id/edit-event" [id] home)
  (GET "/all-vendors/events-list/:id/view-event" [id] home)
  (GET "/logos-list" [] home)
  (GET "/logos-list/add-logo" [] home)
  (GET "/logos-list/:id/edit-event" [id] home)
  (GET "/users-list" [] home)
  (GET "/users-list/add-user" [] home)
  (GET "/users-list/:id/edit-user" [id] home)
  (GET "/complete-actitvity-log" [] home)
  (GET "/complete-payment-log" [] home)
  (route/not-found "<h1>Page not found</h1>"))

;; Application entry point
(defn- add-gzip-handler [server]
  (.setHandler
   server
   (doto (GzipHandler.)
     (.setIncludedMimeTypes
      (into-array
       ["text/css"
        "text/plain"
        "text/javascript"
        "application/javascript"
        "application/json"
        "image/svg+xml"]))
     (.setMinGzipSize 3024)
     (.setHandler (.getHandler server)))))

(def app
  (->
   app-routes
   (wrap-defaults
    (assoc-in
     site-defaults
     [:security :anti-forgery] false))
   (wrap-authentication auth-backend)
   (wrap-authorization auth-backend)
   (wrap-cors :access-control-allow-origin
              [#".*"]
              :access-control-allow-methods
              [:get :put :post :delete])
   (ring-json/wrap-json-body {:keywords? true})
   (ring-json/wrap-json-response)))


(defn -main
  [& args]
  (jetty/run-jetty
   app
   (merge {:port
                                        ;81
           81
           :join? false
           :configurator add-gzip-handler})))
