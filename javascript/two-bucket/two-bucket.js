//
// This is only a SKELETON file for the 'Two Bucket' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class TwoBucket {
  constructor(bucketOne, bucketTwo, goal, startBucket) {
    this.bucketOne = bucketOne;
    this.bucketTwo = bucketTwo;
    this.goal = goal;
    this.startBucket = startBucket;
    
    // Check if goal is achievable
    const maxBucket = Math.max(bucketOne, bucketTwo);
    if (goal > maxBucket) {
      throw new Error('Goal cannot be greater than the largest bucket');
    }
    
    // Check if goal is divisible by the GCD of bucket sizes
    const gcd = this.gcd(bucketOne, bucketTwo);
    if (goal % gcd !== 0) {
      throw new Error('Goal is not achievable with these bucket sizes');
    }
  }
  
  // Helper function to calculate the greatest common divisor
  gcd(a, b) {
    return b === 0 ? a : this.gcd(b, a % b);
  }
  
  solve() {
    // Initialize state based on which bucket to start with
    let initialState;
    if (this.startBucket === 'one') {
      initialState = [this.bucketOne, 0]; // Fill bucket one first
    } else {
      initialState = [0, this.bucketTwo]; // Fill bucket two first
    }
    
    // BFS to find the shortest path to the goal
    const queue = [{ state: initialState, moves: 1, history: new Set([initialState.toString()]) }];
    
    while (queue.length > 0) {
      const { state, moves, history } = queue.shift();
      const [b1, b2] = state;
      
      // Check if we've reached the goal
      if (b1 === this.goal) {
        return {
          moves,
          goalBucket: 'one',
          otherBucket: b2
        };
      }
      
      if (b2 === this.goal) {
        return {
          moves,
          goalBucket: 'two',
          otherBucket: b1
        };
      }
      
      // Generate all possible next states
      const nextStates = this.getNextStates(state, history);
      
      for (const nextState of nextStates) {
        const nextStateStr = nextState.toString();
        if (!history.has(nextStateStr)) {
          const newHistory = new Set(history);
          newHistory.add(nextStateStr);
          queue.push({
            state: nextState,
            moves: moves + 1,
            history: newHistory
          });
        }
      }
    }
    
    throw new Error('Goal is not achievable');
  }
  
  getNextStates(state, history) {
    const [b1, b2] = state;
    const nextStates = [];
    
    // Empty bucket one
    if (b1 > 0) {
      nextStates.push([0, b2]);
    }
    
    // Empty bucket two
    if (b2 > 0) {
      nextStates.push([b1, 0]);
    }
    
    // Fill bucket one
    if (b1 < this.bucketOne) {
      nextStates.push([this.bucketOne, b2]);
    }
    
    // Fill bucket two
    if (b2 < this.bucketTwo) {
      nextStates.push([b1, this.bucketTwo]);
    }
    
    // Pour from bucket one to bucket two
    if (b1 > 0 && b2 < this.bucketTwo) {
      const amount = Math.min(b1, this.bucketTwo - b2);
      nextStates.push([b1 - amount, b2 + amount]);
    }
    
    // Pour from bucket two to bucket one
    if (b2 > 0 && b1 < this.bucketOne) {
      const amount = Math.min(b2, this.bucketOne - b1);
      nextStates.push([b1 + amount, b2 - amount]);
    }
    
    // Filter out invalid states based on the rule:
    // After an action, you may not arrive at a state where the initial starting bucket is empty 
    // and the other bucket is full.
    return nextStates.filter(nextState => {
      const [nextB1, nextB2] = nextState;
      
      if (this.startBucket === 'one' && nextB1 === 0 && nextB2 === this.bucketTwo) {
        return false;
      }
      
      if (this.startBucket === 'two' && nextB2 === 0 && nextB1 === this.bucketOne) {
        return false;
      }
      
      return true;
    });
  }
}
