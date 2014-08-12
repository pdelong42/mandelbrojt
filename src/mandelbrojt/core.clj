(ns mandelbrojt.core
   (:require [clojure.pprint :refer [pprint]])
   (:gen-class)  )

(defn main-loop
   [  [x-pixels y-pixels]
      [  [x-min x-max]
         [y-min y-max]  ]  ]
   (for [  x (range x-pixels)
           y (range y-pixels)  ]
        [x y]  )  )

(defn -main
   [& args]
   (alter-var-root #'*read-eval* (constantly false)) ;; work around dangerous default behaviour in Clojure
   (pprint (main-loop [3 3] [[-2 +2] [-2 +2]]))  )
