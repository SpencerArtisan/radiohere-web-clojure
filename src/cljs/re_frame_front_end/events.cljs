(ns re-frame-front-end.events
  (:require [re-frame.core :as rf]
            [re-frame-front-end.db :as db]
            [radiohere.gigs :refer [make-websocket! send-transit-msg!]]
))

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db               
  :keyword-change          
  (fn [db [_ new-value]]
    (assoc db :keyword new-value)))

(rf/reg-event-db               
  :distance-change          
  (fn [db [_ new-value]]
    (assoc db :distance new-value)))

(rf/reg-event-db               
  :address-change          
  (fn [db [_ new-value]]
    (assoc db :address new-value)))

(rf/reg-event-fx
  :keyword-search
  (fn [{:keys [db]} _]
    {:open-ws {:url "ws://serene-harbor-24890.herokuapp.com/ws" ;"ws://localhost:8080/ws" 
               :on-open [:send (:keyword db)]
               :on-data [:add-gig]} }))

(rf/reg-event-fx
  :location-search
  (fn [{:keys [db]} _]
    (println "location search")
    {:open-ws {:url "ws://serene-harbor-24890.herokuapp.com/ws" ;"ws://localhost:8080/ws" 
               :on-open [:send (str (:address db) ";" (:distance db))]
               :on-data [:add-gig]}
     :db db}))

(rf/reg-fx
   :open-ws
   (fn [{url :url
         on-open :on-open
         on-data :on-data}]
     (println "Opening Websocket " url)
     (make-websocket! url #(rf/dispatch (conj on-data %)) #(rf/dispatch on-open))))

(rf/reg-event-fx
  :send
  (fn [{:keys [db]} [_ data]]
    {:send-to-ws {:message data}}))

(rf/reg-event-fx
  :add-gig
  (fn [cofx [_ gig]]
    (print "Received gig " gig)
    (update-in cofx [:db :gigs] #(conj % {:artist (get gig "artist") :venueName (get gig "venueName") :distance (get gig "distance")}))))
    

(rf/reg-fx
   :send-to-ws
   (fn [{message :message}]
     (print "Sending to Websocket " message)
     (send-transit-msg! message)))

