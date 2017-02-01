(ns sevenm-booking-system.components
  (:require [reagent.core :as r]
            [pushy.core :as pushy]
            [sevenm-booking-system.routes :as routes]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [cljsjs.react-bootstrap]
            [ajax.core :as ajax]
            [cljs.reader :as reader]
            [vendor.datepicker]
            [vendor.timepicker]
            [vendor.star-rating]
            [vendor.notify]
            [cljsjs.react-select]
            [sevenm-booking-system.config :refer [server session]]
            [sevenm-booking-system.activitylog :as activity]
            [clojure.string :as str])
  (:require-macros [reagent.ratom :refer [reaction]]))

(def pagination
  (r/adapt-react-class
   (aget js/ReactBootstrap "Pagination")))

(def button-tool-bar
  (r/adapt-react-class
   (aget js/ReactBootstrap "ButtonToolbar")))

(def terms-and-conditions
  "  1. Check in Time is at 3 PM.  Check out Time is 11 AM. \n
  2. Any other Resort fee / Facilitation fee, hotel taxes not mentioned in the inclusion are extra that guest have to directly at hotel. \n
  3. Please Deposit FULL PAYMENT if client travel date is less than 30 Days prior to travel date. \n
  4. Please Deposit 50% Payment if the travel is beyond 30 Days and the Balance NO LATER THAN 7 Days Prior to travel date. The Final Vouchers WILL NOT BE ISSUED until a Deposit is received on this booking.\n
  5. If payment is not received in the respective time, 7M tours reserves the full Rights to change the hotels and services services which was promised during the confirmation due to non-availability and hotels, transportation, events & Meals.\n
  6. 7M Tours will not be responsible for any client dispute due to delay in payment and not clarifying the situation and/or incident with travel agent.\n
  7. ALL CITY TOURS are standard Three (3) hours as per the itinerary. Anything beyond this must be booked and confirmed by the Travel Agent and incur Overtime Charges.\n
  8. Breakfast Choices as per availability of the particular Hotel that client is staying cannot be changed unless per confirmed.\n
  9. 7M Tours requests the client carry to local USA Phone Number for better communication with them while traveling during their tour.\n
  10. All Transportation services are expected to have the client carry only One (1) Carry-On Luggage 7 KG and check in at 22 KG. Any number of Bags more or weigh more than this, CLIENT WILL BE RESPONSIBLE FOR ADDITIONAL CHARGES.\n
  11. Any Last Minute Changes or Stops Decided by the Traveler and/or the Travel Agent and Tour Leader on the Tour IS NOT ACCEPTABLE AND WILL NOT BE ENTERTAINED.\n
  12. Any Attractions that have NOT been attended on a particular time set by itinerary will automatically be a 100% NON-REFUNDABLE AND NON CHANGEABLE - UNDER ANY CIRCUMSTANCES.\n
  13. NO REFUNDS WILL BE GIVEN FOR ANY LUNCH OR DINNER WHICH WAS NOT USED BY THE TRAVELLER.")

;; input Component
;; -----------------------------------------------
(defn input [id input-type place val]
  [:input.form-control
   {:id id
    :type input-type
    :placeholder place
    :value @val
    :on-change #(reset! val (.-target.value %))}])

(defn input-num
  ([id input-type place val]
   [:input.form-control
    {:id id
     :type input-type
     :placeholder place
     :value @val
     :on-change #(reset! val (int (.-target.value %)))}])
  ([id input-type place val style]
   [:input.form-control
    {:id id
     :style style
     :type input-type
     :placeholder place
     :value @val
     :on-change #(reset! val (int (.-target.value %)))}]))

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

(defn input-checkbox [id val text]
  [:div.checkbox
   [:label
    [:input
     {:id id
      :type "checkbox"
      :checked (or @val false)
      :on-change #(swap! val not)}] text]])

(defn input-checkbox-str [id val text]
  [:div.checkbox
   [:label
    [:input
     {:id id
      :type "checkbox"
      :checked  (reader/read-string (str @val))
      :on-change #(swap! val not)}] text]])

(defn text-area [id rows place val]
  [:textarea.form-control
   {:id id
    :rows rows
    :placeholder place
    :value @val
    :on-change #(reset! val (.-target.value %))}])

(defn input-select [id options val]
  [:select.form-control
   {:id id
    :value (or @val "")
    :on-change #(reset! val (-> % .-target .-value))}
   (for [d options]
     ^{:key d}
     [:option {:value d} d])])

(defn row-input [label id key type place val focus val-fun]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [input id type place val]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

(defn row-num [label id key type place val focus val-fun]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [input-num id type place val]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

(defn row-number [label id key place val focus val-fun]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [input-number id place val]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

(defn row-text-area [label id key rows place val focus val-fun]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [text-area id rows place val]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

(defn row-text-area-terms&cond [label id key rows place val focus val-fun]
  [:div.form-group
   [:label label]
   [:div
    [text-area id rows place val]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])


(defn input-select1 [id options val]
  [:select.form-control
   {:id id
    :value (or @val "")
    :on-change #(reset! val (-> % .-target .-value))}
   [:option {:value ""} "-- Select --"]
   (for [d options]
     ^{:key d}
     [:option {:value d} d])])

(defn row-select [label id key options val focus val-fun]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [input-select1 id options val]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

(defn input-select-onchange [id options val onchange]
  [:select.form-control
   {:id id
    :value (or val "")
    :on-change onchange}
   [:option {:value ""} "-- Select --"]
   (for [d options]
     ^{:key d}
     [:option {:value d} d])])

(defn row-select-onchnage [label id key options val focus val-fun onchange]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [input-select-onchange id options val onchange]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

;; chosen select Component
;; ------------------------------------------------
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

;; ----------------------------------------
;; resct select
;; ----------------------------------------
(def r-select (r/adapt-react-class js/Select))

(defn js-clj [val]
  (js->clj val :keywordize-keys true))

(defn react-select
  [name placeholder options val]
  [r-select
   {:name name
    :value @val
    :placeholder placeholder
    :labelKey :name
    :valueKey :id
    :options @options
    :clearable false
    :on-change #(reset! val (get (js-clj %) :id))}])

(defn row-chosen-select
  [label id key options val focus val-fun]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [react-select id "please select" options val]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

(defn react-select-onchange
  [name placeholder options val on-changefn]
  [r-select
   {:name name
    :value val
    :placeholder placeholder
    :labelKey :name
    :valueKey :id
    :options @options
    :clearable false
    :on-change on-changefn}])

(defn row-react-select-onchange
  [label id key options val focus val-fun on-changefn]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [react-select-onchange id "please select" options val on-changefn]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

;; ----------------------------------------------------
;; Hotel select tag
;; ----------------------------------------------------
(defn set-hotel-data [hotel-id hotels kth data]
  (let [record (first (filter #(= hotel-id (get % :id)) @hotels))]
    ;; (js/console.log (str record))
    (swap! data assoc-in
           [:cities kth :hotelinformation :address]
           (get record :address ""))
    (swap! data assoc-in
           [:cities kth :hotelinformation :telephonenumber]
           (get record :phonenumber ""))
    (swap! data assoc-in
           [:cities kth :hotelinformation :faxnumber]
           (get record :faxnumber ""))))

(defn hotel-select
  [name options val kth data]
  [r-select
   {:name name
    :value @val
    :placeholder "Select Hotel"
    :labelKey :name
    :valueKey :id
    :options @options
    :clearable false
    :on-change #(do (reset! val (get (js-clj %) :id))
                    (set-hotel-data (get (js-clj %) :id)
                                    options kth data))}])

;; -------------------------------------------------
;; select with manual
;; -------------------------------------------------
(defn manual-onchange
  ([data]
   (fn [input]
     (let [record (js-clj input)
           category (@data :category)
           subcategory (@data :subcategory)]
       (swap! data assoc :itemid (record :id))
       (swap! data assoc :vendorid1 (record :vendorid))
       (if (= "meals" category)
         (do (swap! data assoc :activitytitle
                    (str/replace (record :activitytitle "") #"%%" subcategory))
             (swap! data assoc :manualdescription
                    (str/replace (record :activitydescription "") #"%%" subcategory)))
         (do (swap! data assoc :activitytitle (record :activitytitle ""))
             (swap! data assoc :manualdescription (record :activitydescription "")))))))

  ([data kth i]
   (fn [input]
     (let [record (js-clj input)
           category (get-in @data [:cities kth :itineraries i :category] "")
           subcategory (get-in @data [:cities kth :itineraries i :subcategory] "")]
       (swap! data assoc-in [:cities kth :itineraries i :itemid] (record :id))
       (swap! data assoc-in [:cities kth :itineraries i :vendorid1] (record :vendorid))
       (if (= "meals" category)
         (do (swap! data assoc-in
                    [:cities kth :itineraries i :activitytitle]
                    (str/replace (record :activitytitle "") #"%%" subcategory))
             (swap! data assoc-in
                    [:cities kth :itineraries i :manualdescription]
                    (str/replace (record :activitydescription "") #"%%" subcategory)))
         (do (swap! data assoc-in
                    [:cities kth :itineraries i :activitytitle] (record :activitytitle ""))
             (swap! data assoc-in
                    [:cities kth :itineraries i :manualdescription]
                    (record :activitydescription ""))))))))

(defn select-with-manual
  ([id options data]
   (let [new-options (reaction (conj (seq @options) {:id 0 :name "Manual"}))]
     (fn []
       [r-select
        {:name name
         :value (get @data :itemid)
         :placeholder "Select Activity"
         :labelKey :name
         :valueKey :id
         :options  @new-options
         :clearable false
         :on-change (manual-onchange data)}])))
  ([id options data kth i]
   (let [new-options (reaction (conj (seq @options) {:id 0 :name "Manual"}))]
     (fn []
       [r-select
        {:name name
         :value (get-in @data [:cities kth :itineraries i :itemid])
         :placeholder "Select Activity"
         :labelKey :name
         :valueKey :id
         :options  @new-options
         :clearable false
         :on-change (manual-onchange data kth i)}]))))

;; ------------------------------------------------
;; this is only for quotation tag
;; Quation number and ID is concated to string for
;; select tag value
;; ------------------------------------------------
(defn quotation-onchange [data]
  (fn [input]
    (let [record (js-clj input)]
      (swap! data assoc :quotationid (record :key))
      ;; (swap! data assoc :sevenmagentname (record :agentname))
      )))

(defn select-quotations [id options data]
  (let [new-options (reaction
                     (mapv #(assoc % :key (str (:name %)" "(:id %)))
                           @options))]
    (fn []
      [r-select
       {:name name
        :value (get @data :quotationid)
        :placeholder "Select Quotation"
        :labelKey :name
        :valueKey :key
        :options  @new-options
        :clearable false
        :on-change (quotation-onchange data)
        ;; #(reset! val (get (js-clj %) :key))
        }])))

(defn row-select-quotation
  [label id key options val focus val-fun]
  [:div.form-group
   [:label.col-md-4.control-label label]
   [:div.col-md-8
    [select-quotations id options val]
    (if focus
      (if (= nil val-fun)
        [:div]
        [:div {:style  {:color "red"}}
         (str (first (val-fun key)))])
      [:div])]])

;; bootstrap datepicker component
;; ----------------------------------------------------
(defn datepicker-did-mount [this val]
  (let [dp-node (js/$ (r/dom-node this))]
    (.val dp-node (or @val ""))
    (reset! val (.val dp-node))
    (.datepicker dp-node
                 (clj->js {:format "dd-M-yyyy"
                           :autoclose true
                           :todayBtn  true
                           :todayHighlight true}))
    (.on (.datepicker dp-node)
         "changeDate"
         #(reset! val (.val dp-node)))))

(defn datepicker-did-update [this val]
  (let [dp-node (js/$ (r/dom-node this))]
    (.val dp-node (or @val ""))
    (reset! val (.val dp-node))
    (.datepicker dp-node "update")
    (.on (.datepicker dp-node)
         "changeDate"
         #(reset! val (.val dp-node)))))

(defn datepicker
  ([id placeholder val]
   (r/create-class
    {:component-did-mount #(datepicker-did-mount % val)
     :component-did-update #(datepicker-did-update % val)
     :reagent-render
     (fn [this]
       [:input.form-control
        {:id id
         :value (or @val "")
         :type "text"
         :placeholder placeholder
         :on-change #(reset! val (.-target.value %))
         :on-blur #(reset! val (.-target.value %))
         }])}))
  ([id placeholder val style]
   (r/create-class
    {:component-did-mount #(datepicker-did-mount % val)
     :component-did-update #(datepicker-did-update % val)
     :reagent-render
     (fn [this]
       [:input.form-control
        {:id id
         :style style
         :value (or @val "")
         :type "text"
         :placeholder placeholder
         :on-change #(reset! val (.-target.value %))
         :on-blur #(reset! val (.-target.value %))
         }])})))

;; bootstrap timepicker component
;; ------------------------------------------------------
(defn timepicker-did-mount [this val]
  (let [tp-node (js/$ (r/dom-node this))]
    (.val tp-node (or @val ""))
    (.timepicker tp-node
                 (clj->js {:minuteStep 5
                           :showInputs false
                           :disableFocus true
                           :defaultTime false}) )
    (.on (.timepicker tp-node)
         "changeTime.timepicker"
         #(reset! val (.val tp-node)))))

(defn timepicker-did-update [this val]
  (let [tp-node (js/$ (r/dom-node this))]
    (.val tp-node (or @val ""))
    (.timepicker tp-node "setTime" (or @val ""))
    (.on (.timepicker tp-node)
         "changeTime.timepicker"
         #(reset! val (.val tp-node)))))

(defn timepicker
  ([id placeholder val]
   (r/create-class
    {:component-did-mount #(timepicker-did-mount % val)
     :component-did-update #(timepicker-did-update % val)
     :reagent-render
     (fn []
       [:input.form-control.input-small
        {:id id
         :type "text"
         :name @val
         :placeholder placeholder}])}))
  ([id placeholder val style]
   (r/create-class
    {:component-did-mount #(timepicker-did-mount % val)
     :component-did-update #(timepicker-did-update % val)
     :reagent-render
     (fn []
       [:input.form-control.input-small
        {:id id
         :style style
         :type "text"
         :name @val
         :placeholder placeholder}])})))

(defn div-timepicker
  ([id placeholder val]
   [:div.input-group.bootstrap-timepicker.timepicker
    {:style {:z-index 0}}
    [timepicker id placeholder val]])
  ([id placeholder val style]
   [:div.input-group.bootstrap-timepicker.timepicker
    {:style {:z-index 0}}
    [timepicker id placeholder val]]))

;; Star Rating
;; ----------------------------------------------------

(defn star-did-mount [this val disabled]
  (let [dp-node (js/$ (r/dom-node this))]
    (.rating dp-node (clj->js {:displayOnly disabled}))
    (.rating dp-node "update" @val)
    (.on dp-node "rating.change"
         (fn [event value caption]
           (reset! val (reader/read-string value))))
    (.on dp-node "rating.clear"
         (fn [event]
           (reset! val 0)))))

(defn star-did-update [this val disabled]
  (let [dp-node (js/$ (r/dom-node this))]
    (.rating dp-node (clj->js {:displayOnly disabled}))
    (.rating dp-node "update" @val)
    (.on dp-node "rating.change"
         (fn [event value caption]
           (reset! val (reader/read-string value))))
    (.on dp-node "rating.clear"
         (fn [event]
           (reset! val 0)))))

(defn star-rating [id val size disabled]
  (r/create-class
   {:component-did-mount #(star-did-mount % val disabled)
    :component-did-update #(star-did-update % val disabled)
    :reagent-render
    (fn [this]
      [:input
       {:id id
        :value (or @val 0)
        :type "text"
        :class "rating"
        :on-change #()
        :data-size size
        :data-min 0
        :data-max 5
        :data-step 0.5}])}))

;; ----------------------------------------------------------
;; Doc Upload Component
;; ----------------------------------------------------------
(def upload-style {:position "absolute"
                   :top 0
                   :right 0
                   :margin 0
                   :padding 0
                   :font-size "20px"
                   :cursor "pointer"
                   :opacity "0"
                   :filter "alpha(opacity=0)"})

(defn change-file [element-id val filename]
  (let [el (.getElementById js/document element-id)
        name (.-name el)
        file (aget (.-files el) 0)
        form-data (doto
                      (js/FormData.)
                    (.append name file))]
    (if file
      (do
        (js/console.log form-data)
        (reset! filename (.-name file))
        (reset! val form-data))
      (do (reset! filename nil)
          (reset! val nil)))))

(def accept-types-hotel-contractfile
  (str "image/png,"
       "image/jpg,"
       "application/pdf,"
       "image/jpeg,"
       "application/vnd.openxmlformats-
        officedocument.wordprocessingml.document,"
       "application/msword"))
(def accept-types-logo
  (str "image/png,"
       "image/jpg,"
       "image/jpeg,"))

(defn file-upload-button
  [element-id placeholder accept-types val filename]
  [:div.from-horizontal
   [:div.from-group
    [:div.input-group
     [:span.input-group-btn
      [:div.btn.btn-default
       {:style {:position "relative"
                :overflow "hidden"}}
       [:span.glyphicon.glyphicon-file]
       " Choose File"
       [:input
        {:id element-id
         :type "file"
         :accept accept-types
         :name "file"
         :style upload-style
         :on-change #(change-file element-id val filename)}]]]
     [:input.form-control
      {:placeholder placeholder
       :type "text"
       :value @filename
       :disabled true}]]]])

(defn boot-notify [type title message]
  (.notify
   js/$ (clj->js {:title (str "<strong>" title "</strong>")
                  :message message})
   (clj->js {:type type})))

(defn button-tooltip [map-options icon]
  (r/create-class
   {:component-did-mount #(.tooltip (js/$ (r/dom-node %)))
    :reagent-render
    (fn []
      [:button map-options icon])}))

(defn anchor-tooltip
  ([map-options icon]
   (r/create-class
    {:component-did-mount #(.tooltip (js/$ (r/dom-node %)))
     :reagent-render
     (fn []
       [:a map-options icon])}))
  ([map-options icon string]
   (r/create-class
    {:component-did-mount #(.tooltip (js/$ (r/dom-node %)))
     :reagent-render
     (fn []
       [:a map-options icon string])})))

;; Declares
;; -------------------------
(declare fitbookings-atom)
(declare groupbookings-atom)
(declare error-handler)

;; -----------------------------------------------------------
;; search and call for all grids
;; -----------------------------------------------------------
(defn error-handler [res]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token!
             routes/history (routes/url-for :home)))
    (boot-notify
     "warning" "Warning :"
     "Problem with the server, Please try again")))

(defn get-table-data-search [src-in list]
  (let [type (@src-in :type)
        text (@src-in :text)
        url (@src-in :url)]
    (ajax/GET
     (str server url "?index=1&pagesize=10"
          (when (seq text) (str "&type=" type "&text=" text)))
     {:handler #(do (reset! list %)
                    (swap! src-in assoc :idx 1))
      :error-handler error-handler
      :format :json
      :response-format :json
      :keywords? true
      :headers
      {:Authorization
       (str "Token " (get-in @session [:session :token]))}})))

(defn get-table-data [src-in list]
  (let [idx (@src-in :idx)
        type (@src-in :type)
        text (@src-in :text)
        url (@src-in :url)]
    (ajax/GET
     (str server url "?index="idx"&pagesize=10"
          (when (seq text) (str "&type=" type "&text=" text)))
     {:handler #(reset! list %)
      :error-handler error-handler
      :format :json
      :response-format :json
      :keywords? true
      :headers
      {:Authorization
       (str "Token " (get-in @session [:session :token]))}})))

;; for tabel delete calls
;; ----------------------------------

(defn  delete-table-error-handler [res]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token!
             routes/history (routes/url-for :home)))
    (do (.modal (js/$ "#removemodal") "hide")
        (boot-notify
         "warning" "Warning :"
         "Problem with the server, Please try again"))))

(defn get-table-data-delete [src-in list]
  (let [idx (@src-in :idx)
        type (@src-in :type)
        text (@src-in :text)
        url (@src-in :url)]
    (ajax/GET
     (str server url "?index="idx"&pagesize=10"
          (when (seq text) (str "&type=" type "&text=" text)))
     {:handler #(do (reset! list %)
                    (.modal (js/$ "#removemodal") "hide"))
      :error-handler delete-table-error-handler
      :format :json
      :response-format :json
      :keywords? true
      :headers
      {:Authorization
       (str "Token " (get-in @session [:session :token]))}})))

(defn delete-record-handler [res src-in list]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    200 (get-table-data-delete src-in list)
    (boot-notify
     "warning" "Warning :"
     "Problem with the server, Please try again")))

(defn search-select [id options val]
  [:select.form-control
   {:id id
    :style {:width "30%"}
    :value @val
    :on-change #(reset! val (-> % .-target .-value))}
   (for [d options]
     ^{:key (:id d)}
     [:option {:value (:id d)} (:name d)])])

(defn search-input
  ([id input-type place val src-in list]
   [:div.has-feedback
    [:input.form-control
     {:id id
      :style {:width "70%"}
      :type input-type
      :placeholder place
      :value @val
      :on-change #(do
                    (reset! val (.-target.value %))
                    (get-table-data-search src-in list))}]
    [:span.glyphicon.glyphicon-search.form-control-feedback.text-muted]])
  ([id input-type place val src-in list width]
   [:div.has-feedback
    [:input.form-control
     {:id id
      :style {:width width}
      :type input-type
      :placeholder place
      :value @val
      :on-change #(do
                    (reset! val (.-target.value %))
                    (get-table-data-search src-in list))}]
    [:span.glyphicon.glyphicon-search.form-control-feedback.text-muted]]))

;; End of searchcomponents grids
;; ----------------------------------------------------

;; ----------------------------------------------------
;; Quotations list Grid
;; ----------------------------------------------------
(defn get-fitquotations-list [idx list]
  (ajax/GET
   (str server "fit/quotations/" @idx "/10")
   {:handler #(reset! list %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-quotation-pdf [i]
  (let [quotation-id (i :quotationid)
        send-quo-id (str/replace quotation-id #"/" "")]
    (.open js/window
           (str server
                (if  (= \T (get (vec (seq  send-quo-id)) 2))
                  (str "fit/pdf/"  send-quo-id)
                  (str "groups/pdf/"  send-quo-id))))))

(defn make-booking-fit-onclick [record]
  (do (reset! fitbookings-atom
              {:bookingnumber "BK-00"
               :isfinalbooking false
               :emergencynumbers ""
               :termsandconditions terms-and-conditions
               :quotationid (str (record :quotationid)" "(get record :id))
               :cities []
               :pdf-mail-data {}})
      (pushy/set-token!
       routes/history
       (routes/url-for :add-fitbooking))))

(defn fit-quotations-comp []
  (let [idx (r/atom 1)
        list (r/atom {:data []
                      :totalquotations 0})
        star (r/atom 0)]
    (get-fitquotations-list idx list)
    (fn []
      [:div.box.box-primary
       ;; [star-rating "siva" star "xs" false]
       [:div.box-header.with-border
        [:i.fa.fa-list]
        [:h3.box-title "List of FIT Quotations"]]
       [:div.box-body
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            [:th "Quotation Id"]
            [:th "Date of Quotation"]
            [:th "Agent Name"]
            [:th "View Quotation"]
            [:th "Make A Booking"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:quotationid i)}
             [:tr
              [:td (:quotationid i)]
              [:td (:date i)]
              [:td (:agentname i)]
              [:td  [:button.btn.btn-info.btn-sm
                     {:on-click #(get-quotation-pdf i)}
                     "View Quotation"]]
              [:td [:button.btn.btn-info.btn-sm
                    {:on-click #(make-booking-fit-onclick i)}
                    "Make A Booking"]]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalquotations) 10)))
          :maxButtons 5
          :activePage @idx
          :onSelect #(do
                       (reset! idx %)
                       (get-fitquotations-list idx list))}]]])))

;; ----------------------------------------------------
;; Group Quotations list Grid
;; ----------------------------------------------------

(defn get-groupquotations-list [idx list]
  (ajax/GET
   (str server "groups/quotations/" @idx "/10")
   {:handler #(reset! list %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn make-booking-group-onclick [record]
  (do (reset! groupbookings-atom
              {:bookingnumber "BK-00"
               :isfinalbooking false
               :emergencynumbers ""
               :termsandconditions terms-and-conditions
               :quotationid (str (record :quotationid)" "(get record :id))
               :cities []
               :pdf-mail-data {}})
      (pushy/set-token!
       routes/history
       (routes/url-for :add-groupbooking))))

(defn group-quotations-comp []
  (let [idx (r/atom 1)
        list (r/atom {:data []
                      :totalquotations 0})]
    (get-groupquotations-list idx list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-list]
        [:h3.box-title "List of Group Quotations"]]
       [:div.box-body
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            [:th "Quotation Id"]
            [:th "Date of Quotation"]
            [:th "Agent Name"]
            [:th "View Quotation"]
            [:th "Make A Booking"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:quotationid i)}
             [:tr
              [:td (:quotationid i)]
              [:td (:date i)]
              [:td (:agentname i)]
              [:td  [:button.btn.btn-info.btn-sm
                     {:on-click #(get-quotation-pdf i)}
                     "View Quotation"]]
              [:td [:button.btn.btn-info.btn-sm
                    {:on-click #(make-booking-group-onclick i)}
                    "Make A Booking"]]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalquotations) 10)))
          :maxButtons 5
          :activePage @idx
          :onSelect #(do
                       (reset! idx %)
                       (get-groupquotations-list idx list))}]]])))

;; ----------------------------------------------------------
;; FIT and Group Bookings hotels, flight and itineraries
;; ----------------------------------------------------------
;; ajax calls
;; -----------------------------------------
(defn get-all-cities [cities]
  (ajax/GET
   (str server "cities")
   {:handler #(reset! cities %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-all-logos [logos]
  (ajax/GET
   (str server "logos")
   {:handler #(reset! logos %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-all-items [items]
  (ajax/GET
   (str server "items")
   {:handler #(reset! items %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-fit-items-by-city [id items]
  (ajax/GET
   (str server "itemsbyfitcityid/" id)
   {:handler #(reset! items %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-group-items-by-city [id items]
  (ajax/GET
   (str server "itemsbygroupcityid/" id)
   {:handler #(reset! items %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-all-vendors [vendors]
  (ajax/GET
   (str server "vendors")
   {:handler #(reset! vendors %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-vendors-by-city [id vendors]
  (ajax/GET
   (str server "vendorsbycityid/" id)
   {:handler #(reset! vendors %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-all-hotels [hotels]
  (ajax/GET
   (str server "hotels")
   {:handler #(reset! hotels %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-hotels-by-city [id hotels]
  (ajax/GET
   (str server "hotelsbycityid/" id)
   {:handler #(reset! hotels %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-vendors-by-city-category [cityid category vendors]
  (ajax/GET
   (str server (str "city/" cityid "/category/" category "/vendors" ))
   {:handler #(reset! vendors %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-fitactivities-by-city-category [cityid category activities]
  (ajax/GET
   (str server (str "city/"cityid "/category/" category "/fitactivities" ))
   {:handler #(reset! activities %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn get-groupactivities-by-city-category [cityid category activities]
  (ajax/GET
   (str server (str "city/"cityid "/category/" category "/groupactivities" ))
   {:handler #(reset! activities %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))



;; Itineraries add new Itinerary
;; -----------------------------------------
(defn check-manual [data-set]
  (fn [des]
    (if (> (@data-set :itemid) 0)
      true
      (if (seq des)
        true
        false))))

(defn add-itinerary-validator [data-set]
  (first
   (b/validate
    @data-set
    ;; :date [[v/required :message "Field is required"]]
    ;; :time [[v/required :message "Field is required"]]
    ;; :itemid [[v/required :message "Field is required"]]
    ;; :manualdescription [[(check-manual data-set)
    ;;                      :message "Field is required"]]
    ;; :amount [[v/required :message "Field is required"]]
    ;; :paymentmode [[v/required :message "Field is required"]]
    ;; :vendorid [[v/required :message "Field is required"]]
    ;; :remarks [[v/required :message "Field is required"]]
    ;; :numberofpeoples [[v/required :message "Field is required"]]
    )))

(defn error-box [focus val-fun key]
  [:div
   (if @focus
     (if (= nil val-fun)
       [:div]
       [:div {:style  {:color "red"}}
        (str (first (val-fun key)))])
     [:div])])

(defn add-itinerary-on-click [data-set kth add-data focus]
  (if (nil? (add-itinerary-validator add-data))
    (do
      (reset! focus false)
      (if (get-in @data-set [:cities kth :itineraries])
        (swap! data-set update-in
               [:cities kth :itineraries] conj @add-data)
        (swap! data-set assoc-in [:cities kth :itineraries] [@add-data]))
      (swap! add-data assoc
             :amount 0
             :paymentmode ""
             :itemid 0
             :vendorid "0"
             :vendorid1 0
             :confirmationnumber ""
             :numberofpeoples 0
             :manualdescription ""
             :remarks ""))
    (reset! focus true)))

(def categories-atom (r/atom
                      [{:id "meals" :name "Meals"}
                       {:id "hotel" :name "Hotel"}
                       {:id "transportation" :name "Transportation"}
                       {:id "attractions" :name "Attractions"}
                       {:id "guidedcitytours" :name "Guided City Tours"}]))

(def sub-categories ["Breakfast" "Lunch" "Hi-Tea" "Dinner"])
(def sub-categories-atom (r/atom (mapv #(assoc {} :id % :name %) sub-categories)))

(defn iti-category-onchange
  ([type data activities vendors]
   (fn [input]
     (let [record (js-clj input)
           category (record :id)
           cityid (@data :cityid)]
       (swap! data assoc :category category)
       (swap! data assoc :subcategory "")
       (swap! data assoc :activitytitle "")
       (swap! data assoc :itemid 0)
       (swap! data assoc :vendorid1 0)
       (swap! data assoc :manualdescription "")
       (get-vendors-by-city-category cityid category vendors)
       (case type
         "fit" (get-fitactivities-by-city-category cityid category activities)
         (get-fitactivities-by-city-category cityid category activities)))))
  ([type data kth i activities vendors]
   (fn [input]
     (let [record (js-clj input)
           category (record :id)
           cityid (get-in @data [:cities kth :itineraries i :cityid])]
       (swap! data assoc-in [:cities kth :itineraries i :category] category)
       (swap! data assoc-in [:cities kth :itineraries i :subcategory] "")
       (swap! data assoc-in [:cities kth :itineraries i :activitytitle] "")
       (swap! data assoc-in [:cities kth :itineraries i :itemid] 0)
       (swap! data assoc-in [:cities kth :itineraries i :vendorid1] 0)
       (swap! data assoc-in [:cities kth :itineraries i :manualdescription] "")
       (get-vendors-by-city-category cityid category vendors)
       (case type
         "fit" (get-fitactivities-by-city-category cityid category activities)
         (get-fitactivities-by-city-category cityid category activities))))))

(defn add-itinerary [type data kth]
  (let [add-data (r/atom {:cityid (get-in @data [:cities kth :cityid])
                          :date ""
                          :amount 0
                          :paymentmode ""
                          :remarks ""
                          :vendorid "0"
                          :vendorid1 0
                          :numberofpeoples 0
                          :confirmationnumber ""
                          :manualdescription ""
                          :itemid 0})
        focus (r/atom false)
        vendors (r/atom [])
        activities (r/atom [])]
    (fn []
      [:div
       [:div.row
        [:div.col-md-4.col-sm-4
         [:div.row
          [:div.col-md-6.col-sm-6
           [:div.col-md-12.col-sm-12
            [:div.row
             [:small "Date"]
             [datepicker "datepicker" "dd-MMM-yyyy"
              (r/cursor add-data [:date])]
             [error-box focus (add-itinerary-validator add-data) :date]]
            [:div.row
             [:small "Time"]
             [div-timepicker "timepicker" "hh:mm"
              (r/cursor add-data [:time])]
             [error-box focus (add-itinerary-validator add-data) :time]]]]
          [:div.col-md-6.col-sm-6
           [:div.col-md-12.col-sm-12
            [:div.row
             [:small "Category"]
             [react-select-onchange "Category" "Select Category"
              categories-atom (get @add-data :category)
              (iti-category-onchange type add-data activities vendors)]
             [error-box focus (add-itinerary-validator add-data) :category]]
            (when (= "meals" (@add-data :category))
              [:div.row
               [:small "Sub-category"]
               [react-select "Sub-category" "Please Sub-category"
                sub-categories-atom (r/cursor add-data [:subcategory])]
               [error-box focus (add-itinerary-validator add-data) :subcategory]])]]]]
        [:div.col-md-8.col-sm-8
         [:div.row
          [:div.col-md-3.col-sm-3
           [:div.col-md-12.col-sm-12
            [:div.row
             [:small "Activity Name"]
             [select-with-manual "Activityid" activities add-data]
             [error-box focus (add-itinerary-validator add-data) :itemid]]
            [:div.row
             [:small "Activity Title"]
             [input "actitvitytitle" "text" "Activity Title"
              (r/cursor add-data [:activitytitle])]]]]
          [:div.col-md-6.col-sm-6
           [:small "Activity Description"]
           [text-area "manualDescription" 4 "Manual Description"
            (r/cursor add-data [:manualdescription])]
           [error-box focus (add-itinerary-validator add-data) :manualdescription]]
          [:div.col-md-3.col-sm-3
           [:div.col-md-12.col-sm-12
            [:div.row
             [:small "Vendor Name"]
             [react-select "vendor-name" "Select vendor" vendors
              (r/cursor add-data [:vendorid1])]
             [error-box focus (add-itinerary-validator add-data) :vendorid1]]
            [:div.row
             [:br]
             [:button.btn.btn-default.btn-block
              {:on-click #(add-itinerary-on-click data kth add-data focus)}
              "Add Itinerary"]]]]]]]
       #_[:div.row
          [:div.col-md-2.col-sm-3
           [:small "Amount"]
           [input-number "amount" "Amount"
            (r/cursor add-data [:amount])]
           [error-box focus (add-itinerary-validator add-data) :amount]]
          [:div.col-md-2.col-sm-3
           [:small "Payment Mode"]
           [react-select "Payment-mode"
            "Select Paymentmode" payment-mode-data
            (r/cursor add-data [:paymentmode])]
           [error-box focus (add-itinerary-validator add-data) :paymentmode]]

          [:div.col-md-2.col-sm-3
           [:small "Remarks"]
           [input "remarks" "text" "Remarks"
            (r/cursor add-data [:remarks])]
           [error-box focus (add-itinerary-validator add-data) :remarks]]
          [:div.col-md-1.col-sm-3
           [:small "# People"]
           [input-num "# People" "text" "# People"
            (r/cursor add-data [:numberofpeoples])]
           [error-box focus (add-itinerary-validator add-data)
            :numberofpeoples]]
          [:div.col-md-2.col-sm-3
           [:small "Comments"]
           [input "Comments" "text" "Comments"
            (r/cursor add-data [:confirmationnumber])]
           [error-box focus (add-itinerary-validator add-data)
            :confirmation]]]])))

;;-> Show Itineraries
;; -------------------------------

;; ---------------------------------------------
;; Remove Itinerary modal view delete functionality
;; ---------------------------------------------

(defn delete-itineraries-error-handler [res]
  (do (.modal (js/$ "#itineraryremovemodal") "hide")
      (case (:status res)
        401 (do (swap! session dissoc :session)
                (pushy/set-token! routes/history (routes/url-for :home)))
        (boot-notify
         "warning" "Warning :"
         "Problem with the server, Please try again"))))

(defn delete-itineraries-success-handler [data-set kth index]
  (let [itineraries (get-in @data-set
                            [:cities kth :itineraries] [])
        indexes (range (count itineraries))]
    (swap! data-set assoc-in [:cities kth :itineraries]
           (vec (vals (dissoc (zipmap indexes itineraries) @index))))
    (.modal (js/$ "#itineraryremovemodal") "hide")))

(defn delete-itineraries-model-onclick [data-set kth index id]
  (ajax/DELETE
   (str server "bookingdetails/" id)
   {:handler #(delete-itineraries-success-handler data-set kth index)
    :error-handler #(delete-itineraries-error-handler %)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn remove-itineraries-on-click [data-set kth index]
  (let [id (get-in @data-set [:cities kth :itineraries @index :id])]
    (if id
      (delete-itineraries-model-onclick data-set kth index id)
      (delete-itineraries-success-handler data-set kth index))))

(defn delete-itineraries-modal [data kth index]
  [:div.modal.fade
   {:role "dialog" :id "itineraryremovemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Itinerary ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @index "Itinerary")
        :type "button"
        :on-click #(remove-itineraries-on-click data kth index)} "Yes"]
      [:button.btn.btn-default
       {:type "button" :data-dismiss "modal"} "No"]]]]])


(defn show-itineraries-items-comp [type data kth i index ]
  (let [cityid (get-in @data [:cities kth :cityid])
        category (get-in @data [:cities kth :itineraries i :category])
        vendors (r/atom [])
        activities (r/atom [])]
    (when (seq category)
      (do
        (get-vendors-by-city-category cityid category vendors)
        (case type
          "fit" (get-fitactivities-by-city-category cityid category activities)
          (get-fitactivities-by-city-category cityid category activities))))
    (fn []
      [:div.row
       (when (get-in @data [:cities kth :itineraries i :id])
         [activity/paymentlog-model-comp "Itinerary Payment log" data kth i
          (str "paymentlog?paymentid=" (get-in @data [:cities kth :itineraries i :id])
               "&bookingid=" (get @data :id) "&") "itinerary" vendors])
       [:div.col-md-4.col-sm-4
        [:div.row
         [:div.col-md-6.col-sm-6
          [:div.col-md-12.col-sm-12
           [:div.row
            [:small "Date"]
            [datepicker "datepicker" "dd-MMM-yyyy"
             (r/cursor data [:cities kth :itineraries i :date])]]
           [:div.row
            [:small "Time"]
            [div-timepicker "timepicker" "hh:mm"
             (r/cursor data [:cities kth :itineraries i :time])]]]]
         [:div.col-md-6.col-sm-6
          [:div.col-md-12.col-sm-12
           [:div.row
            [:small "Category"]
            [react-select-onchange "Category" "Select Category" categories-atom
             (get-in @data [:cities kth :itineraries i :category])
             (iti-category-onchange type data kth i activities vendors)]]
           (when (= "meals" (get-in @data [:cities kth :itineraries i :category]))
             [:div.row
              [:small "Sub-category"]
              [react-select "Sub-category" "Select Sub-category" sub-categories-atom
               (r/cursor data [:cities kth :itineraries i :subcategory])]])]]]]
       [:div.col-md-8.col-sm-8
        [:div.row
         [:div.col-md-3.col-sm-3
          [:div.col-md-12.col-sm-12
           [:div.row
            [:small "Activity Name"]
            [select-with-manual "Activities" activities data kth i]]
           [:div.row
            [:small "Activity Title"]
            [input "activitytitle" "text"  "Activity Title"
             (r/cursor data [:cities kth :itineraries i :activitytitle])]]]]
         [:div.col-md-6.col-sm-6
          [:small "Activity Description"]
          [text-area "ActivityDescription" 4 "Activity Description"
           (r/cursor data [:cities kth :itineraries i :manualdescription])]]
         [:div.col-md-3.col-sm-3
          [:div.col-md-12.col-sm-12
           [:div.row
            [:small "Vendor Name"]
            [react-select "vendor-name" "Select Vendor" vendors
             (r/cursor data [:cities kth :itineraries i :vendorid1])]]
           [:div.row
            [:br]
            [:div.btn-group
             (when (get-in @data [:cities kth :itineraries i :id])
               [button-tooltip
                {:class "btn btn-info"
                 :on-click #(.modal
                             (js/$ (str "#itineraryActivitylog"
                                        (get-in @data [:cities kth :itineraries i :id])))
                             "show")
                 :title "Actitvity Log"}
                [:i.fa.fa-book]])
             (when (get-in @data [:cities kth :itineraries i :id])
               [button-tooltip
                {:class "btn btn-info"
                 :on-click #(.modal
                             (js/$ (str "#itineraryPaymentlog"
                                        (get-in @data [:cities kth :itineraries i :id])))
                             "show")
                 :title "Payment Log"}
                [:i.fa.fa-credit-card]])
             [button-tooltip
              {:class "btn  btn-info"
               :on-click (fn []
                           (let [vendor-id (get-in @data [:cities kth :itineraries i :vendorid1] 0)
                                 vendor-details (first (filter #(= vendor-id (% :id)) @vendors))]
                             (swap! data assoc :vendor-info vendor-details)
                             (.modal (js/$ "#vendorinfo") "show")))
               :title "Vendor Info"}
              [:i.fa.fa-address-card]]
             [button-tooltip
              {:class "btn btn-info"
               :on-click #(do (reset! index i)
                              (.modal (js/$ "#itineraryremovemodal") "show"))
               :title "Remove Itinerary"}
              [:i.fa.fa-trash]]]]]]]]])))

(defn show-itineraries [type data kth index ]
  [:ul.todo-list.ui-sortable
   (doall
    (for [i (range (count (get-in @data [:cities kth :itineraries] [])))]
      ^{:key i}
      [:li
       (when (get-in @data [:cities kth :itineraries i :id])
         [activity/activity-model-comp "Itinerary Actitvity log" data kth i
          (str "activitylog?activityid=" (get-in @data [:cities kth :itineraries i :id])
               "&bookingid=" (get @data :id) "&") "itinerary"])
       [show-itineraries-items-comp type data kth i index ]]))])


(defn itinerary-comp [type data-set kth ]
  (let [index (r/atom 0)]
    (fn []
      [:div.box.box-default.box-solid
       [:div.box-header.with-border
        [:i.fa.fa-calendar-check-o]
        [:h3.box-title "Day & Time Wise Itinerary"]
        [:div.box-tools.pull-right
         [:button.btn.btn-box-tool
          {:data-widget "collapse", :type "button"}
          [:i.fa.fa-minus]]]]
       [:div.box-body
        [delete-itineraries-modal data-set kth index]
        [show-itineraries type data-set kth index ]]
       [:div.box-footer.clearfix
        [add-itinerary type data-set kth]]])))

;; ---------------------------------------------------
;; Flights Components
;; ---------------------------------------------------
(defn delete-flights-error-handler [res]
  (do (.modal (js/$ "#flightsdeletemodal") "hide")
      (case (:status res)
        401 (do (swap! session dissoc :session)
                (pushy/set-token!
                 routes/history
                 (routes/url-for :home)))
        (boot-notify
         "warning" "Warning :"
         "Problem with the server, Please try again"))))

(defn delete-flights-success-handler [data-set kth index]
  (let [flights (get-in @data-set [:cities kth :flightsinformation] [])
        indexes (range (count flights))]
    (swap! data-set assoc-in [:cities kth :flightsinformation]
           (vec (vals (dissoc (zipmap indexes flights) @index))))
    (.modal (js/$ "#flightsdeletemodal") "hide")))

(defn delete-flights-model-onclick [data-set kth index id]
  (ajax/DELETE
   (str server "flightinformation/" id)
   {:handler #(delete-flights-success-handler data-set kth index)
    :error-handler #(delete-flights-error-handler %)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn remove-flights-on-click [data-set kth index]
  (let [id (get-in @data-set [:cities kth :flightsinformation @index :id])]
    (if id
      (delete-flights-model-onclick data-set kth index id)
      (delete-flights-success-handler data-set kth index))))

(defn delete-Transport-modal [data kth index]
  [:div.modal.fade
   {:role "dialog" :id "flightsdeletemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Transport ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @index "transport")
        :type "button"
        :on-click #(remove-flights-on-click data kth index)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn show-flight-information-comp [data kth index]
  [:ul.todo-list.ui-sortable
   (doall
    (for [i (range (count (get-in @data [:cities kth :flightsinformation] [])))]
      ^{:key i}
      [:li
       [:div.row
        [:div.col-md-2.col-sm-3
         [:small "Journey Type"]
         [input-select
          "journeytype"
          ["Arrive Flight" "Depart Flight"
           "Arrive Cruise" "Depart Cruise"
           "Arrive Surface" "Depart Surface"]
          (r/cursor data [:cities kth :flightsinformation i :journeytype])]]
        [:div.col-md-2.col-sm-3
         [:small "Transportation Number"]
         [input "FlightNo" "text" "TP Number"
          (r/cursor data [:cities kth :flightsinformation i :flightnumber])]]
        [:div.col-md-2.col-sm-3
         [:small "Transportation Name"]
         [input "transportationmname" "text"
          "Transportation Name"
          (r/cursor data [:cities kth :flightsinformation i :flightname])]]
        [:div.col-md-2.col-sm-3
         [:small "Date"]
         [datepicker "date" "dd-MMM-yyyy"
          (r/cursor data [:cities kth :flightsinformation i :date])]]
        [:div.col-md-2.col-sm-3
         [:small "Depart Time"]
         [div-timepicker "time" "hh:mm"
          (r/cursor data [:cities kth :flightsinformation i :departtime])]]
        [:div.col-md-2.col-sm-3
         [:small "Arrival Time"]
         [div-timepicker "time" "hh:mm"
          (r/cursor data [:cities kth :flightsinformation i :arrivetime])]]]
       [:div.row
        [:div.col-md-4.col-sm-3
         [:small "Depart"]
         [input "Depart" "text" "Depart"
          (r/cursor data [:cities kth :flightsinformation i :depart])]]
        [:div.col-md-4.col-sm-3
         [:small "Arrival"]
         [input "Arrival" "text" "Arrival"
          (r/cursor data [:cities kth :flightsinformation i :arrive])]]
        [:div.col-md-2.col-sm-3
         [:small "# of PAX"]
         [input "# of PAX" "text" "# of PAX"
          (r/cursor data [:cities kth :flightsinformation i :noofpax])]]
        [:div.col-md-2.col-sm-3
         [:br]
         [:button.btn.btn-info
          {:on-click #(do (reset! index i)
                          (.modal (js/$ "#flightsdeletemodal") "show"))}
          "Remove Transport"]]]]))])

(defn add-flights-on-click [data-set kth data]
  (do
    (if (get-in @data-set [:cities kth :flightsinformation])
      (swap! data-set update-in
             [:cities kth :flightsinformation] conj @data)
      (swap! data-set assoc-in [:cities kth :flightsinformation] [@data]))
    (reset! data {:cityid (get-in @data-set [:cities kth :cityid])
                  :journeytype "Arrive Flight"
                  :flightnumber ""
                  :date ""
                  :depart ""
                  :arrive ""
                  :departtime ""
                  :noofpax ""
                  :arrivetime ""})))

(defn add-flight-information-comp [data-set kth]
  (let [data (r/atom {:cityid (get-in @data-set [:cities kth :cityid])
                      :journeytype "Arrive Flight"
                      :flightnumber ""
                      :date ""
                      :noofpax ""
                      :depart ""
                      :arrive ""
                      :departtime ""
                      :arrivetime ""})]
    (fn []
      [:div
       [:div.row
        [:div.col-md-2.col-sm-3
         [:small "Journey Type"]
         [input-select
          "journeytype"
          ["Arrive Flight" "Depart Flight"
           "Arrive Cruise" "Depart Cruise"
           "Arrive Surface" "Depart Surface"]
          (r/cursor data [:journeytype])]]
        [:div.col-md-2.col-sm-3
         [:small "Transportation Number"]
         [input "transportationNo" "text"
          "TP Number"
          (r/cursor data [:flightnumber])]]
        [:div.col-md-2.col-sm-3
         [:small "Transportation Name"]
         [input "transportationmname" "text"
          "Transportation Name"
          (r/cursor data [:flightname])]]
        [:div.col-md-2.col-sm-3
         [:small "Date"]
         [datepicker "date" "dd-MMM-yyyy"
          (r/cursor data [:date])]]
        [:div.col-md-2.col-sm-3
         [:small "Depart Time"]
         [div-timepicker "time" "hh:mm"
          (r/cursor data [:departtime])]]
        [:div.col-md-2.col-sm-3
         [:small "Arrival Time"]
         [div-timepicker "time" "hh:mm"
          (r/cursor data [:arrivetime])]]]
       [:div.row
        [:div.col-md-4.col-sm-3
         [:small "Depart"]
         [input "Depart" "text" "Depart"
          (r/cursor data [:depart])]]
        [:div.col-md-4.col-sm-3
         [:small "Arrival"]
         [input "Arrival" "text" "Arrival"
          (r/cursor data [:arrive])]]
        [:div.col-md-2.col-sm-3
         [:small "# of PAX"]
         [input "# of PAX" "text" "# of PAX"
          (r/cursor data [:noofpax])]]
        [:div.col-md-2.col-sm-3
         [:br]
         [:button.btn.btn-default.btn-block
          {:on-click #(add-flights-on-click data-set kth data)}
          "Add Transport"]]]])))


(defn flight-information-comp [data-set kth]
  (let [index (r/atom 0)]
    (fn []
      [:div.box.box-default.box-solid
       [:div.box-header.with-border
        [:i.fa.fa-plane]
        [:h3.box-title "Transport Information"]
        [:div.box-tools.pull-right
         (when (get-in @data-set [:cities kth :flightsinformation 0 :id])
           [button-tooltip
            {:class "btn btn-box-tool"
             :on-click #(.modal
                         (js/$ (str "#transportActivitylog"
                                    (get @data-set :id)
                                    (get-in @data-set [:cities kth :cityid] 0)))
                         "show")
             :title "Actitvity Log"}
            [:i.fa.fa-book]])" "
         [:button.btn.btn-box-tool
          {:data-widget "collapse", :type "button"}
          [:i.fa.fa-minus]]]]
       [:div.box-body
        (when (get-in @data-set [:cities kth :flightsinformation 0 :id])
          [activity/activity-model-comp "Transportation Actitvity log" data-set kth 0
           (str "activitylog?activityid="
                (str
                 (get @data-set :id)
                 (get-in @data-set [:cities kth :cityid] 0))
                "&bookingid=" (get @data-set :id) "&") "transport"])
        [delete-Transport-modal data-set kth index]
        [show-flight-information-comp data-set kth index]]
       [:div.box-footer.clearfix
        [add-flight-information-comp data-set kth]]])))

;; -----------------------------------------------
;; Hotels Comp
;; -----------------------------------------------
;; ------------------------------------------------
;; Delete Hotel Modal View /hotelinformation/:id
;; ------------------------------------------------


(defn delete-hotels-error-handler [res]
  (do (.modal (js/$ "#hotelsremovemodal") "hide")
      (case (:status res)
        401 (do (swap! session dissoc :session)
                (pushy/set-token!
                 routes/history
                 (routes/url-for :home)))
        (boot-notify
         "warning" "Warning :"
         "Problem with the server, Please try again"))))

(defn delete-hotels-success-handler [data-set kth index]
  (let [hotels (get-in @data-set [:cities kth :hotelinformation] [])
        indexes (range (count hotels))]
    (swap! data-set assoc-in [:cities kth :hotelinformation]
           (vec (vals (dissoc (zipmap indexes hotels) @index))))
    (.modal (js/$ "#hotelsremovemodal") "hide")))

(defn delete-hotels-model-onclick [data-set kth index id]
  (ajax/DELETE
   (str server "hotelinformation/" id)
   {:handler #(delete-hotels-success-handler data-set kth index)
    :error-handler #(delete-hotels-error-handler %)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn remove-hotels-on-click [data-set kth index]
  (let [id (get-in @data-set [:cities kth :hotelinformation @index :id])]
    (if id
      (delete-hotels-model-onclick data-set kth index id)
      (delete-hotels-success-handler data-set kth index))))

(defn delete-hotels-modal [data kth index]
  [:div.modal.fade
   {:role "dialog" :id "hotelsremovemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead  "Are you sure you want to remove this Hotel ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @index "Modal")
        :type "button"
        :on-click #(remove-hotels-on-click data kth index)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn edit-hotel-selecttag-onchnage [data kth i]
  (fn [input]
    (let [record (js-clj input)]
      (swap! data assoc-in
             [:cities kth :hotelinformation i :id]
             (record :id))
      (swap! data assoc-in
             [:cities kth :hotelinformation i :address]
             (record :address))
      (swap! data assoc-in
             [:cities kth :hotelinformation i :telephonenumber]
             (record :telephonenumber))
      (swap! data assoc-in
             [:cities kth :hotelinformation i :faxnumber]
             (get record :faxnumber "")))))

(defn show-hotel-information-comp [data kth hotels index vendors ]
  [:ul.todo-list.ui-sortable
   (doall
    (for [i (range (count (get-in @data [:cities kth :hotelinformation] [])))]
      ^{:key i}
      [:li
       [:div.row
        (when (get-in @data [:cities kth :hotelinformation i :id])
          [activity/activity-model-comp "Hotel Information Actitvity log" data kth i
           (str "activitylog?activityid=" (get-in @data [:cities kth :hotelinformation i :id])
                "&bookingid=" (get @data :id) "&") "hotel"])
        (when (get-in @data [:cities kth :hotelinformation i :id])
          [activity/paymentlog-model-comp "Hotel Information Payment log" data kth i
           (str "paymentlog?paymentid=" (get-in @data [:cities kth :hotelinformation i :id])
                "&bookingid=" (get @data :id) "&") "hotel" vendors])
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Hotel Name"]
         [react-select-onchange "Hotel name" "Select hotel name"
          hotels (get-in @data [:cities kth :hotelinformation i :hotelid])
          (edit-hotel-selecttag-onchnage data kth i)]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Address"]
         [input "Address" "text" "Address"
          (r/cursor data [:cities kth :hotelinformation i :address])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Tel No"]
         [input "TelNo" "text" "Tel No"
          (r/cursor data [:cities kth :hotelinformation i :telephonenumber])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Fax No"]
         [input "fax" "text" "Fax No"
          (r/cursor data [:cities kth :hotelinformation i :faxnumber])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Booked Under Name"]
         [input "bookedundername" "text" "Booked Under Name"
          (r/cursor data [:cities kth :hotelinformation i :bookedundername])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Confirmation Number"]
         [input "Confirmation#" "text" "Conformation #"
          (r/cursor data [:cities kth :hotelinformation i :confirmationnumber])]]]
       [:div.row
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Check-In"]
         [datepicker "Check-In" "dd-MMM-yyyy"
          (r/cursor data [:cities kth :hotelinformation i :checkin])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Check-Out"]
         [datepicker "Check-Out" "dd-MMM-yyyy"
          (r/cursor data [:cities kth :hotelinformation i :checkout])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "No of Rooms & People"]
         [:div.input-group
          [input-num "NoOfRooms" "text" "No of Rooms"
           (r/cursor data [:cities kth :hotelinformation i :numerofrooms])
           {:width "50%"}]
          [input-num "numberofpeople" "text" "No of People"
           (r/cursor data [:cities kth :hotelinformation i :numberofpeoples])
           {:width "50%"}]]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Vendor Name"]
         [react-select "vendor-name" "Select Vendor" vendors
          (r/cursor data [:cities kth :hotelinformation i :vendorid1])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:br]
         [input-checkbox "isbreakfastincluded"
          (r/cursor data [:cities kth :hotelinformation i :isbreakfastincluded])
          "Is Breakfast Included"]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:br]
         [:div.btn-group
          (when (get-in @data [:cities kth :hotelinformation i :id])
            [button-tooltip
             {:class "btn btn-info"
              :on-click #(.modal
                          (js/$ (str "#hotelActivitylog"
                                     (get-in @data [:cities kth :hotelinformation i :id])))
                          "show")
              :title "Actitvity Log"}
             [:i.fa.fa-book]])
          (when (get-in @data [:cities kth :hotelinformation i :id])
            [button-tooltip
             {:class "btn btn-info"
              :on-click #(.modal
                          (js/$ (str "#hotelPaymentlog"
                                     (get-in @data [:cities kth :hotelinformation i :id])))
                          "show")
              :title "Payment Log"}
             [:i.fa.fa-credit-card]])
          [button-tooltip
           {:class "btn  btn-info"
            :on-click (fn []
                        (let [vendor-id (get-in @data [:cities kth :hotelinformation i :vendorid1])
                              vendor-details (first (filter #(= vendor-id (% :id)) @vendors))]
                          (swap! data assoc :vendor-info vendor-details)
                          (.modal (js/$ "#vendorinfo") "show")))
            :title "Vendor Info"}
           [:i.fa.fa-address-card]]
          [button-tooltip
           {:class "btn btn-info"
            :on-click #(do (reset! index i)
                           (.modal (js/$ "#hotelsremovemodal") "show"))
            :title "Remove Hotel"}
           [:i.fa.fa-trash]]]]]
       #_[:div
          [react-select "Payment-mode" "Select Paymentmode"
           payment-mode-data
           (r/cursor data [:cities kth :hotelinformation i :paymentmode])]
          [input "Vendor Name" "text" "Vendor Name"
           (r/cursor data [:cities kth :hotelinformation i :vendorid])]
          [input-number "amount" "Amount"
           (r/cursor data [:cities kth :hotelinformation i :amount])]]]))])

(defn add-hotel-onclick [data-set kth data]
  (do
    (if (get-in @data-set [:cities kth :hotelinformation])
      (swap! data-set update-in
             [:cities kth :hotelinformation] conj @data)
      (swap! data-set assoc-in [:cities kth :hotelinformation] [@data]))
    (reset! data
            {:cityid (get-in @data-set [:cities kth :cityid])
             :checkin ""
             :checkout ""
             :hotelid 0
             :address ""
             :telephonenumber ""
             :faxnumber ""
             :numerofrooms 0
             :numberofpeoples 0
             :confirmationnumber ""
             :isbreakfastincluded false
             :bookedundername ""
             :paymentmode ""
             :amount 0
             :vendorid ""
             :vendorid1 0})))

(defn add-hotel-selecttag-onchnage [data]
  (fn [input]
    (let [record (js-clj input)]
      (swap! data assoc :hotelid (record :id))
      (swap! data assoc :address (record :address))
      (swap! data assoc :telephonenumber (record :telephonenumber))
      (swap! data assoc :faxnumber (record :faxnumber)))))

(defn add-hotel-information-comp [data-set kth hotels vendors]
  (let [data (r/atom {:cityid (get-in @data-set [:cities kth :cityid])
                      :checkin ""
                      :checkout ""
                      :hotelid 0
                      :address ""
                      :telephonenumber ""
                      :faxnumber ""
                      :numerofrooms 0
                      :numberofpeoples 0
                      :confirmationnumber ""
                      :isbreakfastincluded false
                      :bookedundername ""
                      :paymentmode ""
                      :amount 0
                      :vendorid ""
                      :vendorid1 0})]
    (fn []
      [:div
       [:div.row
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Hotel Name"]
         [react-select-onchange "Hotel name" "Select Hotel Name"
          hotels (get @data :hotelid) (add-hotel-selecttag-onchnage data)]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Address"]
         [input "Address" "text" "Address"
          (r/cursor data [:address])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Tel No"]
         [input "TelNo" "text" "Tel No"
          (r/cursor data [:telephonenumber])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Fax No"]
         [input "fax" "text" "Fax No"
          (r/cursor data [:faxnumber])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Booked Under Name"]
         [input "bookedundername" "text" "Booked Under Name"
          (r/cursor data [:bookedundername])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Confirmation Number"]
         [input "Confirmation#" "text" "Conformation #"
          (r/cursor data [:confirmationnumber])]]]
       [:div.row
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Check-In"]
         [datepicker "Check-In" "dd-MMM-yyyy"
          (r/cursor data [:checkin])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Check-Out"]
         [datepicker "Check-Out" "dd-MMM-yyyy"
          (r/cursor data [:checkout])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "No of Rooms & Peoples"]
         [:div.input-group
          [input-num "NoOfRooms" "text" "No of Rooms"
           (r/cursor data [:numerofrooms])
           {:width "50%"}]
          [input-num "numberofpeople" "text" "No of People"
           (r/cursor data [:numberofpeoples])
           {:width "50%"}]]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:small "Vendor Name"]
         [react-select "vendor-name" "Select Vendor" vendors
          (r/cursor data [:vendorid1])]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:br]
         [input-checkbox "isbreakfastincluded"
          (r/cursor data [:isbreakfastincluded])
          "Is Breakfast Included"]]
        [:div.col-md-2.col-sm-4.col-xs-6
         [:br]
         [:button.btn.btn-default.btn-block
          {:on-click #(add-hotel-onclick data-set kth data)}
          " Add Hotel"]]]])) )

(defn hotel-information-comp [data-set kth ]
  (let [cityid (get-in @data-set [:cities kth :cityid])
        index (r/atom 0)
        hotels (r/atom [])
        vendors (r/atom [])]
    (get-hotels-by-city cityid hotels)
    (get-vendors-by-city-category cityid "hotel" vendors)
    (fn []
      [:div.box.box-default.box-solid
       [:div.box-header.with-border
        [:i.fa.fa-hotel]
        [:h3.box-title "Hotel Information"]
        [:div.box-tools.pull-right
         [:button.btn.btn-box-tool
          {:data-widget "collapse", :type "button"}
          [:i.fa.fa-minus]]]]
       [:div.box-body
        [delete-hotels-modal data-set kth index]
        [show-hotel-information-comp data-set kth hotels index vendors ]]
       [:div.box-footer.clearfix
        [add-hotel-information-comp data-set kth hotels vendors]]])))

;; ---------------------------------------------------
;; Common function for fit & Group City commp
;; ---------------------------------------------------

;; ----------------------------------
;; Remove city functinality
;; ----------------------------------
(defn delete-cities-error-handler [res]
  (do (.modal (js/$ "#cityremovemodal") "hide")
      (case (:status res)
        401 (do (swap! session dissoc :session)
                (pushy/set-token!
                 routes/history
                 (routes/url-for :home)))
        (boot-notify
         "warning" "Warning :"
         "Problem with the server, Please try again"))))

(defn delete-cities-success-handler [data-set index]
  (let [cities (@data-set :cities [])
        indexes (range (count cities))]
    (swap! data-set assoc :cities
           (vec (vals (dissoc (zipmap indexes cities) @index)))))
  (.modal (js/$ "#cityremovemodal") "hide"))

(defn remove-city-on-click [data-set index]
  (let [bookingid (@data-set :id)
        id (get-in @data-set [:cities @index :cityid])]
    (if bookingid
      (ajax/DELETE
       (str server "bookings/"bookingid"/"id)
       {:handler #(delete-cities-success-handler data-set index)
        :error-handler #(delete-cities-error-handler %)
        :format :json
        :response-format :json
        :keywords? true
        :headers
        {:Authorization
         (str "Token " (get-in @session [:session :token]))}})
      (delete-cities-success-handler data-set index))))


(defn delete-city-modal [data index]
  [:div.modal.fade
   {:role "dialog" :id "cityremovemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this City ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @index "Cityremove")
        :type "button"
        :on-click #(remove-city-on-click data index)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

;; --------------------------------------------------
;; add city functionality
;; --------------------------------------------------
(defn filter-city [id cities]
  (filter #(= id (:id %)) cities))

(defn add-city-on-click [data-set selected-city focus]
  (let [add-citydate {:cityid @selected-city
                      :itineraries []
                      :flightsinformation []
                      :hotelinformation []}]
    (if (zero? @selected-city)
      (reset! focus true)
      (do
        (swap! data-set update-in [:cities] conj add-citydate)
        (reset! selected-city 0)
        (reset! focus false)))))

(defn add-city [data-set cities]
  (let [selected-city (r/atom 0)
        focus (r/atom false)]
    (fn []
      [:div.box.box-info
       [:div.box-header
        [:div.form-horizontal
         [:label.col-md-4.control-label "Booking City"]
         [:div.col-md-4
          [react-select "City name"
           "Please select city name"
           cities  selected-city]
          (when @focus
            (when (zero? @selected-city)
              [:div {:style {:color "red"}} "Please Select City"]))]
         [:div.col-md-4
          [:button.btn.btn-default
           {:on-click #(add-city-on-click data-set selected-city focus)} "Add City"]]]]])))

;; --------------------------------------------------
;; Fit and Group bookings City Comp
;; --------------------------------------------------
(declare bookings-validator)


(defn fit-group-topfields-comp [data-set state logos quotations ]
  [:div.row
   [:div.col-md-6
    [row-chosen-select "Select Logo" "SelectLogo" :logoid logos
     (r/cursor data-set [:logoid]) (@state :focus)
     (bookings-validator @data-set)]
    [row-input "Booking Number" "Booking Number" :bookingnumber
     "text" "Enter Booking Number" (r/cursor data-set [:bookingnumber])
     (@state :focus) (bookings-validator @data-set)]
    [row-select-quotation "Quotation Id" "quotationid" :quotationid
     quotations data-set (@state :focus) (bookings-validator @data-set)]
    [:div.form-group
     [:label.col-md-4.control-label  "7m Booking Agent"]
     [:div.col-md-6 {:style {:padding-top "7px"}}
      [:strong (get (first (filter
                            #(= (@data-set :quotationid)
                                (str (:name %)" "(:id %)))
                            @quotations)) :agentname "")]]]
    [row-input "Emergency Name/No." "emergencynumbers"
     :emergencynumbers "text" "Enter Emergency Name/Numbers"
     (r/cursor data-set [:emergencynumbers])
     (@state :focus) (bookings-validator @data-set)]
    [:div.form-group
     [:div.col-md-4]
     [:div.col-md-6
      [input-checkbox :isfinalbooking
       (r/cursor data-set [:isfinalbooking])
       "Is Final Booking"]]]]
   [:div.col-md-6
    [row-input "Travel Agent Name" "agentname" :agentname "text"
     "Agent Name" (r/cursor data-set [:agentname])
     (@state :focus) (bookings-validator @data-set)]
    [row-input "Guest Name" "guestname" :guestname "text"
     "Guest Name" (r/cursor data-set [:guestname])
     (@state :focus) (bookings-validator @data-set)]
    [row-input "Email" "email" :email "text" "Email id"
     (r/cursor data-set [:email])
     (@state :focus) (bookings-validator @data-set)]
    [row-input "No. of PAX" "noofpax" :noofpax "text"
     "No. of PAX (child/Adult/Tour Managers)"
     (r/cursor data-set [:noofpax])
     (@state :focus) (bookings-validator @data-set)]
    [row-input "Tour Manager(s)" "tourmanagers" :nameoftourmanager
     "text" "Tour Manager(s)" (r/cursor data-set [:nameoftourmanager])
     (@state :focus) (bookings-validator @data-set)]
    [row-text-area "Remarks" "Remarks"
     :remarks 3 "Enter Remarks"
     (r/cursor data-set [:remarks])
     (@state :focus) (bookings-validator @data-set)]]])

(defn fit-group-show-city [type data-set kth cities cityindex]
  (let [cityid (get-in @data-set [:cities kth :cityid])]
    [:div.box.box-info
     [:div.box-header.with-border
      [:span.label.label-warning
       {:style {:font-size 20}}
       (str (get (first (filter-city cityid @cities)) :name ""))]
      [:div.box-tools.pull-right
       [:button.btn.btn-danger.btn-xs
        {:type "button"
         :on-click #(do (reset! cityindex kth)
                        (.modal (js/$ "#cityremovemodal") "show"))}
        " Remove City"] " "
       [:button.btn.btn-box-tool
        {:data-widget "collapse", :type "button"}
        [:i.fa.fa-minus]]]]
     [:div.box-body
      ;; [:p (str @data-set)]
      [activity/vendor-info "vendorinfo" data-set]
      [flight-information-comp data-set kth]
      [hotel-information-comp data-set kth ]
      [itinerary-comp type data-set kth ]]]))

;; ---------------------------------------------------------
;; Fit & Group Common Functions
;; ---------------------------------------------------------
(defn bookings-validator [data-set]
  (first
   (b/validate
    data-set
    :quotationid [[v/required :message "Field is required"]]
    :email [[v/required :message "Field is required"]
            [v/email :message "Plese enter valid email"]]
    :agentname [[v/required :message "Field is required"]]
    :guestname [[v/required :message "Field is required"]]
    :logoid [[v/required :message "Plese select logo"]]
    :bookingnumber [[v/required :message "Field is required"]]
    [:cities 0 :cityid] [[v/required :message "Field is required"]]
    [:cities 0 :itineraries 0 :cityid] [[v/required :message "Field is required"]]
    )))

(defn destruct-data-create-send [data-set]
  (let [quotation-info (str/split (@data-set :quotationid) #" ")]
    (loop [itineraries []
           flights []
           hotels []
           coll (@data-set :cities)]
      (if (empty? coll)
        {:quotationid (js/parseInt (second quotation-info))
         :qrnumber (first quotation-info)
         :bookingnumber (@data-set :bookingnumber)
         :email (@data-set :email)
         :agentname (@data-set :agentname)
         :guestname (@data-set :guestname)
         :emergencynumbers (@data-set :emergencynumbers)
         :isfinalbooking (@data-set :isfinalbooking)
         :termsandconditions (@data-set :termsandconditions)
         :logoid (@data-set :logoid)
         :noofpax (@data-set :noofpax "")
         :nameoftourmanager (@data-set :nameoftourmanager "")
         :remarks (@data-set :remarks "")
         :itinerarydetails itineraries
         :flightinformation flights
         :hotelinformation hotels}
        (recur
         (into itineraries ((first coll) :itineraries))
         (into flights ((first coll) :flightsinformation))
         (into hotels ((first coll) :hotelinformation))
         (rest coll))))))

(defn destruct-data-edit-send [data-set]
  (let [quotation-info (str/split (@data-set :quotationid) #" ")]
    (loop [itineraries []
           flights []
           hotels []
           coll (@data-set :cities)]
      (if (empty? coll)
        {:id (@data-set :id)
         :quotationid (js/parseInt (second quotation-info))
         :qrnumber (first quotation-info)
         :bookingnumber (@data-set :bookingnumber)
         :email (@data-set :email)
         :agentname (@data-set :agentname)
         :guestname (@data-set :guestname)
         :emergencynumbers (@data-set :emergencynumbers)
         :isfinalbooking (@data-set :isfinalbooking)
         :termsandconditions (@data-set :termsandconditions)
         :logoid (@data-set :logoid)
         :noofpax (@data-set :noofpax "")
         :nameoftourmanager (@data-set :nameoftourmanager "")
         :remarks (@data-set :remarks "")
         :itinerarydetails itineraries
         :flightinformation flights
         :hotelinformation hotels}
        (recur
         (into itineraries ((first coll) :itineraries))
         (into flights ((first coll) :flightsinformation))
         (into hotels ((first coll) :hotelinformation))
         (rest coll))))))

(defn combine-type [type1 type2 data]
  (mapv #(assoc % :type type1) (get data type2)))

(defn de-struct-cities [data]
  (mapv #(assoc
          (group-by :type (second %))
          :cityid (first %))
        (into [] (group-by
                  :cityid
                  (reduce into
                          [(combine-type :itineraries :itinerarydetails data)
                           (combine-type :hotelinformation :hotelinformation data)
                           (combine-type :flightsinformation :flightinformation data)])))))

(defn bookings-get-data-destruct-for-edit [data]
  (let [booking (get-in data [:booking 0])
        cities (de-struct-cities data)]
    {:id (booking :id)
     :quotationid (str (booking :qrnumber) " " (booking :quotationid))
     :bookingnumber (booking :bookingnumber "")
     :email (booking :email "")
     :agentname (booking :agentname "")
     :guestname (booking :guestname "")
     :logoid (booking :logoid)
     :emergencynumbers (booking :emergencynumbers "")
     :isfinalbooking (booking :isfinalbooking "")
     :termsandconditions (booking :termsandconditions "")
     :noofpax (booking :noofpax "")
     :nameoftourmanager (booking :nameoftourmanager "")
     :remarks (booking :remarks "")
     :cities cities
     :pdf-mail-data
     {:bookingnumber (booking :bookingnumber "")
      :agentname (booking :agentname "")
      :emailid (booking :email "")}}))

(defn booking-generate-onclick-error [res state data-set]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do (swap! state assoc :focus false)
        (swap! state assoc :busy false)
        (boot-notify "warning" "Warning :" "Problem with the sever, Please try again"))))

(defn get-saved-booking-data-success [response data-set state]
  (let [destructed-data (bookings-get-data-destruct-for-edit response)
        pdf-data {:bookingnumber (destructed-data :bookingnumber "")
                  :agentname (destructed-data :agentname)
                  :emailid (destructed-data :email)
                  :id (destructed-data :id)}
        saved-data (assoc destructed-data :pdf-mail-data pdf-data)]
    (reset! data-set saved-data)
    (swap! state assoc :focus false)
    (swap! state assoc :busy false)
    (boot-notify "success" "Success :"
                 "Itinerary Booking is successfully saved")))

(defn get-saved-booking-data [res data-set state]
  (let [id (get res :id "")]
    (ajax/GET
     (str server "bookings/" id)
     {:handler #(get-saved-booking-data-success % data-set state)
      :error-handler #(booking-generate-onclick-error % state data-set)
      :format :json
      :response-format :json
      :keywords? true
      :headers
      {:Authorization
       (str "Token " (get-in @session [:session :token]))}})))

(defn booking-generate-onclick [data-set state]
  (if (nil? (bookings-validator @data-set))
    (if (@data-set :id)
      (do
        (swap! data-set assoc :pdf-mail-data {})
        (ajax/PUT
         (str server "bookings/" (@data-set :id))
         {:params (destruct-data-edit-send data-set)
          :handler #(get-saved-booking-data % data-set state)
          :error-handler #(booking-generate-onclick-error  % state data-set)
          :format :json
          :response-format :json
          :keywords? true
          :headers
          {:Authorization
           (str "Token " (get-in @session [:session :token]))}}))
      (do
        (swap! data-set assoc :pdf-mail-data {})
        (ajax/POST
         (str server "bookings")
         {:params (destruct-data-create-send data-set)
          :handler #(get-saved-booking-data % data-set state)
          :error-handler #(booking-generate-onclick-error  % state data-set)
          :format :json
          :response-format :json
          :keywords? true
          :headers
          {:Authorization
           (str "Token " (get-in @session [:session :token]))}})))
    (do
      (swap! state assoc :focus true)
      (boot-notify "warning" "Warning :"
                   "Please choose atleast one city with one
                    itinerary and enter valid fields"))))

(defn show-pdf [data]
  (.open
   js/window
   (str server "bookings/pdf/"
        (get-in @data [:pdf-mail-data :bookingnumber]))))


(defn bookings-sendmail-success [res state]
  (do (swap! state assoc :busy false)
      (boot-notify "success" "Success :" "Mail successfully sent")))

(defn bookings-sendmail-error [res state]
  (do (swap! state assoc :busy false)
      (boot-notify "warning" "Warning :"
                   "Error in sending mail, please try again")))

(defn bookings-sendmail-onclick [data-set state]
  (let [send-data (@data-set :pdf-mail-data)]
    (do
      (swap! state assoc :busy true)
      (ajax/POST
       (str server "bookings/pdf/email")
       {:params send-data
        :handler #(bookings-sendmail-success % state)
        :error-handler #(bookings-sendmail-error % state)
        :format :json
        :response-format :json
        :keywords? true
        :headers
        {:Authorization
         (str "Token " (get-in @session [:session :token]))}}))))

;; -----------------------------------------------
;; FIT GRID and Form bookings components
;; -----------------------------------------------
;; Fit bookings Grid
;; -----------------------------------------------

(def fit-search-options
  [{:id "agentname" :name "Agent Name"}
   {:id "quotationnumber" :name "Quotation Number"}
   {:id "bookingnumber" :name "Booking Number"}])

(defn delete-fit-bookings-on-click [id src-in list]
  (ajax/DELETE
   (str server "bookings/" @id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))


(defn delete-fit-bookings-modal [id src-in list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Booking ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-fit-bookings-on-click id src-in list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])


(defn fitbookings-table-comp []
  (let [src-in (r/atom {:url "fit/bookings/search"
                        :idx 1
                        :type "agentname"
                        :text ""})
        list (r/atom {:data []
                      :totalbookings 0})
        remove-id (r/atom 0)]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-list]
        [:h3.box-title "List of FIT Bookings"]
        [:div.box-tools.pull-right
         [:button.btn.btn-primary.btn-sm
          {:on-click
           #(do (reset! fitbookings-atom
                        {:bookingnumber "BK-00"
                         :isfinalbooking false
                         :emergencynumbers ""
                         :termsandconditions terms-and-conditions
                         :cities []
                         :pdf-mail-data {}})
                (pushy/set-token!
                 routes/history
                 (routes/url-for :add-fitbooking)))}
          "Add New FIT Booking"]]]
       [:div.box-body
        [delete-fit-bookings-modal remove-id src-in list]
        [:div.row
         [:div.col-md-6.col-md-offset-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-select "fitsearchselect"
            fit-search-options (r/cursor src-in [:type])]
           [search-input "fitsearchsrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            [:th ""]
            [:th "Booking Number"]
            [:th "Quotation Number"]
            [:th "Agent Name"]
            [:th "Date"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              [:td
               [:div.row
                [:div.col-md-8.col-md-offset-2.btn-group
                 [button-tooltip
                  {:class "btn btn-info"
                   :on-click #(do
                                (reset! remove-id (:id i))
                                (.modal (js/$ "#removemodal") "show"))
                   :title "Delete Booking"}
                  [:i.fa.fa-trash]]
                 [anchor-tooltip
                  {:class "btn btn-info"
                   :href (routes/url-for :edit-fitbooking :id (:id i))
                   :title "Edit Booking"}
                  [:i.fa.fa-edit]]
                 [anchor-tooltip
                  {:class "btn btn-info"
                   :href (routes/url-for :fit-activitylog :id (:id i))
                   :title "Activity Log"}
                  [:i.fa.fa-book]]
                 [anchor-tooltip
                  {:class "btn btn-info"
                   :href (routes/url-for :fit-paymentlog :id (:id i))
                   :title "Payment Log"}
                  [:i.fa.fa-credit-card]]]]]
              [:td (:bookingnumber i)]
              [:td (:qrnumber i)]
              [:td (:agentname i)]
              [:td (:createdatetime i)]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalbookings) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do
                       (swap! src-in assoc :idx  %)
                       (get-table-data src-in list))}]]])))

;; ADD or EDIT FORM For FIT bookings
;; ---------------------------------------------------
(def fitbookings-atom
  (r/atom {:bookingnumber "BK-00"
           :isfinalbooking false
           :emergencynumbers ""
           :termsandconditions terms-and-conditions
           :cities []
           :pdf-mail-data {}}))

(defn get-all-fitquotations [quotations]
  (ajax/GET
   (str server "fit/quotations")
   {:handler #(reset! quotations %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn add-or-edit-fitbooking [data-set]
  (let [quotations (r/atom [])
        cities (r/atom [])
        logos (r/atom [])
        state (r/atom {:focus false :busy false})
        cityindex (r/atom 0)]
    (get-all-fitquotations quotations)
    (get-all-cities cities)
    (get-all-logos logos)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:span.label.label-default
         {:style {:font-size 20}}
         (str (if (@data-set :id)
                "EDIT " "ADD ")
              "FIT BOOKING")]
        [:div.box-tools.pull-right
         [:div.btn-group
          (when (@data-set :id)
            [:button.btn.btn-primary.btn-sm
             {:on-click #(reset!
                          fitbookings-atom
                          {:bookingnumber "BK-00"
                           :isfinalbooking false
                           :emergencynumbers ""
                           :termsandconditions terms-and-conditions
                           :cities []
                           :pdf-mail-data {}})}
             "Add New Booking"])
          [:a.btn.btn-default.btn-sm.pull-right
           {:href (routes/url-for :fitbookings)}
           "Back to Bookings"]]]]
       [:div.box-body
        ;; [:p (str (@data-set :cities))]
        [:div.form-horizontal
         [fit-group-topfields-comp data-set state logos quotations]
         [delete-city-modal data-set cityindex]]
        (doall
         (for [i  (range (count (@data-set :cities [])))]
           ^{:key i}
           [fit-group-show-city "fit" data-set i cities cityindex]))
        [add-city data-set cities]
        [row-text-area-terms&cond "Terms and conditions" "Terms and conditions"
         :termsandconditions 5 "Enter Terms and conditions"
         (r/cursor data-set [:termsandconditions])
         (@state :focus) (bookings-validator @data-set)]]
       [:div.box-footer.clearfix
        ;; [:p (str @data-set)]
        [:center
         [:button.btn.btn-primary
          {:on-click #(booking-generate-onclick data-set state)}
          (str  (if (@data-set :id)
                  "Save "
                  "Generate ") "Booking")]" "
         [:a.btn.btn-default {:href (routes/url-for :fitbookings)} "Back"]" "
         (when (seq (get-in @data-set [:pdf-mail-data :bookingnumber] ""))
           [:button.btn.btn-success
            {:on-click #(show-pdf data-set)}
            " View PDF"])" "
         (when (seq (get-in @data-set [:pdf-mail-data :emailid] ""))
           [:button.btn.btn-info
            {:on-click #(bookings-sendmail-onclick data-set state)}
            [:i.fa.fa-envelope] " Send Mail "
            (when (get @state :busy)
              [:i.fa.fa-spin.fa-refresh])])]]])))

;; --------------------------------------------
;; Group GRID and Form Bookings comp
;; --------------------------------------------
;; Group Grid component
;; --------------------------------------------
(defn delete-group-bookings-on-click [id src-in list]
  (ajax/DELETE
   (str server "bookings/" @id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-group-bookings-modal [id src-in list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Booking ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-group-bookings-on-click id src-in list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn groupbookings-table-comp []
  (let [src-in (r/atom {:url "groups/bookings/search"
                        :idx 1
                        :type "agentname"
                        :text ""})
        list (r/atom {:data []
                      :totalbookings 0})
        remove-id (r/atom 0)]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-list]
        [:h3.box-title "List of Group Bookings"]
        [:div.box-tools.pull-right
         [:button.btn.btn-primary.btn-sm
          {:id "Addnewgroup"
           :on-click #(do (reset!
                           groupbookings-atom
                           {:bookingnumber "BK-00"
                            :isfinalbooking false
                            :emergencynumbers ""
                            :termsandconditions terms-and-conditions
                            :cities []
                            :pdf-mail-data {}})
                          (pushy/set-token!
                           routes/history
                           (routes/url-for :add-groupbooking)))}
          "Add New Group Booking"]]]
       [:div.box-body
        [delete-group-bookings-modal remove-id src-in list]
        [:div.row
         [:div.col-md-6.col-md-offset-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-select "groupsearchselect"
            fit-search-options (r/cursor src-in [:type])]
           [search-input "groupsearchsrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            [:th ""]
            [:th "Booking Number"]
            [:th "Quotation Number"]
            [:th "Agent Name"]
            [:th "Date"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              [:td
               [:div.row
                [:div.col-md-8.col-md-offset-2.btn-group
                 [button-tooltip
                  {:class "btn btn-info"
                   :on-click #(do
                                (reset! remove-id (:id i))
                                (.modal (js/$ "#removemodal") "show"))
                   :title "Delete Booking"}
                  [:i.fa.fa-trash]]
                 [anchor-tooltip
                  {:class "btn btn-info"
                   :href (routes/url-for :edit-groupbooking :id (:id i))
                   :title "Edit Booking"}
                  [:i.fa.fa-edit]]
                 [anchor-tooltip
                  {:class "btn btn-info"
                   :href (routes/url-for :group-activitylog :id (:id i))
                   :title "Activity Log"}
                  [:i.fa.fa-book]]
                 [anchor-tooltip
                  {:class "btn btn-info"
                   :href (routes/url-for :group-activitylog :id (:id i))
                   :title "Payment Log"}
                  [:i.fa.fa-credit-card]]]]]
              [:td (:bookingnumber i)]
              [:td (:qrnumber i)]
              [:td (:agentname i)]
              [:td (:createdatetime i)]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalbookings) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do
                       (swap! src-in assoc :idx %)
                       (get-table-data src-in list))}]]])))

;; ADD or EDIT Form for Group Bookings comp
;; -----------------------------------------
(def groupbookings-atom
  (r/atom {:bookingnumber "BK-00"
           :isfinalbooking false
           :emergencynumbers ""
           :termsandconditions terms-and-conditions
           :cities []
           :pdf-mail-data {}}))

(defn get-all-groupquotations [quotations]
  (ajax/GET
   (str server "groups/quotations")
   {:handler #(reset! quotations %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn add-or-edit-groupbooking [data-set]
  (let [quotations (r/atom [])
        cities (r/atom [])
        logos (r/atom [])
        state (r/atom {:focus false :busy false})
        cityindex  (r/atom 0)]
    (get-all-groupquotations quotations)
    (get-all-cities cities)
    (get-all-logos logos)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:span.label.label-default
         {:style {:font-size 20}}
         (str (if (@data-set :id)
                "EDIT "
                "ADD ")
              "GROUP  BOOKING")]
        [:div.box-tools.pull-right
         [:div.btn-group
          (when (@data-set :id)
            [:button.btn.btn-primary.btn-sm
             {:on-click #(reset!
                          groupbookings-atom
                          {:bookingnumber "BK-00"
                           :isfinalbooking false
                           :emergencynumbers ""
                           :termsandconditions terms-and-conditions
                           :cities []
                           :pdf-mail-data {}})}
             "Add New Booking"])
          [:a.btn.btn-default.pull-right.btn-sm
           {:href (routes/url-for :groupbookings)}
           "Back to Bookings"]]]]
       [:div.box-body
        ;; [:p (str @data-set)]
        [:div.form-horizontal
         [fit-group-topfields-comp data-set state logos quotations]
         [delete-city-modal data-set cityindex]]
        (doall
         (for [i  (range (count (@data-set :cities [])))]
           ^{:key i}
           [fit-group-show-city "group" data-set i cities cityindex]))
        [add-city data-set cities]
        [row-text-area-terms&cond "Terms and conditions" "Terms and conditions"
         :termsandconditions 5 "Enter Terms and conditions"
         (r/cursor data-set [:termsandconditions])
         (@state :focus) (bookings-validator @data-set)]]
       [:div.box-footer.clearfix
        [:center
         [:button.btn.btn-primary
          {:on-click #(booking-generate-onclick data-set state)}
          (str  (if (@data-set :id)
                  "Save "
                  "Generate ") "Booking")]" "
         [:a.btn.btn-default {:href (routes/url-for :groupbookings)} "Back"]" "
         (when (seq (get-in @data-set [:pdf-mail-data :bookingnumber] ""))
           [:button.btn.btn-success
            {:on-click #(show-pdf data-set)}
            " View PDF"]) " "
         (when (seq (get-in @data-set [:pdf-mail-data :emailid] ""))
           [:button.btn.btn-info
            {:on-click #(bookings-sendmail-onclick data-set state)}
            [:i.fa.fa-envelope] " Send Mail "
            (when (get @state :busy)
              [:i.fa.fa-spin.fa-refresh])])]]])))

;; -----------------------------------------------
;; cities component
;; -----------------------------------------------
(defn city-validator [data-set]
  (first
   (b/validate
    data-set
    :cityname [[v/required :message "Field is required"]])))

(defn get-cities-list [idx list]
  (ajax/GET
   (str server "cities/" @idx "/10")
   {:handler #(do (reset! list %)
                  (.modal (js/$ "#removemodal") "hide"))
    :error-handler delete-table-error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-cities-handler [res idx list]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    200 (get-cities-list idx list)
    (boot-notify
     "warning" "Warning :"
     "Problem with the server, Please try again")))

(defn delete-cities-on-click [id idx list]
  (ajax/DELETE
   (str server "cities/" @id)
   {:handler #(delete-cities-handler % idx list)
    :error-handler #(delete-cities-handler % idx list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

;; Add Edit City ajax Calls
(defn city-save-onclick-success [res data idx list]
  (reset! data {})
  (get-cities-list idx list)
  (boot-notify "success" "Success :" "City is successfully saved"))

(defn city-save-onclick-error [res]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (boot-notify "success" "Success :" "Please try again")))

(defn city-save-onclick [data idx list]
  (when (seq (@data :cityname))
    (if (@data :id)
      (do
        (ajax/PUT
         (str server "cities/" (@data :id))
         {:params @data
          :handler #(city-save-onclick-success % data idx list)
          :error-handler #(city-save-onclick-error %)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}}))
      (do
        (ajax/POST
         (str server "cities")
         {:params @data
          :handler #(city-save-onclick-success % data idx list)
          :error-handler #(city-save-onclick-error %)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}})))))

(defn add-city-comp [idx list]
  (let [data (r/atom {})]
    (fn []
      [:div.col-md-6.col-md-offset-6
       [:div.form-group
        [:div.input-group
         [:span.input-group-btn
          [:button.btn.btn-primary
           {:on-click #(city-save-onclick data idx list)}
           "Add New City"]]
         [:input.form-control
          {:placeholder "Enter City Name to add"
           :value (@data :cityname)
           :on-change #(swap! data assoc :cityname (.-target.value %))
           :on-key-press #(when (= 13 (.-charCode %))
                            (city-save-onclick data idx list))}]]]])))


(defn delete-city-table-modal [id idx list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this City ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-cities-on-click id idx list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn cities-comp []
  (let [role (get-in @session [:session :user :role])
        idx (r/atom 1)
        list (r/atom {:data []
                      :totalcities 0})
        city (r/atom {})
        remove-id (r/atom 0)]
    (get-cities-list idx list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-list]
        [:h3.box-title "List of Cities"]]
       [:div.box-body
        [delete-city-table-modal remove-id idx list]
        (when (= role "bookadmin")
          [:div.row [add-city-comp idx list]])
        [:div.table-responsive
         ;; [:p (str @city)]
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            (when (= role "bookadmin")
              [:th {:style {:width "100px"}} ""])
            (when (= role "bookadmin")
              [:th {:style {:width "100px"}} ""])
            [:th "City name"]]]
          [:tbody
           (doall
            (for [i (@list :data)]
              ^{:key (:id i)}
              [:tr
               (when (= role "bookadmin")
                 [:td [button-tooltip
                       {:class "btn btn-danger btn-sm"
                        :on-click #(do
                                     (reset! remove-id (:id i))
                                     (.modal (js/$ "#removemodal") "show"))
                        :title "Delete City"}
                       [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]])
               (when (= role "bookadmin")
                 [:td (if (= (i :id) (get @city :id))
                        [:button.btn.btn-success.btn-sm
                         {:on-click #(city-save-onclick city idx list)
                          :title "Edit City"} "Save"]
                        [:button.btn.btn-default.btn-sm
                         {:on-click #(reset! city i)
                          :title "Edit City"}
                         [:span.glyphicon.glyphicon-edit {:aria-hidden true}]])])
               [:td
                (if (= (i :id) (get @city :id))
                  [:input.form-control
                   {:value (@city :cityname)
                    :placeholder "Enter City Name"
                    :on-change #(swap! city assoc :cityname (.-target.value %))
                    :on-key-press #(when (= 13 (.-charCode %))
                                     (city-save-onclick city idx list))}]
                  [:span (:cityname i)])]]))]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalcities) 10)))
          :maxButtons 5
          :activePage @idx
          :onSelect #(do
                       (reset! idx %)
                       (get-cities-list idx list))}]]])))

;; (defn city-save-onclick-success [res data state]
;;   (reset! data {})
;;   (swap! state assoc :focus false)
;;   (boot-notify "success" "Success :" "City is successfully saved"))

;; (defn city-save-onclick-error [res state]
;;   (case (res :status)
;;     401 (do (swap! session dissoc :session)
;;             (pushy/set-token! routes/history (routes/url-for :home)))
;;     (do
;;       (swap! state assoc :focus false)
;;       (boot-notify "success" "Success :" "Please try again"))))

;; (defn city-save-onclick [data state]
;;   (if (nil? (city-validator @data))
;;     (if (@data :id)
;;       (do
;;         (ajax/PUT
;;          (str server "cities/" (@data :id))
;;          {:params @data
;;           :handler #(pushy/set-token!
;;                      routes/history (routes/url-for :cities))
;;           :error-handler #(city-save-onclick-error % state)
;;           :format :json
;;           :response-format :json
;;           :keywords? true
;;           :headers {:Authorization
;;                     (str "Token " (get-in @session [:session :token]))}}))
;;       (do
;;         (ajax/POST
;;          (str server "cities")
;;          {:params @data
;;           :handler #(city-save-onclick-success % data state)
;;           :error-handler #(city-save-onclick-error % state)
;;           :format :json
;;           :response-format :json
;;           :keywords? true
;;           :headers {:Authorization
;;                     (str "Token " (get-in @session [:session :token]))}})))
;;     (swap! state assoc :focus true)))


(defn add-or-edit-city-comp [data]
  (let [data (r/atom data)
        state (r/atom
               {:focus false
                :busy false})]
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-building]
        [:h3.box-title  (if (@data :id) "Edit " "Add ") " City"]]
       #_[:div.box-body
          [:div..form-horizontal
           [row-input "City Name" "City Name" :cityname "text" "Enter City Name"
            (r/cursor data [:cityname]) (@state :focus) (city-validator @data)]]]
       #_[:div.box-footer.clearfix
          [:div.col-md-3]
          [:div.col-md-6
           [:center
            [:button.btn.btn-primary
             {:on-click #(city-save-onclick data state)} "Save"]"  "
            [:a.btn.btn-default
             {:href (routes/url-for :cities)} "Back"]]]
          [:div.col-md-3]]])))

;; -------------------------------------------------------
;; items comp
;; -------------------------------------------------------

(defn select-valid [value]
  (not (zero? (js/parseInt value))))

(defn items-validator [data-set]
  (first
   (b/validate
    data-set
    :cityid [[v/required :message "Field is required"]
             [select-valid :message "Please select city"]]
    :vendorid [[v/required :message "Field is required"]]
    :type [[v/required :message "Field is required"]]
    :category [[v/required :message "Field is required"]]
    :itemname [[v/required :message "Field is required"]]
    ;; :adultcost [[v/required :message "Field is required"]]
    ;; :childcost [[v/required :message "Field is required"]]
    ;; :description [[v/required :message "Field is required"]]
    )))

(def items-search-options
  [{:id "city" :name "City"}
   {:id "itemname" :name "Item Name"}
   {:id "vendorname" :name "Vendors"}
   {:id "category" :name "Category"}
   {:id "description" :name "Item Description"}])

(defn delete-item-on-click [id src-in list]
  (ajax/DELETE
   (str server "items/" @id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-activity-table-modal [id src-in list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Activity ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-item-on-click id src-in list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn items-comp []
  (let [role (get-in @session [:session :user :role])
        src-in (r/atom {:url "items/search"
                        :idx 1
                        :type "city"
                        :text ""})
        list (r/atom {:data []
                      :totalitems 0})
        remove-id (r/atom 0)]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-list]
        [:h3.box-title "List of Activities"]
        (when (= role "bookadmin")
          [:div.box-tools.pull-right
           [:a.btn.btn-primary.btn-sm
            {:href (routes/url-for :add-item)}
            "Add New Activity"]])]
       [:div.box-body
        [delete-activity-table-modal remove-id src-in list]
        [:div.row
         [:div.col-md-6
          [:a
           {:id "Downloadcsv"
            :href (str server "excel/activities.xlsx?token="
                       (get-in @session [:session :token]))}
           "Export Complete Activities into Excel File"]]
         [:div.col-md-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-select "itemsselect"
            items-search-options (r/cursor src-in [:type])]
           [search-input "itemssrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            (when (= role "bookadmin")
              [:th ""])
            (when (= role "bookadmin")
              [:th ""])
            [:th ""]
            [:th "City"]
            [:th "Vendors"]
            [:th "Activity Name"]
            [:th "Catagory"]
            ;; [:th "Adult/Child Cost"]
            [:th "Activity Description"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              (when (= role "bookadmin")
                [:td [button-tooltip
                      {:class "btn btn-danger btn-sm"
                       :on-click #(do
                                    (reset! remove-id (:id i))
                                    (.modal (js/$ "#removemodal") "show"))
                       :title "Delete Actitvity"}
                      [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]])
              (when (= role "bookadmin")
                [:td [anchor-tooltip
                      {:class "btn btn-success btn-sm"
                       :href (routes/url-for :edit-item :id (:id i))
                       :title "Edit Actitvity"}
                      [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]])
              [:td [:a.btn.btn-info.btn-sm
                    {:href (routes/url-for :view-item :id (:id i))}
                    "View"]]
              [:td (:cityid i)]
              [:td (:vendorid i)]
              [:td (:itemname i)]
              [:td (:category i)]
              ;; [:td (str (:adultcost i) " / "
              ;;           (:childcost i))]
              [:td (:description i)]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalitems) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do
                       (swap! src-in assoc :idx  %)
                       (get-table-data src-in list))}]]])))


(defn item-save-onclick-success [res data state]
  (reset! data {:adultcost 0
                :childcost 0
                :description ""
                :type "Both"
                :activitytitle ""
                :subcategory ""})
  (swap! state assoc :focus false)
  (boot-notify "success" "Success :" "Activity is successfully saved"))

(defn item-save-onclick-error [res state]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do (swap! state assoc :focus false)
        (boot-notify "warning" "warning :" "Please try again"))))

(defn item-save-onclick [data state]
  (if (nil? (items-validator @data))
    (if (@data :id)
      (do
        (ajax/PUT
         (str server "items/" (@data :id))
         {:params @data
          :handler #(pushy/set-token!
                     routes/history (routes/url-for :items))
          :error-handler #(item-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}}))
      (do
        (ajax/POST
         (str server "items")
         {:params @data
          :handler #(item-save-onclick-success % data state)
          :error-handler #(item-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}})))
    (swap! state assoc :focus true)))

(defn city-onchange [data vendors]
  (fn [input]
    (let [record (js-clj input)]
      (swap! data assoc :cityid (record :id))
      (swap! data assoc :category "")
      (swap! data assoc :vendorid "")
      ;; (get-vendors-by-city (record :id) vendors)
      )))

(defn vendor-onchange [data]
  (fn [input]
    (let [record (js-clj input)]
      (swap! data assoc :vendorid (record :id))
      (swap! data assoc :description (record :description))
      (swap! data assoc :itemname (record :name)))))

;; (defn sub-category-onchange [data]
;;   (fn [input]
;;     (let [value (.-target.value input)]
;;       (swap! data assoc :subcategory value)
;;       (swap! data assoc :activitytitle value))))

(defn category-onchange [data vendors]
  (fn [input]
    (let [cityid (@data :cityid)
          record (js-clj input)]
      (swap! data assoc :category (record :id))
      (get-vendors-by-city-category cityid (record :id) vendors)
      (case (record :id)
        "meals" (do (swap! data assoc :description "%%")
                    (swap! data assoc :activitytitle "%%"))
        (do (swap! data assoc :description "")
            (swap! data assoc :activitytitle ""))))))

(defn add-or-edit-item-comp [data]
  (let [data (r/atom data)
        state (r/atom {:focus false
                       :busy false})
        cities (r/atom [])
        vendors (r/atom [])]
    (get-all-cities cities)
    (when (@data :cityid)
      (get-vendors-by-city-category (@data :cityid) (@data :category) vendors))
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-book]
        [:h3.box-title (if (@data :id) "Edit " "Add ") " Activity"]]
       [:div.box-body
        ;; [:p (str @data)]
        [:div.form-horizontal
         [:div.callout.callout-default
          [:h4 " Note : Activity Title and Description for Sub-category"]
          [:p "In Order to differentiate Title and Description for Sub-category
               in Category, Write title and Description with %% instead of sub-category.
               We will replace this %% with subcategory in bookings"]]
         [row-react-select-onchange "City" "City" :cityid cities
          (get @data :cityid nil) (@state :focus) (items-validator @data)
          (city-onchange data vendors)]
         [row-react-select-onchange "Category" "Select Category"
          :category categories-atom (get @data :category "")
          (@state :focus) (items-validator @data) (category-onchange data vendors)]
         #_(when (= "Meals" (@data :category))
             [row-select-onchnage "Sub-Category" "Sub Category" :subcategory
              sub-categories (get @data :subcategory) (@state :focus)
              (items-validator @data) (sub-category-onchange data)])
         [row-react-select-onchange "Vendor" "Vendor" :vendorid vendors
          (get @data :vendorid "") (@state :focus) (items-validator @data)
          (vendor-onchange data)]
         [row-select "Type" "Type" :type ["FIT" "Group" "Both"]
          (r/cursor data [:type]) (@state :focus) (items-validator @data)]
         [row-input "Activity Name" "Activity Name" :itemname "text" "Enter Activity Name"
          (r/cursor data [:itemname]) (@state :focus) (items-validator @data)]
         [row-input "Activity Title" "Activity Title" :activitytitle "text" "Enter Activity Title"
          (r/cursor data [:activitytitle]) (@state :focus) (items-validator @data)]
         [row-text-area "Activity Description" "Activity Description" :description 5
          "Enter Activity Description"
          (r/cursor data [:description]) (@state :focus) (items-validator @data)]]
        ;; [row-number "Adult Cost" "adultcost" :adultcost
        ;;  "Enter Adult Cost" (r/cursor data [:adultcost])
        ;;  (@state :focus) (items-validator @data)]
        ;; [row-number "Child Cost" "childcost" :childcost
        ;;  "Enter Child Cost" (r/cursor data [:childcost])
        ;;  (@state :focus) (items-validator @data)]
        ]
       [:div.box-footer.clearfix
        [:div.col-md-3]
        [:div.col-md-6
         [:center
          (when-not (= (routes/url-for :view-item :id (@data :id 0))
                       (pushy/get-token routes/history))
            [:button.btn.btn-primary
             {:on-click #(item-save-onclick data state)} "Save"])"  "
          [:a.btn.btn-default
           {:href (routes/url-for :items)} "Back"]]]
        [:div.col-md-3]]])))

;; -----------------------------------------------------------
;; Hotels Comp
;; -----------------------------------------------------------
(defn hotel-validator [data-set]
  (first
   (b/validate
    data-set
    :cityid [[v/required :message "Field is required"]]
    :category [[v/required :message "Field is required"]]
    :hotelname [[v/required :message "Field is required"]]
    ;; :address [[v/required :message "Field is required"]]
    ;; :phonenumber [[v/required :message "Field is required"]]
    ;; :faxnumber [[v/required :message "Field is required"]]
    ;; :contactpersonname [[v/required :message "Field is required"]]
    ;; :contactpersonphonenumber [[v/required :message "Field is required"]]
    ;; :contactpersonemail
    ;; [[v/required :message "Field is required"]
    ;;  [v/email :message "Please enter valid email"]]
    )))

(def hotel-search-options
  [{:id "city" :name "City"}
   {:id "hotelname" :name "Hotel Name"}
   {:id "category" :name "Category"}])

(defn delete-hotel-on-click [id src-in list]
  (ajax/DELETE
   (str server "hotels/" @id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-hotel-table-modal [id src-in list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Hotel ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-hotel-on-click id src-in list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn hotels-comp []
  (let [role (get-in @session [:session :user :role])
        src-in (r/atom {:url "hotels/search"
                        :idx 1
                        :type "city"
                        :text ""})
        list (r/atom {:data []
                      :totalhotels 0})
        remove-id (r/atom 0)]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-hotel]
        [:h3.box-title "List of Hotels"]
        (when (= role "bookadmin")
          [:div.box-tools.pull-right
           [:a.btn.btn-primary.btn-sm
            {:href (routes/url-for :add-hotel)}
            "Add New Hotel"]])]
       [:div.box-body
        [delete-hotel-table-modal remove-id src-in list]
        [:div.row
         [:div.col-md-6
          [:a
           {:id "Downloadcsv"
            :href (str server "excel/hotels.xlsx?token="
                       (get-in @session [:session :token]))}
           "Export Complete Hotels into Excel File"]]
         [:div.col-md-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-select "hotelselect"
            hotel-search-options (r/cursor src-in [:type])]
           [search-input "hotelsrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            (when (= role "bookadmin")
              [:th ""])
            (when (= role "bookadmin")
              [:th ""])
            [:th ""]
            [:th "City"]
            [:th "Category"]
            [:th "Hotel Name"]
            [:th "Hotel Phone #"]
            [:th "Contact Person Name"]
            [:th "C P Phone #"]
            [:th "C P Email"]
            [:th "7M Rating"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              (when (= role "bookadmin")
                [:td [button-tooltip
                      {:class "btn btn-danger btn-sm"
                       :title "Delete Hotel"
                       :on-click #(do
                                    (reset! remove-id (:id i))
                                    (.modal (js/$ "#removemodal") "show"))}
                      [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]])
              (when (= role "bookadmin")
                [:td [anchor-tooltip
                      {:class "btn btn-success btn-sm"
                       :title "Edit Hotel"
                       :href (routes/url-for :edit-hotel :id (:id i))}
                      [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]])
              [:td [:a.btn.btn-info.btn-sm
                    {:href (routes/url-for :view-hotel :id (:id i)) } "View"]]
              [:td (:cityid i)]
              [:td (:category i)]
              [:td (:hotelname i)]
              [:td (:phonenumber i)]
              [:td (:contactpersonname i)]
              [:td (:contactpersonphonenumber i)]
              [:td (:contactpersonemail i)]
              [:th [star-rating "hotelrating" (r/atom (:rating i)) "exs" true]]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalhotels) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do
                       (swap! src-in assoc :idx %)
                       (get-table-data src-in list))}]]])))

(defn hotel-save-onclick-success [res data state filename]
  (reset! data {:rating 0
                :type "Not Contract"
                :cost 0
                :category "0 Star"
                :remarks ""
                :cancellationpolicy ""
                :address ""
                :phonenumber ""
                :faxnumber ""
                :contactpersonname ""
                :contactpersonphonenumber ""
                :contactpersonemail ""
                :description ""
                :filename ""})
  (reset! filename "")
  (swap! state assoc :focus false)
  (swap! state assoc :busy false)
  (boot-notify "success" "Success :" "Hotel is successfully saved"))

(defn hotel-save-onclick-error [res state filename]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do (reset! filename "")
        (swap! state assoc :focus false)
        (swap! state assoc :busy false)
        (boot-notify "warning" "Warning :" "Please try again"))))

(defn hotels-put [data state filename]
  (ajax/PUT
   (str server "hotels/" (@data :id))
   {:params @data
    :handler #(pushy/set-token! routes/history (routes/url-for :hotels))
    :error-handler #(hotel-save-onclick-error % state filename)
    :format :json
    :response-format :json
    :keywords? true
    :headers {:Authorization
              (str "Token " (get-in @session [:session :token]))}}))

(defn hotels-post [data state filename]
  (ajax/POST
   (str server "hotels")
   {:params @data
    :handler #(hotel-save-onclick-success % data state filename)
    :error-handler #(hotel-save-onclick-error % state filename)
    :format :json
    :response-format :json
    :keywords? true
    :headers {:Authorization
              (str "Token " (get-in @session [:session :token]))}}))

(defn hotels-post-or-put [data state filename]
  (if (@data :id)
    (hotels-put data state filename)
    (hotels-post data state filename)))

(defn upload-hotel-contactfile [data state filename]
  (if (nil? (hotel-validator @data))
    (do (swap! state assoc :busy true)
        (if (@data :contractfile)
          (ajax/POST
           (str server "hotels/upload")
           {:body (@data :contractfile)
            :response-format :json
            :keywords? true
            :handler #(do (swap! data assoc :filename (:filename %))
                          (hotels-post-or-put data state filename))
            :error-handler #(hotel-save-onclick-error % state filename)
            :headers {:Authorization
                      (str "Token " (get-in @session [:session :token]))}})
          (hotels-post-or-put data state filename)))
    (swap! state assoc :focus true)))

(defn add-or-edit-hotel-comp [data]
  (let [data (r/atom data)
        filename (r/atom "")
        cities (r/atom [])
        state (r/atom
               {:focus false
                :busy false })]
    (get-all-cities cities)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-hotel]
        [:h3.box-title  (if (@data :id) "Edit " "Add ") " Hotel"]]
       [:div.box-body
        [:div..form-horizontal
         [:div.row
          [:div.col-md-6
           [row-chosen-select "City Name" "Cityname" :cityid cities
            (r/cursor data [:cityid]) (@state :focus) (hotel-validator @data)]
           [row-input "Hotel Name" "Hotel Name" :hotelname "text" "Enter Hotel Name"
            (r/cursor data [:hotelname]) (@state :focus) (hotel-validator @data)]
           [:div.form-group
            [:label.col-md-4.control-label "7M Rating"]
            [:div.col-md-8
             [star-rating "HotelRating" (r/cursor data [:rating]) "xs" false]]]
           [row-select "Contract Type" "Type" :type
            ["Not Contract" "Contract"  "Mutual Agreement"] (r/cursor data [:type])
            (@state :focus) (hotel-validator @data)]
           (when-not (or (= "Not Contract" (@data :type)) (= "" (@data :type "")))
             [row-number "Cost" "Cost" :cost  "Enter Cost for Hotel"
              (r/cursor data [:cost]) (@state :focus) (hotel-validator @data)])
           [row-select "Hotel Star Grading" "Category" :category
            ["0 Star" "1 Star" "2 Star" "3 Star" "4 Star" "5 Star"] (r/cursor data [:category])
            (@state :focus) (hotel-validator @data)]
           [row-text-area "Address" "Address" :address 5 "Enter Hotel Address"
            (r/cursor data [:address]) (@state :focus) (hotel-validator @data)]
           [row-input "Phone Number" "Phone Number" :phonenumber "text"
            "Enter Hotel Phone Number" (r/cursor data [:phonenumber])
            (@state :focus) (hotel-validator @data)]
           [row-input "Fax Number" "Fax Number" :faxnumber "text" "Enter Fax Number"
            (r/cursor data [:faxnumber]) (@state :focus) (hotel-validator @data)]]
          [:div.col-md-6
           [row-input "Contact Person Name" "Contact Person Name" :contactpersonname
            "text" "Enter Contact Person Name"
            (r/cursor data [:contactpersonname]) (@state :focus)
            (hotel-validator @data)]
           [row-input "Contact Person Phone #" "Contact Person Phone #"
            :contactpersonphonenumber "text" "Enter  Contact Person Phone #"
            (r/cursor data [:contactpersonphonenumber]) (@state :focus)
            (hotel-validator @data)]
           [row-input "Contact Person Email" "Contact Person Email" :contactpersonemail
            "email" "Enter Contact Person Email" (r/cursor data [:contactpersonemail])
            (@state :focus) (hotel-validator @data)]
           [row-input "Estimated Room Rates" "Estimated Room Rates" :estimatedroomrate
            "text" "Enter Estimated Room Rate"
            (r/cursor data [:estimatedroomrate]) (@state :focus)
            (hotel-validator @data)]
           [row-input "Remarks" "remarks" :remarks "text"
            "Enter Remarks" (r/cursor data [:remarks])
            (@state :focus) (hotel-validator @data)]
           [row-input "Cancellation Policy" "Cancellation Policy" :cancellationpolicy "text"
            "Enter Cancellation Policy" (r/cursor data [:cancellationpolicy])
            (@state :focus) (hotel-validator @data)]
           [row-text-area "Description" "Description" :description 5
            "Enter Description" (r/cursor data [:description])
            (@state :focus) (hotel-validator @data)]
           [:div.form-group
            [:label.col-md-4.control-label "Upload Contract PDF"]
            [:div.col-md-6
             [file-upload-button "contractfile" "Contract PDF"
              accept-types-hotel-contractfile
              (r/cursor data [:contractfile]) filename]]
            [:div.col-md-2
             (when (seq (@data :filename))
               [:button.btn.btn-primary
                {:on-click
                 #(.open js/window
                         (str
                          server "hotelcontractfiles/"
                          (@data :filename)))}
                [:span.glyphicon.glyphicon-download-alt]])]]]]]]
       [:div.box-footer.clearfix
        [:center
         (when-not (= (routes/url-for :view-hotel :id (@data :id 0))
                      (pushy/get-token routes/history))
           [:button.btn.btn-primary
            {:on-click #(upload-hotel-contactfile data state filename)} "Save "
            (when (@state :busy) [:i.fa.fa-spin.fa-refresh])])"  "
         [:a.btn.btn-default
          {:href (routes/url-for :hotels)} "Back"]]]])))

;; -----------------------------------------------------------
;; Restaurants Comp
;; -----------------------------------------------------------
(defn restaurant-validator [data-set]
  (first
   (b/validate
    data-set
    :cityid [[v/required :message "Field is required"]]
    :restaurantname [[v/required :message "Field is required"]]
    ;; :area [[v/required :message "Field is required"]]
    ;; :address [[v/required :message "Field is required"]]
    ;; :cuisine [[v/required :message "Field is required"]]
    ;; :contactperson [[v/required :message "Field is required"]]
    ;; :contactnumber [[v/required :message "Field is required"]]
    ;; :faxnumber [[v/required :message "Field is required"]]
    ;; :cellnumber [[v/required :message "Field is required"]]
    ;; :lunch [[v/required :message "Field is required"]]
    ;; :dinner [[v/required :message "Field is required"]]
    ;; :kids [[v/required :message "Field is required"]]
    ;; :waytoconfirm [[v/required :message "Field is required"]
    ;;                [v/email :message "Please enter valid email"]]
    )))

(def restaurant-search-options
  [{:id "city" :name "City"}
   {:id "restaurantname" :name "Restaurants Name"}
   {:id "area" :name "Area"}])

(defn delete-restaurants-on-click [id src-in list]
  (ajax/DELETE
   (str server "restaurants/" @id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-restaurants-table-modal [id src-in list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Restaurant ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-restaurants-on-click id src-in list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn restaurants-comp []
  (let [role (get-in @session [:session :user :role])
        src-in (r/atom {:url "restaurants/search"
                        :idx 1
                        :type "city"
                        :text ""})
        list (r/atom {:data []
                      :totalrestaurants 0})
        remove-id (r/atom 0)]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-cutlery]
        [:h3.box-title "List of Restaurants"]
        (when (= role "bookadmin")
          [:div.box-tools.pull-right
           [:a.btn.btn-primary.btn-sm
            {:href (routes/url-for :add-restaurant)}
            "Add New Restaurant"]])]
       [:div.box-body
        [delete-restaurants-table-modal remove-id src-in list]
        [:div.row
         [:div.col-md-6
          [:a
           {:id "Downloadcsv"
            :href (str server "excel/restaurents.xlsx?token="
                       (get-in @session [:session :token]))}
           "Export Complete Restaurants into Excel File"]]
         [:div.col-md-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-select "restaurantselect"
            restaurant-search-options (r/cursor src-in [:type])]
           [search-input "restaurantsrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            (when (= role "bookadmin")
              [:th ""])
            (when (= role "bookadmin")
              [:th ""])
            [:th ""]
            [:th "City"]
            [:th "Area"]
            [:th "Restaurant Name"]
            [:th "Cuisine"]
            [:th "Contact Person Name"]
            [:th "Contact Number"]
            [:th "Cell Number"]
            ;;  [:th "Way to confirm"]
            [:th "7M Rating"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              (when (= role "bookadmin")
                [:td [button-tooltip
                      {:class "btn btn-danger btn-sm"
                       :title "Delete Restaurant"
                       :on-click #(do
                                    (reset! remove-id (:id i))
                                    (.modal (js/$ "#removemodal") "show"))}
                      [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]])
              (when (= role "bookadmin")
                [:td [anchor-tooltip
                      {:class "btn btn-success btn-sm"
                       :title "Edit Restaurant"
                       :href (routes/url-for :edit-restaurant :id (:id i))}
                      [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]])
              [:td [:a.btn.btn-info.btn-sm
                    {:href (routes/url-for :view-restaurant :id (:id i)) } "View"]]
              [:td (:cityid i)]
              [:td (:area i)]
              [:td (:restaurantname i)]
              [:td (:cuisine i)]
              [:td (:contactperson i)]
              [:td (:contactnumber i)]
              [:td (:cellnumber i)]
              ;; [:td (:waytoconfirm i)]
              [:td [star-rating "Tourguide" (r/atom (:rating i)) "exs" true]]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalrestaurants) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do
                       (swap! src-in assoc :idx %)
                       (get-table-data src-in list))}]]])))

(defn restaurant-save-onclick-success [res data state]
  (reset! data {:area "" :cuisine "" :address "" :contactperson ""
                :contactnumber "" :faxnumber "" :cellnumber ""
                :lunch 0 :dinner 0 :kids 0 :rating 0
                :waytoconfirm "" :description ""
                :remarks "" :email ""})
  (swap! state assoc :focus false)
  (boot-notify "success" "Success :" "Restaurant is successfully saved"))

(defn restaurant-save-onclick-error [res state]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do (swap! state assoc :focus false)
        (boot-notify "warning" "Warning :" "Please try again"))))

(defn restaurant-save-onclick [data state]
  (if (nil? (restaurant-validator @data))
    (if (@data :id)
      (do (ajax/PUT
           (str server "restaurants/" (@data :id))
           {:params @data
            :handler #(pushy/set-token! routes/history
                                        (routes/url-for :restaurants))
            :error-handler #(restaurant-save-onclick-error % state)
            :format :json
            :response-format :json
            :keywords? true
            :headers {:Authorization
                      (str "Token " (get-in @session [:session :token]))}}))
      (do (ajax/POST
           (str server "restaurants")
           {:params @data
            :handler #(restaurant-save-onclick-success % data state)
            :error-handler #(restaurant-save-onclick-error % state)
            :format :json
            :response-format :json
            :keywords? true
            :headers {:Authorization
                      (str "Token " (get-in @session [:session :token]))}})))
    (swap! state assoc :focus true)))

(defn add-or-edit-restaurant-comp [data]
  (let [data (r/atom data)
        cities (r/atom [])
        state (r/atom
               {:focus false
                :busy false})]
    (get-all-cities cities)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-cutlery]
        [:h3.box-title  (if (@data :id) "Edit " "Add ") " Restaurant"]]
       [:div.box-body
        [:div..form-horizontal
         [:div.row
          [:div.col-md-6
           [row-chosen-select "City Name" "Cityname" :cityid cities
            (r/cursor data [:cityid]) (@state :focus) (restaurant-validator @data)]
           [row-input "Restaurant Name" "Restaurant Name" :restaurantname "text"
            "Enter Restaurant Name" (r/cursor data [:restaurantname])
            (@state :focus) (restaurant-validator @data)]
           [:div.form-group
            [:label.col-md-4.control-label "7M Rating"]
            [:div.col-md-8
             [star-rating "RestaurantRating" (r/cursor data [:rating]) "xs" false]]]
           [row-input "Area" "Area" :area "text" "Enter Area"
            (r/cursor data [:area]) (@state :focus) (restaurant-validator @data)]
           [row-text-area "Address" "Address" :address 5 "Enter Restaurant Address"
            (r/cursor data [:address]) (@state :focus) (restaurant-validator @data)]
           [row-input "Cuisine" "Cuisine" :Cuisine "text" "Enter Cuisine"
            (r/cursor data [:cuisine]) (@state :focus) (restaurant-validator @data)]
           [row-input "Remarks" "Remarks" :remarks "text"
            "Enter Remarks" (r/cursor data [:remarks])
            (@state :focus) (restaurant-validator @data)]]
          [:div.col-md-6
           [row-input "Contact Person" "Contact Person" :contactperson
            "text" "Enter Contact Person Name" (r/cursor data [:contactperson])
            (@state :focus) (restaurant-validator @data)]
           [row-input "Contact Number" "Contact Number" :contactnumber "text"
            "Enter  Contact Number" (r/cursor data [:contactnumber])
            (@state :focus) (restaurant-validator @data)]
           [row-input "Cell Number" "Cell Number" :cellnumber
            "text" "Enter Cell Number" (r/cursor data [:cellnumber])
            (@state :focus) (restaurant-validator @data)]
           [row-input "Fax Number" "Fax Number" :faxnumber
            "text" "Enter Fax Number" (r/cursor data [:faxnumber])
            (@state :focus) (restaurant-validator @data)]
           [row-input "Email" "Email" :email
            "email" "Enter email" (r/cursor data [:email])
            (@state :focus) (restaurant-validator @data)]
           [row-number "Lunch" "lunch" :lunch
            "Enter Lunch amount" (r/cursor data [:lunch])
            (@state :focus) (restaurant-validator @data)]
           [row-number "Dinner" "dinner" :dinner
            "Enter Dinner amount" (r/cursor data [:dinner])
            (@state :focus) (restaurant-validator @data)]
           [row-number "Child/Student Cost" "kids" :kids
            "Enter Kids amount" (r/cursor data [:kids])
            (@state :focus) (restaurant-validator @data)]]]
         ;; [row-input "Way to confirm" "Way to confirm" :waytoconfirm
         ;;  "email" "Enter Way to confirm" (r/cursor data [:waytoconfirm])
         ;;  (@state :focus) (restaurant-validator @data)]

         ;; [row-text-area "Description" "Description" :description 5
         ;;  "Enter Description" (r/cursor data [:description])
         ;;  (@state :focus) (restaurant-validator @data)]
         ]]
       [:div.box-footer.clearfix
        [:center
         (when-not (= (routes/url-for :view-restaurant :id (@data :id 0))
                      (pushy/get-token routes/history))
           [:button.btn.btn-primary
            {:on-click #(restaurant-save-onclick data state)} "Save"])"  "
         [:a.btn.btn-default
          {:href (routes/url-for :restaurants)} "Back"]]]])))

;; -----------------------------------------------------------
;; Tourguides Comp
;; -----------------------------------------------------------
(defn tourguide-validator [data-set]
  (first
   (b/validate
    data-set
    :cityid [[v/required :message "Field is required"]]
    :tourguidename [[v/required :message "Field is required"]]
    ;; :vendorid [[v/required :message "Field is required"]]
    ;; :address [[v/required :message "Field is required"]]
    ;; :contactnumber [[v/required :message "Field is required"]]
    ;; :cellnumber [[v/required :message "Field is required"]]
    ;; :amount [[v/required :message "Field is required"]]
    ;; :hours [[v/required :message "Field is required"]]
    ;; :description [[v/required :message "Field is required"]]
    ;; :email [[v/required :message "Field is required"]
    ;;         [v/email :message "Please enter valid email"]]
    )))

(def tourgide-search-options
  [{:id "city" :name "City"}
   {:id "tourguidename" :name "Tour Guide Name"}])

(defn delete-tourguides-on-click [id src-in list]
  (ajax/DELETE
   (str server "tourguides/" @id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-tourguides-table-modal [id src-in list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Tourguide ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-tourguides-on-click id src-in list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn tourguides-comp []
  (let [role (get-in @session [:session :user :role])
        src-in (r/atom {:url "tourguides/search"
                        :idx 1
                        :type "city"
                        :text ""})
        list (r/atom {:data []
                      :totaltourguides 0})
        remove-id (r/atom 0)]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-users]
        [:h3.box-title "List of Tour Guides"]
        (when (= role "bookadmin")
          [:div.box-tools.pull-right
           [:a.btn.btn-primary.btn-sm
            {:href (routes/url-for :add-tourguide)}
            "Add New Tourguide"]])]
       [:div.box-body
        [delete-tourguides-table-modal remove-id src-in list]
        [:div.row
         [:div.col-md-6
          [:a
           {:id "Downloadcsv"
            :href (str server "excel/tourguides.xlsx?token="
                       (get-in @session [:session :token]))}
           "Export Complete Tour Guides into Excel File"]]
         [:div.col-md-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-select "tourguideselect"
            tourgide-search-options (r/cursor src-in [:type])]
           [search-input "tourgidesrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            (when (= role "bookadmin")
              [:th ""])
            (when (= role "bookadmin")
              [:th ""])
            [:th ""]
            [:th "City"]
            [:th "Tour Guide Name"]
            [:th "Contact #"]
            [:th "Cell #"]
            [:th "Email"]
            [:th "Tour Guide Rate"]
            ;; [:th "per/hours"]
            [:th "7M Rating"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              (when (= role "bookadmin")
                [:td [button-tooltip
                      {:class "btn btn-danger btn-sm"
                       :title "Delete Tour Guide"
                       :on-click #(do
                                    (reset! remove-id (:id i))
                                    (.modal (js/$ "#removemodal") "show"))}
                      [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]])
              (when (= role "bookadmin")
                [:td [anchor-tooltip
                      {:class "btn btn-success btn-sm"
                       :title "Edit Tour Guide"
                       :href (routes/url-for :edit-tourguide :id (:id i))}
                      [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]])
              [:td [:a.btn.btn-info.btn-sm
                    {:href (routes/url-for :view-tourguide :id (:id i)) } "View"]]
              [:td (:cityid i)]
              [:td (:tourguidename i)]
              [:td (:contactnumber i)]
              [:td (:cellnumber i)]
              [:td (:email i)]
              [:td (str "$ " (:amount i) "/hr")]
              ;; [:td (str (:hours i) " Hours")]
              [:td [star-rating "Tourguide" (r/atom (:rating i)) "exs" true]]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totaltourguides) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do
                       (swap! src-in assoc :idx %)
                       (get-table-data src-in list))}]]])))

(defn tourguide-save-onclick-success [res data state]
  (reset! data {:vendorid 0
                :rating 0
                :address ""
                :contactnumber ""
                :cellnumber ""
                :amount 0
                :hours 0
                :description ""
                :email ""})
  (swap! state assoc :focus false)
  (boot-notify "success" "Success :" "Tourguide is successfully saved"))

(defn tourguide-save-onclick-error [res state]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do
      (swap! state assoc :focus false)
      (boot-notify "warning" "Warning :" "Please try again"))))

(defn tourguide-save-onclick [data state]
  (if (nil? (tourguide-validator @data))
    (if (@data :id)
      (do
        (ajax/PUT
         (str server "tourguides/" (@data :id))
         {:params @data
          :handler #(pushy/set-token! routes/history (routes/url-for :tourguides))
          :error-handler #(tourguide-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}}))
      (do
        (ajax/POST
         (str server "tourguides")
         {:params @data
          :handler #(tourguide-save-onclick-success % data state)
          :error-handler #(tourguide-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}})))
    (swap! state assoc :focus true)))

(def hours-data
  (r/atom (mapv #(hash-map :id % :name (str "Per " % " Hour")) (range 1 25))))

(defn add-or-edit-tourguide-comp [data]
  (let [data (r/atom data)
        cities (r/atom [])
        state (r/atom
               {:focus false
                :busy false})
        vendors (r/atom [])]
    (get-all-cities cities)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-user-plus]
        [:h3.box-title  (if (@data :id) "Edit " "Add ") " Tour Guide"]]
       [:div.box-body
        [:div.form-horizontal
         [:div.row
          [:div.col-md-6
           [row-chosen-select "City Name" "Cityname" :cityid cities
            (r/cursor data [:cityid]) (@state :focus)
            (tourguide-validator @data)]
           [row-input "Tour Guide Name" "Tour Guide Name" :tourguidename "text"
            "Enter Tour Guide Name" (r/cursor data [:tourguidename])
            (@state :focus) (tourguide-validator @data)]
           [:div.form-group
            [:label.col-md-4.control-label "7M Rating"]
            [:div.col-md-8
             [star-rating "TourGuideRating" (r/cursor data [:rating]) "xs" false]]]
           [row-text-area "Address" "Address" :address 5 "Enter Tourguide Address"
            (r/cursor data [:address]) (@state :focus) (tourguide-validator @data)]]
          [:div.col-md-6
           [row-input "Contact Number" "Contact Number" :contactnumber "text"
            "Enter  Contact Number" (r/cursor data [:contactnumber])
            (@state :focus) (tourguide-validator @data)]
           [row-input "Cell Number" "Cell Number" :cellnumber
            "text" "Enter Cell Number" (r/cursor data [:cellnumber])
            (@state :focus) (tourguide-validator @data)]
           [row-input "Email" "Email" :email
            "email" "Enter email" (r/cursor data [:email])
            (@state :focus) (tourguide-validator @data)]
           [row-text-area "Bank/Payment Info" "Bank/Payment Info" :bankorpaymentinfo 3 "Enter Bank/Payment Info"
            (r/cursor data [:bankorpaymentinfo]) (@state :focus) (tourguide-validator @data)]
           [row-number "Tour Guide Rate per hr" "Tourguiderate" :amount
            "Enter Tour Guide Rate" (r/cursor data [:amount])
            (@state :focus) (tourguide-validator @data)]]]
         ;; [row-chosen-select "Per Hours" "PerHours" :hours hours-data
         ;;  (r/cursor data [:hours]) (@state :focus) (tourguide-validator @data)]
         ;; [row-text-area "Description" "Description" :description 5 "Enter Description"
         ;;  (r/cursor data [:description]) (@state :focus) (tourguide-validator @data)]
         ]]
       [:div.box-footer.clearfix
        [:center
         (when-not (= (routes/url-for :view-tourguide :id (@data :id 0))
                      (pushy/get-token routes/history))
           [:button.btn.btn-primary
            {:on-click #(tourguide-save-onclick data state)} "Save"])"  "
         [:a.btn.btn-default
          {:href (routes/url-for :tourguides)} "Back"]]]])))

;; -----------------------------------------------------------
;; Transportations Comp
;; -----------------------------------------------------------
(defn transportation-validator [data-set]
  (first
   (b/validate
    data-set
    :cityid [[v/required :message "Field is required"]]
    ;; :vendorid [[v/required :message "Field is required"]]
    ;; :type [[v/required :message "Field is required"]]
    :transportationname [[v/required :message "Field is required"]]
    ;; :address [[v/required :message "Field is required"]]
    ;; :contactperson [[v/required :message "Field is required"]]
    ;; :contactnumber [[v/required :message "Field is required"]]
    ;; :faxnumber [[v/required :message "Field is required"]]
    ;; :email [[v/required :message "Field is required"]]
    ;; :bankinformation [[v/required :message "Field is required"]]
    )))

(def transportations-search-options
  [{:id "city" :name "City"}
   {:id "transportationname" :name "Transportation Name"}])

(defn delete-transportations-on-click [id src-in list]
  (ajax/DELETE
   (str server "transportations/" @id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-transportations-table-modal [id src-in list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Transportation ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-transportations-on-click id src-in list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn transportations-comp []
  (let [role (get-in @session [:session :user :role])
        src-in (r/atom {:url "transportations/search"
                        :idx 1
                        :type "city"
                        :text ""})
        list (r/atom {:data []
                      :totaltransportations 0})
        remove-id (r/atom 0)]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-plane]
        [:h3.box-title "List of Transportations"]
        (when (= role "bookadmin")
          [:div.box-tools.pull-right
           [:a.btn.btn-primary.btn-sm
            {:href (routes/url-for :add-transportation)}
            "Add New Transportation"]])]
       [:div.box-body
        [delete-transportations-table-modal remove-id src-in list]
        [:div.row
         [:div.col-md-6
          [:a
           {:id "Downloadcsv"
            :href (str server "excel/transportation.xlsx?token="
                       (get-in @session [:session :token]))}
           "Export Complete Transportations into Excel File"]]
         [:div.col-md-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-select "transportationsselect"
            transportations-search-options (r/cursor src-in [:type])]
           [search-input "transportationssrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            (when (= role "bookadmin")
              [:th ""])
            (when (= role "bookadmin")
              [:th ""])
            [:th ""]
            [:th "City"]
            [:th "Type"]
            [:th "Transportation Name"]
            [:th "Contact Person"]
            [:th "Contact Number"]
            [:th "Fax Number"]
            [:th "Email"]
            [:th "7M Rating"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              (when (= role "bookadmin")
                [:td [button-tooltip
                      {:class "btn btn-danger btn-sm"
                       :title "Delete Transportation"
                       :on-click #(do
                                    (reset! remove-id (:id i))
                                    (.modal (js/$ "#removemodal") "show"))}
                      [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]])
              (when (= role "bookadmin")
                [:td [anchor-tooltip
                      {:class "btn btn-success btn-sm"
                       :title "Edit Transportation"
                       :href (routes/url-for :edit-transportation :id (:id i))}
                      [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]])
              [:td [:a.btn.btn-info.btn-sm
                    {:href (routes/url-for :view-transportation :id (:id i)) } "View"]]
              [:td (:cityid i)]
              [:td (:type i)]
              [:td (:transportationname i)]
              [:td (:contactperson i)]
              [:td (:contactnumber i)]
              [:td (:faxnumber i)]
              [:td (:email i)]
              [:td [star-rating "TransportRating" (r/atom (:rating i)) "exs" true]]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totaltransportations) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do
                       (swap! src-in assoc :idx %)
                       (get-table-data src-in list))}]]])))

(defn transportation-save-onclick-success [res data state]
  (reset! data {:vendorid 0
                :type "None"
                :rating 0
                :address ""
                :contactperson ""
                :contactnumber ""
                :faxnumber ""
                :email ""
                :bankinformation ""
                :description ""})
  (swap! state assoc :focus false)
  (boot-notify "success" "Success :" "Transportation is successfully saved"))

(defn transportation-save-onclick-error [res state]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do
      (swap! state assoc :focus false)
      (boot-notify "warning" "Warning :" "Please try again"))))

(defn transportation-save-onclick [data state]
  (if (nil? (transportation-validator @data))
    (if (@data :id)
      (do
        (ajax/PUT
         (str server "transportations/" (@data :id))
         {:params @data
          :handler #(pushy/set-token! routes/history (routes/url-for :transportations))
          :error-handler #(transportation-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}}))
      (do
        (ajax/POST
         (str server "transportations")
         {:params @data
          :handler #(transportation-save-onclick-success % data state)
          :error-handler #(transportation-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}})))
    (swap! state assoc :focus true)))

(defn add-or-edit-transportation-comp [data]
  (let [data (r/atom data)
        cities (r/atom [])
        state (r/atom
               {:focus false
                :busy false})
        vendors (r/atom [])]
    (get-all-cities cities)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-plane]
        [:h3.box-title  (if (@data :id) "Edit " "Add ") " Transportation"]]
       [:div.box-body
        [:div..form-horizontal
         [:div.row
          [:div.col-md-6
           [row-chosen-select "City Name" "Cityname" :cityid cities
            (r/cursor data [:cityid]) (@state :focus)
            (transportation-validator @data)]
           [row-input "Transportation Name" "Transportation Name" :transportationname "text"
            "Enter Transportation Name" (r/cursor data [:transportationname])
            (@state :focus) (transportation-validator @data)]
           [:div.form-group
            [:label.col-md-4.control-label "7M Rating"]
            [:div.col-md-8
             [star-rating "TransportRating" (r/cursor data [:rating]) "xs" false]]]
           [row-select "Type" "Type" :type ["SIC" "PVT" "Both"]
            (r/cursor data [:type]) (@state :focus) (items-validator @data)]
           [row-text-area "Address" "Address" :address 5 "Enter Address"
            (r/cursor data [:address]) (@state :focus) (transportation-validator @data)]]
          [:div.col-md-6
           [row-input "Contact Person" "Contact Person" :contactperson "text"
            "Enter  Contact Person" (r/cursor data [:contactperson])
            (@state :focus) (transportation-validator @data)]
           [row-input "Contact Number" "Contact Number" :contactnumber
            "text" "Enter Contact Number" (r/cursor data [:contactnumber])
            (@state :focus) (transportation-validator @data)]
           [row-input "Fax Number" "Fax Number" :faxnumber
            "text" "Enter Fax Number" (r/cursor data [:faxnumber])
            (@state :focus) (transportation-validator @data)]
           [row-input "Email" "Email" :email
            "text" "Enter Email" (r/cursor data [:email])
            (@state :focus) (transportation-validator @data)]
           [row-text-area "Bank/Payment Info" "BankPaymentInfo" :bankinformation 3
            "Enter Bank Info" (r/cursor data [:bankinformation])
            (@state :focus) (transportation-validator @data)]]]]]
       [:div.box-footer.clearfix
        [:center
         (when-not (= (routes/url-for :view-transportation :id (@data :id 0))
                      (pushy/get-token routes/history))
           [:button.btn.btn-primary
            {:on-click #(transportation-save-onclick data state)} "Save"])"  "
         [:a.btn.btn-default
          {:href (routes/url-for :transportations)} "Back"]]]])))

;; -----------------------------------------------------------
;; Events Comp
;; -----------------------------------------------------------
(defn event-validator [data-set]
  (first
   (b/validate
    data-set
    :cityid [[v/required :message "Field is required"]]
    ;; :vendorid [[v/required :message "Field is required"]]
    :eventname [[v/required :message "Field is required"]]
    ;; :adultcost [[v/required :message "Field is required"]]
    ;; :childcost [[v/required :message "Field is required"]]
    ;; :st [[v/required :message "Field is required"]]
    ;; :cp [[v/required :message "Field is required"]]
    ;; :contactnumber [[v/required :message "Field is required"]]
    ;; :phonenumber [[v/required :message "Field is required"]]
    ;; :faxnumber [[v/required :message "Field is required"]]
    ;; :website [[v/required :message "Field is required"]]
    ;; :email [[v/required :message "Field is required"]]
    ;; :actual [[v/required :message "Field is required"]]
    ;; :meal [[v/required :message "Field is required"]]
    ;; :remarks [[v/required :message "Field is required"]]
    ;; :contract [[v/required :message "Field is required"]]
    ;; :description [[v/required :message "Field is required"]]
    )))

(def events-search-options
  [{:id "city" :name "City"}
   {:id "eventname" :name "Event Name"}])

(defn delete-events-on-click [id src-in list]
  (ajax/DELETE
   (str server "events/" @id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))


(defn delete-events-table-modal [id src-in list]
  [:div.modal.fade
   {:role "dialog" :id "removemodal"}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:button.close
       {:type "button" :data-dismiss "modal"} "×"]
      [:h4.modal-title "Conformation Message"]]
     [:div.modal-body
      [:div.text-center
       [:p.lead "Are you sure you want to remove this Event ?"]]]
     [:div.modal-footer
      [:button.btn.btn-primary
       {:id (str @id "remove")
        :type "button"
        :on-click #(delete-events-on-click id src-in list)} "Yes"]
      [:button.btn.btn-default
       {:type "button"
        :data-dismiss "modal"} "No"]]]]])

(defn events-comp []
  (let [role (get-in @session [:session :user :role])
        src-in (r/atom {:url "events/search"
                        :idx 1
                        :type "city"
                        :text ""})
        list (r/atom {:data []
                      :totalevents 0})
        remove-id (r/atom 0)]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-calendar-check-o]
        [:h3.box-title "List of Events"]
        (when (= role "bookadmin")
          [:div.box-tools.pull-right
           [:a.btn.btn-primary.btn-sm
            {:href (routes/url-for :add-event)}
            "Add New Events"]])]
       [:div.box-body
        [delete-events-table-modal remove-id src-in list]
        [:div.row
         [:div.col-md-6
          [:a
           {:id "Downloadcsv"
            :href (str server "excel/events.xlsx?token="
                       (get-in @session [:session :token]))}
           "Export Complete Events into Excel File"]]
         [:div.col-md-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-select "eventselect"
            events-search-options (r/cursor src-in [:type])]
           [search-input "eventsrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            (when (= role "bookadmin")
              [:th ""])
            (when (= role "bookadmin")
              [:th ""])
            [:th ""]
            [:th "City"]
            [:th "Event Name"]
            [:th "Adult Cost"]
            [:th "Child Cost"]
            [:th "Email"]
            [:th "7M Rating"]]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              (when (= role "bookadmin")
                [:th [button-tooltip
                      {:class "btn btn-danger btn-sm"
                       :title "Delete Event"
                       :on-click #(do
                                    (reset! remove-id (:id i))
                                    (.modal (js/$ "#removemodal") "show"))}
                      [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]])
              (when (= role "bookadmin")
                [:th [anchor-tooltip
                      {:class "btn btn-success btn-sm"
                       :title "Edit Event"
                       :href (routes/url-for :edit-event :id (:id i))}
                      [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]])
              [:td [:a.btn.btn-info.btn-sm
                    {:href (routes/url-for :view-event :id (:id i))}
                    "View"]]
              [:td (:cityid i)]
              [:td (:eventname i)]
              [:td (:adultcost i)]
              [:td (:childcost i)]
              [:td (:email i)]
              [:td [star-rating "EventRating" (r/atom (:rating i)) "exs" true]]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalevents) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do
                       (swap! src-in assoc :idx %)
                       (get-table-data src-in list))}]]])))

(defn event-save-onclick-success [res data state]
  (reset! data {:rating 0 :vendorid 0 :adultcost 0 :childcost 0
                :st 0 :cp 0 :contactnumber "" :phonenumber ""
                :faxnumber "" :website "" :email ""
                :actual "" :meal "" :remarks ""
                :contract "" :description ""})
  (swap! state assoc :focus false)
  (boot-notify "success" "Success :" "Event is successfully saved"))

(defn event-save-onclick-error [res state]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do
      (swap! state assoc :focus false)
      (boot-notify "warning" "Warning :" "Please try again"))))

(defn event-save-onclick [data state]
  (if (nil? (event-validator @data))
    (if (@data :id)
      (do
        (ajax/PUT
         (str server "events/" (@data :id))
         {:params @data
          :handler #(pushy/set-token! routes/history (routes/url-for :events))
          :error-handler #(event-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}}))
      (do
        (ajax/POST
         (str server "events")
         {:params @data
          :handler #(event-save-onclick-success % data state)
          :error-handler #(event-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}})))
    (swap! state assoc :focus true)))

(defn add-or-edit-event-comp [data]
  (let [data (r/atom data)
        cities (r/atom [])
        state (r/atom
               {:focus false
                :busy false})]
    (get-all-cities cities)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-calendar-check-o]
        [:h3.box-title  (if (@data :id) "Edit " "Add ") " Event"]]
       [:div.box-body
        [:div.form-horizontal
         [:div.row
          [:div.col-md-6
           [row-chosen-select "City Name" "Cityname" :cityid cities
            (r/cursor data [:cityid]) (@state :focus)
            (event-validator @data)]
           [row-input "Event Name" "Event Name" :eventname
            "text" "Enter Event Name" (r/cursor data [:eventname])
            (@state :focus) (event-validator @data)]
           [:div.form-group
            [:label.col-md-4.control-label "7M Rating"]
            [:div.col-md-8
             [star-rating "EventRating" (r/cursor data [:rating]) "xs" false]]]
           [row-select "Contract Type" "Contract" :contract
            ["Not Contract" "Contract"  "Mutual Agreement"]
            (r/cursor data [:contract])
            (@state :focus) (event-validator @data)]
           [:div.form-group
            [:div.col-md-4]
            [:div.col-md-6
             [input-checkbox-str :meal
              (r/cursor data [:meal])
              "Includes Meal"]]]
           [row-input "Actual Price" "Actual Price" :actual "text" "Enter Actual Price"
            (r/cursor data [:actual]) (@state :focus) (event-validator @data)]
           [row-input "Remarks" "Remarks" :remarks "text" "Enter Remarks"
            (r/cursor data [:remarks]) (@state :focus) (event-validator @data)]
           [row-text-area "Booking Guide" "bookingguide" :bookingguide 6
            "Enter Booking Guide Info" (r/cursor data [:bookingguide])
            (@state :focus) (event-validator @data)]]
          [:div.col-md-6
           [row-input "Contact Number" "Contact Number" :contactnumber
            "text" "Enter Contact Number" (r/cursor data [:contactnumber])
            (@state :focus) (event-validator @data)]
           [row-input "Phone Number" "Phone Number" :phonenumber
            "text" "Enter Phone Number" (r/cursor data [:phonenumber])
            (@state :focus) (event-validator @data)]
           [row-input "Fax Number" "fax Number" :faxnumber
            "text" "Enter Fax Number" (r/cursor data [:faxnumber])
            (@state :focus) (event-validator @data)]
           [row-input "Website" "Website" :website
            "text" "Enter Website" (r/cursor data [:website])
            (@state :focus) (event-validator @data)]
           [row-input "Email" "Email" :email "email" "Enter Email"
            (r/cursor data [:email]) (@state :focus) (event-validator @data)]
           [row-number "Adult Cost" "adultcost" :adultcost
            "Enter Adult Cost" (r/cursor data [:adultcost])
            (@state :focus) (event-validator @data)]
           [row-number "Child Cost" "childcost" :childcost
            "Enter Child Cost" (r/cursor data [:childcost])
            (@state :focus) (event-validator @data)]
           [row-number "Student Cost" "Student Cost" :st  "Enter Student Cost"
            (r/cursor data [:st]) (@state :focus) (event-validator @data)]
           [row-number "Com Policy" "Com Policy" :cp  "Enter Com Policy"
            (r/cursor data [:cp]) (@state :focus) (event-validator @data)]]]]]
       [:div.box-footer.clearfix
        [:div.col-md-3]
        [:div.col-md-6
         [:center
          (when-not (= (routes/url-for :view-event :id (@data :id 0))
                       (pushy/get-token routes/history))
            [:button.btn.btn-primary
             {:on-click #(event-save-onclick data state)} "Save"])"  "
          [:a.btn.btn-default
           {:href (routes/url-for :events)} "Back"]]]
        [:div.col-md-3]]])))


;; -----------------------------------------------------------
;; Logos Comp
;; -----------------------------------------------------------
(defn logo-validator [data-set]
  (first
   (b/validate
    data-set
    :logoname [[v/required :message "Field is required"]])))

(defn delete-logo-on-click [id src-in list]
  (ajax/DELETE
   (str server "logos/" id)
   {:handler #(delete-record-handler % src-in list)
    :error-handler #(delete-record-handler % src-in list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn logos-comp []
  (let [role (get-in @session [:session :user :role])
        src-in (r/atom {:url "logos/search"
                        :idx 1
                        :type "logoname"
                        :text ""})
        list (r/atom {:data []
                      :totallogos 0})]
    (get-table-data src-in list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-photo]
        [:h3.box-title "List of Logos"]
        (when (= role "bookadmin")
          [:div.box-tools.pull-right
           [:a.btn.btn-primary.btn-sm
            {:href (routes/url-for :add-logo)}
            "Add New Logo"]])]
       [:div.box-body
        [:div.row
         [:div.col-md-6.col-md-offset-6
          [:div.col-md-12.col-sm-12.col-xs-12.input-group.form-group
           [search-input "logosrctext" "text"
            "Enter something to search" (r/cursor src-in [:text])
            src-in list "100%"]]]]
        [:div.table-responsive
         [:table.table.table-bordered.table-hover
          [:thead
           [:tr
            (when (= role "bookadmin")
              [:th ""])
            (when (= role "bookadmin")
              [:th ""])
            [:th "View Logo"]
            [:th "Logo Name"]
            ]]
          [:tbody
           (for [i (@list :data)]
             ^{:key (:id i)}
             [:tr
              (when (= role "bookadmin")
                [:td [button-tooltip
                      {:class "btn btn-danger btn-sm"
                       :title "Delete Logo"
                       :on-click #(delete-logo-on-click (:id i) src-in list)}
                      [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]])
              (when (= role "bookadmin")
                [:td [anchor-tooltip
                      {:class "btn btn-success btn-sm"
                       :title "Edit Logo"
                       :href (routes/url-for :edit-logo :id (:id i))}
                      [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]])
              [:td [:button.btn.btn-info.btn-sm
                    {:on-click
                     #(.open js/window (str server "logosfilename/" (i :filename)))}
                    "View"]]
              [:td (:logoname i)]])]]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totallogos) 10)))
          :maxButtons 5
          :activePage (@src-in :idx)
          :onSelect #(do (swap! src-in assoc :idx %)
                         (get-table-data src-in list))}]]])))

(defn logo-save-onclick-success [res data state filename]
  (reset! data {:filename ""
                :logoname ""})
  (reset! filename "")
  (swap! state assoc :focus false)
  (swap! state assoc :busy false)
  (boot-notify "success" "Success :" "Logo is successfully saved"))

(defn logo-save-onclick-error [res state filename]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do
      (reset! filename "")
      (swap! state assoc :focus false)
      (swap! state assoc :busy false)
      (boot-notify "warning" "Warning :" "Please try again"))))

(defn logos-put [data state filename]
  (ajax/PUT
   (str server "logos/" (@data :id))
   {:params @data
    :handler #(pushy/set-token! routes/history (routes/url-for :logos))
    :error-handler #(logo-save-onclick-error % state filename)
    :format :json
    :response-format :json
    :keywords? true
    :headers {:Authorization
              (str "Token " (get-in @session [:session :token]))}}))

(defn logos-post [data state filename]
  (ajax/POST
   (str server "logos")
   {:params @data
    :handler #(logo-save-onclick-success % data state filename)
    :error-handler #(logo-save-onclick-error % state filename)
    :format :json
    :response-format :json
    :keywords? true
    :headers {:Authorization
              (str "Token " (get-in @session [:session :token]))}}))

(defn logos-post-or-put [data state filename]
  (if (@data :id)
    (logos-put data state filename)
    (logos-post data state filename)))

(defn upload-logo-contactfile [data state filename]
  (if (nil? (logo-validator @data))
    (do (swap! state assoc :busy true)
        (if (@data :logofile)
          (ajax/POST
           (str server "logos/upload")
           {:body (@data :logofile)
            :response-format :json
            :keywords? true
            :handler #(do (swap! data assoc :filename (:filename %))
                          (logos-post-or-put data state filename))
            :error-handler #(logo-save-onclick-error % state filename)
            :headers {:Authorization
                      (str "Token " (get-in @session [:session :token]))}})
          (logos-post-or-put data state filename)))
    (swap! state assoc :focus true)))

(defn add-or-edit-logo-comp [data]
  (let [data (r/atom data)
        filename (r/atom "")
        state (r/atom {:focus false
                       :busy false})]
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:i.fa.fa-photo]
        [:h3.box-title  (if (@data :id) "Edit " "Add New ") "Logo"]]
       [:div.box-body
        [:div..form-horizontal
         [row-input "Logo Name" "Logo Name" :logoname "text" "Enter Logo Name"
          (r/cursor data [:logoname]) (@state :focus) (logo-validator @data)]
         [:div.form-group
          [:label.col-md-4.control-label "Upload file"]
          [:div.col-md-6
           [file-upload-button "logofile" "logofile"
            accept-types-logo (r/cursor data [:logofile]) filename]]
          [:div.col-md-2
           (when (seq (@data :filename))
             [:button.btn.btn-primary
              {:on-click
               #(.open js/window
                       (str
                        server "logosfilename/"
                        (@data :filename)))}
              [:span.glyphicon.glyphicon-download-alt]])]]]]
       [:div.box-footer.clearfix
        [:center
         (when-not (= (routes/url-for :view-logo :id (@data :id 0))
                      (pushy/get-token routes/history))
           [:button.btn.btn-primary
            {:on-click #(upload-logo-contactfile data state filename)} "Save "
            (when (@state :busy) [:i.fa.fa-spin.fa-refresh])])"  "
         [:a.btn.btn-default
          {:href (routes/url-for :logos)} "Back"]]]])))

;; Unused Components
;; -----------------------------------------------------------
;; Bookingfits Comp
;; -----------------------------------------------------------

(defn bookingfit-validator [data-set]
  (first
   (b/validate
    data-set
    :cityid [[v/required :message "Field is required"]]
    ;; :vendorid [[v/required :message "Field is required"]]
    :rate [[v/required :message "Field is required"]]
    :contactperson [[v/required :message "Field is required"]]
    :contactnumber [[v/required :message "Field is required"]]
    :sictrans [[v/required :message "Field is required"]]
    :pvttrans [[v/required :message "Field is required"]]
    :meetgreet [[v/required :message "Field is required"]]
    :pvtcitytour [[v/required :message "Field is required"]]
    :siccitytour [[v/required :message "Field is required"]]
    :email [[v/required :message "Field is required"]]
    :address [[v/required :message "Field is required"]]
    :bankaccountinfo [[v/required :message "Field is required"]]
    )))

(defn get-bookingfits-list [idx list]
  (ajax/GET
   (str server "bookingfits/" @idx "/10")
   {:handler #(reset! list %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-bookingfits-handler [res idx list]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    200 (get-bookingfits-list idx list)
    (js/console.log "Server error")))

(defn delete-bookingfits-on-click [id idx list]
  (ajax/DELETE
   (str server "bookingfits/" id)
   {:handler #(delete-bookingfits-handler % idx list)
    :error-handler #(delete-bookingfits-handler % idx list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn bookingfits-comp []
  (let [idx (r/atom 1)
        list (r/atom {:data []
                      :totalbookingfits 0})]
    (get-bookingfits-list idx list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:div.col-md-8
         [:h3.box-title "List of FIT's"]]
        [:div.col-md-4
         [:a.btn.btn-primary.pull-right
          {:href (routes/url-for :add-bookingfit)}
          "Add New FIT"]]]
       [:div.box-body.table-responsive
        [:table.table.table-bordered.table-hover
         [:thead
          [:tr
           [:th ""]
           [:th ""]
           [:th "City"]
           [:th "Vendor Name"]
           [:th "Contact Person"]
           [:th "Contact Number"]
           [:th "Rate"]
           [:th "Email/Website"]]]
         [:tbody
          (for [i (@list :data)]
            ^{:key (:id i)}
            [:tr
             [:th [:button.btn.btn-danger.btn-sm
                   {:on-click #(delete-bookingfits-on-click (:id i) idx list)}
                   [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]]
             [:th [:a.btn.btn-success.btn-sm {:href (routes/url-for :edit-bookingfit :id (:id i))}
                   [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]]
             [:td (:cityid i)]
             [:td (:vendorid i)]
             [:td (:contactperson i)]
             [:td (:contactnumber i)]
             [:td (:rate i)]
             [:td (:email i)]])]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalbookingfits) 10)))
          :maxButtons 5
          :activePage @idx
          :onSelect #(do
                       (reset! idx %)
                       (get-bookingfits-list idx list))}]]])))

(defn bookingfit-save-onclick-success [res data state]
  (reset! data {})
  (swap! state assoc :focus false)
  (swap! state update-in [:show-message] not)
  (swap! state assoc :status "success")
  (swap! state assoc :server-message
         "Bookingfit is Successfully saved"))

(defn bookingfit-save-onclick-error [res state]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do
      (swap! state update-in [:focus] not)
      (swap! state update-in [:show-message] not)
      (swap! state assoc :status "error")
      (swap! state assoc :server-message
             "Please try again"))))

(defn bookingfit-save-onclick [data state]
  (if (nil? (bookingfit-validator @data))
    (if (@data :id)
      (do
        (swap! state assoc :show-message false)
        (ajax/PUT
         (str server "bookingfits/" (@data :id))
         {:params @data
          :handler #(pushy/set-token! routes/history (routes/url-for :bookingfits))
          :error-handler #(bookingfit-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}}))
      (do
        (swap! state assoc :show-message false)
        (ajax/POST
         (str server "bookingfits")
         {:params @data
          :handler #(bookingfit-save-onclick-success % data state)
          :error-handler #(bookingfit-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}})))
    (swap! state assoc :focus true)))

(def fit-list ["Yes" "No" "Yes w/pvt"])

(defn add-or-edit-bookingfit-comp [data]
  (let [data (r/atom data)
        cities (r/atom [])
        state (r/atom
               {:focus false
                :show-message false
                :busy false
                :status ""
                :server-message ""})
        vendors (r/atom [])]
    (get-all-cities cities)
    ;; (when (@data :cityid)
    ;;   (get-vendors-by-city (@data :cityid) vendors))
    ;; (add-watch
    ;;  data :watcher
    ;;  (fn [key atom old-state new-state]
    ;;    (when-not (= (old-state :cityid) (new-state :cityid))
    ;;      (get-vendors-by-city (@data :cityid) vendors))))
    (swap! data assoc :vendorid 0)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:h3.box-title  (if (@data :id) "Edit " "Add ") " New FIT"]]
       [:div.box-body
        [:div.row
         [:div.col-md-3]
         [:div.col-md-6
          (when (@state :show-message)
            [:div
             (when (= (@state :status) "success")
               [:div.callout.callout-success
                [:div.close.pull-right
                 {:style {:cursor "pointer"}
                  :on-click #(swap! state update-in [:show-message] not)} "×"]
                [:h4 "Success ! "]
                [:p (@state :server-message)]])
             (when (= (@state :status) "error")
               [:div.callout.callout-warning
                [:span.close.pull-right
                 {:style {:cursor "pointer"}
                  :on-click #(swap! state update-in [:show-message] not)} "×"]
                [:h4 "Warning !"]
                [:p (@state :server-message)]])])]
         [:div.col-md-3]]
        [:div..form-horizontal
         [row-chosen-select "City Name" "Cityname" :cityid cities
          (r/cursor data [:cityid]) (@state :focus)
          (bookingfit-validator @data)]
         ;; [row-chosen-select "Vendor" "Vendor" :vendorid vendors
         ;;  (r/cursor data [:vendorid]) (@state :focus) (bookingfit-validator @data)]
         [row-input "Contact" "Contact" :contactperson "text"
          "Enter  Contact" (r/cursor data [:contactperson])
          (@state :focus) (bookingfit-validator @data)]
         [row-input "Contact Number" "Contact Number" :contactnumber
          "text" "Enter Contact Number" (r/cursor data [:contactnumber])
          (@state :focus) (bookingfit-validator @data)]
         [row-number "Rate" "Rate" :rate
          "Enter Rate" (r/cursor data [:rate])
          (@state :focus) (bookingfit-validator @data)]
         [row-select "SIC Trans" "sictrans" :sictrans fit-list
          (r/cursor data [:sictrans]) (@state :focus) (items-validator @data)]
         [row-select "PVT Trans" "pvttrans" :pvttrans fit-list
          (r/cursor data [:pvttrans]) (@state :focus) (items-validator @data)]
         [row-select "Meet & Greet" "meetgreet" :meetgreet fit-list
          (r/cursor data [:meetgreet]) (@state :focus) (items-validator @data)]
         [row-select "PVT City Tour" "pvtcitytour" :pvtcitytour fit-list
          (r/cursor data [:pvtcitytour]) (@state :focus) (items-validator @data)]
         [row-select "SIC City Tour" "siccitytour" :siccitytour fit-list
          (r/cursor data [:siccitytour]) (@state :focus) (items-validator @data)]
         [row-input "Email/Website" "Email/Website" :email
          "text" "Enter Email/Website" (r/cursor data [:email])
          (@state :focus) (bookingfit-validator @data)]
         [row-text-area "Address" "Address" :address 5 "Enter Address"
          (r/cursor data [:address]) (@state :focus) (bookingfit-validator @data)]
         [row-input "Bank Account Info" "BankaccountInfo" :bankaccountinfo
          "text" "Enter Bank Account Info" (r/cursor data [:bankaccountinfo])
          (@state :focus) (bookingfit-validator @data)]]]
       [:div.box-footer.clearfix
        [:div.col-md-3]
        [:div.col-md-6
         [:center
          [:button.btn.btn-primary
           {:on-click #(bookingfit-save-onclick data state)} "Save"]"  "
          [:a.btn.btn-default
           {:href (routes/url-for :bookingfits)} "Back"]]]
        [:div.col-md-3]]])))


;; -----------------------------------------------
;; vendors component
;; -----------------------------------------------
(defn vendor-validator [data-set]
  (first
   (b/validate
    data-set
    :cityid [[v/required :message "Field is required"]]
    :vendorname [[v/required :message "Field is required"]]
    :contactname [[v/required :message "Field is required"]]
    :phonenumber [[v/required :message "Field is required"]]
    :emailid [[v/required :message "Field is required"]
              [v/email :message "Please enter valid email id"]]
    :address [[v/required :message "Field is required"]]
    )))

(defn get-vendors-list [idx list]
  (ajax/GET
   (str server "vendors/" @idx "/10")
   {:handler #(reset! list %)
    :error-handler error-handler
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))

(defn delete-vendors-handler [res idx list]
  (case (:status res)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    200 (get-vendors-list idx list)
    (js/console.log "Server error")))

(defn delete-vendors-on-click [id idx list]
  (ajax/DELETE
   (str server "vendors/" id)
   {:handler #(delete-vendors-handler % idx list)
    :error-handler #(delete-vendors-handler % idx list)
    :format :json
    :response-format :json
    :keywords? true
    :headers
    {:Authorization
     (str "Token " (get-in @session [:session :token]))}}))


(defn vendors-comp []
  (let [idx (r/atom 1)
        list (r/atom {:data []
                      :totalvendors 0})]
    (get-vendors-list idx list)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:div.col-md-8
         [:h3.box-title "List of Vendors"]]
        [:div.col-md-4
         [:a.btn.btn-primary.pull-right
          {:href (routes/url-for :add-vendor)}
          "Add New Vendor"]]]
       [:div.box-body.table-responsive
        [:table.table.table-bordered.table-hover
         [:thead
          [:tr
           [:th ""]
           [:th ""]
           [:th "City"]
           [:th "Vendor Name"]
           [:th "Contact Name"]
           [:th "Email Id"]
           [:th "Phone Number"]]]
         [:tbody
          (for [i (@list :data)]
            ^{:key (:id i)}
            [:tr
             [:td [:button.btn.btn-danger.btn-sm
                   {:on-click #(delete-vendors-on-click (:id i) idx list)}
                   [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]]
             [:th [:a.btn.btn-success.btn-sm {:href (routes/url-for :edit-vendor :id (:id i))}
                   [:span.glyphicon.glyphicon-edit {:aria-hidden true}]]]
             [:td (:cityid i)]
             [:td (:vendorname i)]
             [:td (:contactname i)]
             [:td (:emailid i)]
             [:td (:phonenumber i)]])]]]
       [:div.box-footer
        [pagination
         {:class "pull-right no-margin"
          :prev true
          :next true
          :first true
          :last true
          :ellipsis true
          :boundaryLinks true
          :items (int (Math/ceil (/ (@list :totalvendors) 10)))
          :maxButtons 5
          :activePage @idx
          :onSelect #(do
                       (reset! idx %)
                       (get-vendors-list idx list))}]]])))

(defn vendor-save-onclick-success [res data state]
  (reset! data {})
  (swap! state assoc :focus false)
  (swap! state update-in [:show-message] not)
  (swap! state assoc :status "success")
  (swap! state assoc :server-message
         "Vendor is Successfully saved"))

(defn vendor-save-onclick-error [res state]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do
      (swap! state update-in [:focus] not)
      (swap! state update-in [:show-message] not)
      (swap! state assoc :status "error")
      (swap! state assoc :server-message
             "Please try again"))))

(defn vendor-save-onclick [data state]
  (if (nil? (vendor-validator @data))
    (if (@data :id)
      (do
        (swap! state assoc :show-message false)
        (ajax/PUT
         (str server "vendors/" (@data :id))
         {:params @data
          :handler #(pushy/set-token! routes/history (routes/url-for :vendors))
          :error-handler #(vendor-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}}))
      (do
        (swap! state assoc :show-message false)
        (ajax/POST
         (str server "vendors")
         {:params @data
          :handler #(vendor-save-onclick-success % data state)
          :error-handler #(vendor-save-onclick-error % state)
          :format :json
          :response-format :json
          :keywords? true
          :headers {:Authorization
                    (str "Token " (get-in @session [:session :token]))}})))
    (swap! state assoc :focus true)))


(defn add-or-edit-vendor-comp [data]
  (let [data (r/atom data)
        state (r/atom
               {:focus false
                :show-message false
                :busy false
                :status ""
                :server-message ""})
        cities (r/atom [])]
    (get-all-cities cities)
    (fn []
      [:div.box.box-primary
       [:div.box-header.with-border
        [:h3.box-title  (if (@data :id) "Edit " "Add ") " New Vendor"]]
       [:div.box-body
        [:div.row
         [:div.col-md-3]
         [:div.col-md-6
          (when (@state :show-message)
            [:div
             (when (= (@state :status) "success")
               [:div.callout.callout-success
                [:div.close.pull-right
                 {:style {:cursor "pointer"}
                  :on-click #(swap! state update-in [:show-message] not)} "×"]
                [:h4 "Success ! "]
                [:p (@state :server-message)]])
             (when (= (@state :status) "error")
               [:div.callout.callout-warning
                [:span.close.pull-right
                 {:style {:cursor "pointer"}
                  :on-click #(swap! state update-in [:show-message] not)} "×"]
                [:h4 "Warning !"]
                [:p (@state :server-message)]])])]
         [:div.col-md-3]]
        [:div..form-horizontal
         [row-chosen-select "City" "City" :cityid cities
          (r/cursor data [:cityid]) (@state :focus) (items-validator @data)]
         [row-input "Vendor Name" "Vendor Name" :vendorname "text" "Enter Vendor Name"
          (r/cursor data [:vendorname]) (@state :focus) (vendor-validator @data)]
         [row-input "Contact Name" "Contact Name" :contactname "text"
          "Enter Contact Name"
          (r/cursor data [:contactname]) (@state :focus) (vendor-validator @data)]
         [row-input "Phone Number" "Phone Number" :phonenumber "text"
          "Enter Phone Number"
          (r/cursor data [:phonenumber]) (@state :focus) (vendor-validator @data)]
         [row-input "Email Id" "Email Id" :emailid "email"
          "Enter Email Id"
          (r/cursor data [:emailid]) (@state :focus) (vendor-validator @data)]
         [row-text-area "Address" "Address" :address 5
          "Enter Address"
          (r/cursor data [:address]) (@state :focus) (vendor-validator @data)]]]
       [:div.box-footer.clearfix
        [:div.col-md-3]
        [:div.col-md-6
         [:center
          [:button.btn.btn-primary
           {:on-click #(vendor-save-onclick data state)} "Save"]"  "
          [:a.btn.btn-default
           {:href (routes/url-for :vendors)} "Back"]]]
        [:div.col-md-3]]])))

;; --------------------------------------------------
;; Login Comp
;; --------------------------------------------------

(defn login-validator [data-set]
  (first
   (b/validate
    data-set
    :email [[v/required :message "Field is required"]
            [v/email :message "Enter valid email-id"]]
    :password [[v/required :message "Field is required"]
               [v/string  :message "Enter valid password"]])))

(defn login-input [id span-class type data-set placeholder focus val]
  [:div.form-group.has-feedback
   [input id type placeholder val]
   [:span {:class span-class}]
   (if @focus
     (if (= nil (login-validator @data-set))
       [:div]
       [:div {:style  {:color "red"}}
        (str (first ((login-validator @data-set) id)))])
     [:div])])

;; ajax call for login
;; ------------------------------------
(defn login-success-handler [res server-error]
  (swap! session assoc :session res)
  (let [role (get-in @session [:session :user :role])]
    (case role
      "bookadmin" (pushy/set-token!
                   routes/history
                   (routes/url-for :fit-quotations))
      "bookstandard" (pushy/set-token!
                      routes/history
                      (routes/url-for :fit-quotations))
      "bookgroup" (pushy/set-token!
                   routes/history
                   (routes/url-for :group-quotations))
      (reset! server-error "Not a valid role !"))))

(defn login-error-handler [res server-error]
  (reset! server-error (get-in res [:response :message] "Server Error")))

(defn login-on-click [data focus server-error]
  (if (nil? (login-validator @data))
    (do
      (reset! server-error "")
      (ajax/POST
       (str server "login")
       {:params @data
        :handler #(login-success-handler % server-error)
        :error-handler #(login-error-handler % server-error)
        :format :json
        :response-format :json
        :keywords? true}))
    (reset! focus true)))

(defn login-comp []
  (let [login-data (r/atom {})
        focus (r/atom nil)
        server-error (r/atom nil)]
    (fn []
      [:div.login-box
       [:div.login-logo
        [:span "Log-in"]]
       [:div.login-box-body
        [login-input :email
         "glyphicon glyphicon-envelope form-control-feedback"
         "email" login-data "Email" focus
         (r/cursor login-data [:email])]
        [login-input :password
         "glyphicon glyphicon-lock form-control-feedback"
         "password" login-data "Password" focus
         (r/cursor login-data [:password])]
        (when @server-error
          [:div {:style  {:color "red"}}
           (str @server-error)])
        [:div.row
         [:div.col-sm-12
          [:button.btn.btn-primary.btn-block
           {:on-click #(login-on-click login-data focus server-error)}
           " Sign In"]]]]])))

;; Register Comp
;; ------------------------------------------------------
(defn register-validator [data-set]
  (first
   (b/validate
    data-set
    :firstname [[v/required :message "Field is required"]]
    :lastname [[v/required :message "Field is required"]]
    :email [[v/required :message "Field is required"]
            [v/email :message "Enter valid email-id"]]
    :password [[v/required :message "Field is required"]]
    ;; :mobilenumber [[v/required :message "Field is required"]]
    :role [[v/required :message "Field is required"]])))

(defn register-input [id span-class type data-set placeholder focus val]
  [:div.form-group.has-feedback
   [input id type placeholder val]
   [:span {:class span-class}]
   (if focus
     (if (= nil (register-validator @data-set))
       [:div]
       [:div {:style  {:color "red"}}
        [:b (str (first ((register-validator @data-set) id)))]])
     [:div])])

(def register-options
  [{:id "bookadmin" :name "Admin"}
   {:id "bookstandard" :name "Standard"}
   {:id "bookgroup" :name "Group"}] )

(defn reg-select [id options val]
  [:select.form-control
   {:id id
    :value (or @val "")
    :on-change #(reset! val (-> % .-target .-value))}
   (for [d options]
     ^{:key (:id d)}
     [:option {:value (:id d)} (:name d) ])])


(defn register-select [id options data-set focus val]
  [:div.form-group.has-feedback
   [reg-select id options val]
   (if focus
     (if (= nil (register-validator @data-set))
       [:div]
       [:div {:style  {:color "red"}}
        [:b (str (first ((register-validator @data-set) id)))]])
     [:div])])

(defn register-success [res data state]
  (reset! data {:role "bookadmin"
                :mobilenumber ""})
  (swap! state assoc :focus false)
  (swap! state update-in [:show-message] not)
  (swap! state assoc :status "success")
  (swap! state assoc :server-message
         "User Successfully Created")
  (boot-notify "success" "Success :" "User successfully created"))

(defn register-error [res state]
  (case (res :status)
    401 (do (swap! session dissoc :session)
            (pushy/set-token! routes/history (routes/url-for :home)))
    (do
      (swap! state update-in [:focus] not)
      (swap! state update-in [:show-message] not)
      (swap! state assoc :status "error")
      (swap! state assoc :server-message
             "Please Try Again")
      (boot-notify "warning" "Warning :" "Please try again"))))


(defn register-onclick [data state]
  (if (nil? (register-validator @data))
    (do
      (swap! state assoc :show-message false)
      (ajax/POST
       (str server "register")
       {:params @data
        :handler #(register-success % data state)
        :error-handler #(register-error % state)
        :format :json
        :response-format :json
        :keywords? true
        :headers {:Authorization
                  (str "Token " (get-in @session [:session :token]))}}))
    (swap! state assoc :focus true)))

(defn register-comp []
  (let [register-data (r/atom {:role "bookadmin"
                               :mobilenumber ""})
        state (r/atom
               {:focus false
                :show-message false
                :busy false
                :status ""
                :server-message ""})]
    (fn []
      [:div
       #_[:div.row
          [:div.col-md-3]
          [:div.col-md-6
           (when (@state :show-message)
             [:div
              (when (= (@state :status) "success")
                [:div.callout.callout-success
                 [:div.close.pull-right
                  {:style {:cursor "pointer"}
                   :on-click #(swap! state update-in [:show-message] not)} "×"]
                 [:h4 "Success ! "]
                 [:p (@state :server-message)]])
              (when (= (@state :status) "error")
                [:div.callout.callout-warning
                 [:span.close.pull-right
                  {:style {:cursor "pointer"}
                   :on-click #(swap! state update-in [:show-message] not)} "×"]
                 [:h4 "Warning !"]
                 [:p (@state :server-message)]])])]
          [:div.col-md-3]]
       [:div.register-box
        [:div.register-logo
         [:span "Register New User"]]
        [:div.register-box-body
         [register-input :firstname
          "glyphicon glyphicon-user form-control-feedback"
          "text" register-data "First Name" (@state :focus)
          (r/cursor register-data [:firstname])]
         [register-input :lastname
          "glyphicon glyphicon-user form-control-feedback"
          "text" register-data "Last Name" (@state :focus)
          (r/cursor register-data [:lastname])]
         [register-input :email
          "glyphicon glyphicon-envelope form-control-feedback"
          "email" register-data "Email" (@state :focus)
          (r/cursor register-data [:email])]
         [register-input :password
          "glyphicon glyphicon-lock form-control-feedback"
          "password" register-data "Password" (@state :focus)
          (r/cursor register-data [:password])]
         [register-input :mobilenumber
          "glyphicon glyphicon-phone form-control-feedback"
          "text" register-data "Mobile Number" (@state :focus)
          (r/cursor register-data [:mobilenumber])]
         [register-select :role register-options
          register-data (@state :focus)
          (r/cursor register-data [:role])]
         [:div.row
          [:div.col-sm-12
           [:button.btn.btn-primary.btn-block
            {:type "button"
             :on-click #(register-onclick register-data state)}
            "Register"]]]]]])))

;; --------------------------------------------------
;; All tabs component
;; --------------------------------------------------
(defn check-all-routes [word length]
  (= word (apply str (take length (pushy/get-token routes/history)))))


(defn itineraries-tabs-comp [id comp]
  (let [role (get-in @session [:session :user :role])]
    [:div.nav-tabs-custom
     [:ul.nav.nav-tabs
      (when (or (= role "bookadmin") (= role "bookstandard"))
        [:li {:class (str "dropdown " (when (check-all-routes "/fit" 4) "active"))}
         [:a.dropdown-toggle
          {:href "#",
           :data-toggle "dropdown"} " FIT " [:span.caret]]
         [:ul.dropdown-menu
          [:li (when (= (routes/url-for :fit-quotations) (pushy/get-token routes/history))
                 {:class "active"})
           [:a {:href (routes/url-for :fit-quotations)
                :tabindex "-1", :role "menuitem"} "FIT Quotations"]]
          [:li (when (or
                      (= (routes/url-for :fitbookings) (pushy/get-token routes/history))
                      (= (routes/url-for :fit-activitylog :id id) (pushy/get-token routes/history)))
                 {:class "active"})
           [:a {:href (routes/url-for :fitbookings)
                :tabindex "-1", :role "menuitem"} "FIT Bookings"]]
          [:li (when (or
                      (= (routes/url-for :add-fitbooking) (pushy/get-token routes/history))
                      (= (routes/url-for :edit-fitbooking :id id) (pushy/get-token routes/history)))
                 {:class "active"})
           [:a {:href (routes/url-for :add-fitbooking)} "Add FIT Booking"]]]])

      (when (or (= role "bookadmin") (= role "bookgroup"))
        [:li {:class (str "dropdown " (when (check-all-routes "/group" 6) "active"))}
         [:a.dropdown-toggle
          {:href "#",
           :data-toggle "dropdown"} " Group " [:span.caret]]
         [:ul.dropdown-menu
          [:li (when (= (routes/url-for :group-quotations) (pushy/get-token routes/history))
                 {:class "active"})
           [:a {:href (routes/url-for :group-quotations)} "Group Quotations"]]
          [:li (when (or
                      (= (routes/url-for :groupbookings) (pushy/get-token routes/history))
                      (= (routes/url-for :group-activitylog :id id) (pushy/get-token routes/history)))
                 {:class "active"})
           [:a {:href (routes/url-for :groupbookings)} "Group Bookings"]]
          [:li (when (or
                      (= (routes/url-for :add-groupbooking) (pushy/get-token routes/history))
                      (= (routes/url-for :edit-groupbooking :id id) (pushy/get-token routes/history)))
                 {:class "active"})
           [:a {:href (routes/url-for :add-groupbooking)} "Add Group Booking"]]]])

      [:li {:class (str "dropdown " (when (check-all-routes "/vendors" 8) "active"))}
       [:a.dropdown-toggle
        {:href "#",
         :data-toggle "dropdown"} " Vendors " [:span.caret]]
       [:ul.dropdown-menu
        [:li (when (or (= (routes/url-for :hotels) (pushy/get-token routes/history))
                       (= (routes/url-for :add-hotel) (pushy/get-token routes/history))
                       (= (routes/url-for :edit-hotel :id id) (pushy/get-token routes/history))
                       (= (routes/url-for :view-hotel :id id) (pushy/get-token routes/history)))
               {:class "active"})
         [:a {:href (routes/url-for :hotels)}  "Hotels"]]

        [:li (when (or (= (routes/url-for :restaurants) (pushy/get-token routes/history))
                       (= (routes/url-for :add-restaurant) (pushy/get-token routes/history))
                       (= (routes/url-for :edit-restaurant :id id) (pushy/get-token routes/history))
                       (= (routes/url-for :view-restaurant :id id) (pushy/get-token routes/history)))
               {:class "active"})
         [:a {:href (routes/url-for :restaurants)}  "Restaurants"]]

        [:li (when (or (= (routes/url-for :tourguides) (pushy/get-token routes/history))
                       (= (routes/url-for :add-tourguide) (pushy/get-token routes/history))
                       (= (routes/url-for :edit-tourguide :id id) (pushy/get-token routes/history))
                       (= (routes/url-for :view-tourguide :id id) (pushy/get-token routes/history)))
               {:class "active"})
         [:a {:href (routes/url-for :tourguides)}  "Tour Guides"]]

        [:li (when (or (= (routes/url-for :transportations) (pushy/get-token routes/history))
                       (= (routes/url-for :add-transportation) (pushy/get-token routes/history))
                       (= (routes/url-for :edit-transportation :id id) (pushy/get-token routes/history))
                       (= (routes/url-for :view-transportation :id id) (pushy/get-token routes/history)))
               {:class "active"})
         [:a {:href (routes/url-for :transportations)}  "Transportations"]]

        ;; [:li (when (or (= (routes/url-for :bookingfits) (pushy/get-token routes/history))
        ;;                (= (routes/url-for :add-bookingfit) (pushy/get-token routes/history))
        ;;                (= (routes/url-for :edit-bookingfit :id id) (pushy/get-token routes/history)))
        ;;        {:class "active"})
        ;;  [:a {:href (routes/url-for :bookingfits)}  "F.I.T's"]]

        [:li (when (or (= (routes/url-for :events) (pushy/get-token routes/history))
                       (= (routes/url-for :add-event) (pushy/get-token routes/history))
                       (= (routes/url-for :edit-event :id id) (pushy/get-token routes/history))
                       (= (routes/url-for :view-event :id id) (pushy/get-token routes/history)))
               {:class "active"})
         [:a {:href (routes/url-for :events)}  "Events"]]]]

      [:li (when  (or (= (routes/url-for :items) (pushy/get-token routes/history))
                      (= (routes/url-for :add-item) (pushy/get-token routes/history))
                      (= (routes/url-for :edit-item :id id) (pushy/get-token routes/history))
                      (= (routes/url-for :view-item :id id) (pushy/get-token routes/history)))
             {:class "active"})
       [:a {:href (routes/url-for :items)}  "Activities"]]

      [:li (when (or (= (routes/url-for :cities) (pushy/get-token routes/history))
                     (= (routes/url-for :add-city) (pushy/get-token routes/history))
                     (= (routes/url-for :edit-city :id id) (pushy/get-token routes/history)))
             {:class "active"})
       [:a {:href (routes/url-for :cities)}  "Cities"]]

      ;; [:li (when (or (= (routes/url-for :vendors) (pushy/get-token routes/history))
      ;;                (= (routes/url-for :add-vendor) (pushy/get-token routes/history))
      ;;                (= (routes/url-for :edit-vendor :id id) (pushy/get-token routes/history)))
      ;;        {:class "active"})
      ;;  [:a {:href (routes/url-for :vendors)}  "Vendors"]]


      [:li (when (or (= (routes/url-for :logos) (pushy/get-token routes/history))
                     (= (routes/url-for :add-logo) (pushy/get-token routes/history))
                     (= (routes/url-for :edit-logo :id id) (pushy/get-token routes/history))
                     (= (routes/url-for :view-logo :id id) (pushy/get-token routes/history)))
             {:class "active"})
       [:a {:href (routes/url-for :logos)}  "Logos"]]
      [:li (when (or (= (routes/url-for :complete-activity-log) (pushy/get-token routes/history)))
             {:class "active"})
       [:a {:href (routes/url-for :complete-activity-log)}
        "Complete Activity Log"]]
      [:li (when (or (= (routes/url-for :complete-payment-log) (pushy/get-token routes/history)))
             {:class "active"})
       [:a {:href (routes/url-for :complete-payment-log)}
        "Complete Payment Log"]]

      [:li.pull-right
       [:div
        (when (= role "bookadmin")
          [:a.btn.btn-default {:href (routes/url-for :register)} "Register"])" "
        [:button.btn.btn-primary
         {:on-click #(do (swap! session  dissoc :session)
                         (pushy/set-token!
                          routes/history
                          (routes/url-for :home)))}
         "Logout"]]]]
     [:div.tab-content
      [:div.tab-pane.active comp]]]))
