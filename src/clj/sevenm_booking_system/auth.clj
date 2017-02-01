(ns sevenm-booking-system.auth
  (:require [buddy.auth.backends.token :refer [token-backend]]
            [buddy.auth.accessrules :refer [success error]]
            [sevenm-booking-system.db.core :as db]
            [buddy.auth :refer [authenticated?]]
            [crypto.random :refer [base64 url-part]]))

(defn gen-session-id [] (url-part 32))

(defn make-token!
  "Creates an auth token in the database for the given user and puts it in the dat   abase"
  [user-id]
  (let [token (gen-session-id)]
    (db/insert-token {:id token
                      :user_id user-id})
    token))

(defn authenticate-token
  "Validates a token, returning the id of the associated user when valid and nil otherwise"
  [req token]
  (println token)
  (:user_id (db/validate-token
             {:token token})))

(defn unauthorized-handler [req msg]
  {:status 401
   :body {:status :error
          :message (or msg "User not authorized")}})

(def auth-backend (token-backend
                   {:authfn authenticate-token
                    :unauthorized-handler unauthorized-handler}))

(defn authenticated-user [req]
  (if (authenticated? req)
    true
    (error "User must be authenticated")))
