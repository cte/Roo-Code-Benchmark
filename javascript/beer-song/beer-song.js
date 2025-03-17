//
// This is only a SKELETON file for the 'Beer Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (initialBottlesCount, takeDownCount) => {
  const lyrics = [];
  
  for (let i = 0; i < takeDownCount; i++) {
    const currentBottles = initialBottlesCount - i;
    
    // Add an empty line between verses (except for the first verse)
    if (i > 0) {
      lyrics.push('');
    }
    
    // First line of the verse
    if (currentBottles === 0) {
      lyrics.push('No more bottles of beer on the wall, no more bottles of beer.');
    } else {
      lyrics.push(`${formatBottles(currentBottles)} of beer on the wall, ${formatBottles(currentBottles)} of beer.`);
    }
    
    // Second line of the verse
    if (currentBottles === 0) {
      // Special case for 0 bottles
      lyrics.push('Go to the store and buy some more, 99 bottles of beer on the wall.');
    } else {
      const nextBottles = currentBottles - 1;
      const takeAction = currentBottles === 1 ? 'Take it down' : 'Take one down';
      const nextBottlesText = nextBottles === 0 ? 'no more bottles' : formatBottles(nextBottles);
      lyrics.push(`${takeAction} and pass it around, ${nextBottlesText} of beer on the wall.`);
    }
  }
  
  return lyrics;
};

// Helper function to format the bottle count with proper grammar
function formatBottles(count) {
  if (count === 0) {
    return 'No more bottles';
  } else if (count === 1) {
    return '1 bottle';
  } else {
    return `${count} bottles`;
  }
}
