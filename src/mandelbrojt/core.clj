(ns mandelbrojt.core
   (:require
      [clojure.pprint    :refer [pprint]]
      [clojure.string    :refer [join]]
      [clojure.tools.cli :refer [parse-opts]]  )
   (:gen-class)  )

(def cli-options
   [  [  "-w"
         "--width INT"
         "width of grid in pixels"
         :parse-fn #(Integer/parseInt %)
         :validate [#(< 1 %) "width must be greater than one pixel"]
         :default 100  ]
      [  "-h"
         "--height INT"
         "height of grid in pixels"
         :parse-fn #(Integer/parseInt %)
         :validate [#(< 1 %) "height must be greater than one pixel"]
         :default 100  ]
      [  "-x"
         "--xorig REAL"
         "`x` coordinate of center"
         :parse-fn #(Double/parseDouble %)
         :validate [float? "not a real"]
         :default 0  ]
      [  "-y"
         "--yorig REAL"
         "`y` coordinate of center"
         :parse-fn #(Double/parseDouble %)
         :validate [float? "not a real"]
         :default 0  ]
      [  "-r"
         "--radius REAL"
         "radius of the area we are rendering"
         :parse-fn #(Double/parseDouble %)
         :validate [float? "not a real"]
         :default 2  ]
      [  "-H" "--help" "help"  ]  ]  )

(defn usage
   [exit-code options-summary & [error-msg]]
   (if error-msg (println error-msg "\n"))
   (println
      (join \newline
         [  "usage: write me"
            ""
            "Options:"
            options-summary  ]  )  )
   (System/exit exit-code)  )

(defn test-point [x y] true)

(defn render-rectangle
   [  [x-pixels y-pixels]
      [  [x-min x-max]
         [y-min y-max]  ]  ]
   (let
      [  x-ratio (/ (- x-max x-min) (- x-pixels 1))
         y-ratio (/ (- y-max y-min) (- y-pixels 1))
         x-real #(+ x-min (* % x-ratio))
         y-real #(+ y-min (* % y-ratio))  ]
      (for
         [  x-pixel (range x-pixels)
            y-pixel (range y-pixels)  ]
         [x-pixel y-pixel (test-point (x-real x-pixel) (y-real y-pixel))]  )  )  )

(defn main-body
   [  {  {:keys [width height help radius xorig yorig]} :options
          :keys [arguments errors summary]  }  ]
   (if help   (usage 0 summary errors))
   (if errors (usage 1 summary errors))
   ;;(render-rectangle [5 5] [[-2 +2] [-2 +2]])
   (render-rectangle
      [width height]
      (let
         [  minor (min width height)
            major (max width height)
            aspect (/ major minor)
            apoapsis (* aspect radius)
            periapsis radius  ]
         (if
            (< width height)
            [  [  (- xorig periapsis)
                  (+ xorig periapsis)  ]
               [  (- yorig apoapsis)
                  (+ yorig apoapsis)  ]  ]
            [  [  (- xorig apoapsis)
                  (+ xorig apoapsis)  ]
               [  (- yorig periapsis)
                  (+ yorig periapsis)  ]  ]  )  )  )  )

(defn -main
   [& args]
   (alter-var-root #'*read-eval* (constantly false)) ;; work around dangerous default behaviour in Clojure
   (pprint (main-body (parse-opts args cli-options)))  )
