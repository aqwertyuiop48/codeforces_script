#!/bin/bash

script="$1"
folder="${script%.*}"   # remove extension to get folder name
ext="${script##*.}"     # get extension: py / java / kt / clj / cljs

# Create output folders if they don't exist
mkdir -p py_output java_output kt_output clj_output cljs_output

for f in "$folder"/test*.in; do
    echo "== Running $f =="
    echo "Input:"
    cat "$f"
    echo    # blank line
    echo "====="
    echo    # blank line after =====
    echo "Output:"

    testname=$(basename "$f" .in)  # e.g. test1

    if [ "$ext" = "kt" ]; then
        echo "[Kotlin] Compiling: $script"
        basename="${script%.*}"   # removes .kt → gets '1A'
        jarfile="${basename}.jar"
        kotlinc "$script" -include-runtime -d "$jarfile"

        echo "[Kotlin] Running: $jarfile"
        cat "$f" | java -jar "$jarfile" | tee "kt_output/${testname}.txt"

        echo "====="
        echo "[Kotlin] Running script with appended main(): $script"
        tmpfile=$(mktemp /tmp/${folder}_temp_XXXXXX.kts)
        cat "$script" > "$tmpfile"
        echo "" >> "$tmpfile"
        echo "main()" >> "$tmpfile"
        cat "$f" | kotlin "$tmpfile" >> "kt_output/${testname}.txt"
        rm "$tmpfile"

    elif [ "$ext" = "py" ]; then
        echo "[Python] Running: $script"
        python3 "$script" < "$f" | tee "py_output/${testname}.txt"

    elif [ "$ext" = "java" ]; then
        echo "[Java] Running: $script"
        java "$script" < "$f" | tee "java_output/${testname}.txt"

    elif [ "$ext" = "clj" ]; then
        echo "[Clojure] Running: $script"
        clojure "$script" < "$f" | tee "clj_output/${testname}.txt"

    elif [ "$ext" = "cljs" ]; then
        echo "[ClojureScript] Running: $script"
        nbb "$script" < "$f" | tee "cljs_output/${testname}.txt"

    else
        echo "Unsupported extension: $ext"
        exit 1
    fi

    echo
done
