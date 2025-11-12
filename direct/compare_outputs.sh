#!/bin/bash
# Usage: ./compare_outputs.sh 1A

base="$1"
folder="$base"

echo
echo "=== Side-by-side Output Comparison ==="

for f in "$folder"/test*.in; do
    testname=$(basename "$f" .in)
    echo
    echo "--- Test Case: $testname ---"
    echo -e "[PYTHON]\t[JAVA]\t[KOTLIN]\t[CLOJURE]\t[CLJS]"

    # Read outputs into arrays
    mapfile -t py_lines     < "py_output/${testname}.txt"
    mapfile -t java_lines   < "java_output/${testname}.txt"
    mapfile -t kt_lines     < "kt_output/${testname}.txt"
    mapfile -t clj_lines    < "clj_output/${testname}.txt"
    mapfile -t cljs_lines   < "cljs_output/${testname}.txt"

    # Determine max number of lines
    max_lines=$(printf "%s\n" "${#py_lines[@]}" "${#java_lines[@]}" "${#kt_lines[@]}" "${#clj_lines[@]}" "${#cljs_lines[@]}" | sort -nr | head -1)

    # Print line by line
    for ((i = 0; i < max_lines; i++)); do
        printf "%-45s\t%-45s\t%-45s\t%-45s\t%-45s\n" \
            "${py_lines[i]}" \
            "${java_lines[i]}" \
            "${kt_lines[i]}" \
            "${clj_lines[i]}" \
            "${cljs_lines[i]}"
    done
done
