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
      [  "-m"
         "--maxiter INT"
         "maximum iterations to use in generating the orbit sequence"
         :parse-fn #(Integer/parseInt %)
         :validate [#(< -1 %) "cannot do a negative number of iterations"]
         :default 100  ]
      [  "-r"
         "--radius REAL"
         "radius of the area we are rendering"
         :parse-fn #(Double/parseDouble %)
         :validate [float? "not a real"]
         :default 2.0  ]
      [  "-x"
         "--xorig REAL"
         "`x` coordinate of center"
         :parse-fn #(Double/parseDouble %)
         :validate [float? "not a real"]
         :default 0.0  ]
      [  "-y"
         "--yorig REAL"
         "`y` coordinate of center"
         :parse-fn #(Double/parseDouble %)
         :validate [float? "not a real"]
         :default 0.0  ]
      [  "-f"
         "--filename STRING"
         "output filename"
         :default "output.pbm"  ]
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

(defn equation
   [  [a b]
      [x y]  ]
   [  (+ a (* (+ x y) (- x y)))
      (+ b (* 2 x y))  ]  )

(defn mod-square
   [  [x y]  ]
   (+ (* x x) (* y y))  )

(defn test-point
   [maxiter c]
   (loop ; I hate this...
      [  iteration 0 z [0 0]  ]
      (cond
         (> (mod-square z) 4)  0
         (> iteration maxiter) 1
         :default
         (recur
            (inc iteration)
            (equation c z)  )  )  )  )

(defn render-rectangle

   "take a canvas of pixels, map each pixel to the given finite subset
   of the complex plane, and test each pixel for membership in the
   set; return the original pixels, each associated with a color value
   based on its set membership"

   [  max-iterations
      [x-pixels y-pixels]
      [  [x-min x-max]
         [y-min y-max]  ]  ]
   (let
      [  x-ratio (/ (- x-max x-min) (- x-pixels 1))
         y-ratio (/ (- y-max y-min) (- y-pixels 1))  ]
      (into
         {}
         (for
            [  x-pixel (range x-pixels)
               y-pixel (range y-pixels)  ]
            [  [  x-pixel y-pixel  ]
               (test-point
                  max-iterations
                  [  (+ x-min (* x-pixel x-ratio))
                     (+ y-min (* y-pixel y-ratio))  ]  )  ]  )  )  )  )

(defn format-as-pbm
   [  [width height] points  ]
   (reduce
      #(str % %2 \newline)
      (list*
         ""
         "P1"
         (str width " " height)
         (map
            (fn [y]
               (join " "
                  (map
                     (fn [x] (str (points [x y])))
                     (range width)  )  )  )
            (range height)  )  )  )  )

(defn main-body

   "parse the command-line args, translate the parameters defining the
   input circular area into a rectangular area (defined by the pixel
   dimensions) in which the circle is inscribed, and pass that to the
   rendering routine (which only cares about rectangular areas)"

   [  {  {  :keys [width height maxiter radius xorig yorig filename help]  }
            :options :keys [arguments errors summary]  }  ]
   (if help   (usage 0 summary errors))
   (if errors (usage 1 summary errors))
   (spit
      filename
      (format-as-pbm
         [width height]
         (render-rectangle
            maxiter
            [width height]
            (let  ; footnote 1
               [  minor (min width height)
                  major (max width height)
                  aspect (/ major minor)
                  apoapsis (* aspect radius)
                  periapsis radius  ]
               (if
                  (< width height)
                  [  [  (- xorig periapsis)
                        (+ xorig periapsis)  ]
                     [  (- yorig  apoapsis)
                        (+ yorig  apoapsis)  ]  ]
                  [  [  (- xorig  apoapsis)
                        (+ xorig  apoapsis)  ]
                     [  (- yorig periapsis)
                        (+ yorig periapsis)  ]  ]  )  )  )  )  )  )

(defn -main
   [& args]
   ;; work around dangerous default behaviour in Clojure
   (alter-var-root #'*read-eval* (constantly false))
   (main-body (parse-opts args cli-options))  )

; Footnote 1:
;
; I realize that, out of these five intermediate symbols, only one is
; strictly necessary (one is a "renaming", and is therefore redundant;
; and the other three are only used once.  But I think this just makes
; for easier reading, and it gives some clue as to what I was thinking
; (until I can write some real documentation).
