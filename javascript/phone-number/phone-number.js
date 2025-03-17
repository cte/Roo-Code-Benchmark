//
// This is only a SKELETON file for the 'Phone Number' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const clean = (phoneNumber) => {
  // Remove all non-alphanumeric characters
  const digitsOnly = phoneNumber.replace(/\D/g, '');
  
  // Check for letters in the original input (after removing valid punctuation)
  if (/[a-zA-Z]/.test(phoneNumber)) {
    throw new Error('Letters not permitted');
  }
  
  // Check for invalid punctuation (anything that's not a digit, space, parenthesis, dash, dot, or plus)
  if (/[^\d\s()\-\.+]/.test(phoneNumber)) {
    throw new Error('Punctuations not permitted');
  }
  
  // Check the length of the digits
  if (digitsOnly.length < 10) {
    throw new Error('Incorrect number of digits');
  } else if (digitsOnly.length > 11) {
    throw new Error('More than 11 digits');
  } else if (digitsOnly.length === 11 && digitsOnly[0] !== '1') {
    throw new Error('11 digits must start with 1');
  }
  
  // Get the 10-digit number (remove country code if present)
  const tenDigits = digitsOnly.length === 11 ? digitsOnly.substring(1) : digitsOnly;
  
  // Check area code (first 3 digits)
  if (tenDigits[0] === '0') {
    throw new Error('Area code cannot start with zero');
  } else if (tenDigits[0] === '1') {
    throw new Error('Area code cannot start with one');
  }
  
  // Check exchange code (next 3 digits)
  if (tenDigits[3] === '0') {
    throw new Error('Exchange code cannot start with zero');
  } else if (tenDigits[3] === '1') {
    throw new Error('Exchange code cannot start with one');
  }
  
  return tenDigits;
};
