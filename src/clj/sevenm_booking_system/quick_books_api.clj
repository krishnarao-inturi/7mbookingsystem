(ns sevenm-booking-system.quick-books-api
  (:require [cheshire.core :refer [generate-string]]
            [clj-http.client :as client]
            [oauth.client :as oauth]))

(defn post [url body query-params]
  (client/request
   {:method :post
    :url url
    :query-params query-params
    :body (generate-string body)
    :content-type :json
    :accept :json}))

(def OAuth-Consumer-Key "qyprdDemBTSoG6ONCSq8A0HlQiJ54z")
(def OAuth-Consumer-Secret "r5OKLm9YJrGSQJlQXWjHADBS03UYI7RzB6X9tV2J")
(def OAuth-Access-Token "qyprdqSuZWHfj0mrZaP0maJR3Nwt5QtARxeXh36eM9HJqyYN")
(def OAuth-Access-Token-Secret "SckPiHsxFhWh3oZksLZltCglha5gscXghI9eHnDW")
(def consumer (oauth/make-consumer OAuth-Consumer-Key
                                   OAuth-Consumer-Secret
                                   ""
                                   ""
                                   ""
                                   :hmac-sha1))
(def vendor-get-url "https://sandbox-quickbooks.api.intuit.com/v3/company/193514458736764/vendor/30")
(def get-user-params {})
(def post-user-params {:cache-control "no-cache"})
(defn get-credentials [url] (oauth/credentials consumer
                                               OAuth-Access-Token
                                               OAuth-Access-Token-Secret
                                               :GET
                                               url
                                               get-user-params))

(def post-vendor-url "https://sandbox-quickbooks.api.intuit.com/v3/company/193514458736764/vendor")

(defn post-credentials [url] (oauth/credentials consumer
                                                OAuth-Access-Token
                                                OAuth-Access-Token-Secret
                                                :POST
                                                url
                                                post-user-params))

(defn get-restaurent-quickbook-mapped-data [restaurent-details]
  {:BillAddr {
              :Line1 (restaurent-details :address)
              :City (restaurent-details :city)
              :Country "U.S.A",
              :CountrySubDivisionCode "CA",
              :PostalCode "12482"},
   :CompanyName (restaurent-details :restaurantname),
   :DisplayName (restaurent-details :restaurantname),
   :PrintOnCheckName (restaurent-details :restaurantname),
   :PrimaryPhone {
                  :FreeFormNumber (restaurent-details :contactnumber) }
   :Mobile {
            :FreeFormNumber (restaurent-details ::cellnumber)},
   :PrimaryEmailAddr {
                      :Address (restaurent-details :email) 
                      }})

(defn get-tour-guide-quickbook-mapped-data [tour-guide-details]
  {:BillAddr {
              :Line1 (tour-guide-details :address)
              :City (tour-guide-details :city)
              :Country "U.S.A",
              :CountrySubDivisionCode "CA",
              :PostalCode "12482"},
   :CompanyName (tour-guide-details :tourguidename),
   :DisplayName (tour-guide-details :tourguidename),
   :PrintOnCheckName (tour-guide-details :tourguidename),
   :PrimaryPhone {
                  :FreeFormNumber (tour-guide-details :contactnumber) }
   :Mobile {
            :FreeFormNumber (tour-guide-details ::cellnumber)},
   :PrimaryEmailAddr {
                      :Address (tour-guide-details :email)
                      }})


(defn get-transportations-quickbook-mapped-data [transportation-details]
  {:BillAddr {
              :Line1 (transportation-details :address)
              :City (transportation-details :city)
              :Country "U.S.A",
              :CountrySubDivisionCode "CA",
              :PostalCode "12482"},
   :CompanyName (transportation-details :transportationname),
   :DisplayName (transportation-details :transportationname),
   :PrintOnCheckName (transportation-details :transportationname),
   :PrimaryPhone {
                  :FreeFormNumber (transportation-details :contactnumber) }
   :Mobile {
            :FreeFormNumber (transportation-details :contactnumber)},
   :PrimaryEmailAddr {
                      :Address (transportation-details :email)
                      }})



(defn post-restaurent-vendor [restaurent-details]
  (let [{status :status body :body} (post post-vendor-url (get-restaurent-quickbook-mapped-data
                                                           restaurent-details)
                                          (merge (post-credentials
                                                  post-vendor-url) post-user-params))]
    {:status status :body body}))


(defn post-tour-guide-vendor [tour-guide-details]
  (let [{status :status body :body} (post post-vendor-url (get-tour-guide-quickbook-mapped-data
                                                           tour-guide-details)
                                          (merge (post-credentials
                                                  post-vendor-url) post-user-params))]
    {:status status :body body}))

(defn post-transportportation-vendor [transportation-details]
  (let [{status :status body :body} (post post-vendor-url (get-transportations-quickbook-mapped-data
                                                           transportation-details)
                                          (merge (post-credentials
                                                  post-vendor-url) post-user-params))]
    {:status status :body body}))
