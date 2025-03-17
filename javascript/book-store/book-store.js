//
// This is only a SKELETON file for the 'BookStore' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const cost = (books) => {
  if (books.length === 0) {
    return 0;
  }

  // Count the occurrences of each book
  const bookCounts = {};
  for (const book of books) {
    bookCounts[book] = (bookCounts[book] || 0) + 1;
  }

  // Calculate the cost for different group sizes
  const calculateGroupCost = (groupSize) => {
    const basePrice = 800 * groupSize;
    
    // Apply discount based on group size
    switch (groupSize) {
      case 5: return basePrice * 0.75; // 25% discount
      case 4: return basePrice * 0.8;  // 20% discount
      case 3: return basePrice * 0.9;  // 10% discount
      case 2: return basePrice * 0.95; // 5% discount
      default: return basePrice;       // No discount
    }
  };

  // Try different grouping strategies and return the minimum cost
  const findMinimumCost = () => {
    // Get the maximum number of copies of any book
    const maxCopies = Math.max(...Object.values(bookCounts));
    
    // Initialize an array to represent each group
    const groups = Array(maxCopies).fill(0);
    
    // Fill the groups with books
    for (const count of Object.values(bookCounts)) {
      for (let i = 0; i < count; i++) {
        groups[i]++;
      }
    }
    
    // Calculate the cost of the current grouping
    let currentCost = groups.reduce((total, size) => total + calculateGroupCost(size), 0);
    
    // Try to optimize by converting groups of 5 and 3 into groups of 4 and 4
    // This is a known optimization for this problem
    let optimized = true;
    while (optimized) {
      optimized = false;
      
      // Find indices of groups with size 5 and 3
      const fiveIndices = groups.map((size, index) => size === 5 ? index : -1).filter(i => i !== -1);
      const threeIndices = groups.map((size, index) => size === 3 ? index : -1).filter(i => i !== -1);
      
      // Try to optimize by converting a group of 5 and a group of 3 into two groups of 4
      for (const fiveIndex of fiveIndices) {
        for (const threeIndex of threeIndices) {
          // Create a copy of the groups array
          const newGroups = [...groups];
          newGroups[fiveIndex] = 4;
          newGroups[threeIndex] = 4;
          
          // Calculate the cost of the new grouping
          const newCost = newGroups.reduce((total, size) => total + calculateGroupCost(size), 0);
          
          // If the new grouping is cheaper, update the groups and cost
          if (newCost < currentCost) {
            groups[fiveIndex] = 4;
            groups[threeIndex] = 4;
            currentCost = newCost;
            optimized = true;
            break;
          }
        }
        if (optimized) break;
      }
    }
    
    return currentCost;
  };

  return findMinimumCost();
};
