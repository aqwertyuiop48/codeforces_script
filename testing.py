import subprocess
inner_code = '''
(require '[clojure.string :as str])
(def inner-code1 "
(defn greetings [msg]
  (println (format ¶Hello, %s¶ msg))
  (println (+ 1 1))
  (println (- 1 1))
)

(greetings ¶World!¶)
")
(def inner-code (str/replace inner-code1 #"¶" "\\\""))
(load-string inner-code)
'''
print('About to run nested clojure...')
subprocess.run(['clojure', '-e', inner_code])
print('Back in Python!')



# (outermost to innermost) : python -> clojure -> python -> clojure
import subprocess

inner_code = r"""
(require '[clojure.string :as str])
(require '[clojure.java.shell :as shell])

(def inner-code2 "§(defn greetings [msg]
  (println (format ¶Hello ¥there¥, %s¶ msg))
  (println (+ 1 90))
  (println (- 1 90)))
(greetings ¶World!¶)§")

(def inner-code3 (str/replace inner-code2 #"§" ""))
(def inner-code4 (str/replace inner-code3 #"¶" "\""))
;; 8 backslashes here for full escaping layers:
(def inner-code5 (str/replace inner-code4 #"¥" "\\\\\\\\\""))

(def python-code
  (str
    "import subprocess\n"
    "inner_code = '''" inner-code5 "'''\n"
    "print('About to run nested clojure...')\n"
    "result = subprocess.run(['clojure', '-e', inner_code], capture_output=True, text=True)\n"
    "print(result.stdout)\n"
    "print('Back in Python!')\n"))

(def result (shell/sh "python3" "-c" python-code))

(println (:out result))
(println (:err result))
"""

print("Running nested clojure from Python...")
subprocess.run(["clojure", "-e", inner_code])
print("Done.")

