import java.util.*;

class TwoBucket {
    private final int bucketOneCap;
    private final int bucketTwoCap;
    private final int desiredLiters;
    private final boolean startWithOne;
    
    private int moves;
    private String finalBucket;
    private int otherBucketAmount;

    TwoBucket(int bucketOneCap, int bucketTwoCap, int desiredLiters, String startBucket) {
        this.bucketOneCap = bucketOneCap;
        this.bucketTwoCap = bucketTwoCap;
        this.desiredLiters = desiredLiters;
        this.startWithOne = startBucket.equals("one");
        
        solve();
    }

    private void solve() {
        // Use BFS to find the shortest path to the solution
        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();
        
        // Initial state: both buckets empty
        State initialState = new State(0, 0, 0);
        queue.add(initialState);
        visited.add(initialState);
        
        while (!queue.isEmpty()) {
            State current = queue.poll();
            
            // If this is the first move, fill the starting bucket
            if (current.moves == 0) {
                State next = new State(
                    startWithOne ? bucketOneCap : 0,
                    startWithOne ? 0 : bucketTwoCap,
                    1
                );
                
                if (!visited.contains(next)) {
                    queue.add(next);
                    visited.add(next);
                }
                continue;
            }
            
            // Check if we've reached the goal
            if (current.bucketOne == desiredLiters) {
                moves = current.moves;
                finalBucket = "one";
                otherBucketAmount = current.bucketTwo;
                return;
            } else if (current.bucketTwo == desiredLiters) {
                moves = current.moves;
                finalBucket = "two";
                otherBucketAmount = current.bucketOne;
                return;
            }
            
            // Try all possible actions
            List<State> nextStates = new ArrayList<>();
            
            // 1. Empty bucket one
            if (current.bucketOne > 0) {
                nextStates.add(new State(0, current.bucketTwo, current.moves + 1));
            }
            
            // 2. Empty bucket two
            if (current.bucketTwo > 0) {
                nextStates.add(new State(current.bucketOne, 0, current.moves + 1));
            }
            
            // 3. Fill bucket one
            if (current.bucketOne < bucketOneCap) {
                nextStates.add(new State(bucketOneCap, current.bucketTwo, current.moves + 1));
            }
            
            // 4. Fill bucket two
            if (current.bucketTwo < bucketTwoCap) {
                nextStates.add(new State(current.bucketOne, bucketTwoCap, current.moves + 1));
            }
            
            // 5. Pour from bucket one to bucket two
            if (current.bucketOne > 0 && current.bucketTwo < bucketTwoCap) {
                int amountToPour = Math.min(current.bucketOne, bucketTwoCap - current.bucketTwo);
                nextStates.add(new State(
                    current.bucketOne - amountToPour,
                    current.bucketTwo + amountToPour,
                    current.moves + 1
                ));
            }
            
            // 6. Pour from bucket two to bucket one
            if (current.bucketTwo > 0 && current.bucketOne < bucketOneCap) {
                int amountToPour = Math.min(current.bucketTwo, bucketOneCap - current.bucketOne);
                nextStates.add(new State(
                    current.bucketOne + amountToPour,
                    current.bucketTwo - amountToPour,
                    current.moves + 1
                ));
            }
            
            // Add valid next states to the queue
            for (State next : nextStates) {
                // Skip invalid states (rule 3: after an action, you may not arrive at a state 
                // where the initial starting bucket is empty and the other bucket is full)
                if ((startWithOne && next.bucketOne == 0 && next.bucketTwo == bucketTwoCap) ||
                    (!startWithOne && next.bucketTwo == 0 && next.bucketOne == bucketOneCap)) {
                    continue;
                }
                
                if (!visited.contains(next)) {
                    queue.add(next);
                    visited.add(next);
                }
            }
        }
    }

    int getTotalMoves() {
        return moves;
    }

    String getFinalBucket() {
        return finalBucket;
    }

    int getOtherBucket() {
        return otherBucketAmount;
    }
    
    // Helper class to represent a state in the search
    private class State {
        final int bucketOne;
        final int bucketTwo;
        final int moves;
        
        State(int bucketOne, int bucketTwo, int moves) {
            this.bucketOne = bucketOne;
            this.bucketTwo = bucketTwo;
            this.moves = moves;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return bucketOne == state.bucketOne && bucketTwo == state.bucketTwo;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(bucketOne, bucketTwo);
        }
    }
}
