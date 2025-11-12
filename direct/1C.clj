(ns polygon-area
  (:import [java.util Scanner]))

(def eps 1e-3)

;; floating point "gcd" using fmod
(defn fmod [x y]
  (- x (* y (Math/floor (/ x y)))))

(defn gcd [x y]
  (if (< y eps)
    x
    (recur y (fmod x y))))

(defn hypot [x y]
  (Math/hypot x y))

(defn -main []
  (let [scan (Scanner. System/in)
        ;; read three points
        p (repeatedly 3 #(vector (.nextDouble scan) (.nextDouble scan)))
        ;; compute side lengths
        sides (map (fn [[p1 p2]]
                     (apply hypot (map - p1 p2)))
                   [[(nth p 0) (nth p 1)]
                    [(nth p 1) (nth p 2)]
                    [(nth p 2) (nth p 0)]])
        [a b c] sides
        ;; compute angles
        angles (map (fn [[x y z]]
                      (Math/acos (/ (+ (* y y) (* z z) (- (* x x))) (* 2 y z))))
                    [[a b c] [b c a] [c a b]])
        [A B C] angles
        R (/ a (* 2 (Math/sin A)))
        u (* 2 (gcd A (gcd B C)))
        area (* R R Math/PI (/ (Math/sin u) u))]
    (println (format "%.7f" area))))

;; call main
(-main)
