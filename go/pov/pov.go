package pov

type Tree struct {
	value    string
	children []*Tree
}

// New creates and returns a new Tree with the given root value and children.
func New(value string, children ...*Tree) *Tree {
	return &Tree{
		value:    value,
		children: children,
	}
}

// Value returns the value at the root of a tree.
func (tr *Tree) Value() string {
	return tr.value
}

// Children returns a slice containing the children of a tree.
// There is no need to sort the elements in the result slice,
// they can be in any order.
func (tr *Tree) Children() []*Tree {
	return tr.children
}

// String describes a tree in a compact S-expression format.
// This helps to make test outputs more readable.
// Feel free to adapt this method as you see fit.
func (tr *Tree) String() string {
	if tr == nil {
		return "nil"
	}
	result := tr.Value()
	if len(tr.Children()) == 0 {
		return result
	}
	for _, ch := range tr.Children() {
		result += " " + ch.String()
	}
	return "(" + result + ")"
}

// POV problem-specific functions

// FromPov returns the pov from the node specified in the argument.
func (tr *Tree) FromPov(from string) *Tree {
	// If the tree is nil, return nil
	if tr == nil {
		return nil
	}

	// If the current node is the target, return a copy of the tree
	if tr.Value() == from {
		return tr.clone()
	}

	// Find the path from the root to the target node
	path := tr.findPath(from)
	if path == nil {
		return nil // Target node not found
	}

	// Create a new tree with the target node as the root
	newRoot := &Tree{value: from}

	// Reparent the tree
	current := newRoot
	for i := len(path) - 2; i >= 0; i-- {
		parent := path[i]
		// Create a new node for the parent
		newParent := &Tree{value: parent.Value()}

		// Add all children of the parent except the one in the path
		for _, child := range parent.Children() {
			if i > 0 && child == path[i-1] {
				continue // Skip the child that's in the path
			}
			if child.Value() == current.Value() {
				continue // Skip the current node
			}
			newParent.children = append(newParent.children, child.clone())
		}

		// Add the parent as a child of the current node
		current.children = append(current.children, newParent)
		current = newParent
	}

	// If the target node has children, add them to the new root
	targetNode := path[len(path)-1]
	for _, child := range targetNode.Children() {
		newRoot.children = append(newRoot.children, child.clone())
	}

	return newRoot
}

// PathTo returns the shortest path between two nodes in the tree.
func (tr *Tree) PathTo(from, to string) []string {
	// First, reorient the tree from the perspective of 'from'
	fromPov := tr.FromPov(from)
	if fromPov == nil {
		return nil // 'from' node not found
	}

	// Find the path from the new root ('from') to 'to'
	path := []string{from} // Start with 'from'
	if from == to {
		return path // If 'from' and 'to' are the same, return just 'from'
	}

	// Find the path from 'from' to 'to'
	found := fromPov.findPathValues(to, &path)
	if !found {
		return nil // 'to' node not found
	}

	return path
}

// Helper function to find a path from the root to a node with the given value
func (tr *Tree) findPath(value string) []*Tree {
	if tr.Value() == value {
		return []*Tree{tr}
	}

	for _, child := range tr.Children() {
		if path := child.findPath(value); path != nil {
			return append([]*Tree{tr}, path...)
		}
	}

	return nil
}

// Helper function to find a path from the root to a node with the given value
// and store the values in the provided path slice
func (tr *Tree) findPathValues(value string, path *[]string) bool {
	if tr.Value() == value {
		return true
	}

	for _, child := range tr.Children() {
		*path = append(*path, child.Value())
		if child.findPathValues(value, path) {
			return true
		}
		*path = (*path)[:len(*path)-1] // Backtrack
	}

	return false
}

// Helper function to create a deep copy of a tree
func (tr *Tree) clone() *Tree {
	if tr == nil {
		return nil
	}

	newTree := &Tree{value: tr.Value()}
	for _, child := range tr.Children() {
		newTree.children = append(newTree.children, child.clone())
	}

	return newTree
}
