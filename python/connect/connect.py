class ConnectGame:
    def __init__(self, board):
        # Parse the board string into a 2D grid
        lines = board.strip().split('\n')
        self.board = []
        
        for line in lines:
            # Remove leading/trailing whitespace and split by spaces
            row = [cell for cell in line.strip().split(' ') if cell]
            self.board.append(row)
        
        self.height = len(self.board)
        self.width = max(len(row) for row in self.board) if self.board else 0

    def get_winner(self):
        # Check if player O has won (connected top to bottom)
        if self._has_o_won():
            return "O"
        
        # Check if player X has won (connected left to right)
        if self._has_x_won():
            return "X"
        
        # No winner
        return ""
    
    def _has_x_won(self):
        """Check if player X has won by connecting left to right."""
        if not self.board:
            return False
        
        # Find all X stones on the left edge
        left_edge_x = []
        for i in range(self.height):
            if self.board[i] and self.board[i][0] == "X":
                left_edge_x.append((i, 0))
        
        # For each X on the left edge, check if it's connected to the right edge
        for start in left_edge_x:
            visited = set()
            if self._dfs_x(start[0], start[1], visited):
                return True
        
        return False
    
    def _has_o_won(self):
        """Check if player O has won by connecting top to bottom."""
        if not self.board:
            return False
        
        # Find all O stones on the top edge
        top_edge_o = []
        if self.board[0]:
            for j in range(len(self.board[0])):
                if self.board[0][j] == "O":
                    top_edge_o.append((0, j))
        
        # For each O on the top edge, check if it's connected to the bottom edge
        for start in top_edge_o:
            visited = set()
            if self._dfs_o(start[0], start[1], visited):
                return True
        
        return False
    
    def _is_valid_position(self, i, j):
        """Check if position (i, j) is valid on the board."""
        return (0 <= i < self.height and 
                0 <= j < len(self.board[i]))
    
    def _get_neighbors(self, i, j):
        """Get valid neighboring positions for position (i, j)."""
        # In a hexagonal grid with this representation, the neighbors are:
        # The grid is offset, so the neighbors depend on the row
        potential_neighbors = [
            (i-1, j), (i-1, j+1),  # Up-left, Up-right
            (i, j-1), (i, j+1),    # Left, Right
            (i+1, j-1), (i+1, j)   # Down-left, Down-right
        ]
        
        # Filter out invalid positions
        return [(ni, nj) for ni, nj in potential_neighbors if self._is_valid_position(ni, nj)]
    
    def _dfs_x(self, i, j, visited):
        """DFS to check if X at position (i, j) is connected to the right edge."""
        # Check if position is valid and is an X
        if not self._is_valid_position(i, j) or self.board[i][j] != "X":
            return False
        
        # Check if already visited
        if (i, j) in visited:
            return False
        
        # Mark as visited
        visited.add((i, j))
        
        # Check if reached the right edge
        if j == len(self.board[i]) - 1:
            return True
        
        # Check all valid neighbors
        for ni, nj in self._get_neighbors(i, j):
            if self._dfs_x(ni, nj, visited):
                return True
        
        return False
    
    def _dfs_o(self, i, j, visited):
        """DFS to check if O at position (i, j) is connected to the bottom edge."""
        # Check if position is valid and is an O
        if not self._is_valid_position(i, j) or self.board[i][j] != "O":
            return False
        
        # Check if already visited
        if (i, j) in visited:
            return False
        
        # Mark as visited
        visited.add((i, j))
        
        # Check if reached the bottom edge
        if i == self.height - 1:
            return True
        
        # Check all valid neighbors
        for ni, nj in self._get_neighbors(i, j):
            if self._dfs_o(ni, nj, visited):
                return True
        
        return False
