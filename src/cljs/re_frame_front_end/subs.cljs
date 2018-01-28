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
