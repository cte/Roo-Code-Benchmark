#!/bin/bash

# https://github.com/exercism/cpp/blob/main/docs/TESTS.md

lang="c++"
success_count=0
failure_count=0
total_count=0

echo "--------------------------------------------------------------------------------"

for dir in */; do
  if [ -d "$dir" ]; then
    name=${dir%/} # Remove trailing slash from directory name.

    if [ -f "$dir/CMakeLists.txt" ]; then
      ((total_count++))
      (cd "$dir" && mkdir -p build && cd build && cmake -G "Unix Makefiles" .. >/dev/null 2>&1 && make >/dev/null 2>&1)

      if [ $? -eq 0 ]; then
        echo "🟢 $lang/$name"
        ((success_count++))
      else
        echo "🔴 $lang/$name"
        ((failure_count++))
      fi
    else
      echo "⚠️ Skipped (no CMakeLists.txt found)"
    fi
  fi
done

echo "$success_count / $total_count ($(((success_count * 100) / (total_count > 0 ? total_count : 1)))%)"
echo '{"success": '$success_count', "total": '$total_count'}' > results.json
