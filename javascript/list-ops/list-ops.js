export class List {
  constructor(values = []) {
    this.values = [...values];
  }

  append(list) {
    const newList = new List([...this.values]);
    
    for (let i = 0; i < list.values.length; i++) {
      newList.values.push(list.values[i]);
    }
    
    return newList;
  }

  concat(listOfLists) {
    const newList = new List([...this.values]);
    
    for (let i = 0; i < listOfLists.values.length; i++) {
      const currentList = listOfLists.values[i];
      
      for (let j = 0; j < currentList.values.length; j++) {
        newList.values.push(currentList.values[j]);
      }
    }
    
    return newList;
  }

  filter(predicate) {
    const newList = new List();
    
    for (let i = 0; i < this.values.length; i++) {
      if (predicate(this.values[i])) {
        newList.values.push(this.values[i]);
      }
    }
    
    return newList;
  }

  length() {
    let count = 0;
    
    for (let i = 0; i < this.values.length; i++) {
      count++;
    }
    
    return count;
  }

  map(transform) {
    const newList = new List();
    
    for (let i = 0; i < this.values.length; i++) {
      newList.values.push(transform(this.values[i]));
    }
    
    return newList;
  }

  foldl(reducer, initial) {
    let accumulator = initial;
    
    for (let i = 0; i < this.values.length; i++) {
      accumulator = reducer(accumulator, this.values[i]);
    }
    
    return accumulator;
  }

  foldr(reducer, initial) {
    let accumulator = initial;
    
    for (let i = this.values.length - 1; i >= 0; i--) {
      accumulator = reducer(accumulator, this.values[i]);
    }
    
    return accumulator;
  }

  reverse() {
    const newList = new List();
    
    for (let i = this.values.length - 1; i >= 0; i--) {
      newList.values.push(this.values[i]);
    }
    
    return newList;
  }
}
