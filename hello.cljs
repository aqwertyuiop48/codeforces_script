(ns hello
  (:require ["csv-parse/sync" :as csv]
            ["fs" :as fs]
            ["path" :as path]
            ["shelljs$default" :as sh]
            ["term-size$default" :as term-size]
            ["zx" :refer [$]]
            ["zx$fs" :as zxfs]
            [nbb.core :refer [*file* await]]))

(println "Hello from nbb!")
(println "Sum of 1+2+3 is" (+ 1 2 3))
(println "Files in current directory:")
(println (fs/readdirSync "."))  ;; lists files

(prn (path/resolve "."))

(prn (term-size))

(println (count (str (fs/readFileSync *file*))))

(prn (sh/ls "."))

(prn (csv/parse "foo,bar"))

(prn (zxfs/existsSync *file*))

(await ($ #js ["ls"]))