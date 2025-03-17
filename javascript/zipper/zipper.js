//
// This is only a SKELETON file for the 'Zipper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Zipper {
  constructor(focus, trail = []) {
    this.focus = focus;
    this.trail = trail;
  }

  static fromTree(tree) {
    return new Zipper(tree);
  }

  toTree() {
    if (this.trail.length === 0) {
      return this.focus;
    }
    
    // Navigate back to the root and return the complete tree
    return this.up().toTree();
  }

  value() {
    return this.focus.value;
  }

  left() {
    if (!this.focus.left) {
      return null;
    }
    
    // Move focus to left child, add current position to trail
    return new Zipper(this.focus.left, [
      { node: this.focus, dir: 'left' },
      ...this.trail
    ]);
  }

  right() {
    if (!this.focus.right) {
      return null;
    }
    
    // Move focus to right child, add current position to trail
    return new Zipper(this.focus.right, [
      { node: this.focus, dir: 'right' },
      ...this.trail
    ]);
  }

  up() {
    if (this.trail.length === 0) {
      return null;
    }
    
    const [{ node, dir }, ...parentTrail] = this.trail;
    
    // Create a new parent node with the current focus as the appropriate child
    const newParent = {
      value: node.value,
      left: dir === 'left' ? this.focus : node.left,
      right: dir === 'right' ? this.focus : node.right
    };
    
    return new Zipper(newParent, parentTrail);
  }

  setValue(newValue) {
    // Create a new focus node with the updated value
    return new Zipper(
      {
        value: newValue,
        left: this.focus.left,
        right: this.focus.right
      },
      this.trail
    );
  }

  setLeft(newLeft) {
    // Create a new focus node with the updated left child
    return new Zipper(
      {
        value: this.focus.value,
        left: newLeft,
        right: this.focus.right
      },
      this.trail
    );
  }

  setRight(newRight) {
    // Create a new focus node with the updated right child
    return new Zipper(
      {
        value: this.focus.value,
        left: this.focus.left,
        right: newRight
      },
      this.trail
    );
  }
}
