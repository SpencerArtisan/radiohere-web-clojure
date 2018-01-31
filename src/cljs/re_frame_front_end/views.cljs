(ns re-frame-front-end.views
  (:require [re-frame.core :as rf]
            [re-frame-front-end.subs :as subs]
            ))

(defn gig-list
  []
  (let [gigs @(rf/subscribe [:gigs])]
    [:table#gig-table
      (for [gig gigs]
        [:tr 
          [:td (:artist gig)]
          [:td (:venueName gig)]
          [:td (:distance gig)]
        ])
      ]
))

(defn main-panel []
   [:div 
     [:div#search
          [:label "Within"]
          [:input#distance {:type "text"}]
          [:span "km "]
          [:label "of"]
          [:input#address {:type "text"}]
          [:button#location-search 
            {:on-click #(rf/dispatch [:location-search])}
            "Search"]
          [:label {:style {:margin-left "40px"}} "...or search on a keyword"]
          [:input#who {:type "text"
            :value @(rf/subscribe [:keyword])
            :on-change #(rf/dispatch [:keyword-change (-> % .-target .-value)])}]
          [:button#word-search 
            {:on-click #(rf/dispatch [:keyword-search])}
            "Search"]
      ]
      [:div#music
        [:div#gigs
            (when (seq @(rf/subscribe [:gigs])) [gig-list])
        ]
        [:div#songs
            [:table#song-table]
        ]
      ]
    ]
)

