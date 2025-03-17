//
// This is only a SKELETON file for the 'Pig Latin' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const translate = (text) => {
  // Split the text into words if it's a phrase
  const words = text.split(' ');
  
  // Translate each word and join them back together
  return words.map(translateWord).join(' ');
};

function translateWord(word) {
  // Rule 1: If a word begins with a vowel, or starts with "xr" or "yt", add "ay" to the end
  if (
    isVowel(word[0]) || 
    word.startsWith('xr') || 
    word.startsWith('yt')
  ) {
    return word + 'ay';
  }
  
  // Rule 3: If a word starts with zero or more consonants followed by "qu"
  const quIndex = word.indexOf('qu');
  if (quIndex >= 0 && allConsonantsBefore(word, quIndex)) {
    return word.slice(quIndex + 2) + word.slice(0, quIndex + 2) + 'ay';
  }
  
  // Rule 4: If a word starts with one or more consonants followed by "y"
  if (word.includes('y') && !isVowel(word[0])) {
    for (let i = 1; i < word.length; i++) {
      if (word[i] === 'y' && allConsonantsBefore(word, i)) {
        return word.slice(i) + word.slice(0, i) + 'ay';
      }
    }
  }
  
  // Rule 2: If a word begins with one or more consonants
  for (let i = 0; i < word.length; i++) {
    if (isVowel(word[i])) {
      return word.slice(i) + word.slice(0, i) + 'ay';
    }
  }
  
  // Default case (should not reach here with valid input)
  return word + 'ay';
}

function isVowel(char) {
  return ['a', 'e', 'i', 'o', 'u'].includes(char.toLowerCase());
}

function allConsonantsBefore(word, index) {
  for (let i = 0; i < index; i++) {
    if (isVowel(word[i])) {
      return false;
    }
  }
  return true;
}
