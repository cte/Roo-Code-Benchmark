//
// This is only a SKELETON file for the 'OCR Numbers' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const convert = (input) => {
  // Check if input is provided
  if (!input) {
    throw new Error('Input required');
  }

  // Split the input into rows
  const rows = input.split('\n');
  
  // Group rows into sets of 4 (each set represents a line of digits)
  const lineGroups = [];
  for (let i = 0; i < rows.length; i += 4) {
    if (i + 3 < rows.length) {
      lineGroups.push(rows.slice(i, i + 4));
    }
  }

  // Process each line group
  const results = lineGroups.map(lineGroup => {
    // Check if all rows in the group have the same length
    const rowLength = lineGroup[0].length;
    if (!lineGroup.every(row => row.length === rowLength)) {
      throw new Error('Invalid input: rows have different lengths');
    }

    // Check if the row length is a multiple of 3
    if (rowLength % 3 !== 0) {
      throw new Error('Invalid input: row length is not a multiple of 3');
    }

    // Split each row into chunks of 3 characters
    const digitCount = rowLength / 3;
    let digits = '';

    for (let i = 0; i < digitCount; i++) {
      const pattern = [
        lineGroup[0].substring(i * 3, (i + 1) * 3),
        lineGroup[1].substring(i * 3, (i + 1) * 3),
        lineGroup[2].substring(i * 3, (i + 1) * 3),
        lineGroup[3].substring(i * 3, (i + 1) * 3)
      ].join('');

      digits += recognizeDigit(pattern);
    }

    return digits;
  });

  // Join the results with commas
  return results.join(',');
};

// Function to recognize a digit based on its pattern
function recognizeDigit(pattern) {
  // Define patterns for each digit
  const patterns = {
    ' _ | ||_|   ': '0',
    '     |  |   ': '1',
    ' _  _||_    ': '2',
    ' _  _| _|   ': '3',
    '   |_|  |   ': '4',
    ' _ |_  _|   ': '5',
    ' _ |_ |_|   ': '6',
    ' _   |  |   ': '7',
    ' _ |_||_|   ': '8',
    ' _ |_| _|   ': '9'
  };

  return patterns[pattern] || '?';
}
