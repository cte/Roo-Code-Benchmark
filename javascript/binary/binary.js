export class Binary {
  constructor(binaryString) {
    this.binaryString = binaryString;
  }

  toDecimal() {
    // Check if the input is valid (contains only '0' and '1')
    if (!/^[01]+$/.test(this.binaryString)) {
      return null;
    }

    // Convert binary to decimal using first principles
    let decimal = 0;
    const digits = this.binaryString.split('');
    
    // Iterate through each digit from right to left
    for (let i = 0; i < digits.length; i++) {
      // Calculate position from right (0-indexed)
      const position = digits.length - 1 - i;
      
      // Add the value of the current digit (0 or 1) multiplied by 2^position
      decimal += parseInt(digits[i]) * Math.pow(2, position);
    }
    
    return decimal;
  }
}
