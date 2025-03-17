export const say = (n) => {
  // Check if number is within the valid range
  if (n < 0 || n > 999999999999) {
    throw new Error('Number must be between 0 and 999,999,999,999.');
  }

  // Handle zero as a special case
  if (n === 0) {
    return 'zero';
  }

  // Break the number into chunks of thousands
  const chunks = [];
  let remaining = n;
  
  while (remaining > 0) {
    chunks.push(remaining % 1000);
    remaining = Math.floor(remaining / 1000);
  }

  // Scale words for each chunk position
  const scales = ['', 'thousand', 'million', 'billion'];
  
  // Process each chunk and add the appropriate scale
  const parts = [];
  
  for (let i = 0; i < chunks.length; i++) {
    if (chunks[i] !== 0) {
      const chunkText = convertLessThanThousand(chunks[i]);
      const scale = scales[i];
      
      if (scale) {
        parts.unshift(`${chunkText} ${scale}`);
      } else {
        parts.unshift(chunkText);
      }
    }
  }
  
  return parts.join(' ');
};

// Convert a number less than 1000 to words
function convertLessThanThousand(n) {
  if (n === 0) {
    return '';
  }
  
  const hundreds = Math.floor(n / 100);
  const remainder = n % 100;
  
  let result = '';
  
  if (hundreds > 0) {
    result += `${convertLessThanTwenty(hundreds)} hundred`;
    if (remainder > 0) {
      result += ' ';
    }
  }
  
  if (remainder > 0) {
    result += convertLessThanHundred(remainder);
  }
  
  return result;
}

// Convert a number less than 100 to words
function convertLessThanHundred(n) {
  if (n < 20) {
    return convertLessThanTwenty(n);
  }
  
  const tens = Math.floor(n / 10);
  const ones = n % 10;
  
  if (ones === 0) {
    return convertTens(tens);
  } else {
    return `${convertTens(tens)}-${convertLessThanTwenty(ones)}`;
  }
}

// Convert a number less than 20 to words
function convertLessThanTwenty(n) {
  const words = [
    '', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine', 'ten',
    'eleven', 'twelve', 'thirteen', 'fourteen', 'fifteen', 'sixteen', 'seventeen', 'eighteen', 'nineteen'
  ];
  
  return words[n];
}

// Convert tens (20, 30, 40, etc.) to words
function convertTens(n) {
  const words = [
    '', '', 'twenty', 'thirty', 'forty', 'fifty', 'sixty', 'seventy', 'eighty', 'ninety'
  ];
  
  return words[n];
}
