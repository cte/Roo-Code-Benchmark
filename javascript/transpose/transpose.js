//
// This is only a SKELETON file for the 'Transpose' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const transpose = (input) => {
  // Handle empty input case
  if (input.length === 0) {
    return [];
  }

  // Find the maximum length of any row
  const maxLength = Math.max(...input.map(row => row.length));
  
  // Create the result array
  const result = [];
  
  // Iterate through each column
  for (let col = 0; col < maxLength; col++) {
    let newRow = '';
    
    // Iterate through each row
    for (let row = 0; row < input.length; row++) {
      // If the current row has a character at this column, add it
      // Otherwise, add a space (but only if there are rows below with characters)
      if (col < input[row].length) {
        newRow += input[row][col];
      } else {
        // Check if any row below has a character at this column
        let needsSpace = false;
        for (let nextRow = row + 1; nextRow < input.length; nextRow++) {
          if (col < input[nextRow].length) {
            needsSpace = true;
            break;
          }
        }
        newRow += needsSpace ? ' ' : '';
      }
    }
    
    result.push(newRow);
  }
  
  return result;
};
