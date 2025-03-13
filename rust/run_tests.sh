#!/bin/bash

# https://github.com/exercism/rust/blob/main/docs/TESTS.md
# https://www.rust-lang.org/tools/install

success_count=0
failure_count=0
total_count=0

for dir in */; do # Loop through each subdirectory.
  if [ -d "$dir" ]; then
    name=${dir%/} # Remove trailing slash from directory name.

    if [ -f "$dir/Cargo.toml" ]; then
      ((total_count++))
      (cd "$dir" && cargo test > /dev/null 2>&1)

      if [ $? -eq 0 ]; then
        echo "🟢 $name"
        ((success_count++))
      else
        echo "🔴 $name"
        ((failure_count++))
      fi
    else
      echo "⚠️ Skipped (no Cargo.toml found)"
    fi
  fi
done

echo "----------------------------------"
echo "$success_count / $total_count ($(((success_count * 100) / (total_count > 0 ? total_count : 1)))%)"
