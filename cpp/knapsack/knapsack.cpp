#include "knapsack.h"
#include <algorithm>
#include <vector>

namespace knapsack
{

int maximum_value(int maximum_weight, const std::vector<Item>& items)
{
    // If there are no items or the maximum weight is 0, return 0
    if (items.empty() || maximum_weight <= 0) {
        return 0;
    }

    // Create a 2D DP table where dp[i][w] represents the maximum value
    // that can be obtained with the first i items and a maximum weight of w
    const int n = items.size();
    std::vector<std::vector<int>> dp(n + 1, std::vector<int>(maximum_weight + 1, 0));

    // Fill the DP table
    for (int i = 1; i <= n; ++i) {
        const Item& current_item = items[i - 1]; // Adjust for 0-based indexing
        
        for (int w = 0; w <= maximum_weight; ++w) {
            // If the current item's weight is less than or equal to w,
            // we have two choices: include it or exclude it
            if (current_item.weight <= w) {
                // Maximum of including or excluding the current item
                dp[i][w] = std::max(
                    current_item.value + dp[i - 1][w - current_item.weight], // Include
                    dp[i - 1][w] // Exclude
                );
            } else {
                // If the current item is too heavy, we can only exclude it
                dp[i][w] = dp[i - 1][w];
            }
        }
    }

    // The final answer is in the bottom-right cell of the DP table
    return dp[n][maximum_weight];
}

} // namespace knapsack
