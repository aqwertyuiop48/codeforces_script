;; (outermost to innermost) : clojure -> shell -> python -> clojure -> python -> clojure

(require '[clojure.java.shell :as shell])

(def shell-script-content
  "python3 - <<'EOF'

import subprocess




inner_code = r\"\"\"

(require '[clojure.string :as str])

(require '[clojure.java.shell :as shell])




(def inner-code2 \"§(defn greetings [msg]

  (println (format ¶Hello ¥there¥, %s¶ msg))

  (println (+ 1 90))

  (println (- 1 90)))

(greetings ¶World!¶)§\")




(def inner-code3 (str/replace inner-code2 #\"§\" \"\"))

(def inner-code4 (str/replace inner-code3 #\"¶\" \"\\\"\"))

;; 8 backslashes = 4 for Java + 2 for Clojure + 2 for Python

(def inner-code5 (str/replace inner-code4 #\"¥\" \"\\\\\\\\\\\\\\\\\\\"\"))




(def python-code

  (str

    \"import subprocess\\n\"

    \"inner_code = '''\" inner-code5 \"'''\\n\"

    \"print('About to run nested clojure...')\\n\"

    \"result = subprocess.run(['clojure', '-e', inner_code], capture_output=True, text=True)\\n\"

    \"print(result.stdout)\\n\"

    \"print('Back in Python!')\\n\"))




(def result (shell/sh \"python3\" \"-c\" python-code))




(println (:out result))

(println (:err result))

\"\"\"




print(\"Running nested clojure from Python...\")

subprocess.run([\"clojure\", \"-e\", inner_code])

print(\"Done.\")

EOF
")

(println "Executing the embedded shell script...")
(let [result (shell/sh "bash" "-c" shell-script-content)]
  (println (:out result))
  (when-not (empty? (:err result))
    (println "--- STDERR ---")
    (println (:err result)))
  (println "Shell script execution finished."))
