(ns re-frame-front-end.gigs
    (:require [cognitect.transit :as t]
              [chord.client :refer [ws-ch]]
              [cljs.core.async :refer [<! >! put! close! timeout]]))

(enable-console-print!)
             
(defonce ws-chan (atom nil))
(def json-reader (t/reader :json))

(defn receive-transit-msg!  [update-fn]
 (fn [msg] (update-fn (->> msg .-data (t/read json-reader)))))

(defn send-transit-msg!  [msg]
 (println "Sending message")
 (if @ws-chan
   (.send @ws-chan msg)
   (throw (js/Error. "Websocket is not available!"))))

(defn make-websocket! [url receive-handler connected-handler]
 (println "attempting to connect websocket")
 (if-let [chan (js/WebSocket. url)]
   (do
     (set! (.-onmessage chan) (receive-transit-msg! receive-handler))
     (reset! ws-chan chan)
     (println "Websocket connection established with: " url)
     (set! (.-onopen chan) connected-handler))
   (throw (js/Error. "Websocket connection failed!"))))

;test
