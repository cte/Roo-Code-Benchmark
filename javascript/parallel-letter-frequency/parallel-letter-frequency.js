//
// This is only a SKELETON file for the 'Parallel Letter Frequency' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

// Helper function to count letter frequencies in a single text
const countLetterFrequency = (text) => {
  const frequency = {};
  
  // Convert to lowercase and process each character
  for (const char of text.toLowerCase()) {
    // Check if the character is a letter (including Unicode letters)
    // \p{L} matches any kind of letter from any language
    if (/\p{L}/u.test(char)) {
      frequency[char] = (frequency[char] || 0) + 1;
    }
  }
  
  return frequency;
};

// Merge multiple frequency objects into one
const mergeFrequencies = (frequencies) => {
  const result = {};
  
  for (const frequency of frequencies) {
    for (const [letter, count] of Object.entries(frequency)) {
      result[letter] = (result[letter] || 0) + count;
    }
  }
  
  return result;
};

export const parallelLetterFrequency = async (texts) => {
  // If there are no texts, return an empty object
  if (texts.length === 0) {
    return {};
  }
  
  // Process each text in parallel using Promise.all
  const promises = texts.map(text => {
    return new Promise(resolve => {
      // Count letter frequencies for this text
      const frequency = countLetterFrequency(text);
      resolve(frequency);
    });
  });
  
  // Wait for all promises to resolve
  const results = await Promise.all(promises);
  
  // Merge all frequency results
  return mergeFrequencies(results);
};
