(ns my-app.core
  (:require ["left-pad" :as left-pad]))

(defn main []
  (println "Hello from ClojureScript running on Node.js!")
  (println "2+3=" (+ 2 3))

  ;; Use the left-pad npm module
  (println "Left pad '42' to length 5 with zeros:" (left-pad "42" 5 "0")))