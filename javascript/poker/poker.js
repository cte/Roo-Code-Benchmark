// Poker hand evaluator

// Card values in ascending order (2 is lowest, Ace is highest)
const VALUES = '23456789TJQKA';
// Special case for A-5 straight (Ace is treated as 1)
const LOW_ACE_VALUES = 'A2345';

/**
 * Determines the best poker hand(s) from a list of hands
 * @param {string[]} hands - Array of poker hand strings
 * @return {string[]} - Array of the best hand(s)
 */
export const bestHands = (hands) => {
  // Parse and evaluate each hand
  const evaluatedHands = hands.map(hand => ({
    original: hand,
    evaluation: evaluateHand(hand)
  }));

  // Sort hands by rank (highest first)
  evaluatedHands.sort((a, b) => compareHands(b.evaluation, a.evaluation));

  // Get the best hand(s)
  const bestHand = evaluatedHands[0];
  const bestHands = evaluatedHands.filter(hand => 
    compareHands(hand.evaluation, bestHand.evaluation) === 0
  );

  // Return the original hand strings
  return bestHands.map(hand => hand.original);
};

/**
 * Parses a hand string into a structured format
 * @param {string} handString - A string representing a poker hand (e.g. "4S 5S 7H 8D JC")
 * @return {Object[]} - Array of card objects with value and suit properties
 */
function parseHand(handString) {
  return handString.split(' ').map(card => {
    // Handle "10" as a special case
    if (card.startsWith('10')) {
      return {
        value: 'T',
        suit: card[2]
      };
    }
    
    return {
      value: card[0],
      suit: card[1]
    };
  });
}

/**
 * Evaluates a poker hand and returns its rank and relevant values for comparison
 * @param {string} handString - A string representing a poker hand
 * @return {Object} - Object containing the hand's rank and values for comparison
 */
function evaluateHand(handString) {
  const cards = parseHand(handString);
  
  // Get counts of each card value
  const valueCounts = {};
  cards.forEach(card => {
    valueCounts[card.value] = (valueCounts[card.value] || 0) + 1;
  });
  
  // Sort values by count (descending) and then by card rank (descending)
  const valuesByCount = Object.keys(valueCounts).sort((a, b) => {
    const countDiff = valueCounts[b] - valueCounts[a];
    if (countDiff !== 0) return countDiff;
    return VALUES.indexOf(b) - VALUES.indexOf(a);
  });
  
  // Check if all cards have the same suit (flush)
  const isFlush = cards.every(card => card.suit === cards[0].suit);
  
  // Get card values in descending order of rank
  const values = cards.map(card => card.value)
    .sort((a, b) => VALUES.indexOf(b) - VALUES.indexOf(a));
  
  // Check for straight
  let isStraight = false;
  let straightHighCard = null;
  
  // Regular straight check
  if (isConsecutive(values.map(v => VALUES.indexOf(v)))) {
    isStraight = true;
    straightHighCard = values[0];
  }
  
  // Check for A-5 straight (special case where Ace is low)
  if (values.join('').indexOf('A5432') === 0 || 
      values.join('').indexOf('A5432') === 1) {
    isStraight = true;
    straightHighCard = '5'; // 5 is the highest card in A-5 straight
  }
  
  // Determine hand rank
  if (isStraight && isFlush) {
    return { 
      rank: 8, // Straight flush
      values: [straightHighCard]
    };
  }
  
  if (valueCounts[valuesByCount[0]] === 4) {
    return { 
      rank: 7, // Four of a kind
      values: [valuesByCount[0], valuesByCount[1]]
    };
  }
  
  if (valueCounts[valuesByCount[0]] === 3 && valueCounts[valuesByCount[1]] === 2) {
    return { 
      rank: 6, // Full house
      values: [valuesByCount[0], valuesByCount[1]]
    };
  }
  
  if (isFlush) {
    return { 
      rank: 5, // Flush
      values: values
    };
  }
  
  if (isStraight) {
    return { 
      rank: 4, // Straight
      values: [straightHighCard]
    };
  }
  
  if (valueCounts[valuesByCount[0]] === 3) {
    return { 
      rank: 3, // Three of a kind
      values: [valuesByCount[0], ...valuesByCount.slice(1)]
    };
  }
  
  if (valueCounts[valuesByCount[0]] === 2 && valueCounts[valuesByCount[1]] === 2) {
    return { 
      rank: 2, // Two pair
      values: [valuesByCount[0], valuesByCount[1], valuesByCount[2]]
    };
  }
  
  if (valueCounts[valuesByCount[0]] === 2) {
    return { 
      rank: 1, // One pair
      values: [valuesByCount[0], ...valuesByCount.slice(1)]
    };
  }
  
  return { 
    rank: 0, // High card
    values: values
  };
}

/**
 * Checks if an array of numbers is consecutive (straight)
 * @param {number[]} indices - Array of card value indices
 * @return {boolean} - True if the values form a straight
 */
function isConsecutive(indices) {
  // Sort indices in descending order
  const sorted = [...indices].sort((a, b) => b - a);
  
  // Check if indices are consecutive
  for (let i = 1; i < sorted.length; i++) {
    if (sorted[i-1] - sorted[i] !== 1) {
      return false;
    }
  }
  
  return true;
}

/**
 * Compares two hand evaluations to determine which is better
 * @param {Object} a - First hand evaluation
 * @param {Object} b - Second hand evaluation
 * @return {number} - Positive if a is better, negative if b is better, 0 if equal
 */
function compareHands(a, b) {
  // Compare by rank first
  if (a.rank !== b.rank) {
    return a.rank - b.rank;
  }
  
  // If ranks are equal, compare by values
  for (let i = 0; i < a.values.length; i++) {
    const valueA = VALUES.indexOf(a.values[i]);
    const valueB = VALUES.indexOf(b.values[i]);
    
    if (valueA !== valueB) {
      return valueA - valueB;
    }
  }
  
  // Hands are equal
  return 0;
}
