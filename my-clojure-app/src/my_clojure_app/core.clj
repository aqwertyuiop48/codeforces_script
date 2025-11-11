(ns my-clojure-app.core
  (:require [clojure.java.io :as io]
  [clojure.string :as str]
  ;; [clojure-triple-quote-strings.core :refer [patch-clojure-reader! triple-quote]]
  [clojure.java.shell :as shell]))


;; (patch-clojure-reader!)

( println "
/*********************  Strings   *********************
" )


(defn -main [& _]
  (println "
  Hello, World from Clojure! 
  \"""multiline string""\"
  \"\"\"multiline string escaped \"\"\"
  \"\"""multiline string 2 ""\"\"
  `backtick string`
  "))

(defmacro raw-string [& lines]
  (let [line-strs
        (map (fn [line]
               (cond
                 (string? line) line
                 (symbol? line) (name line)
                 :else (str line)))
             lines)]
    (str/join "\n" line-strs)))
    
    
(def my-text
  (raw-string
    This is line one,
    """
    This is line two wid@#$%^&*th quotes
    """,
    Another line with a symbol,
    12345,
    YetAnotherSymbol))

(println my-text)


(def raw-text
"##
Hello world. This is ##quoted text##
##")

(def with-quotes(clojure.string/replace raw-text #"##" "\""))
(def without-quotes(clojure.string/replace with-quotes #"\"" ""))
(println "With quotes:")(println with-quotes)
(println "\nWithout quotes:")(println without-quotes)

(def raw-text1
"¶
Hello world. This is ¶quoted text again!¶
¶")

(def with-quotes(clojure.string/replace raw-text1 #"¶" "\""))
(def without-quotes(clojure.string/replace with-quotes #"\"" ""))
(println "With quotes:")(println with-quotes)
(println "\nWithout quotes:")(println without-quotes)

  
( println "
/*********************  0. Shell script  *********************
" )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; simple ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def result
  (shell/sh "bash" "-c"
      "
      echo 'Hello, World!'
      echo \"Hello, World again!\"
      echo 'Listing files:'
      ls -l
      echo 'Done.'
      "))

(println "Exit code:" (:exit result))
(println "Output:" (:out result))
(println "Error:"  (:err result))



;; -----------------------------------------------------------
;; Step 1: define your raw shell script text
(def raw-text1
  "¶
  echo 'Hello, World from result1!'
  echo ¶Hello, World again from result1!¶
  echo 'Listing files:'
  ls -l
  echo 'Done from result1.'
¶")

;; -----------------------------------------------------------
;; Step 2: clean it up (remove your ¶ markers)
(def script-with-quotes
  (str/replace raw-text1 #"¶" "\""))

(def final-script
  (str/replace script-with-quotes #"\"" ""))

;; -----------------------------------------------------------
;; Step 3: run it with bash -c
(def result1
  (shell/sh "bash" "-c" final-script))

;; -----------------------------------------------------------
;; Step 4: print the results nicely
(println "Exit code:" (:exit result1))
(println "--- Output ---")
(println (:out result1))
(println "--- Error ---")
(println (:err result1))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; -c ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmacro raw-string [& lines]
  (let [line-strs
        (map (fn [line]
               (cond
                 (sequential? line)
                 (str/join " " (map (fn [item]
                                      (cond
                                        (string? item) item
                                        (symbol? item) (name item)
                                        :else (str item)))
                                    line))
                 (symbol? line) (name line)
                 (string? line) line
                 :else (str line)))
             lines)]
    (str/join "\n" line-strs)))

(def script
  (raw-string
    (echo "Hello, World!")
    (echo "Listing current dir:")
    "multiline_text=$(cat <<EOF
This is line one
This is line two
EOF
)"
    (echo "$multiline_text")
    (ls -l)))

(def result (shell/sh "bash" "-c" script))

(println "Exit code:" (:exit result))
(println "Output:" (:out result))
(println "Errors:" (:err result))


(defmacro raw-string1 [& lines]
  (let [line-strs
        (map (fn [line]
               (cond
                 (string? line) line
                 (symbol? line) (name line)
                 :else (str line)))
             lines)]
    (str/join "\n" line-strs)))
    
    
(def my-text
  (raw-string1
    This is line one,
    """
    This is line "one", 'and_' two width quotes
    """,
    Another line with a symbol,
    12345,
    YetAnotherSymbol))

(println my-text)


;; Define the script template with custom markers
(def raw-script2
  "
   echo 'Hello, World!'
   echo ##Hello, World again!##
   echo '#\\#Hello, World again!##'
   echo 'Listing files:'
   ls -l
   echo 'Done.'
  ")

;; Replace ## with real double quotes before running
(def script (str/replace raw-script2 #"##" "\""))

;; Now run
(def result (shell/sh "bash" "-c" script))

(println "Exit code:" (:exit result))
(println "Output:" (:out result))
(println "Error:"  (:err result))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; -s ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmacro raw-string [& lines]
  (let [line-strs
        (map (fn [line]
               (cond
                 (string? line) line
                 (symbol? line) (name line)
                 :else (str line)))
             lines)]
    (str/join "\n" line-strs)))

(def my-script
  (raw-string
    """
    echo 'Hello from raw-string macro!'
    echo 'Current directory'
    pwd
    echo 'List of files'
    ls -l
    echo 'Print date'
    date
    """))


(defn run-script [script]
  (println "\n=== Running script via bash -s ===")
  (let [proc (.exec (Runtime/getRuntime) "bash -s")]
    ;; write script and close
    (with-open [writer (io/writer (.getOutputStream proc))]
      (.write writer script)
      (.flush writer))
    ;; read stdout
    (with-open [reader (io/reader (.getInputStream proc))]
      (doseq [line (line-seq reader)]
        (println line)))
    (.waitFor proc)))

(run-script my-script)



( println "
/*********************  1. Two Sum  *********************
" )


(defn two-sum [nums target]
  (loop [i 0
         enum {}
         nums nums]
    (if (empty? nums)
      nil
      (let [j (first nums)
            seen (- target j)]
        (if (contains? enum seen)
          (vec (sort [(enum seen) i]))   ;; convert result to vector
          (recur (inc i) (assoc enum j i) (rest nums)))))))

;; Example inputs and outputs
(def examples
  [{:nums [2 7 11 15] :target 9}
   {:nums [3 2 4] :target 6}
   {:nums [3 3] :target 6}])

(doseq [{:keys [nums target]} examples]
  (println "Input nums:" nums "target:" target)
  (println "Output:" (two-sum nums target)))


( println "/*********************  2. Add Two Numbers  *********************" )
  

  
(defn ListNode
  ([val] {:val val :next nil})
  ([val next] {:val val :next next}))

(defn addTwoNumbers [l1 l2]
  (let [list1 (loop [node l1 acc []]
                (if (nil? node)
                  acc
                  (recur (:next node) (conj acc (:val node)))))
        list2 (loop [node l2 acc []]
                (if (nil? node)
                  acc
                  (recur (:next node) (conj acc (:val node)))))
        num1 (bigint (apply str (reverse list1)))
        num2 (bigint (apply str (reverse list2)))
        total (+ num1 num2)
        sum-list (map #(Character/digit % 10)
                      (reverse (str total)))]
    ;; build linked list functionally (without atoms)
    (reduce (fn [next digit] (ListNode digit next))
            nil
            (reverse sum-list))))

(defn vec-to-listnode [v]
  (reduce (fn [next val] (ListNode val next)) nil (reverse v)))

(defn listnode-to-vec [node]
  (loop [n node acc []]
    (if (nil? n)
      acc
      (recur (:next n) (conj acc (:val n))))))

(def examples
  [{:l1 [2 4 3] :l2 [5 6 4]}
   {:l1 [0] :l2 [0]}
   {:l1 [9 9 9 9 9 9 9] :l2 [9 9 9 9]}])

(doseq [{:keys [l1 l2]} examples]
  (let [ln1 (vec-to-listnode l1)
        ln2 (vec-to-listnode l2)
        result (addTwoNumbers ln1 ln2)
        result-vec (listnode-to-vec result)]
    (println "Input l1:" l1 "l2:" l2)
    (println "Output:" result-vec)
    (println "Output:" (str "[" (str/join "," result-vec) "]"))
    (println "Output:" (str/join "," result-vec))))
    

( println "/*********************  4. Median of Two Sorted Arrays  *********************" )
    


(defn find-median-sorted-arrays [nums1 nums2]
  (let [merged (sort (concat nums1 nums2))
        n (count merged)]
    (if (even? n)
      (/ (+ (nth merged (dec (/ n 2)))
            (nth merged (/ n 2)))
         2.0)
      (double (nth merged (quot n 2))))))

;; Example inputs
(def examples
  [{:nums1 [1 3] :nums2 [2]}
   {:nums1 [1 2] :nums2 [3 4]}])

;; Run and print outputs
(doseq [{:keys [nums1 nums2]} examples]
  (let [result (find-median-sorted-arrays nums1 nums2)]
    (println "Input nums1:" nums1 "nums2:" nums2)
    (println "Output:" result)))
    
( println "/*********************  3. Longest Substring Without Repeating Characters  *********************" )



(defn length-of-longest-substring [s]
  (if (empty? s)
    0
    (loop [i 0
           length 0
           list_ []
           covered ""]
      (if (= i (count s))
        (apply max (conj list_ length))
        (let [c (str (nth s i))]
          (if (not (.contains covered c))
            (recur (inc i) (count (str covered c)) list_ (str covered c))
            (let [idx (.indexOf covered c)
                  new-covered (str (subs covered (inc idx)) c)]
              (recur (inc i) (count new-covered) (conj list_ length) new-covered))))))))

;; Example inputs
(def examples
  [{:s "abcabcbb"}
   {:s "bbbbb"}
   {:s "pwwkew"}
   {:s " "}
   {:s ""}])

;; Run and print outputs
(doseq [{:keys [s]} examples]
  (let [result (length-of-longest-substring s)]
    (println "Input: s =" (pr-str s))
    (println "Output:" result)))


( println "/*********************  5. Longest Palindromic Substring  *********************" )



(defn longest-palindrome [s]
  (let [n (count s)]
    (loop [i 0
           lp ""]
      (if (< i n)
        (let [new-lp
              (loop [j (inc i)
                     current-lp lp]
                (if (<= j n)
                  (let [substr (subs s i j)]
                    (if (= substr (apply str (reverse substr)))
                      (if (> (count substr) (count current-lp))
                        (recur (inc j) substr)
                        (recur (inc j) current-lp))
                      (recur (inc j) current-lp)))
                  current-lp))]
          (recur (inc i) new-lp))
        lp))))

;; Example inputs
(def examples
  [{:s "babad"}
   {:s "cbbd"}])

;; Run and print outputs
(doseq [{:keys [s]} examples]
  (let [result (longest-palindrome s)]
    (println "Input: s =" (pr-str s))
    (println "Output:" result)))

( println "/*********************  7. Reverse Integer  *********************" )



(defn reverse-integer [x]
  (let [max-val (dec (Math/pow 2 31))
        min-val (- (Math/pow 2 31))
        abs-x (Math/abs x)
        reversed-str (apply str (reverse (str abs-x)))
        reversed-num (Long/parseLong reversed-str)
        final-num (if (neg? x) (- reversed-num) reversed-num)]
    (if (and (<= min-val final-num) (<= final-num max-val))
      final-num
      0)))

;; Example inputs
(def examples [123 -123 1534236469 0 120])

;; Run and print outputs
(doseq [x examples]
  (println "Input:" x)
  (println "Output:" (reverse-integer x)))
  
  
( println "/*********************  8. String to Integer (atoi)  *********************" )
  


(def min-int (- (bit-shift-left 1 31)))
(def max-int (dec (bit-shift-left 1 31)))

(defn my-atoi [s]
  (let [s (str/trim s)]
    (if (or (str/includes? s "+-")
            (str/includes? s "-+")
            (empty? s))
      0
      (let [s (if (str/starts-with? s "+") (subs s 1) s)]
        (if (empty? s)
          0
          (if (and (not (Character/isDigit (first s)))
                   (not= (first s) \-))
            0
            (loop [idx 0 ans ""]
              (if (>= idx (count s))
                (let [num (try
                            (Long/parseLong ans)
                            (catch Exception _
                              0))]
                  (cond
                    (= ans "-") 0
                    (< num min-int) min-int
                    (> num max-int) max-int
                    :else num))
                (let [c (nth s idx)]
                  (cond
                    (Character/isDigit c)
                    (recur (inc idx) (str ans c))

                    (and (= c \-)
                         (= (count ans) 0))
                    (recur (inc idx) (str ans c))

                    (and (= c \+)
                         (= (count ans) 0))
                    (recur (inc idx) ans)

                    :else
                    (let [num (try
                                (Long/parseLong ans)
                                (catch Exception _
                                  0))]
                      (cond
                        (= ans "-") 0
                        (< num min-int) min-int
                        (> num max-int) max-int
                        :else num))))))))))))

;; Tests
(println (my-atoi "42"))        ;; 42
(println (my-atoi " -042"))     ;; -42
(println (my-atoi "1337c0d3"))  ;; 1337
(println (my-atoi "+1"))         ;; 1
(println (my-atoi "-+12"))       ;; 0
(println (my-atoi "+-12"))       ;; 0
(println (my-atoi "0-1"))        ;; 0
(println (my-atoi "words and 987")) ;; 0
(println (my-atoi "     +004500"))
(println (my-atoi "-91283472332"))

( println "/********************* 9. Palindrome Number  *********************" )


(defn is-palindrome [x]
  (= (str x) (apply str (reverse (str x)))))

;; Example inputs and outputs
(let [examples [{:x 121 :expected true}
                {:x -121 :expected false}
                {:x 10 :expected false}]]
  (doseq [{:keys [x expected]} examples]
    (println "Input: x =" x)
    (println "Output:" (is-palindrome x))
    (println "Expected:" expected)
    (println)))

( println "/********************* 10. Regular Expression Matching  *********************" )




(defn is-match [s p]
  (boolean (re-matches (re-pattern p) s)))


  ;; Hardcoded test cases
  (let [tests [{:s "aa" :p "a"}
               {:s "aa" :p "a*"}
               {:s "ab" :p ".*"}]]
    (doseq [{:keys [s p]} tests]
      (println "Input: s =" (pr-str s) ", p =" (pr-str p))
      (println "Output:" (is-match s p))
      (println)))


( println "/******************** 12. Integer to Roman **********************" )


(def roman-map
  {1000 "M", 2000 "MM", 3000 "MMM",
   900 "CM", 500 "D", 600 "DC", 700 "DCC", 800 "DCCC",
   400 "CD", 100 "C", 200 "CC", 300 "CCC",
   90 "XC", 50 "L", 60 "LX", 70 "LXX", 80 "LXXX",
   40 "XL", 10 "X", 20 "XX", 30 "XXX",
   9 "IX", 5 "V", 6 "VI", 7 "VII", 8 "VIII",
   4 "IV", 1 "I", 2 "II", 3 "III"})

(defn int-to-roman [num]
  (if (contains? roman-map num)
    (roman-map num)
    (let [str-num (str num)
          len (count str-num)
          ;; create list of parts like [3000 700 40 9]
          parts (->> str-num
                     (map-indexed (fn [idx ch]
                                    (let [digit (Character/digit ch 10)
                                          pow (Math/pow 10 (- len idx 1))]
                                      (* digit (int pow)))))
                     (filter #(not= % 0)))]
      ;; join roman strings
      (apply str (map roman-map parts)))))

;; Test cases
(doseq [n [3749 58 1994]]
  (println "Input:" n)
  (println "Output:" (int-to-roman n)))
  
( println "/******************** 14. Longest Common Prefix **********************" )

  

(defn longest-common-prefix [strs]
  (if (some #(= "" %) strs)
    ""
    (let [min-len (count (apply min-key count strs))]
      (loop [i 1
             ans_ ""]
        (if (> i min-len)
          ans_
          (let [ans (map #(subs % 0 i) strs)]
            (if (> (count (set ans)) 1)
              ans_
              (recur (inc i) (first ans)))))))))

;; Test cases
(def inputs
  [["flower" "flow" "flight"]
   ["dog" "racecar" "car"]])

(doseq [strs inputs]
  (println "Input:" strs)
  (println "Output:" (longest-common-prefix strs)))


( println "/******************** 13. Roman to Integer **********************" )



(defn roman-to-int [s]
  (let [romans (into (sorted-map-by (fn [a b] (compare b a)))   ;; reversed order for two-letter match first
                     {"I" 1, "IV" 4, "V" 5, "IX" 9, "X" 10, "XL" 40,
                      "L" 50, "XC" 90, "C" 100, "CD" 400, "D" 500,
                      "CM" 900, "M" 1000})]
    (loop [i 0
           sum_ 0]
      (if (>= i (count s))
        sum_
        (let [two (subs s i (min (+ i 2) (count s)))]
          (if (contains? romans two)
            (recur (+ i 2) (+ sum_ (romans two)))
            (recur (inc i) (+ sum_ (romans (subs s i (inc i)))))))))))

;; Hardcoded inputs
(def inputs ["III" "LVIII" "MCMXCIV"])

(doseq [s inputs]
  (println "Input:" s)
  (println "Output:" (roman-to-int s)))
  
  
( println "/******************** 17. Letter Combinations of a Phone Number **********************" )

  

(def digit-to-chars
  {\2 "abc" \3 "def" \4 "ghi" \5 "jkl"
   \6 "mno" \7 "pqrs" \8 "tuv" \9 "wxyz"})

(defn letter-combinations [digits]
  (if (empty? digits)
    []
    (let [char-lists (map digit-to-chars digits)]
      (reduce
        (fn [acc chars]
          (for [prefix acc
                c chars]
            (str prefix c)))
        [""]
        char-lists))))

;; Tests with hardcoded inputs
(println "Input: \"23\"")
(prn "Output:" (vec (letter-combinations "23")))
;; => ["ad" "ae" "af" "bd" "be" "bf" "cd" "ce" "cf"]

(println "Input: \"\"")
(prn "Output:" (vec (letter-combinations "")))
;; => []

(println "Input: \"2\"")
(prn "Output:" (vec (letter-combinations "2")))
;; => ["a" "b" "c"]


( println "/******************** 20. Valid Parentheses **********************" )



(defn is-valid [s]
  (let [pairs {\) \( \] \[ \} \{}]
    (loop [chars (seq s) stack []]
      (cond
        (empty? chars) (empty? stack)
        (#{\( \[ \{} (first chars)) (recur (rest chars) (conj stack (first chars)))
        (#{\) \] \}} (first chars))
        (if (= (peek stack) (pairs (first chars)))
          (recur (rest chars) (pop stack))
          false)
        :else (recur (rest chars) stack)))))


;; Hardcoded test cases
(println "(\"()\") =>" (is-valid "()"))           ;; true
(println "(\"()[]{}\") =>" (is-valid "()[]{}"))   ;; true
(println "(\"(]\") =>" (is-valid "(]"))          ;; false
(println "(\"([])\") =>" (is-valid "([])"))       ;; true


( println "/******************** Complex macros **********************" )

;; Step 1: define your raw shell script text
(def raw-text1
  "¶
  echo 'Hello, World ¶from¶ result1!'
  echo '
  Hello, World ¤from¤ result1!
  '
  echo ¶Hello, World ¤here¤ from result1!¶
  echo ¶Listing files:¶
  echo ¤Listing files:¤
  echo ¤¤Listing files:¤¤
  ls -l
  echo 'Done from result1.'
¶")

;; -----------------------------------------------------------
;; Step 2: clean it up (replace ¤ with " and remove ¶ markers)
(def final-script
  (-> raw-text1
      (str/replace #"¤" "\"") ; replace ¤ with "
      (str/replace #"¶" ""))
      
      ) ; remove ¶

;; -----------------------------------------------------------
;; Step 3: run it with bash -c

(println "Final script to run:")
(println final-script)

(def result1
  (shell/sh "bash" "-c" final-script))

;; -----------------------------------------------------------
;; Step 4: print the results nicely
(println "Exit code:" (:exit result1))
(println "--- Output ---")
(println (:out result1))
(println "--- Error ---")
(println (:err result1))


( println "/******************** Clojure to python to clojure **********************" )

(ns my-clojure-app.core
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell]))



;; Assume inner-code is a defined variable
(def inner-code1 "
(defn greetings [msg]
  (println (format ¶Hello, %s¶ msg))
  (println (+ 1 1))
  (println (- 1 1))
)
(greetings ¶World!¶)
")
(def inner-code(clojure.string/replace inner-code1 #"¶" "\""))
(def python-code
  (str "
import subprocess
inner_code = '''" inner-code "'''
print('About to run nested clojure...')
subprocess.run(['clojure', '-e', inner_code])
print('Back in Python!')
"))
(def result (shell/sh "python3" "-c" python-code))
(println (:out result))




(def inner-code2 "

§(defn greetings [msg]
  (println (format ¶Hello ¥there¥, %s¶ msg))
  (println (+ 1 90))
  (println (- 1 90)))
(greetings ¶World!¶)§

")
(def inner-code3(clojure.string/replace inner-code2 #"§" ""))
(def inner-code(clojure.string/replace inner-code3 #"¶" "\""))
(def inner-code(clojure.string/replace inner-code #"¥" "\\\\\\\\\""))
(def python-code
  (str "
import subprocess
inner_code = '''" inner-code "'''
print('About to run nested clojure...')
subprocess.run(['clojure', '-e', inner_code])
print('Back in Python!')
"))
(def result (shell/sh "python3" "-c" python-code))
(println (:out result))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;  (outermost to innermost) : clojure -> python -> clojure -> python -> clojure

(require '[clojure.string :as str])
(require '[clojure.java.shell :as shell])

;; Inner Clojure code, escaped for Python
(def clojure-code
  (str/join "\n"
            (concat
              ["(require '[clojure.string :as str])"
               "(defn greetings [msg]"
               "  (println (format \"Hello there, %s\" msg))"
               "  (println (+ 1 90))"
               "  (println (- 1 90)))"
               "(greetings \"World!\")"]
              
              ["(println \"This is another block!\")"
               "(println (* 2 3))"
               (shell/sh "python3" "-c" "
print(3456+1234*4)
print(3456+1234*5)
import subprocess
result = subprocess.run([\"clojure\", \"-e\", \"(+ 1 -33)\"], capture_output=True, text=True)
print(result.stdout)
")])))

;; Escape quotes and backslashes for Python string literal
(def escaped-clojure
  (-> clojure-code
      (str/replace "\\" "\\\\")
      (str/replace "\"" "\\\"")
      (str/replace "\n" "\\n")))

;; Python code that uses the escaped string
(def python-code
  (str/join "\n" [
    "import subprocess"
    (str "inner_code = \"" escaped-clojure "\"")
    "print(\"Running nested clojure from Python...\")"
    "result = subprocess.run([\"clojure\", \"-e\", inner_code], capture_output=True, text=True)"
    "print(result.stdout)"
    "print(\"Done.\")"
  ]))

;; Run the Python script from Clojure
(def result (shell/sh "python3" "-c" python-code))

;; Print results
(println "--- STDOUT ---")
(println (:out result))
(println "--- STDERR ---")
(println (:err result))