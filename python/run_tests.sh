#!/bin/bash

# https://github.com/exercism/python/blob/main/docs/TESTS.md
# https://github.com/astral-sh/uv

success_count=0
failure_count=0
total_count=0

python3 -m uv sync

for dir in */; do # Loop through each subdirectory.
  if [ -d "$dir" ]; then
    name=${dir%/} # Remove trailing slash from directory name.

    if [ -n "$(find "$dir" -maxdepth 1 -name "*_test.py" -print -quit)" ]; then
      ((total_count++))
      (cd "$dir" && python3 -m uv run python3 -m pytest -o markers=task ${name}_test.py > /dev/null 2>&1)

      if [ $? -eq 0 ]; then
        echo "🟢 $name"
        ((success_count++))
      else
        echo "🔴 $name"
        ((failure_count++))
      fi
    else
      echo "⚠️ Skipped (no test.py found)"
    fi
  fi
done

echo "----------------------------------"
echo "$success_count / $total_count ($(((success_count * 100) / (total_count > 0 ? total_count : 1)))%)"
