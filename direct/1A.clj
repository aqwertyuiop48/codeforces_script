;; z.clj
(let [[n m a] (map #(Long/parseLong %) (clojure.string/split (read-line) #" "))]
  (println (* (quot (+ n a -1) a)
              (quot (+ m a -1) a))))
