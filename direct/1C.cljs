(ns polygon-area
  (:require [clojure.string :as str]
            ["process" :as process]))

(def eps 1e-3)

(defn fmod [x y]
  (- x (* y (js/Math.floor (/ x y)))))

(defn gcd [x y]
  (if (< y eps)
    x
    (recur y (fmod x y))))

(defn hypot [x y]
  (js/Math.hypot x y))

(defn -main []
  (let [lines (atom [])]
    (.on (.-stdin process) "data"
         (fn [chunk]
           (swap! lines into (str/split-lines (.toString chunk)))))
    (.on (.-stdin process) "end"
         (fn []
           (let [p (mapv #(mapv js/parseFloat (str/split % #" ")) @lines)
                 sides (map (fn [[p1 p2]]
                              (apply hypot (map - p1 p2)))
                            [[(nth p 0) (nth p 1)]
                             [(nth p 1) (nth p 2)]
                             [(nth p 2) (nth p 0)]])
                 [a b c] sides
                 angles (map (fn [[x y z]]
                               (js/Math.acos (/ (+ (* y y) (* z z) (- (* x x))) (* 2 y z))))
                             [[a b c] [b c a] [c a b]])
                 [A B C] angles
                 R (/ a (* 2 (js/Math.sin A)))
                 u (* 2 (gcd A (gcd B C)))
                 area (* R R js/Math.PI (/ (js/Math.sin u) u))]
             (js/console.log (.toFixed area 7)))))))

(-main)
