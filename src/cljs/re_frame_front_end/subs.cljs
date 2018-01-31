(ns re-frame-front-end.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :gigs
 (fn [db]
   (:gigs db)))

(re-frame/reg-sub
 :keyword
 (fn [db]
   (:keyword db)))

(re-frame/reg-sub
 :address
 (fn [db]
   (:address db)))

(re-frame/reg-sub
 :distance
 (fn [db]
   (:distance db)))
