pub fn lowest_price(books: &[u32]) -> u32 {
    // If there are no books, the price is 0
    if books.is_empty() {
        return 0;
    }

    // Count the number of copies of each book
    let mut counts = [0; 5];
    for &book in books {
        counts[(book as usize) - 1] += 1;
    }

    // Calculate the price using a dynamic programming approach
    calculate_min_price(&counts)
}

fn calculate_min_price(counts: &[u32; 5]) -> u32 {
    // Base price of a single book in cents
    const BOOK_PRICE: u32 = 800;
    
    // Price factors for different group sizes (after discount)
    const PRICE_FACTORS: [f64; 6] = [0.0, 1.0, 0.95, 0.90, 0.80, 0.75];
    
    // The key insight from the test cases is that sometimes it's better to have
    // groups of 4 than groups of 5 + 3. Specifically:
    // - 2 groups of 4 (2 * 4 * 0.8 = 6.4) is cheaper than
    // - 1 group of 5 (5 * 0.75 = 3.75) + 1 group of 3 (3 * 0.9 = 2.7) = 6.45
    
    // We'll use a recursive approach with memoization to find the optimal grouping
    
    // First, let's define a function to calculate the price of a specific grouping
    let group_price = |size: usize| -> u32 {
        (BOOK_PRICE as f64 * size as f64 * PRICE_FACTORS[size]) as u32
    };
    
    // This is a special case optimization for the book store problem
    // The key insight is that we should always prefer 2 groups of 4 over 1 group of 5 + 1 group of 3
    // Let's implement a recursive solution with this optimization
    
    fn optimal_price(
        remaining: &mut [u32; 5],
        memo: &mut std::collections::HashMap<[u32; 5], u32>,
        group_price: &dyn Fn(usize) -> u32,
    ) -> u32 {
        // Check if we've already computed the price for this state
        if let Some(&price) = memo.get(remaining) {
            return price;
        }
        
        // If there are no books left, the price is 0
        if remaining.iter().all(|&count| count == 0) {
            return 0;
        }
        
        let mut min_price = u32::MAX;
        
        // Try to form a group of 4 books
        if remaining.iter().filter(|&&count| count > 0).count() >= 4 {
            // Make a copy of the remaining books
            let mut new_remaining = *remaining;
            let mut books_taken = 0;
            
            // Take one book of each type, prioritizing the most common books
            let mut indices: Vec<usize> = (0..5).collect();
            indices.sort_by_key(|&i| std::cmp::Reverse(new_remaining[i]));
            
            for &i in indices.iter() {
                if new_remaining[i] > 0 && books_taken < 4 {
                    new_remaining[i] -= 1;
                    books_taken += 1;
                }
            }
            
            // Calculate the price with this group of 4
            let price = group_price(4) + optimal_price(&mut new_remaining, memo, group_price);
            min_price = min_price.min(price);
        }
        
        // Try to form a group of 5 books
        if remaining.iter().filter(|&&count| count > 0).count() == 5 {
            // Make a copy of the remaining books
            let mut new_remaining = *remaining;
            
            // Take one book of each type
            for book_count in new_remaining.iter_mut() {
                *book_count -= 1;
            }
            
            // Calculate the price with this group of 5
            let price = group_price(5) + optimal_price(&mut new_remaining, memo, group_price);
            min_price = min_price.min(price);
        }
        
        // Try to form a group of 3 books
        if remaining.iter().filter(|&&count| count > 0).count() >= 3 {
            // Make a copy of the remaining books
            let mut new_remaining = *remaining;
            let mut books_taken = 0;
            
            // Take one book of each type, prioritizing the most common books
            let mut indices: Vec<usize> = (0..5).collect();
            indices.sort_by_key(|&i| std::cmp::Reverse(new_remaining[i]));
            
            for &i in indices.iter() {
                if new_remaining[i] > 0 && books_taken < 3 {
                    new_remaining[i] -= 1;
                    books_taken += 1;
                }
            }
            
            // Calculate the price with this group of 3
            let price = group_price(3) + optimal_price(&mut new_remaining, memo, group_price);
            min_price = min_price.min(price);
        }
        
        // Try to form a group of 2 books
        if remaining.iter().filter(|&&count| count > 0).count() >= 2 {
            // Make a copy of the remaining books
            let mut new_remaining = *remaining;
            let mut books_taken = 0;
            
            // Take one book of each type, prioritizing the most common books
            let mut indices: Vec<usize> = (0..5).collect();
            indices.sort_by_key(|&i| std::cmp::Reverse(new_remaining[i]));
            
            for &i in indices.iter() {
                if new_remaining[i] > 0 && books_taken < 2 {
                    new_remaining[i] -= 1;
                    books_taken += 1;
                }
            }
            
            // Calculate the price with this group of 2
            let price = group_price(2) + optimal_price(&mut new_remaining, memo, group_price);
            min_price = min_price.min(price);
        }
        
        // Try to take just one book
        for i in 0..5 {
            if remaining[i] > 0 {
                // Make a copy of the remaining books
                let mut new_remaining = *remaining;
                new_remaining[i] -= 1;
                
                // Calculate the price with this single book
                let price = group_price(1) + optimal_price(&mut new_remaining, memo, group_price);
                min_price = min_price.min(price);
                
                // We only need to try one book (they all have the same price)
                break;
            }
        }
        
        // Memoize the result
        memo.insert(*remaining, min_price);
        
        min_price
    }
    
    // Create a memoization cache
    let mut memo = std::collections::HashMap::new();
    
    // Calculate the optimal price
    optimal_price(&mut counts.clone(), &mut memo, &group_price)
}
