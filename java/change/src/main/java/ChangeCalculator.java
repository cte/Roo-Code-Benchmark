import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ChangeCalculator {
    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        this.coins = new ArrayList<>(currencyCoins);
        // Sort coins to ensure consistent processing
        Collections.sort(this.coins);
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        // Handle edge cases
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Negative totals are not allowed.");
        }
        
        if (grandTotal == 0) {
            return new ArrayList<>();
        }
        
        // Check if the amount is less than the smallest coin
        int smallestCoin = coins.get(0); // Coins are sorted in ascending order
        if (grandTotal < smallestCoin) {
            throw new IllegalArgumentException(
                "The total " + grandTotal + " cannot be represented in the given currency.");
        }
        
        // dp[i] will store the minimum number of coins needed to make i amount
        int[] dp = new int[grandTotal + 1];
        // prev[i] will store the last coin used to make i amount
        int[] prev = new int[grandTotal + 1];
        
        // Initialize dp array with a value larger than any possible solution
        for (int i = 1; i <= grandTotal; i++) {
            dp[i] = Integer.MAX_VALUE - 1;
        }
        
        // Base case
        dp[0] = 0;
        
        // Fill dp array
        for (int coin : coins) {
            for (int amount = coin; amount <= grandTotal; amount++) {
                if (dp[amount - coin] + 1 < dp[amount]) {
                    dp[amount] = dp[amount - coin] + 1;
                    prev[amount] = coin;
                }
            }
        }
        
        // Check if a solution exists
        if (dp[grandTotal] == Integer.MAX_VALUE - 1) {
            throw new IllegalArgumentException(
                "The total " + grandTotal + " cannot be represented in the given currency.");
        }
        
        // Reconstruct the solution
        List<Integer> change = new ArrayList<>();
        int remaining = grandTotal;
        
        while (remaining > 0) {
            int coin = prev[remaining];
            change.add(coin);
            remaining -= coin;
        }
        
        // Sort the change in ascending order to match test expectations
        Collections.sort(change);
        
        return change;
    }
}
