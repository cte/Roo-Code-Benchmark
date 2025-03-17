//
// This is only a SKELETON file for the 'Sum Of Multiples' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const sum = (factors, limit) => {
  // Handle edge cases
  if (factors.length === 0) return 0;
  
  // Create a set to store unique multiples
  const multiples = new Set();
  
  // For each factor, find all its multiples less than the limit
  for (const factor of factors) {
    // Skip factor 0 as it doesn't contribute any valid multiples
    if (factor === 0) continue;
    
    // Find all multiples of the current factor less than the limit
    for (let multiple = factor; multiple < limit; multiple += factor) {
      multiples.add(multiple);
    }
  }
  
  // Sum all unique multiples
  return Array.from(multiples).reduce((total, num) => total + num, 0);
};
