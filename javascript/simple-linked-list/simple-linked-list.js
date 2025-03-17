//
// This is only a SKELETON file for the 'Simple Linked List' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Element {
  constructor(value) {
    this._value = value;
    this._next = null;
  }

  get value() {
    return this._value;
  }

  get next() {
    return this._next;
  }

  set next(element) {
    this._next = element;
  }
}

export class List {
  constructor(values = []) {
    this._length = 0;
    this._head = null;
    
    // Initialize from array if provided
    if (values.length > 0) {
      for (let i = 0; i < values.length; i++) {
        this.add(new Element(values[i]));
      }
    }
  }

  add(element) {
    if (this._head) {
      element.next = this._head;
    }
    this._head = element;
    this._length++;
  }

  get length() {
    return this._length;
  }

  get head() {
    return this._head;
  }

  toArray() {
    const result = [];
    let current = this._head;
    
    while (current) {
      result.push(current.value);
      current = current.next;
    }
    
    return result;
  }

  reverse() {
    const values = this.toArray();
    const newList = new List();
    
    // Create a new list with elements in the reverse order
    // Since our add method adds to the front, we need to iterate in the original order
    for (let i = 0; i < values.length; i++) {
      newList.add(new Element(values[i]));
    }
    
    return newList;
  }
}
