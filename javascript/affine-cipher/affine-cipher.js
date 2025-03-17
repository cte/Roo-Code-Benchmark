// Function to check if two numbers are coprime (gcd = 1)
const areCoprime = (a, m) => {
  const gcd = (x, y) => (y === 0 ? x : gcd(y, x % y));
  return gcd(a, m) === 1;
};

// Function to find the modular multiplicative inverse
const findMMI = (a, m) => {
  for (let x = 1; x < m; x++) {
    if ((a * x) % m === 1) {
      return x;
    }
  }
  return 1; // This should never happen if a and m are coprime
};

export const encode = (phrase, key) => {
  const m = 26; // Length of the alphabet
  const { a, b } = key;
  
  // Check if a and m are coprime
  if (!areCoprime(a, m)) {
    throw new Error('a and m must be coprime.');
  }
  
  // Normalize the input: lowercase, remove spaces and punctuation
  const normalized = phrase.toLowerCase().replace(/[^a-z0-9]/g, '');
  
  // Encrypt each character
  let encrypted = '';
  for (let i = 0; i < normalized.length; i++) {
    const char = normalized[i];
    
    // Keep digits as they are
    if (/[0-9]/.test(char)) {
      encrypted += char;
      continue;
    }
    
    // Apply the encryption formula: E(x) = (a*i + b) mod m
    const charIndex = char.charCodeAt(0) - 'a'.charCodeAt(0);
    const encryptedIndex = (a * charIndex + b) % m;
    encrypted += String.fromCharCode(encryptedIndex + 'a'.charCodeAt(0));
  }
  
  // Group the output in chunks of 5 characters
  const chunks = [];
  for (let i = 0; i < encrypted.length; i += 5) {
    chunks.push(encrypted.slice(i, i + 5));
  }
  
  return chunks.join(' ');
};

export const decode = (phrase, key) => {
  const m = 26; // Length of the alphabet
  const { a, b } = key;
  
  // Check if a and m are coprime
  if (!areCoprime(a, m)) {
    throw new Error('a and m must be coprime.');
  }
  
  // Find the modular multiplicative inverse of a
  const aInverse = findMMI(a, m);
  
  // Remove all spaces from the input
  const normalized = phrase.replace(/\s/g, '');
  
  // Decrypt each character
  let decrypted = '';
  for (let i = 0; i < normalized.length; i++) {
    const char = normalized[i];
    
    // Keep digits as they are
    if (/[0-9]/.test(char)) {
      decrypted += char;
      continue;
    }
    
    // Apply the decryption formula: D(y) = (a^-1)(y - b) mod m
    const charIndex = char.charCodeAt(0) - 'a'.charCodeAt(0);
    // We add m before taking the modulo to handle negative numbers
    let decryptedIndex = (aInverse * (charIndex - b % m + m)) % m;
    decrypted += String.fromCharCode(decryptedIndex + 'a'.charCodeAt(0));
  }
  
  return decrypted;
};
