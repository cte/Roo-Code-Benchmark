export class Song {
  constructor() {
    this.animals = [
      'fly',
      'spider',
      'bird',
      'cat',
      'dog',
      'goat',
      'cow',
      'horse'
    ];
    
    this.secondLines = [
      'I don\'t know why she swallowed the fly. Perhaps she\'ll die.',
      'It wriggled and jiggled and tickled inside her.',
      'How absurd to swallow a bird!',
      'Imagine that, to swallow a cat!',
      'What a hog, to swallow a dog!',
      'Just opened her throat and swallowed a goat!',
      'I don\'t know how she swallowed a cow!',
      'She\'s dead, of course!'
    ];
  }

  verse(n) {
    // Adjust for 0-based indexing
    const index = n - 1;
    const animal = this.animals[index];
    
    // Special case for horse
    if (animal === 'horse') {
      return `I know an old lady who swallowed a horse.\nShe's dead, of course!\n`;
    }
    
    let verse = `I know an old lady who swallowed a ${animal}.\n`;
    verse += `${this.secondLines[index]}\n`;
    
    // Special case for fly - no chain of swallows
    if (animal === 'fly') {
      return verse;
    }
    
    // Add the chain of swallows in reverse order
    for (let i = index; i > 0; i--) {
      if (i === 2) { // Special case for bird -> spider
        verse += `She swallowed the bird to catch the spider that wriggled and jiggled and tickled inside her.\n`;
      } else {
        verse += `She swallowed the ${this.animals[i]} to catch the ${this.animals[i-1]}.\n`;
      }
    }
    
    // Add the final line for all non-horse verses
    verse += `I don't know why she swallowed the fly. Perhaps she'll die.\n`;
    
    return verse;
  }

  verses(start, end) {
    let result = '';
    
    for (let i = start; i <= end; i++) {
      result += this.verse(i);
      
      // Add a blank line between verses
      if (i < end) {
        result += '\n';
      }
    }
    
    // Add an extra newline at the end for the expected format
    result += '\n';
    
    return result;
  }
}
