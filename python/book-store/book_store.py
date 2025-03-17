def total(basket):
    if not basket:
        return 0
    
    # Count the occurrences of each book
    book_counts = {}
    for book in basket:
        book_counts[book] = book_counts.get(book, 0) + 1
    
    # Define discount rates based on group size
    discount_rates = {
        1: 1.00,  # No discount
        2: 0.95,  # 5% discount
        3: 0.90,  # 10% discount
        4: 0.80,  # 20% discount
        5: 0.75,  # 25% discount
    }
    
    # Base price per book
    BOOK_PRICE = 800
    
    # Function to calculate price for a given grouping
    def calculate_price(grouping):
        total_price = 0
        for group_size, count in grouping.items():
            total_price += count * group_size * BOOK_PRICE * discount_rates[group_size]
        return int(total_price)
    
    # Try all possible ways to group the books
    def find_optimal_grouping(remaining_counts, current_grouping=None):
        if current_grouping is None:
            current_grouping = {1: 0, 2: 0, 3: 0, 4: 0, 5: 0}
        
        # If no books left, return the price of the current grouping
        if all(count == 0 for count in remaining_counts.values()):
            return calculate_price(current_grouping)
        
        min_price = float('inf')
        
        # Try forming groups of different sizes
        for size in range(1, min(5, len(remaining_counts)) + 1):
            # Check if we can form a group of this size
            if sum(1 for book, count in remaining_counts.items() if count > 0) >= size:
                # Create a copy of remaining counts
                new_remaining = remaining_counts.copy()
                
                # Take one book from each of the 'size' most frequent books
                books_taken = 0
                for book in sorted(new_remaining.keys(), key=lambda b: (-new_remaining[b], b)):
                    if new_remaining[book] > 0 and books_taken < size:
                        new_remaining[book] -= 1
                        books_taken += 1
                
                # Update the grouping
                new_grouping = current_grouping.copy()
                new_grouping[size] += 1
                
                # Recursively find the best price for the remaining books
                price = find_optimal_grouping(new_remaining, new_grouping)
                min_price = min(min_price, price)
        
        return min_price
    
    # Special case: it's always better to have 2 groups of 4 than 1 group of 5 and 1 group of 3
    def optimize_grouping():
        # Start with a greedy approach
        result = find_optimal_grouping(book_counts)
        
        # Check if we can apply the 2×4 vs 5+3 optimization
        # This is a specific optimization for the case where we have 8 books
        # that could be grouped as either 2 groups of 4 or 1 group of 5 and 1 group of 3
        
        # Count total books
        total_books = sum(book_counts.values())
        
        # If we have at least 8 books and at least 5 different titles
        if total_books >= 8 and len(book_counts) >= 4:
            # Try forcing two groups of 4 instead of other combinations
            temp_counts = book_counts.copy()
            
            # Create a grouping with two groups of 4
            forced_grouping = {1: 0, 2: 0, 3: 0, 4: 0, 5: 0}
            
            # Try to form two groups of 4
            for _ in range(2):
                if sum(1 for book, count in temp_counts.items() if count > 0) >= 4:
                    books_taken = 0
                    for book in sorted(temp_counts.keys(), key=lambda b: (-temp_counts[b], b)):
                        if temp_counts[book] > 0 and books_taken < 4:
                            temp_counts[book] -= 1
                            books_taken += 1
                    
                    if books_taken == 4:
                        forced_grouping[4] += 1
            
            # If we successfully formed two groups of 4
            if forced_grouping[4] == 2:
                # Calculate the price for the remaining books
                remaining_price = find_optimal_grouping(temp_counts)
                forced_price = calculate_price(forced_grouping) + remaining_price
                
                # Return the better price
                return min(result, forced_price)
        
        return result
    
    return optimize_grouping()
