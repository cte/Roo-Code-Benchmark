from json import dumps


class Tree:
    def __init__(self, label, children=None):
        self.label = label
        self.children = children if children is not None else []

    def __dict__(self):
        return {self.label: [c.__dict__() for c in sorted(self.children)]}

    def __str__(self, indent=None):
        return dumps(self.__dict__(), indent=indent)

    def __lt__(self, other):
        return self.label < other.label

    def __eq__(self, other):
        return self.__dict__() == other.__dict__()

    def _find_node(self, target_label, parent=None, path=None):
        """Find a node with the given label and return the node and its path from root."""
        if path is None:
            path = []
        
        if self.label == target_label:
            return self, path, parent
        
        for child in self.children:
            result = child._find_node(target_label, self, path + [self])
            if result[0] is not None:
                return result
        
        return None, None, None

    def _create_new_tree(self, target_node, parent_node, visited=None):
        """Create a new tree with the target node as root."""
        if visited is None:
            visited = set()
        
        # Create a copy of the current node
        new_tree = Tree(self.label)
        visited.add(self.label)
        
        # Add all children except the parent
        for child in self.children:
            if child.label != parent_node:
                if child.label not in visited:
                    new_child = child._create_new_tree(target_node, self.label, visited)
                    new_tree.children.append(new_child)
        
        # Add parent as a child if it's not None and not visited
        if parent_node is not None and parent_node != target_node and parent_node not in visited:
            parent = Tree(parent_node)
            new_tree.children.append(parent)
            
        return new_tree

    def from_pov(self, from_node):
        """Reorient the tree with from_node as the new root."""
        # Find the target node
        target, path, parent = self._find_node(from_node)
        
        if target is None:
            raise ValueError("Tree could not be reoriented")
        
        # If the target is already the root, return a copy of the tree
        if parent is None:
            return Tree(self.label, [Tree(child.label, child.children) for child in self.children])
        
        # Create a new tree with the target as root
        new_root = Tree(from_node)
        
        # Add target's children to the new root
        for child in target.children:
            new_root.children.append(Tree(child.label, child.children))
        
        # Build the path from target to original root
        current = parent
        path_to_root = [current]
        
        # Traverse up the path to build the new structure
        for i in range(len(path) - 1, -1, -1):
            node = path[i]
            if node.label == current.label:
                # Find the next node in the path
                if i > 0:
                    current = path[i-1]
                    path_to_root.append(current)
        
        # Build the new tree structure
        current_node = new_root
        for node in path_to_root:
            # Create a new node for the parent
            new_node = Tree(node.label)
            
            # Add all siblings of the target node to the parent
            for child in node.children:
                if child.label != current_node.label and child.label not in [n.label for n in current_node.children]:
                    new_node.children.append(Tree(child.label, child.children))
            
            # Add the parent to the current node's children
            current_node.children.append(new_node)
            current_node = new_node
        
        return new_root

    def path_to(self, from_node, to_node):
        """Find the path from from_node to to_node."""
        # Reorient the tree with from_node as root
        try:
            new_tree = self.from_pov(from_node)
        except ValueError:
            raise ValueError("Tree could not be reoriented")
        
        # Find the path from the new root to to_node
        path = []
        
        def find_path(node, target, current_path):
            if node.label == target:
                return current_path + [node.label]
            
            for child in node.children:
                result = find_path(child, target, current_path + [node.label])
                if result:
                    return result
            
            return None
        
        result = find_path(new_tree, to_node, [])
        
        if result is None:
            raise ValueError("No path found")
        
        return result
