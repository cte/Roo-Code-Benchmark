#!/bin/bash

# https://github.com/exercism/python/blob/main/docs/TESTS.md
# https://github.com/astral-sh/uv

lang="python"
success_count=0
failure_count=0
total_count=0

echo "--------------------------------------------------------------------------------"

for dir in */; do
  if [ -d "$dir" ]; then
    name=${dir%/} # Remove trailing slash from directory name.

    if [ -n "$(find "$dir" -maxdepth 1 -name "*_test.py" -print -quit)" ]; then
      ((total_count++))
      (timeout 15s bash -c "cd '$dir' && uv run python3 -m pytest -o markers=task *_test.py")

      if [ $? -eq 0 ]; then
        echo -n "🟢 $lang/$name"
        ((success_count++))
        PASSED=true
      else
        echo -n "🔴 $lang/$name"
        ((failure_count++))
        PASSED=false
      fi

      USAGE="$dir/usage.json"

      if [ -f $USAGE ]; then
        curl -f -X POST http://localhost:3000/api/tasks \
          -H "Content-Type: application/json" \
          -d "{
            \"runId\": $(jq -r '.runId' $USAGE),
            \"language\": \"$lang\",
            \"exercise\": \"$name\",
            \"tokensIn\": $(jq -r '.totalTokensIn' $USAGE),
            \"tokensOut\": $(jq -r '.totalTokensOut' $USAGE),
            \"tokensContext\": $(jq -r '.contextTokens' $USAGE),
            \"cacheWrites\": $(jq -r '.totalCacheWrites' $USAGE),
            \"cacheReads\": $(jq -r '.totalCacheReads' $USAGE),
            \"cost\": $(jq -r '.totalCost' $USAGE),
            \"duration\": $(jq -r '.duration' $USAGE),
            \"passed\": $PASSED
          }" >/dev/null 2>&1 && echo " 💾" || echo " 🚨"
      else
        echo " 🚨"
      fi
    else
      echo "⚠️ Skipped (no test.py found)"
    fi
  fi
done

echo "$success_count / $total_count ($(((success_count * 100) / (total_count > 0 ? total_count : 1)))%)"
