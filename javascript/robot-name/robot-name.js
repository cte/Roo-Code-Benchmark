export class Robot {
  static usedNames = new Set();

  constructor() {
    this._name = this.generateUniqueName();
  }

  get name() {
    return this._name;
  }

  reset() {
    const oldName = this._name;
    // Generate a new unique name different from the current one
    do {
      this._name = this.generateUniqueName();
    } while (this._name === oldName);
  }

  generateUniqueName() {
    // Generate a random name that hasn't been used before
    let name;
    do {
      name = this.generateRandomName();
    } while (Robot.usedNames.has(name));

    Robot.usedNames.add(name);
    return name;
  }

  generateRandomName() {
    // Generate 2 random uppercase letters
    const letters = Array(2)
      .fill()
      .map(() => String.fromCharCode(65 + Math.floor(Math.random() * 26)))
      .join('');

    // Generate 3 random digits
    const digits = Array(3)
      .fill()
      .map(() => Math.floor(Math.random() * 10))
      .join('');

    return letters + digits;
  }

  static releaseNames() {
    Robot.usedNames.clear();
  }
}
