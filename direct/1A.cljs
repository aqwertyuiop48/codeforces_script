(ns z
  (:require [clojure.string :as str]
            ["fs" :as fs]))

(def input (.toString (fs/readFileSync "/dev/stdin" "utf8")))

(let [[n m a] (map #(js/parseInt %) (str/split input #" "))]
  (println (* (quot (+ n a -1) a)
              (quot (+ m a -1) a))))
