import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;

class PalindromeCalculator {

    SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
        if (minFactor > maxFactor) {
            throw new IllegalArgumentException("invalid input: min must be <= max");
        }

        SortedMap<Long, List<List<Integer>>> palindromeProducts = new TreeMap<>();

        // Iterate through all possible factor pairs
        for (int i = minFactor; i <= maxFactor; i++) {
            for (int j = i; j <= maxFactor; j++) {
                long product = (long) i * j;
                
                // Check if the product is a palindrome
                if (isPalindrome(product)) {
                    // Add the factor pair to our result map
                    if (!palindromeProducts.containsKey(product)) {
                        palindromeProducts.put(product, new ArrayList<>());
                    }
                    palindromeProducts.get(product).add(Arrays.asList(i, j));
                }
            }
        }

        return palindromeProducts;
    }

    /**
     * Checks if a number is a palindrome (reads the same forwards and backwards).
     * 
     * @param number The number to check
     * @return true if the number is a palindrome, false otherwise
     */
    private boolean isPalindrome(long number) {
        String numberStr = Long.toString(number);
        int length = numberStr.length();
        
        for (int i = 0; i < length / 2; i++) {
            if (numberStr.charAt(i) != numberStr.charAt(length - 1 - i)) {
                return false;
            }
        }
        
        return true;
    }
}