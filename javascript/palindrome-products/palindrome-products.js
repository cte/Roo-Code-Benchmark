//
// This is only a SKELETON file for the 'Palindrome Products' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Palindromes {
  static generate({ minFactor, maxFactor }) {
    // Validate input
    if (minFactor > maxFactor) {
      throw new Error('min must be <= max');
    }

    // Store all palindromes and their factors
    const palindromes = new Map();

    // Generate all possible products and check if they're palindromes
    for (let i = minFactor; i <= maxFactor; i++) {
      for (let j = i; j <= maxFactor; j++) {
        const product = i * j;
        
        if (isPalindrome(product)) {
          if (!palindromes.has(product)) {
            palindromes.set(product, []);
          }
          palindromes.get(product).push([i, j]);
        }
      }
    }

    // Convert Map to array of [value, factors] pairs and sort by value
    const sortedPalindromes = Array.from(palindromes.entries()).sort((a, b) => a[0] - b[0]);
    
    // Prepare result object
    return {
      get smallest() {
        if (sortedPalindromes.length === 0) {
          return { value: null, factors: [] };
        }
        const [value, factors] = sortedPalindromes[0];
        return { value, factors };
      },
      
      get largest() {
        if (sortedPalindromes.length === 0) {
          return { value: null, factors: [] };
        }
        const [value, factors] = sortedPalindromes[sortedPalindromes.length - 1];
        return { value, factors };
      }
    };
  }
}

// Helper function to check if a number is a palindrome
function isPalindrome(num) {
  const str = num.toString();
  return str === str.split('').reverse().join('');
}
