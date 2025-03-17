export const solve = (puzzle) => {
  // Parse the puzzle into operands and result
  const parts = puzzle.split('==');
  if (parts.length !== 2) return null;
  
  const rightSide = parts[1].trim();
  const leftSide = parts[0].trim();
  const operands = leftSide.split('+').map(op => op.trim());
  
  // Extract all unique letters from the puzzle
  const letters = new Set();
  for (const char of puzzle) {
    if (/[A-Z]/i.test(char)) {
      letters.add(char);
    }
  }
  
  // If we have more than 10 unique letters, it's impossible to solve
  if (letters.size > 10) return null;
  
  // Find the first letter of each operand and the result
  const firstLetters = new Set();
  for (const operand of operands) {
    if (operand.length > 0) {
      firstLetters.add(operand[0]);
    }
  }
  if (rightSide.length > 0) {
    firstLetters.add(rightSide[0]);
  }
  
  // Convert sets to arrays for easier manipulation
  const lettersArray = Array.from(letters);
  const firstLettersArray = Array.from(firstLetters);
  
  // Generate permutations and check for valid solutions
  return findSolution(lettersArray, firstLettersArray, operands, rightSide);
};

/**
 * Recursively try different digit assignments to find a valid solution
 */
function findSolution(letters, firstLetters, operands, result, assignment = {}, index = 0, usedDigits = new Set()) {
  // Base case: all letters have been assigned a digit
  if (index === letters.length) {
    // Check if the solution is valid
    return isValidSolution(operands, result, assignment) ? { ...assignment } : null;
  }
  
  const letter = letters[index];
  
  // Try each possible digit for the current letter
  for (let digit = 0; digit <= 9; digit++) {
    // Skip if this digit is already used
    if (usedDigits.has(digit)) continue;
    
    // Skip if this is a first letter and digit is 0
    if (firstLetters.includes(letter) && digit === 0) continue;
    
    // Assign the digit to the letter
    assignment[letter] = digit;
    usedDigits.add(digit);
    
    // Recursively try to assign digits to the remaining letters
    const solution = findSolution(letters, firstLetters, operands, result, assignment, index + 1, usedDigits);
    if (solution) return solution;
    
    // Backtrack
    delete assignment[letter];
    usedDigits.delete(digit);
  }
  
  return null;
}

/**
 * Check if the current assignment produces a valid equation
 */
function isValidSolution(operands, result, assignment) {
  // Convert operands and result to numbers based on the assignment
  const operandValues = operands.map(operand => {
    return convertToNumber(operand, assignment);
  });
  
  const resultValue = convertToNumber(result, assignment);
  
  // Check if the sum of operands equals the result
  return operandValues.reduce((sum, value) => sum + value, 0) === resultValue;
}

/**
 * Convert a string of letters to a number using the assignment
 */
function convertToNumber(str, assignment) {
  let result = 0;
  for (const char of str) {
    if (/[A-Z]/i.test(char)) {
      result = result * 10 + assignment[char];
    }
  }
  return result;
}
