(ns sevenm-booking-system.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]))

(def routes
  ["/"
   {""      :home
    "register" :register
    "users-list" {"" :all-users
                  "/add-user" :add-user
                  ["/" :id "/edit-user"] :edit-user}
    "fitquotations-list" :fit-quotations
    "groupquotations-list" :group-quotations
    "fitbookings" {"" :fitbookings
                   "/add-fitbooking" :add-fitbooking
                   ["/" :id "/edit-fitbooking"] :edit-fitbooking
                   ["/" :id "/fit-activitylog"] :fit-activitylog
                   ["/" :id "/fit-paymentlog"] :fit-paymentlog}
    "groupbookings" {"" :groupbookings
                     "/add-groupbooking" :add-groupbooking
                     ["/" :id "/edit-groupbooking"] :edit-groupbooking
                     ["/" :id "/group-activitylog"] :group-activitylog
                     ["/" :id "/group-paymentlog"] :group-paymentlog}

    "items-list" {"" :items
                  "/add-item" :add-item
                  ["/" :id "/edit-item"] :edit-item
                  ["/" :id "/view-item"] :view-item}

    "all-vendors" {"/events-list" {"" :events
                                   "/add-event" :add-event
                                   ["/" :id "/edit-event"] :edit-event
                                   ["/" :id "/view-event"] :view-event}
                   "/hotels-list" {"" :hotels
                                   "/add-hotel" :add-hotel
                                   ["/" :id "/edit-hotel"] :edit-hotel
                                   ["/" :id "/view-hotel"] :view-hotel}
                   "/restaurants-list" {"" :restaurants
                                        "/add-restaurant" :add-restaurant
                                        ["/" :id "/edit-restaurant"] :edit-restaurant
                                        ["/" :id "/view-restaurant"] :view-restaurant}
                   "/tourguides-list" {"" :tourguides
                                       "/add-tourguide" :add-tourguide
                                       ["/" :id "/edit-tourguide"] :edit-tourguide
                                       ["/" :id "/view-tourguide"] :view-tourguide}
                   "/transportations-list" {"" :transportations
                                            "/add-transportation" :add-transportation
                                            ["/" :id "/edit-transportation"] :edit-transportation
                                            ["/" :id "/view-transportation"] :view-transportation}}
    ;; "/vendors-list" {"" :vendors
    ;;                  "/add-vendor" :add-vendor
    ;;                  ["/" :id "/edit-vendor"] :edit-vendor}
    ;; "bookingfits-list" {"" :bookingfits
    ;;                     "/add-bookingfit" :add-bookingfit
    ;;                     ["/" :id "/edit-bookingfit"] :edit-bookingfit}

    "logos-list" {"" :logos
                  "/add-logo" :add-logo
                  ["/" :id "/edit-logo"] :edit-logo}

    "cities-list" {"" :cities
                   "/add-city" :add-city
                   ["/" :id "/edit-city"] :edit-city}

    "complete-actitvity-log" :complete-activity-log
    "complete-payment-log" :complete-payment-log
    "unauthorized" :unauthorized}])

(defn- dispatch-route [matched-route]
  (re-frame/dispatch [:set-active-panel  matched-route]))

(def history
  (pushy/pushy dispatch-route (partial bidi/match-route routes)))

(defn app-routes []
  (pushy/start! history))

(def url-for
  (partial bidi/path-for routes))
