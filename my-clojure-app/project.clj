(defproject my-clojure-app "0.1.0-SNAPSHOT"
  :description "Polyglot Clojure App"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.jetbrains.kotlin/kotlin-stdlib "1.9.24"]
                 [org.codehaus.groovy/groovy "3.0.21"]
                ;;  [vdmit11/clojure-triple-quote-strings "0.1.0-SNAPSHOT"]
                ]
  :source-paths ["src/main/clojure" "src"]
  :resource-paths ["target/kotlin" "target/groovy"]
  :main ^:skip-aot my-clojure-app.core
  :target-path "target/%s"
  :aliases {"run-gradle" ["run" "-m" "clojuregradle.core"]
            "run-my" ["run" "-m" "my-clojure-app.core"]}
  :profiles {:uberjar {:aot :all}})
