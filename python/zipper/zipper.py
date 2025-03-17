class Zipper:
    @staticmethod
    def from_tree(tree):
        """Create a zipper from a tree, with focus on the root node."""
        return Zipper(tree, [])

    def __init__(self, focus, path):
        """
        Initialize a zipper with a focus node and a path back to the root.
        
        Args:
            focus: The current focus node (a tree dictionary or None)
            path: A list of (parent, direction, other_child) tuples representing the path back to the root
                  where direction is 'left' or 'right' indicating which child the focus is
                  and other_child is the sibling of the focus
        """
        self.focus = focus
        self.path = path

    def value(self):
        """Get the value of the current focus node."""
        if self.focus is None:
            return None
        return self.focus["value"]

    def set_value(self, new_value):
        """Set the value of the current focus node, returning a new zipper."""
        if self.focus is None:
            return None
        
        # Create a new focus node with the updated value
        new_focus = {
            "value": new_value,
            "left": self.focus["left"],
            "right": self.focus["right"]
        }
        
        # Return a new zipper with the updated focus
        return Zipper(new_focus, self.path)

    def left(self):
        """Move focus to the left child, returning a new zipper."""
        if self.focus is None or self.focus["left"] is None:
            return None
        
        # The new focus is the left child
        new_focus = self.focus["left"]
        
        # Add the current focus to the path
        new_path = self.path + [(self.focus, "left", self.focus["right"])]
        
        # Return a new zipper with the updated focus and path
        return Zipper(new_focus, new_path)

    def set_left(self, new_left):
        """Set the left child of the current focus node, returning a new zipper."""
        if self.focus is None:
            return None
        
        # Create a new focus node with the updated left child
        new_focus = {
            "value": self.focus["value"],
            "left": new_left,
            "right": self.focus["right"]
        }
        
        # Return a new zipper with the updated focus
        return Zipper(new_focus, self.path)

    def right(self):
        """Move focus to the right child, returning a new zipper."""
        if self.focus is None or self.focus["right"] is None:
            return None
        
        # The new focus is the right child
        new_focus = self.focus["right"]
        
        # Add the current focus to the path
        new_path = self.path + [(self.focus, "right", self.focus["left"])]
        
        # Return a new zipper with the updated focus and path
        return Zipper(new_focus, new_path)

    def set_right(self, new_right):
        """Set the right child of the current focus node, returning a new zipper."""
        if self.focus is None:
            return None
        
        # Create a new focus node with the updated right child
        new_focus = {
            "value": self.focus["value"],
            "left": self.focus["left"],
            "right": new_right
        }
        
        # Return a new zipper with the updated focus
        return Zipper(new_focus, self.path)

    def up(self):
        """Move focus to the parent node, returning a new zipper."""
        if not self.path:
            # We're at the root, can't go up
            return None
        
        # Get the parent, direction, and other child from the path
        parent, direction, other_child = self.path[-1]
        
        # Create a new parent with the updated child
        new_parent = {
            "value": parent["value"],
            "left": self.focus if direction == "left" else other_child,
            "right": self.focus if direction == "right" else other_child
        }
        
        # Return a new zipper with the parent as focus and the rest of the path
        return Zipper(new_parent, self.path[:-1])

    def to_tree(self):
        """Convert the zipper back to a tree."""
        if not self.path:
            # We're at the root
            return self.focus
        
        # Navigate up to the root and return the tree
        return self.up().to_tree()
