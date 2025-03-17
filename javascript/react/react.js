//
// This is only a SKELETON file for the 'React' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class InputCell {
  constructor(value) {
    this.value = value;
    this.dependents = [];
  }

  setValue(value) {
    if (this.value === value) return; // No change, no need to update
    
    this.value = value;
    
    // Create a set to track compute cells that need to be notified
    const computeCells = new Set();
    
    // Update all dependent cells
    this.dependents.forEach(cell => {
      cell.update(computeCells);
    });
    
    // After all cells are updated, notify callbacks
    computeCells.forEach(cell => {
      cell.notifyCallbacks();
    });
  }
  
  addDependent(cell) {
    if (!this.dependents.includes(cell)) {
      this.dependents.push(cell);
    }
  }
}

export class ComputeCell {
  constructor(inputCells, fn) {
    this.inputCells = inputCells;
    this.fn = fn;
    this.callbacks = [];
    this.dependents = [];
    this.lastValue = null;
    
    // Register this cell as a dependent of each input cell
    inputCells.forEach(cell => cell.addDependent(this));
    
    // Calculate initial value
    this.value = this.fn(this.inputCells);
    this.lastValue = this.value; // Initialize lastValue to the current value
  }
  
  update(computeCells) {
    // Calculate the new value
    const newValue = this.fn(this.inputCells);
    
    // If the value changed, update it and add this cell to the set of cells to notify
    if (newValue !== this.value) {
      this.lastValue = this.value;
      this.value = newValue;
      computeCells.add(this);
    }
    
    // Update dependent cells
    this.dependents.forEach(cell => {
      cell.update(computeCells);
    });
  }
  
  notifyCallbacks() {
    // Only notify callbacks if the value actually changed
    if (this.value !== this.lastValue) {
      this.callbacks.forEach(callback => callback.execute(this));
    }
  }

  addCallback(cb) {
    if (!this.callbacks.includes(cb)) {
      this.callbacks.push(cb);
    }
  }

  removeCallback(cb) {
    const index = this.callbacks.indexOf(cb);
    if (index !== -1) {
      this.callbacks.splice(index, 1);
    }
  }
  
  addDependent(cell) {
    if (!this.dependents.includes(cell)) {
      this.dependents.push(cell);
    }
  }
}

export class CallbackCell {
  constructor(fn) {
    this.fn = fn;
    this.values = [];
  }
  
  execute(cell) {
    this.values.push(this.fn(cell));
  }
}
