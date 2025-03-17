import java.util.*;

class BookStore {
    private static final double BOOK_PRICE = 8.0;
    private static final Map<Integer, Double> DISCOUNT_RATES = new HashMap<>();
    
    static {
        DISCOUNT_RATES.put(1, 0.0);    // No discount for 1 book
        DISCOUNT_RATES.put(2, 0.05);   // 5% discount for 2 different books
        DISCOUNT_RATES.put(3, 0.10);   // 10% discount for 3 different books
        DISCOUNT_RATES.put(4, 0.20);   // 20% discount for 4 different books
        DISCOUNT_RATES.put(5, 0.25);   // 25% discount for 5 different books
    }

    double calculateBasketCost(List<Integer> books) {
        if (books.isEmpty()) {
            return 0.0;
        }
        
        // Count the occurrences of each book
        Map<Integer, Integer> bookCounts = new HashMap<>();
        for (Integer book : books) {
            bookCounts.put(book, bookCounts.getOrDefault(book, 0) + 1);
        }
        
        // Try different grouping strategies to find the optimal one
        return findOptimalPrice(bookCounts);
    }
    
    private double findOptimalPrice(Map<Integer, Integer> bookCounts) {
        // Convert book counts to a list for easier manipulation
        List<Integer> counts = new ArrayList<>(bookCounts.values());
        
        // Sort in descending order to prioritize books with more copies
        counts.sort(Collections.reverseOrder());
        
        // Try different grouping strategies
        return findMinimumPrice(counts);
    }
    
    private double findMinimumPrice(List<Integer> counts) {
        // Base case: no more books to process
        if (counts.stream().allMatch(count -> count == 0)) {
            return 0.0;
        }
        
        double minPrice = Double.MAX_VALUE;
        
        // Try to form groups of different sizes (from 1 to 5)
        for (int groupSize = 1; groupSize <= 5; groupSize++) {
            // Check if we can form a group of this size
            if (counts.size() >= groupSize && counts.get(groupSize - 1) > 0) {
                // Create a copy of the counts list
                List<Integer> newCounts = new ArrayList<>(counts);
                
                // Take one book from each of the top 'groupSize' piles
                for (int i = 0; i < groupSize; i++) {
                    newCounts.set(i, newCounts.get(i) - 1);
                }
                
                // Remove any zero counts
                newCounts.removeIf(count -> count == 0);
                
                // Sort in descending order
                newCounts.sort(Collections.reverseOrder());
                
                // Calculate the price for this group
                double groupPrice = groupSize * BOOK_PRICE * (1 - DISCOUNT_RATES.get(groupSize));
                
                // Recursively find the price for the remaining books
                double remainingPrice = findMinimumPrice(newCounts);
                
                // Update minimum price if this grouping is better
                minPrice = Math.min(minPrice, groupPrice + remainingPrice);
            }
        }
        
        return minPrice;
    }
}