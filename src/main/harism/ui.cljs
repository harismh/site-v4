(ns harism.ui
  (:require [harism.three :as three]))

(defn anchor [{:keys [href on-click c cl target id]}]
  [:a.text-zinc-900.dark:text-zinc-100.hover:text-yellow-500.transition.duration-150
   {:href href
    :class cl
    :on-click on-click
    :target target
    :id id}
   c])

(defn paragraph [{:keys [c cl]}]
  [:p.text-zinc-900.dark:text-zinc-100.font-serif
   {:class (str "text-lg/8 " cl)}
   c])

(defn heading [{:keys [c cl]}]
  [:h1.text-zinc-900.dark:text-zinc-100
   {:class cl}
   c])

(defn section [& body]
  `[:div.flex.flex-col.gap-4 ~@body])

(defn canvas-dims []
  [(- (/ (.-innerWidth js/window) 2) 50)
   (/ (.-innerHeight js/window) 1.5)])

(defn color-scheme []
  (if (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
    :dark
    :light))

(defn render-knot! [{:keys [width height canvas-id]}]
  (when (js/document.getElementById canvas-id)
    (let [scene    (three/scene)
          camera   (three/camera 30 (/ width height) 1 100)
          geometry (three/torus-knot-geometry
                    {:radius 4
                     :tube 0.2
                     :tubular-segments 512
                     :radial-segments 256
                     :p 7
                     :q 10})
          material (three/mesh-material)
          mesh     (three/mesh geometry material)
          renderer (three/renderer width height canvas-id)
          color    (case (color-scheme) 
                     :dark "rgb(244, 244, 245)"
                     "rgb(24, 24, 27)")
          effect   (three/ascii-effect {:color color})
          composer (three/composer renderer scene camera effect) 
          orbit    (three/orbit-controls camera (.-domElement renderer))
          _        (js/window.addEventListener
                    "resize"
                    (fn []
                      (let [[width' height'] (canvas-dims)]
                        (.set (.-position camera) 0 0 (* width' 0.05))
                        (set! (.-aspect camera) (/ width' height'))
                        (.updateProjectionMatrix camera)
                        (.setSize composer width' height'))))]
      (.set (.-position camera) 0 0 (* width 0.05))
      (set! (.-autoRotate orbit) true)
      (set! (.-autoRotateSpeed orbit) 2)
      (set! (.-enableZoom orbit) false)
      (set! (.-enableDamping  orbit) true)
      (.add scene camera)
      (.add scene mesh)
      (three/animate! orbit composer scene camera)
      :ok)))

(def spinner-fs
  [:div.grid.h-screen.place-items-center.bg-zinc-50.dark:bg-zinc-900
   {:aria-label "Loading..." :role "status"}
   [:svg.h-10.w-10.stroke-zinc-900.dark:stroke-zinc-100
    {:view-box "0 0 256 256"}
    [:line {:x1 "128", :y1 "32", :x2 "128", :y2 "64", :stroke-linecap "round", :stroke-linejoin "round", :stroke-width "20"}]
    [:line {:x1 "195.9", :y1 "60.1", :x2 "173.3", :y2 "82.7", :stroke-linecap "round", :stroke-linejoin "round", :stroke-width "20"}]
    [:line {:x1 "224", :y1 "128", :x2 "192", :y2 "128", :stroke-linecap "round", :stroke-linejoin "round", :stroke-width "20"}]
    [:line {:x1 "195.9", :y1 "195.9", :x2 "173.3", :y2 "173.3", :stroke-linecap "round", :stroke-linejoin "round", :stroke-width "20"}]
    [:line {:x1 "128", :y1 "224", :x2 "128", :y2 "192", :stroke-linecap "round", :stroke-linejoin "round", :stroke-width "20"}]
    [:line {:x1 "60.1", :y1 "195.9", :x2 "82.7", :y2 "173.3", :stroke-linecap "round", :stroke-linejoin "round", :stroke-width "20"}]
    [:line {:x1 "32", :y1 "128", :x2 "64", :y2 "128", :stroke-linecap "round", :stroke-linejoin "round", :stroke-width "20"}]
    [:line {:x1 "60.1", :y1 "60.1", :x2 "82.7", :y2 "82.7", :stroke-linecap "round", :stroke-linejoin "round", :stroke-width "20"}]]])

(def todo
  [:article (paragraph {:c "Under construction."})])

(def home
  [:<>
   [:article.flex.flex-col.gap-20
    [section
     (paragraph
      {:c [:span "Hello, my name is Haris. Being a software engineer for
                   over 8 years, I had many opportunities to work
                   on a variety of projects. Notably, as CTO at "
           (anchor {:href "https://flip.cards"
                    :c "FLIP"
                    :cl "underline underline-offset-4"
                    :target "_blank"})
           [:span ", I led the development of a learning app 
                          that served over 2 million users."]]})

     (paragraph
      {:c "I now consult independently, specializing in 
            full-stack Clojure and functional UIs. I studied 
            Computer Science at Hack Reactor in San Francisco
            and at the University of Michigan in Ann Arbor. 
            Outside of work, I enjoy reading, writing, running, 
            and brewing ☕️."})]
    [:div.flex.flex-col.gap-2
     (heading {:c "Currently"})
     [:div.flex.flex-row
      [:p.text-zinc-500.dark:text-zinc-400.mr-6
       {:class "w-1/5"}
       "Reading"]
      (paragraph {:c "The City and its Uncertain Walls by Haruki Murakami"
                  :cl "w-4/5"})]
     [:div.flex.flex-row
      [:p.text-zinc-500.dark:text-zinc-400.mr-6
       {:class "w-1/5"}
       "Watching"]
      (paragraph {:c "Building GPT by Andrej Karpathy"
                  :cl "w-4/5"})]
     [:div.flex.flex-row
      [:p.text-zinc-500.dark:text-zinc-400.mr-6
       {:class "w-1/5"}
       "Listening"]
      (paragraph {:c "Clock of the Long Now by Sungazer"
                  :cl "w-4/5"})]]
    (section
     (heading {:c "Work Experience"})
     [:ul
      [:li.flex.items-center.justify-between
       (paragraph {:c "Chief Technology Officer"})
       [:div.flex.text-zinc-500.dark:text-zinc-400.gap-4
        (anchor {:href "https://flip.cards"
                 :target "_blank"
                 :c "FLIP"
                 :cl "underline underline-offset-4 text-zinc-500 dark:text-zinc-400"})
        [:p "2021-2024"]]]
      [:li.flex.items-center.justify-between
       (paragraph {:c "Client Development Lead"})
       [:div.flex.text-zinc-500.dark:text-zinc-400.gap-4
        [:p "2020-2021"]]]
      [:li.flex.items-center.justify-between
       (paragraph {:c "Fullstack React Developer"})
       [:div.flex.text-zinc-500.dark:text-zinc-400.gap-4
        [:p "2019-2020"]]]]
     (paragraph
      {:c [:span "Download full resume "
           (anchor {:c "here."
                    :cl "underline underline-offset-4"
                    :href "resources/resume.pdf"
                    :target "_blank"})]}))
    (section
     (heading {:c "Open Source"})
     [:ul
      [:li
       (paragraph
        {:c (anchor
             {:c "ClojureScript Project Starter"
              :cl "underline underline-offset-4"
              :href "https://github.com/harismh/utsb-cljs-starter"
              :target "_blank"})})]
      [:li
       (paragraph
        {:c (anchor
             {:c "Data-driven UIs w/ Dumdom"
              :cl "underline underline-offset-4"
              :href "https://github.com/harismh/todomvc-dumdom"
              :target "_blank"})})]
      [:li
       (paragraph
        {:c (anchor
             {:c "Joy of Clojure Code Notes"
              :cl "underline underline-offset-4"
              :href "https://github.com/harismh/joy-notes"
              :target "_blank"})})]
      [:li
       (paragraph
        {:c (anchor
             {:c "React Native Web Track Player"
              :cl "underline underline-offset-4"
              :href "https://github.com/harismh/react-native-webview-track-player"
              :target "_blank"})})]
      [:li
       (paragraph
        {:c (anchor
             {:c "Tokyo Twilight VS Code Theme"
              :cl "underline underline-offset-4"
              :href "https://github.com/harismh/tokyo-twilight"
              :target "_blank"})})]])
    (section
     (heading {:c "Connect"})
     (paragraph {:c [:span "Reach me at "
                     (anchor {:href "https://github.com/harismh"
                              :c "@harismh"
                              :cl "underline underline-offset-4"
                              :target "_blank"})
                     [:span " on GitHub or at "]
                     (anchor {:href ""
                              :c ""
                              :cl "underline underline-offset-4"
                              :id "contact"})]}))
    (section
     (heading {:c "Colophon"})
     (paragraph {:c
                 [:span "Made with ClojureScript. Fonts are Untitled Sans 
                          from the Klim Foundry and Plex Sans from IBM. 
                          Torus knot made using Three.js. Code is open
                          source at "
                  (anchor {:href "https://github.com/harismh/site-v4"
                           :target "_blank"
                           :cl "underline underline-offset-4"
                           :c "GitHub."})]}))]])

(def routes
  {:home home
   :writing todo
   :projects todo
   :contact todo})

;; (defn route-to! [atom route]
;;   (fn [] (reset! atom route)))

(defn render-route [current-route]
  (or
   (get routes current-route)
   (:home routes)))

(defn main [*current-route]
  [:main.grid.md-grid-cols-2.lg:grid-cols-2.lg:w-full.h-screen
   [:div.basis-full.bg-zinc-50.dark:bg-zinc-900.text-zinc-100.flex.flex-col.gap-20.p-12.sm:p-20
    {:class "sm:basis-1/2"}
    [:nav.flex.flex-row.gap-8.items-center
     (heading {:c "Haris Muhammad" :cl "flex-1 text-5xl font-bold"})
     #_(anchor {:href "#writing" :on-click (route-to! *current-route :writing) :c "Writing"})
     #_(anchor {:href "#resume" :on-click (route-to! *current-route :resume) :c "Resume"})
     #_(anchor {:href "#contact" :on-click (route-to! *current-route :contact) :c "Contact"})]
    [:section
     (render-route (deref *current-route))]
    [:footer.flex.flex-row.gap-4.justify-end
     [:p.text-sm.font-sans.text-zinc-500.dark:text-zinc-400 "© 2024"]
     [:img {:height 20 :width 100 :src "resources/signature.svg" :alt "harism signature"}]]]
   [:div.basis-full.bg-zinc-100.dark:bg-zinc-950.text-zinc-900.hidden.lg:flex.justify-center
    {:class "sm:basis-1/2"}
    [:canvas#three-canvas.sticky
     {:style {:top (/ (get (canvas-dims) 1) 4)}}]]])
    