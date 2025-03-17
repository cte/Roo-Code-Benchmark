//
// This is only a SKELETON file for the 'Killer Sudoku Helper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const combinations = (cage) => {
  const { sum, size, exclude } = cage;
  
  // For trivial 1-digit cages, just return the digit if it's valid
  if (size === 1) {
    // Check if the sum is a valid digit (1-9) and not excluded
    if (sum >= 1 && sum <= 9 && !exclude.includes(sum)) {
      return [[sum]];
    }
    return [];
  }
  
  // For the special case of all digits 1-9
  if (size === 9 && sum === 45) {
    return [[1, 2, 3, 4, 5, 6, 7, 8, 9]];
  }
  
  // Generate all valid combinations
  return findValidCombinations(sum, size, exclude);
};

/**
 * Finds all valid combinations of digits that sum to the target
 * @param {number} targetSum - The sum the digits should add up to
 * @param {number} size - The number of digits in the combination
 * @param {number[]} exclude - Digits that cannot be used
 * @returns {number[][]} - Array of valid combinations
 */
function findValidCombinations(targetSum, size, exclude) {
  const result = [];
  
  // Helper function to generate combinations using backtracking
  function backtrack(currentSum, currentSize, start, currentCombination) {
    // Base case: if we have the right number of digits
    if (currentSize === size) {
      // Check if the sum matches the target
      if (currentSum === targetSum) {
        result.push([...currentCombination]);
      }
      return;
    }
    
    // Try adding each possible digit
    for (let digit = start; digit <= 9; digit++) {
      // Skip excluded digits
      if (exclude.includes(digit)) {
        continue;
      }
      
      // Skip if adding this digit would exceed the target sum
      if (currentSum + digit > targetSum) {
        break;
      }
      
      // Add the digit to our combination
      currentCombination.push(digit);
      
      // Recursively try to complete the combination
      backtrack(currentSum + digit, currentSize + 1, digit + 1, currentCombination);
      
      // Backtrack: remove the digit to try other possibilities
      currentCombination.pop();
    }
  }
  
  // Start the backtracking with empty combination
  backtrack(0, 0, 1, []);
  
  // Return the sorted result
  return result;
}
