#include "yacht.h"
#include <algorithm>
#include <map>
#include <numeric>

namespace yacht {
    int score(const std::vector<int>& dice, const std::string& category) {
        // Count occurrences of each dice value
        std::map<int, int> counts;
        for (int die : dice) {
            counts[die]++;
        }

        // Handle number categories (ones through sixes)
        if (category == "ones") return counts[1] * 1;
        if (category == "twos") return counts[2] * 2;
        if (category == "threes") return counts[3] * 3;
        if (category == "fours") return counts[4] * 4;
        if (category == "fives") return counts[5] * 5;
        if (category == "sixes") return counts[6] * 6;

        // Handle yacht (five of a kind)
        if (category == "yacht") {
            for (const auto& [value, count] : counts) {
                if (count == 5) return 50;
            }
            return 0;
        }

        // Handle full house (three of one number and two of another)
        if (category == "full house") {
            bool has_three = false;
            bool has_two = false;
            
            for (const auto& [value, count] : counts) {
                if (count == 3) has_three = true;
                if (count == 2) has_two = true;
            }
            
            if (has_three && has_two) {
                return std::accumulate(dice.begin(), dice.end(), 0);
            }
            return 0;
        }

        // Handle four of a kind
        if (category == "four of a kind") {
            for (const auto& [value, count] : counts) {
                if (count >= 4) return value * 4;
            }
            return 0;
        }

        // Handle little straight (1-2-3-4-5)
        if (category == "little straight") {
            if (counts[1] == 1 && counts[2] == 1 && counts[3] == 1 &&
                counts[4] == 1 && counts[5] == 1) {
                return 30;
            }
            return 0;
        }

        // Handle big straight (2-3-4-5-6)
        if (category == "big straight") {
            if (counts[2] == 1 && counts[3] == 1 && counts[4] == 1 &&
                counts[5] == 1 && counts[6] == 1) {
                return 30;
            }
            return 0;
        }

        // Handle choice (sum of all dice)
        if (category == "choice") {
            return std::accumulate(dice.begin(), dice.end(), 0);
        }

        // Default case (unknown category)
        return 0;
    }
}  // namespace yacht
