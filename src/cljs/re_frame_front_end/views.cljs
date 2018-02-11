(ns re-frame-front-end.views
  (:require [re-frame-front-end.subs :as subs]
            [re-frame.core :as rf]))

(defn gig-list
  []
  (let [gigs @(rf/subscribe [:gigs])
        selected-gig @(rf/subscribe [:selected-gig])]
    [:table#gig-table
     [:tbody
      (for [gig gigs]
        [:tr {:class (if (= gig selected-gig) :selected nil)
              :on-click #(rf/dispatch [:select-gig gig])}
          [:td (get gig "artist")]
          [:td (get gig "venueName")]
          [:td (get gig "distance")]
          [:td (get gig "date")]])]]))
        
(defn song-list
  []
  (let [gig @(rf/subscribe [:selected-gig])
        selected-song @(rf/subscribe [:selected-song])]
    [:table#song-table
     [:tbody
      (for [song (get gig "tracks")]
        [:tr {:class (if (= song selected-song) :selected nil)
              :on-click #(rf/dispatch [:select-song song])}
          [:td (get song "title")]])]]))
        
(defn main-panel []
  [:div 
    [:div#search
        [:label "Within"]
        [:input#distance {:type "text"
                          :value @(rf/subscribe [:distance])
                          :on-change #(rf/dispatch [:distance-change (-> % .-target .-value)])}]
        [:span "km "]
        [:label "of"]
        [:input#address {:type "text"
                         :value @(rf/subscribe [:address])
                         :on-change #(rf/dispatch [:address-change (-> % .-target .-value)])}]
        [:button#location-search {:on-click #(rf/dispatch [:location-search])} "Search"]
        [:label {:style {:margin-left "40px"}} "...or search on a keyword"]
        [:input#who {:type "text"
                     :value @(rf/subscribe [:keyword])
                     :on-change #(rf/dispatch [:keyword-change (-> % .-target .-value)])}]
        [:button#word-search {:on-click #(rf/dispatch [:keyword-search])} "Search"]]
      
    [:div#music
      [:div#gigs (when (seq @(rf/subscribe [:gigs])) [gig-list])]
      [:div#songs (when (seq @(rf/subscribe [:selected-gig])) [song-list])]]])
        
      
    


