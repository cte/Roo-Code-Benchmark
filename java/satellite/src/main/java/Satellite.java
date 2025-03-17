import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Satellite {
    public Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput) {
        // Validate inputs
        if (preorderInput.size() != inorderInput.size()) {
            throw new IllegalArgumentException("traversals must have the same length");
        }
        
        // Check for unique items
        Set<Character> preorderSet = new HashSet<>(preorderInput);
        Set<Character> inorderSet = new HashSet<>(inorderInput);
        
        if (preorderSet.size() != preorderInput.size() || inorderSet.size() != inorderInput.size()) {
            throw new IllegalArgumentException("traversals must contain unique items");
        }
        
        // Check for same elements
        if (!preorderSet.equals(inorderSet)) {
            throw new IllegalArgumentException("traversals must have the same elements");
        }
        
        // Handle empty tree
        if (preorderInput.isEmpty()) {
            return new Tree(null);
        }
        
        // Build the tree recursively
        Node root = buildTree(preorderInput, 0, preorderInput.size() - 1,
                             inorderInput, 0, inorderInput.size() - 1);
        
        return new Tree(root);
    }
    
    private Node buildTree(List<Character> preorder, int preStart, int preEnd,
                          List<Character> inorder, int inStart, int inEnd) {
        // Base case
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }
        
        // The first element in preorder is the root
        char rootValue = preorder.get(preStart);
        Node root = new Node(rootValue);
        
        // Find the position of the root in inorder
        int rootIndex = -1;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder.get(i) == rootValue) {
                rootIndex = i;
                break;
            }
        }
        
        // Calculate the size of the left subtree
        int leftSubtreeSize = rootIndex - inStart;
        
        // Recursively build left and right subtrees
        root.left = buildTree(preorder, preStart + 1, preStart + leftSubtreeSize,
                             inorder, inStart, rootIndex - 1);
        
        root.right = buildTree(preorder, preStart + leftSubtreeSize + 1, preEnd,
                              inorder, rootIndex + 1, inEnd);
        
        return root;
    }
}
