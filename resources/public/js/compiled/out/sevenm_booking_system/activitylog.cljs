(ns sevenm-booking-system.activitylog
  (:require [reagent.core :as r]
            [pushy.core :as pushy]
            [sevenm-booking-system.routes :as routes]
            [ajax.core :as ajax]
            [vendor.notify]
            [cljsjs.react-select]
            [cljs.reader :as reader]
            [sevenm-booking-system.config :refer [server session]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn boot-notify [type title message]
  (.notify
   js/$ (clj->js {:title (str "<strong>" title "</strong>")
                  :message message})
   (clj->js {:type type})))

(defn input [id input-type place val]
  [:input.form-control
   {:id id
    :type input-type
    :placeholder place
    :value @val
    :on-change #(reset! val (.-target.value %))}])


;; ----------------------------------------------------------------
;; Activities Comp for all (Itineraries, Transportation, Hotel)
;; ----------------------------------------------------------------
(defn get-activities-success [response state]
  (swap! state update-in [:page-index] inc)
  (swap! state update-in [:data] into (get response :data []))
  (swap! state assoc :records  (get response :records 0)))

(defn activity-post-error-handler [res]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token!
             routes/history
             (routes/url-for :home)))
    (boot-notify
     "warning" "Warning :"
     "Problem with the server while fetching activity log data, Please try again")))

(defn get-activities [state]
  (let [get-url (get @state :get-url)
        pageindex (get @state :page-index)
        pagesize (get @state :page-size)]
    (ajax/GET (str server get-url
                   "index=" pageindex
                   "&pagesize=" pagesize)
              {:handler #(get-activities-success % state)
               :error-handler #(activity-post-error-handler %)
               :format :json
               :response-format :json
               :keywords? true
               :headers
               {:Authorization
                (str "Token " (get-in @session [:session :token]))}})))

(defn show-all-activities-comp [state]
  [:div.overflow {:style {:background-color "#ecf0f5"}}
   [:ul.timeline
    (for [i (get @state :data)]
      ^{:key (i :id)}
      [:li
       (case (i :type)
         "itinerary" [:i.fa.fa-calendar-check-o.bg-maroon]
         "transport" [:i.fa.fa-plane.bg-aqua]
         [:i.fa.fa-hotel.bg-yellow])
       [:div.timeline-item
        [:span.time
         [:i.fa.fa-clock-o] " " (i :datetime)]
        [:h3.timeline-header
         [:a {:href "#"} (i :username)]]
        [:div.timeline-body
         [:dl.dl-horizontal
          [:div.row
           [:div.col-md-6
            [:dt "Activity Text :"] [:dd (i :activitytext "")]
            [:dt "Booking Number :"] [:dd (i :bookingnumber )]]
           [:div.col-md-6
            [:dt "Booking Type :"] [:dd (i :bookingtype )]
            [:dt "Payment Log for :"] [:dd (i :type "")]]]]]]])
    (when-not (zero? (@state :page-index))
      (when (< (* 10 (dec (@state :page-index))) (@state :records))
        [:li>div
         [:center
          [:button.btn.btn-default
           {:on-click #(get-activities state)} "More"]]]))]])

(defn onlyget-activities-comp [get-url]
  (let [state (r/atom {:get-url get-url
                       :page-index 1
                       :page-size 10
                       :data []
                       :records 0})]
    (get-activities state)
    (fn []
      [:div.row
       [:div.col-md-12
        [show-all-activities-comp state]]])))

(defn activity-log-comp [heading get-url]
  [:div.box.box-primary
   [:div.box-header.with-border
    [:i.fa.fa-book]
    [:h3.box-title heading]
    [:div.box-tools.pull-right
     [:button.btn.btn-box-tool
      {:data-widget "collapse"
       :type "button"}
      [:i.fa.fa-minus]]]]
   [:div.box-body
    [onlyget-activities-comp get-url]]])

;; ---------------------------------------------------------
;; Post Get activity Comp
;; ---------------------------------------------------------
(defn post-get-activities-success [response state]
  (swap! state assoc :page-index 1)
  (swap! state assoc :page-index 10)
  (swap! state assoc :data (get response :data []))
  (swap! state assoc :records (get response :records 0))
  (swap! state assoc :activitytext ""))

(defn post-activity-error-handler [res]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token!
             routes/history
             (routes/url-for :home)))
    (boot-notify
     "warning" "Warning :"
     "Problem with the server while posting activity log data, Please try again")))

(defn post-activity-success-handler [response state]
  (let [get-url (get @state :get-url)]
    (ajax/GET (str server get-url "index=" 1 "&pagesize=" 10)
              {:handler #(post-get-activities-success % state)
               :error-handler #(activity-post-error-handler %)
               :format :json
               :response-format :json
               :keywords? true
               :headers
               {:Authorization
                (str "Token " (get-in @session [:session :token]))}})))

(defn post-activity [state activityid vendorid category]
  (let [send-data (assoc (get @state :post-data {})
                         :activityid @activityid
                         :vendorid @vendorid
                         :category @category)]
    (ajax/POST
     (str server "activitylog")
     {:params send-data
      :handler #(post-activity-success-handler % state)
      :error-handler #(post-activity-error-handler %)
      :format :json
      :response-format :json
      :keywords? true
      :headers
      {:Authorization
       (str "Token " (get-in @session [:session :token]))}})))

(defn get-post-activities-comp
  [get-url type bookingid activityid vendorid category]
  (let [state (r/atom {:get-url get-url
                       :page-index 1
                       :page-size 10
                       :data []
                       :records 0
                       :post-data {:bookingid @bookingid
                                   :type type
                                   :activitytext ""}})]
    (get-activities state)
    (fn []
      [:div.row
       ;; [:p (str @state)]
       [:div.col-md-12
        [:div.form-group
         [:div.input-group
          [input "text" "text" "Enter Activity Note"
           (r/cursor state [:post-data :activitytext])]
          [:span.input-group-btn
           [:button.btn.btn-info
            {:type "button"
             :on-click #(post-activity state activityid vendorid category)}
            "Add Activity"]]]]
        [:div.overflow {:style {:background-color "#ecf0f5"}}
         [:ul.timeline
          (for [i (get @state :data)]
            ^{:key (i :id)}
            [:li
             (case (i :type)
               "itinerary" [:i.fa.fa-calendar-check-o.bg-maroon]
               "transport" [:i.fa.fa-plane.bg-aqua]
               [:i.fa.fa-hotel.bg-yellow])
             [:div.timeline-item
              [:span.time
               [:i.fa.fa-clock-o] " " (i :datetime)]
              [:h3.timeline-header
               [:a {:href "#"} (i :username)]]
              [:div.timeline-body (i :activitytext)]]])
          (when-not (zero? (@state :page-index))
            (when (< (* 10 (dec (@state :page-index))) (@state :records))
              [:li>div
               [:center
                [:button.btn.btn-default
                 {:on-click #(get-activities state)} "More"]]]))]]]])))

(defn activity-model-comp
  [heading data kth i get-url type]
  (let [bookingid (reaction (get @data :id 0))
        cityid  (reaction (get-in @data [:cities kth :cityid] 0))
        iti-id (reaction (get-in @data [:cities kth :itineraries i :id] 0))
        iti-vendorid (reaction (get-in @data [:cities kth :itineraries i :vendorid1] 0))
        iti-category (reaction (get-in @data [:cities kth :itineraries i :category] ""))
        hotel-id (reaction (get-in @data [:cities kth :hotelinformation i :id] 0))
        hotel-vendorid (reaction (get-in @data [:cities kth :hotelinformation i :vendorid1] 0))
        transportation-id (reaction (reader/read-string (str @bookingid @cityid)))
        modal-id (reaction (case type
                             "itinerary" (str "itineraryActivitylog" @iti-id)
                             "transport" (str "transportActivitylog" @transportation-id)
                             (str "hotelActivitylog" @hotel-id)))]
    (fn []
      [:div.modal.fade
       {:role "dialog" :id (str @modal-id)}
       [:div.modal-dialog
        [:div.modal-content
         [:div.modal-header
          [:button.close
           {:type "button" :data-dismiss "modal"} "×"]
          [:h4.modal-title heading]]
         [:div.modal-body
          (case type
            "itinerary" [get-post-activities-comp get-url type bookingid
                         iti-id iti-vendorid iti-category]
            "hotel" [get-post-activities-comp get-url type bookingid
                     hotel-id hotel-vendorid (r/atom "hotel")]
            [get-post-activities-comp get-url type bookingid
             transportation-id (r/atom 0) (r/atom "transportation")])]
         [:div.modal-footer
          [:button.btn.btn-default
           {:type "button" :data-dismiss "modal"} "Close"]]]]])))


;; ------------------------------------------------------
;; Vendor Info
;; ------------------------------------------------------

(defn vendor-info [modal-id data-set]
  (let [vendor-info (reaction (get @data-set :vendor-info {}))]
    (fn []
      [:div.modal.fade
       {:role "dialog" :id modal-id}
       [:div.modal-dialog
        [:div.modal-content
         [:div.modal-header
          [:button.close
           {:type "button" :data-dismiss "modal"} "×"]
          [:h4.modal-title (str "Vendor Info for " (get @vendor-info :category ""))]]
         [:div.modal-body
          ;; [:p (str @vendor-info)]
          (case (get @vendor-info :category "")
            "Attractions" [:dl.dl-horizontal
                           [:dt "Event Name :"] [:dd (get @vendor-info :name "")]
                           ;; [:dt "Contact Person :"] [:dd (get @vendor-info :contactperson "")]
                           [:dt "Contact Number :"] [:dd (get @vendor-info :contactnumber "")]
                           [:dt "Email id :"] [:dd (get @vendor-info :vendoremail "")]
                           [:dt "Booking Guide :"] [:dd (get @vendor-info :address "")]]
            [:dl.dl-horizontal
             [:dt "Vendor Name :"] [:dd (get @vendor-info :name "")]
             [:dt "Contact Person :"] [:dd (get @vendor-info :contactperson "")]
             [:dt "Contact Number :"] [:dd (get @vendor-info :contactnumber "")]
             [:dt "Email id :"] [:dd (get @vendor-info :vendoremail "")]
             [:dt "Address :"] [:dd (get @vendor-info :address "")]])]
         [:div.modal-footer
          [:button.btn.btn-default
           {:type "button" :data-dismiss "modal"} "Close"]]]]])))


;; --------------------------------------------------------
;; Payment Log Component
;; --------------------------------------------------------

;; ----------------------------------------------------------------
;; Paymentlog Comp for all (Itineraries, Transportation, Hotel)
;; ----------------------------------------------------------------
(defn get-paymentlog-success [response state]
  (swap! state update-in [:page-index] inc)
  (swap! state update-in [:data] into (get response :data []))
  (swap! state assoc :records (get response :records 0)))

(defn paymentlog-post-error-handler [res]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token!
             routes/history
             (routes/url-for :home)))
    (boot-notify
     "warning" "Warning :"
     "Problem with the server while fetching activity log data, Please try again")))

(defn get-paymentlogs [state]
  (let [get-url (get @state :get-url)
        pageindex (get @state :page-index)
        pagesize (get @state :page-size)]
    (ajax/GET (str server get-url
                   "index=" pageindex
                   "&pagesize=" pagesize)
              {:handler #(get-paymentlog-success % state)
               :error-handler #(paymentlog-post-error-handler %)
               :format :json
               :response-format :json
               :keywords? true
               :headers
               {:Authorization
                (str "Token " (get-in @session [:session :token]))}})))

(defn show-all-paymentlog-comp [state]
  [:div.overflow {:style {:background-color "#ecf0f5"}}
   [:ul.timeline
    (for [i (get @state :data)]
      ^{:key (i :id)}
      [:li
       (case (i :type)
         "itinerary" [:i.fa.fa-calendar-check-o.bg-maroon]
         "transport" [:i.fa.fa-plane.bg-aqua]
         [:i.fa.fa-hotel.bg-yellow])
       [:div.timeline-item
        [:span.time
         [:i.fa.fa-clock-o] " " (i :datetime)]
        [:h3.timeline-header
         [:a {:href "#"} (i :username)]]
        [:div.timeline-body
         [:dl.dl-horizontal
          [:div.row
           [:div.col-md-6
            [:dt "Vendor Name :"] [:dd (i :vendorname "")]
            [:dt "Payment Mode :"] [:dd (i :paymentmode "")]
            [:dt "Amount :"] [:dd (i :amount )]]
           [:div.col-md-6
            [:dt "Booking Number :"] [:dd (i :bookingnumber )]
            [:dt "Booking Type :"] [:dd (i :bookingtype )]
            [:dt "Payment Log for :"] [:dd (i :type "")]]]]]]])
    (when-not (zero? (@state :page-index))
      (when (< (* 10 (dec (@state :page-index))) (@state :records))
        [:li>div
         [:center
          [:button.btn.btn-default
           {:on-click #(get-paymentlogs state)} "More"]]]))]])

(defn onlyget-paymentlog-comp [get-url]
  (let [state (r/atom {:get-url get-url
                       :page-index 1
                       :page-size 10
                       :data []
                       :records 0})]
    (get-paymentlogs state)
    (fn []
      [:div.row
       [:div.col-md-12
        [show-all-paymentlog-comp state]]])))

(defn paymentlog-comp [heading get-url]
  [:div.box.box-primary
   [:div.box-header.with-border
    [:i.fa.fa-credit-card]
    [:h3.box-title heading]
    [:div.box-tools.pull-right
     [:button.btn.btn-box-tool
      {:data-widget "collapse"
       :type "button"}
      [:i.fa.fa-minus]]]]
   [:div.box-body
    [onlyget-paymentlog-comp get-url]]])


;; Post Get activity Comp
;; ---------------------------------------------------------

(def payment-mode-data
  (r/atom
   [{:id "ACH" :name "ACH"}
    {:id "AMEX" :name "AMEX"}
    {:id "Cash" :name "Cash"}
    {:id "Cheque" :name "Cheque"}
    {:id "Master Card" :name "Master Card"}
    {:id "PayPal" :name "PayPal"}
    {:id "Quick Pay" :name "Quick Pay"}
    {:id "Wire Transfer" :name "Wire Transfer"}
    {:id "Visa" :name "Visa"}]))

(def r-select (r/adapt-react-class js/Select))

(defn js-clj [val]
  (js->clj val :keywordize-keys true))

(defn react-select
  [name placeholder options val]
  [r-select
   {:name name
    :value (or @val "")
    :placeholder placeholder
    :labelKey :name
    :valueKey :id
    :options @options
    :clearable false
    :on-change #(reset! val (get (js-clj %) :id))}])

(defn str-number [str]
  (let [n (reader/read-string str)]
    (if (number? n) n 0)))

(defn input-number [id place val]
  [:input.form-control
   {:id id
    :type "number"
    :step 0.001
    :placeholder place
    :value @val
    :on-change #(reset! val (.-target.value %))
    :on-blur #(reset! val (str-number (.-target.value %)))}])

(defn post-get-paymentlog-success [response state]
  (swap! state assoc :page-index 1)
  (swap! state assoc :page-index 10)
  (swap! state assoc :data (get response :data []))
  (swap! state assoc :records (get response :records 0))
  (swap! state assoc :amount "")
  (swap! state assoc :paymentmode ""))

(defn post-paymentlog-error-handler [res]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token!
             routes/history
             (routes/url-for :home)))
    (boot-notify
     "warning" "Warning :"
     "Problem with the server while posting paymentlog log data, Please try again")))

(defn post-paymentlog-success-handler [response state]
  (let [get-url (get @state :get-url)]
    (ajax/GET (str server get-url "index=" 1 "&pagesize=" 10)
              {:handler #(post-get-paymentlog-success % state)
               :error-handler #(paymentlog-post-error-handler %)
               :format :json
               :response-format :json
               :keywords? true
               :headers
               {:Authorization
                (str "Token " (get-in @session [:session :token]))}})))

(defn post-paymentlog [state paymentid category]
  (let [send-data (assoc (get @state :post-data {})
                         :paymentid @paymentid
                         :category @category)]
    (ajax/POST
     (str server "paymentlog")
     {:params send-data
      :handler #(post-paymentlog-success-handler % state)
      :error-handler #(post-paymentlog-error-handler %)
      :format :json
      :response-format :json
      :keywords? true
      :headers
      {:Authorization
       (str "Token " (get-in @session [:session :token]))}})))

(defn get-post-paymentlog-comp
  [get-url type bookingid paymentlogid vendorid category vendors]
  (let [state (r/atom {:get-url get-url
                       :page-index 1
                       :page-size 10
                       :data []
                       :records 0
                       :post-data {:bookingid @bookingid
                                   :type type
                                   :paymentmode ""
                                   :amount ""
                                   :vendorid @vendorid}})]
    (get-paymentlogs state)
    (fn []
      [:div.row
       ;; [:p (str @state)]
       [:div.col-md-12
        [:div.row.form-group
         [:div.col-md-3.col-sm-6
          [react-select "vendorid" "Vendor Name"
           vendors (r/cursor state [:post-data :vendorid])]]
         [:div.col-md-3.col-sm-6
          [react-select "paymentmode" "Payment Mode"
           payment-mode-data (r/cursor state [:post-data :paymentmode])]]
         [:div.col-md-3.col-sm-6
          [input-number "modalamount" "Amount"
           (r/cursor state [:post-data :amount])] ]
         [:div.col-md-3.col-sm-6
          [:button.btn.btn-info
           {:type "button"
            :on-click #(post-paymentlog state paymentlogid category)}
           "Add Activity"]]]
        [:div.overflow {:style {:background-color "#ecf0f5"}}
         [:ul.timeline
          (for [i (get @state :data)]
            ^{:key (i :id)}
            [:li
             (case (i :type)
               "itinerary" [:i.fa.fa-calendar-check-o.bg-maroon]
               "transport" [:i.fa.fa-plane.bg-aqua]
               [:i.fa.fa-hotel.bg-yellow])
             [:div.timeline-item
              [:span.time
               [:i.fa.fa-clock-o] " " (i :datetime)]
              [:h3.timeline-header
               [:a {:href "#"} (i :username)]]
              [:div.timeline-body
               [:dl.dl-horizontal
                [:div.row
                 [:div.col-md-12
                  [:dt "Vendor Name :"] [:dd (i :vendorname "")]
                  [:dt "Payment Mode :"] [:dd (i :paymentmode "")]]
                 [:div.col-md-12
                  [:dt "Amount :"] [:dd (i :amount )]
                  [:dt "Payment Log for :"] [:dd (i :type "")]]]]]]])
          (when (< (* 10 (@state :page-index)) (@state :records))
            [:li>div
             [:center
              [:button.btn.btn-default
               {:on-click #(get-paymentlogs state)} "More"]]])]]]])))


(defn paymentlog-model-comp [heading data kth i get-url type vendors]
  (let [bookingid (reaction (get @data :id 0))
        iti-id (reaction (get-in @data [:cities kth :itineraries i :id] 0))
        iti-vendorid (reaction (get-in @data [:cities kth :itineraries i :vendorid1] 0))
        iti-category (reaction (get-in @data [:cities kth :itineraries i :category] ""))
        hotel-id (reaction (get-in @data [:cities kth :hotelinformation i :id] 0))
        hotel-vendorid (reaction (get-in @data [:cities kth :hotelinformation i :vendorid1] 0))
        modal-id (reaction (case type
                             "itinerary" (str "itineraryPaymentlog" @iti-id)
                             (str "hotelPaymentlog" @hotel-id)))]
    (fn []
      [:div.modal.fade
       {:role "dialog" :id (str @modal-id)}
       [:div.modal-dialog
        [:div.modal-content
         [:div.modal-header
          [:button.close
           {:type "button" :data-dismiss "modal"} "×"]
          [:h4.modal-title heading]]
         [:div.modal-body
          (case type
            "itinerary" [get-post-paymentlog-comp get-url type bookingid
                         iti-id iti-vendorid iti-category vendors]
            [get-post-paymentlog-comp get-url type bookingid
             hotel-id hotel-vendorid (r/atom "hotel") vendors])]
         [:div.modal-footer
          [:button.btn.btn-default
           {:type "button" :data-dismiss "modal"} "Close"]]]]])))
