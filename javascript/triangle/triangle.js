//
// This is only a SKELETON file for the 'Triangle' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Triangle {
  constructor(...sides) {
    this.sides = sides;
    this.isValid = this.validateTriangle();
  }

  validateTriangle() {
    // Check if all sides are greater than 0
    if (this.sides.some(side => side <= 0)) {
      return false;
    }

    // Check triangle inequality: sum of any two sides must be >= the third side
    const [a, b, c] = this.sides;
    return (a + b >= c) && (b + c >= a) && (a + c >= b);
  }

  get isEquilateral() {
    if (!this.isValid) return false;
    
    const [a, b, c] = this.sides;
    return a === b && b === c;
  }

  get isIsosceles() {
    if (!this.isValid) return false;
    
    const [a, b, c] = this.sides;
    return a === b || b === c || a === c;
  }

  get isScalene() {
    if (!this.isValid) return false;
    
    const [a, b, c] = this.sides;
    return a !== b && b !== c && a !== c;
  }
}
