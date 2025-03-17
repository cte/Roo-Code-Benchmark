import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class WordSearcher {
    Map<String, Optional<WordLocation>> search(final Set<String> words, final char[][] grid) {
        Map<String, Optional<WordLocation>> result = new HashMap<>();
        
        // Initialize all words as not found
        for (String word : words) {
            result.put(word, Optional.empty());
        }
        
        // If grid is empty, return empty results
        if (grid.length == 0 || grid[0].length == 0) {
            return result;
        }
        
        int rows = grid.length;
        int cols = grid[0].length;
        
        // Search for each word
        for (String word : words) {
            // Try all possible starting positions
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // Try all 8 directions
                    for (int dRow = -1; dRow <= 1; dRow++) {
                        for (int dCol = -1; dCol <= 1; dCol++) {
                            // Skip the case where both dRow and dCol are 0 (no direction)
                            if (dRow == 0 && dCol == 0) {
                                continue;
                            }
                            
                            // Check if the word can be found starting at (row, col) in direction (dRow, dCol)
                            Optional<WordLocation> location = findWordInDirection(word, grid, row, col, dRow, dCol);
                            
                            // If found, update the result
                            if (location.isPresent()) {
                                result.put(word, location);
                                // Break out of the direction loop once found
                                break;
                            }
                        }
                        // Break out of the dCol loop if word is found
                        if (result.get(word).isPresent()) {
                            break;
                        }
                    }
                    // Break out of the col loop if word is found
                    if (result.get(word).isPresent()) {
                        break;
                    }
                }
                // Break out of the row loop if word is found
                if (result.get(word).isPresent()) {
                    break;
                }
            }
        }
        
        return result;
    }
    
    private Optional<WordLocation> findWordInDirection(String word, char[][] grid, int startRow, int startCol, int dRow, int dCol) {
        int rows = grid.length;
        int cols = grid[0].length;
        int wordLength = word.length();
        
        // Check if the word would go out of bounds
        int endRow = startRow + (wordLength - 1) * dRow;
        int endCol = startCol + (wordLength - 1) * dCol;
        
        if (endRow < 0 || endRow >= rows || endCol < 0 || endCol >= cols) {
            return Optional.empty();
        }
        
        // Check if the word matches in this direction
        for (int i = 0; i < wordLength; i++) {
            int currentRow = startRow + i * dRow;
            int currentCol = startCol + i * dCol;
            
            if (grid[currentRow][currentCol] != word.charAt(i)) {
                return Optional.empty();
            }
        }
        
        // Word found, return the location
        // Note: The coordinates in the Pair are (x, y) where x is column and y is row
        // Also, the coordinates are 1-indexed according to the test cases
        return Optional.of(new WordLocation(
            new Pair(startCol + 1, startRow + 1),
            new Pair(endCol + 1, endRow + 1)
        ));
    }
}
