(ns re-frame-front-end.events
  (:require [re-frame.core :as rf]
            [re-frame-front-end.db :as db]
            [re-frame-front-end.gigs :refer [make-websocket! send-transit-msg!]]
))

(def ws-url
    ;"ws://localhost:8080/ws") 
    "ws://serene-harbor-24890.herokuapp.com/ws")

(rf/reg-event-db
 :initialize-db
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

(rf/reg-event-db
  :select-gig
  (fn [db [_ gig]]
    (println "select gig " (get gig "artist"))
    (assoc db :selected-gig gig)))

(rf/reg-event-fx
  :select-song
  (fn [cofx [_ song]]
    (println "select song " song) 
    (merge-with into 
                cofx 
                {:db {:selected-song song} 
                 :play-song {:song-url (get song "streamUrl")}})))
      
(rf/reg-event-fx
  :keyword-search
  (fn [{:keys [db]} _]
    {:open-ws {:url ws-url
               :on-open [:send (:keyword db)]
               :on-data [:add-gig]}}))

(rf/reg-event-fx
  :location-search
  (fn [{:keys [db]} _]
    (println "location search")
    {:open-ws {:url ws-url 
               :on-open [:send (str (:address db) ";" (:distance db))]
               :on-data [:add-gig]}}))

(rf/reg-event-fx
  :send
  (fn [_ [_ data]]
    {:send-to-ws {:message data}}))

(rf/reg-event-fx
  :add-gig
  (fn [cofx [_ gig]]
      (update-in cofx [:db :gigs] #(conj % gig))))

(rf/reg-fx
   :send-to-ws
   (fn [{message :message}]
     (print "Sending to Websocket " message)
     (send-transit-msg! message)))

(rf/reg-fx
   :open-ws
   (fn [{url :url
         on-open :on-open
         on-data :on-data}]
     (println "Opening Websocket " url)
     (make-websocket! url #(rf/dispatch (conj on-data %)) #(rf/dispatch on-open))))

(def audio (atom (js/Audio.)))
(rf/reg-fx
    :play-song
    (fn [{song-url :song-url}]
         (println "Playing " song-url)
         (.pause @audio)
         (reset! audio (js/Audio. song-url))
         (.play @audio)))
