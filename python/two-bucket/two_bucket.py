def measure(bucket_one, bucket_two, goal, start_bucket):
    # Check if goal is achievable
    if goal > max(bucket_one, bucket_two):
        raise ValueError("Goal cannot be greater than the capacity of either bucket")
    
    # Check if goal is possible to measure with these bucket sizes
    # Goal must be a multiple of the GCD of the bucket sizes
    if goal % gcd(bucket_one, bucket_two) != 0:
        raise ValueError("Goal is not measurable with these bucket sizes")
    
    # Initialize buckets based on start_bucket
    if start_bucket == "one":
        start = (bucket_one, 0)
    else:  # start_bucket == "two"
        start = (0, bucket_two)
    
    # Track visited states to avoid cycles
    visited = {start}
    
    # Queue for BFS, storing (state, moves) pairs
    queue = [(start, 1)]  # Start with 1 move (initial fill)
    
    while queue:
        (x, y), moves = queue.pop(0)
        
        # Check if we've reached the goal
        if x == goal:
            return (moves, "one", y)
        if y == goal:
            return (moves, "two", x)
        
        # Generate all possible next states
        next_states = []
        
        # 1. Fill bucket one
        next_states.append(((bucket_one, y), moves + 1))
        
        # 2. Fill bucket two
        next_states.append(((x, bucket_two), moves + 1))
        
        # 3. Empty bucket one
        next_states.append(((0, y), moves + 1))
        
        # 4. Empty bucket two
        next_states.append(((x, 0), moves + 1))
        
        # 5. Pour from bucket one to bucket two
        pour_amount = min(x, bucket_two - y)
        next_states.append(((x - pour_amount, y + pour_amount), moves + 1))
        
        # 6. Pour from bucket two to bucket one
        pour_amount = min(y, bucket_one - x)
        next_states.append(((x + pour_amount, y - pour_amount), moves + 1))
        
        # Add valid next states to the queue
        for state, new_moves in next_states:
            # Skip invalid states (violating rule 3)
            if start_bucket == "one" and state == (0, bucket_two):
                continue
            if start_bucket == "two" and state == (bucket_one, 0):
                continue
            
            if state not in visited:
                visited.add(state)
                queue.append((state, new_moves))
    
    # If we get here, no solution was found
    raise ValueError("No solution exists")


def gcd(a, b):
    """Calculate the greatest common divisor of a and b."""
    while b:
        a, b = b, a % b
    return a
