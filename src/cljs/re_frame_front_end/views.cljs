(ns re-frame-front-end.views
  (:require [re-frame.core :as re-frame]
            [re-frame-front-end.subs :as subs]
            ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
   [:div 
     [:div#search
          [:label "Within"]
          [:input#distance {:type "text"}]
          [:span "km "]
          [:label "of"]
          [:input#address {:type "text"}]
          [:button#location-search "Search"]
          [:label {:style {:margin-left "40px"}} "...or search on a keyword"]
          [:input#who  {:type "text"}]
          [:button#word-search "Search"]
      ]
      [:div#music
        [:div#gigs
            [:table#gig-table]
        ]
        [:div#songs
            [:table#song-table]
        ]
      ]
    ]
))

