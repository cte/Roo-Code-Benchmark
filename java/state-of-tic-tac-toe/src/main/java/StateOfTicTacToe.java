class StateOfTicTacToe {
    public GameState determineState(String[] board) {
        // Count the number of X's and O's on the board
        int countX = countChar(board, 'X');
        int countO = countChar(board, 'O');
        
        // Validate turn order: X starts, so there should be equal number of X and O,
        // or one more X than O
        if (countX < countO) {
            throw new IllegalArgumentException("Wrong turn order: O started");
        }
        if (countX > countO + 1) {
            throw new IllegalArgumentException("Wrong turn order: X went twice");
        }
        
        // Check for wins
        boolean xWins = checkWin(board, 'X');
        boolean oWins = checkWin(board, 'O');
        
        // Validate that the game stops when someone wins
        if (xWins && oWins) {
            throw new IllegalArgumentException("Impossible board: game should have ended after the game was won");
        }
        
        // If X wins, ensure X has one more move than O
        if (xWins && countX != countO + 1) {
            throw new IllegalArgumentException("Impossible board: game should have ended after the game was won");
        }
        
        // If O wins, ensure equal number of moves
        if (oWins && countX != countO) {
            throw new IllegalArgumentException("Impossible board: game should have ended after the game was won");
        }
        
        // Determine the game state
        if (xWins || oWins) {
            return GameState.WIN;
        }
        
        // If the board is full, it's a draw
        if (countX + countO == 9) {
            return GameState.DRAW;
        }
        
        // Otherwise, the game is ongoing
        return GameState.ONGOING;
    }
    
    // Count occurrences of a character in the board
    private int countChar(String[] board, char c) {
        int count = 0;
        for (String row : board) {
            for (int i = 0; i < row.length(); i++) {
                if (row.charAt(i) == c) {
                    count++;
                }
            }
        }
        return count;
    }
    
    // Check if a player has won
    private boolean checkWin(String[] board, char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i].charAt(0) == player && 
                board[i].charAt(1) == player && 
                board[i].charAt(2) == player) {
                return true;
            }
        }
        
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0].charAt(i) == player && 
                board[1].charAt(i) == player && 
                board[2].charAt(i) == player) {
                return true;
            }
        }
        
        // Check diagonals
        if (board[0].charAt(0) == player && 
            board[1].charAt(1) == player && 
            board[2].charAt(2) == player) {
            return true;
        }
        
        if (board[0].charAt(2) == player && 
            board[1].charAt(1) == player && 
            board[2].charAt(0) == player) {
            return true;
        }
        
        return false;
    }
}
