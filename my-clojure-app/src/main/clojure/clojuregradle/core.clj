(ns clojuregradle.core
  (:require
   [clojure.string :as str]) ;; Reserved for future string ops

  (:import
   ;; Java
   (java.time LocalDate)
   (java.time.format DateTimeFormatter)
   (java.util Locale)

   ;; Kotlin
   (kotlin Pair)
   (utils KStrings)

   ;; Groovy
   (utils GStrings)
   )
  (:gen-class))

(defn -main
  "Main entry point demonstrating interop with Java, Kotlin, and Groovy libraries."
  [& args]
  (println "
  ==============================================
  Hello from Clojure with Gradle, Kotlin, Groovy & Java 21!
  ==============================================
  ")

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; --- Java Interop Examples ---
  (println "\n--- Java Interop ---")
  (let [today (LocalDate/now)
        ten-days-later (.plusDays today 10)
        fmt (DateTimeFormatter/ofPattern "dd MMM yyyy")]
    (println (str "Today + 10 days (raw): " (.toString ten-days-later)))
    (println (str "Today + 10 days (formatted): " (.format ten-days-later fmt))))

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; --- Kotlin Interop Examples ---
  (println "\n--- Kotlin Interop ---")

  ;; Kotlin Pair
  (let [pair (Pair. "Clojure" 2025)]
    (println (str "Kotlin Pair first: " (.getFirst pair)))
    (println (str "Kotlin Pair second: " (.getSecond pair))))

  ;; Kotlin object with @JvmStatic methods
  (println "\n--- Kotlin String Extension ---")
  (println "Uppercase: " (KStrings/uppercase "clojure"))
  (println "Lowercase: " (KStrings/lowercase "CLOJURE"))
  (println "Capitalized: " (KStrings/capitalize "hello"))
  (println "Decapitalized: " (KStrings/decapitalize "Hello"))
  (println "Reversed: " (KStrings/reversed "abcde"))
  (println "Trimmed: " (KStrings/trim "   lots of space   "))
  (println "Is Blank: " (KStrings/isBlank "   "))
  (println "Repeated: " (KStrings/repeat "ha" 3))
  (println "Removed prefix: " (KStrings/removePrefix "unhappy" "un"))
  (println "Removed suffix: " (KStrings/removeSuffix "filename.txt" ".txt"))
  (println "Replaced: " (KStrings/replace "hello world" "world" "Clojure"))

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; --- Groovy interop ---
  (println "\n=== Groovy interop ===")
  (println "Reversed: " (GStrings/reverse "clojure"))
  (println "Capitalized: " (GStrings/capitalize "clojure"))
  (println "Is all upper case: " (GStrings/isAllUpperCase "CLOJURE"))
  (println "Is all lower case: " (GStrings/isAllLowerCase "clojure"))
  (println "Repeated: " (GStrings/repeat "ha" 3))
  (println "Removed prefix: " (GStrings/removePrefix "unhappy" "un"))
  (println "Removed suffix: " (GStrings/removeSuffix "filename.txt" ".txt"))
  (println "Replaced: " (GStrings/replace "hello world" "world" "Groovy"))
  (println "Trimmed: " (GStrings/trim "   lots of space   "))
  (println "Is Blank: " (GStrings/isBlank "   "))


  ;; End of main
  (println "\n=== Done! ===")
  )

;; Optional dev-only code block
(comment
  ;; Example dev-only code
  (+ 1 2 3)
  (println "This won't run during main.")
)
