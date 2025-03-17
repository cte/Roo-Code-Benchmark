import java.util.*;

class Connect {
    private char[][] board;
    private int rows;
    private int cols;

    public Connect(String[] boardInput) {
        // Parse the board representation
        rows = boardInput.length;
        
        // Find the maximum length of a row after removing spaces
        cols = 0;
        for (String row : boardInput) {
            // Count non-space characters
            int nonSpaceCount = 0;
            for (char c : row.toCharArray()) {
                if (c != ' ') {
                    nonSpaceCount++;
                }
            }
            cols = Math.max(cols, nonSpaceCount);
        }
        
        // Initialize the board
        board = new char[rows][cols];
        
        // Fill the board with the input
        for (int i = 0; i < rows; i++) {
            String row = boardInput[i];
            int col = 0;
            
            for (int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if (c != ' ') {
                    board[i][col] = c;
                    col++;
                }
            }
        }
    }

    public Winner computeWinner() {
        // Check if player X has won (connected left to right)
        if (hasPlayerXWon()) {
            return Winner.PLAYER_X;
        }
        
        // Check if player O has won (connected top to bottom)
        if (hasPlayerOWon()) {
            return Winner.PLAYER_O;
        }
        
        // No winner
        return Winner.NONE;
    }
    
    private boolean hasPlayerXWon() {
        // Check if player X has connected left to right
        boolean[][] visited = new boolean[rows][cols];
        
        // Start from each cell in the leftmost column
        for (int i = 0; i < rows; i++) {
            if (board[i][0] == 'X') {
                if (dfsForX(i, 0, visited)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean hasPlayerOWon() {
        // Check if player O has connected top to bottom
        boolean[][] visited = new boolean[rows][cols];
        
        // Start from each cell in the top row
        for (int j = 0; j < cols; j++) {
            if (board[0][j] == 'O') {
                if (dfsForO(0, j, visited)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean dfsForX(int row, int col, boolean[][] visited) {
        // If we've already visited this cell, return false
        if (visited[row][col]) {
            return false;
        }
        
        // Mark current cell as visited
        visited[row][col] = true;
        
        // If we reached the rightmost column, player X has won
        if (col == cols - 1) {
            return true;
        }
        
        // Check all adjacent cells
        for (int[] neighbor : getNeighbors(row, col)) {
            int newRow = neighbor[0];
            int newCol = neighbor[1];
            
            if (isValidPosition(newRow, newCol) && board[newRow][newCol] == 'X') {
                if (dfsForX(newRow, newCol, visited)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean dfsForO(int row, int col, boolean[][] visited) {
        // If we've already visited this cell, return false
        if (visited[row][col]) {
            return false;
        }
        
        // Mark current cell as visited
        visited[row][col] = true;
        
        // If we reached the bottom row, player O has won
        if (row == rows - 1) {
            return true;
        }
        
        // Check all adjacent cells
        for (int[] neighbor : getNeighbors(row, col)) {
            int newRow = neighbor[0];
            int newCol = neighbor[1];
            
            if (isValidPosition(newRow, newCol) && board[newRow][newCol] == 'O') {
                if (dfsForO(newRow, newCol, visited)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private List<int[]> getNeighbors(int row, int col) {
        List<int[]> neighbors = new ArrayList<>();
        
        // In a hexagonal grid, each cell has 6 neighbors
        // The directions depend on the position in the grid
        
        // Horizontal neighbors (always present)
        if (col > 0) neighbors.add(new int[]{row, col - 1}); // Left
        if (col < cols - 1) neighbors.add(new int[]{row, col + 1}); // Right
        
        // Upper neighbors
        if (row > 0) {
            neighbors.add(new int[]{row - 1, col}); // Upper
            if (col < cols - 1) neighbors.add(new int[]{row - 1, col + 1}); // Upper-right
        }
        
        // Lower neighbors
        if (row < rows - 1) {
            neighbors.add(new int[]{row + 1, col}); // Lower
            if (col > 0) neighbors.add(new int[]{row + 1, col - 1}); // Lower-left
        }
        
        return neighbors;
    }
    
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}
