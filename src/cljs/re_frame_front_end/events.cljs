(ns re-frame-front-end.events
  (:require [re-frame.core :as rf]
            [re-frame-front-end.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db               
  :keyword-change          
  (fn [db [_ new-value]]
    (assoc db :keyword new-value)))

(rf/reg-event-db               
  :keyword-search
  (fn [db _]
    (assoc db :gigs [{:artist "test" :venueName "The Garage" :distance 2}])))

