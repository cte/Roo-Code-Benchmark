export class House {
  static subjects = [
    'house that Jack built',
    'malt',
    'rat',
    'cat',
    'dog',
    'cow with the crumpled horn',
    'maiden all forlorn',
    'man all tattered and torn',
    'priest all shaven and shorn',
    'rooster that crowed in the morn',
    'farmer sowing his corn',
    'horse and the hound and the horn'
  ];

  static verbs = [
    'lay in',
    'ate',
    'killed',
    'worried',
    'tossed',
    'milked',
    'kissed',
    'married',
    'woke',
    'kept',
    'belonged to'
  ];

  static verse(number) {
    if (number < 1 || number > 12) {
      throw new Error('Verse number must be between 1 and 12');
    }

    // Special case for verse 1
    if (number === 1) {
      return [`This is the ${this.subjects[0]}.`];
    }

    // For other verses
    const lines = [];
    lines.push(`This is the ${this.subjects[number - 1]}`);

    for (let i = number - 2; i >= 0; i--) {
      const verb = this.verbs[i];
      const subject = this.subjects[i];
      
      if (i === 0) {
        lines.push(`that ${verb} the ${subject}.`);
      } else {
        lines.push(`that ${verb} the ${subject}`);
      }
    }

    return lines;
  }

  static verses(start, end) {
    const result = [];

    for (let i = start; i <= end; i++) {
      const verse = this.verse(i);
      result.push(...verse);
      
      // Add an empty line between verses, except after the last verse
      if (i < end) {
        result.push('');
      }
    }

    return result;
  }
}
