//
// This is only a SKELETON file for the 'Resistor Color Trio' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ResistorColorTrio {
  constructor(colors) {
    this.colors = colors;
    this.colorValues = {
      'black': 0,
      'brown': 1,
      'red': 2,
      'orange': 3,
      'yellow': 4,
      'green': 5,
      'blue': 6,
      'violet': 7,
      'grey': 8,
      'white': 9
    };
    
    // Validate colors
    for (const color of colors) {
      if (!(color in this.colorValues)) {
        throw new Error('invalid color');
      }
    }
  }

  get label() {
    // Get the first two digits
    const firstDigit = this.colorValues[this.colors[0]];
    const secondDigit = this.colorValues[this.colors[1]];
    
    // Calculate the main value (first two digits)
    const mainValue = firstDigit * 10 + secondDigit;
    
    // Get the number of zeros to add (from the third color)
    const zeros = this.colorValues[this.colors[2]];
    
    // Calculate the total value
    let value = mainValue * Math.pow(10, zeros);
    let unit = 'ohms';
    
    // Convert to kiloohms if value is 1000 or more
    if (value >= 1000) {
      value = value / 1000;
      unit = 'kiloohms';
    }
    
    // Return the formatted label
    return `Resistor value: ${value} ${unit}`;
  }
}
