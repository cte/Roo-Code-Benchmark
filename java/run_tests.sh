#!/bin/bash

# https://github.com/exercism/java/blob/main/docs/TESTS.md

lang="java"
success_count=0
failure_count=0
total_count=0

echo "--------------------------------------------------------------------------------"

for dir in */; do
  if [ -d "$dir" ]; then
    name=${dir%/} # Remove trailing slash from directory name.

    if [ -f "$dir/build.gradle" ]; then
      ((total_count++))
      (timeout --foreground 15s bash -c "cd '$dir' && ./gradlew test > /dev/null 2>&1")

      if [ $? -eq 0 ]; then
        echo "🟢 $lang/$name"
        ((success_count++))
      else
        echo "🔴 $lang/$name"
        ((failure_count++))
      fi
    else
      echo "⚠️ Skipped (no build.gradle found)"
    fi
  fi
done

echo "$success_count / $total_count ($(((success_count * 100) / (total_count > 0 ? total_count : 1)))%)"
