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
         :validate [integer? "not an integer"]
         :default 100  ]
      [  "-h"
         "--height INT"
         "height of grid in pixels"
         :parse-fn #(Integer/parseInt %)
         :validate [integer? "not an integer"]
         :default 100  ]
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

(defn render-area
   [  [x-pixels y-pixels]
      [  [x-min x-max]
         [y-min y-max]  ]  ]
   (for [  x (range x-pixels)
           y (range y-pixels)  ]
        [x y]  )  )

(defn main-body
   [  {  {:keys [width height help]} :options
          :keys [arguments errors summary]  }  ]
   (if help   (usage 0 summary errors))
   (if errors (usage 1 summary errors))
   ; translate from input parameters to grid bounds
   (render-area [2 2] [[-2 +2] [-2 +2]])  )

(defn -main
   [& args]
   (alter-var-root #'*read-eval* (constantly false)) ;; work around dangerous default behaviour in Clojure
   (pprint (main-body (parse-opts args cli-options)))  )
