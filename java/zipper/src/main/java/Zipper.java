class Zipper {
    Zipper up;
    Zipper left;
    Zipper right;
    private int value;

    Zipper(int val) {
        this.value = val;
    }

    BinaryTree toTree() {
        // Find the root of the tree
        Zipper root = this;
        while (root.up != null) {
            root = root.up;
        }
        return new BinaryTree(root);
    }

    int getValue() {
        return this.value;
    }

    Zipper setLeft(Zipper leftChild) {
        this.left = leftChild;
        if (leftChild != null) {
            leftChild.up = this;
        }
        return this;
    }

    Zipper setRight(Zipper rightChild) {
        this.right = rightChild;
        if (rightChild != null) {
            rightChild.up = this;
        }
        return this;
    }

    void setValue(int val) {
        this.value = val;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Zipper other = (Zipper) obj;
        // Two zippers are equal if they point to the same node in the tree
        // This means they have the same value and same structure
        return this.value == other.value &&
               (this.left == null ? other.left == null : this.left.equals(other.left)) &&
               (this.right == null ? other.right == null : this.right.equals(other.right));
    }
}

class BinaryTree {
    private Zipper root;
    
    BinaryTree(int value) {
        this.root = new Zipper(value);
    }

    BinaryTree(Zipper root) {
        this.root = root;
    }

    Zipper getRoot() {
        return this.root;
    }

    String printTree() {
        return printNode(this.root);
    }
    
    private String printNode(Zipper node) {
        if (node == null) {
            return "null";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("value: ").append(node.getValue());
        
        // Handle left child
        sb.append(", left: ");
        if (node.left == null) {
            sb.append("null");
        } else {
            sb.append("{ ").append(printNode(node.left)).append(" }");
        }
        
        // Handle right child
        sb.append(", right: ");
        if (node.right == null) {
            sb.append("null");
        } else {
            sb.append("{ ").append(printNode(node.right)).append(" }");
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BinaryTree other = (BinaryTree) obj;
        // Two binary trees are equal if their roots are equal
        return (this.root == null ? other.root == null : this.root.equals(other.root));
    }
}
