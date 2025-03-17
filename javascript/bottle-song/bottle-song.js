//
// This is only a SKELETON file for the 'Bottle Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (initialBottlesCount, takeDownCount) => {
  const lyrics = [];
  
  for (let i = 0; i < takeDownCount; i++) {
    const currentBottleCount = initialBottlesCount - i;
    const nextBottleCount = currentBottleCount - 1;
    
    // Add verse
    lyrics.push(bottleLine(currentBottleCount));
    lyrics.push(bottleLine(currentBottleCount));
    lyrics.push(`And if one green bottle should accidentally fall,`);
    lyrics.push(`There'll be ${bottlePhrase(nextBottleCount)} hanging on the wall.`);
    
    // Add empty line between verses (except after the last verse)
    if (i < takeDownCount - 1) {
      lyrics.push(``);
    }
  }
  
  return lyrics;
};

// Helper function to generate the bottle line
function bottleLine(count) {
  return `${capitalize(bottlePhrase(count))} hanging on the wall,`;
}

// Helper function to generate the bottle phrase
function bottlePhrase(count) {
  if (count === 0) {
    return 'no green bottles';
  } else if (count === 1) {
    return 'one green bottle';
  } else {
    return `${numberToWord(count)} green bottles`;
  }
}

// Helper function to convert numbers to words
function numberToWord(num) {
  const words = [
    'zero', 'one', 'two', 'three', 'four',
    'five', 'six', 'seven', 'eight', 'nine', 'ten'
  ];
  
  return num <= 10 ? words[num] : num.toString();
}

// Helper function to capitalize the first letter of a string
function capitalize(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}
