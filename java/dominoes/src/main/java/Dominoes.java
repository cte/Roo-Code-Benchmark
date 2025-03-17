import java.util.ArrayList;
import java.util.List;

class Dominoes {

    List<Domino> formChain(List<Domino> inputDominoes) throws ChainNotFoundException {
        // Handle empty input case
        if (inputDominoes.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Handle singleton case
        if (inputDominoes.size() == 1) {
            Domino domino = inputDominoes.get(0);
            if (domino.getLeft() == domino.getRight()) {
                return new ArrayList<>(inputDominoes);
            } else {
                throw new ChainNotFoundException("No domino chain found.");
            }
        }
        
        // For multiple dominoes, use backtracking to find a valid chain
        List<Domino> result = new ArrayList<>();
        boolean[] used = new boolean[inputDominoes.size()];
        
        // Try each domino as the starting point
        for (int i = 0; i < inputDominoes.size(); i++) {
            Domino startDomino = inputDominoes.get(i);
            
            // Try the domino in both orientations
            // First orientation: as is
            used[i] = true;
            result.add(startDomino);
            if (findChain(inputDominoes, used, result, startDomino.getRight(), startDomino.getLeft())) {
                return result;
            }
            
            // Second orientation: flipped
            result.clear();
            Domino flippedDomino = new Domino(startDomino.getRight(), startDomino.getLeft());
            result.add(flippedDomino);
            if (findChain(inputDominoes, used, result, flippedDomino.getRight(), flippedDomino.getLeft())) {
                return result;
            }
            
            used[i] = false;
            result.clear();
        }
        
        // If no valid chain is found
        throw new ChainNotFoundException("No domino chain found.");
    }
    
    private boolean findChain(List<Domino> dominoes, boolean[] used, List<Domino> result,
                             int currentValue, int targetValue) {
        // If all dominoes are used, check if the chain is valid
        if (result.size() == dominoes.size()) {
            return currentValue == targetValue;
        }
        
        // Try to add each unused domino to the chain
        for (int i = 0; i < dominoes.size(); i++) {
            if (used[i]) {
                continue;
            }
            
            Domino domino = dominoes.get(i);
            
            // Try the domino in both orientations
            // First orientation: as is
            if (domino.getLeft() == currentValue) {
                used[i] = true;
                result.add(domino);
                
                if (findChain(dominoes, used, result, domino.getRight(), targetValue)) {
                    return true;
                }
                
                result.remove(result.size() - 1);
                used[i] = false;
            }
            
            // Second orientation: flipped
            if (domino.getRight() == currentValue && domino.getLeft() != domino.getRight()) {
                used[i] = true;
                Domino flippedDomino = new Domino(domino.getRight(), domino.getLeft());
                result.add(flippedDomino);
                
                if (findChain(dominoes, used, result, domino.getLeft(), targetValue)) {
                    return true;
                }
                
                result.remove(result.size() - 1);
                used[i] = false;
            }
        }
        
        return false;
    }
}