package connect

// ResultOf determines the winner of a Hex game.
// Player X wins by connecting left to right.
// Player O wins by connecting top to bottom.
// Returns "X" if X wins, "O" if O wins, or "" if there's no winner.
func ResultOf(lines []string) (string, error) {
	// Create a 2D board representation
	board := make([][]rune, len(lines))
	for i, line := range lines {
		board[i] = []rune(line)
	}

	// Check if player X has a path from left to right
	if hasWinningPath(board, 'X') {
		return "X", nil
	}

	// Check if player O has a path from top to bottom
	if hasWinningPath(board, 'O') {
		return "O", nil
	}

	// No winner
	return "", nil
}

// hasWinningPath checks if the specified player has a winning path
func hasWinningPath(board [][]rune, player rune) bool {
	rows := len(board)
	if rows == 0 {
		return false
	}

	// For a 1x1 board
	if rows == 1 && len(board[0]) == 1 {
		return board[0][0] == player
	}

	// Create a visited array
	visited := make([][]bool, rows)
	for i := range visited {
		visited[i] = make([]bool, len(board[i]))
	}

	// For player X, check if there's a path from left to right
	if player == 'X' {
		// Find starting points on the left edge
		for i := 0; i < rows; i++ {
			if i < len(board) && len(board[i]) > 0 && board[i][0] == 'X' {
				// Start DFS from this point
				if dfsX(board, visited, i, 0) {
					return true
				}
			}
		}
	}

	// For player O, check if there's a path from top to bottom
	if player == 'O' {
		// Reset visited array
		for i := range visited {
			for j := range visited[i] {
				visited[i][j] = false
			}
		}

		// Find starting points on the top edge
		if len(board[0]) > 0 {
			for j := 0; j < len(board[0]); j++ {
				if board[0][j] == 'O' {
					// Start DFS from this point
					if dfsO(board, visited, 0, j) {
						return true
					}
				}
			}
		}
	}

	return false
}

// dfsX performs depth-first search for player X (left to right)
func dfsX(board [][]rune, visited [][]bool, row, col int) bool {
	// Check if we've reached the right edge
	if col == len(board[row])-1 {
		return true
	}

	// Mark current cell as visited
	visited[row][col] = true

	// Define possible directions (including hexagonal adjacency)
	// In a hex grid, the adjacent cells depend on the row's parity
	directions := [][]int{
		{-1, 0}, {-1, 1}, // Up-left, Up-right
		{0, -1}, {0, 1},  // Left, Right
		{1, -1}, {1, 0},  // Down-left, Down-right
	}

	// Check all adjacent cells
	for _, dir := range directions {
		newRow, newCol := row+dir[0], col+dir[1]

		// Check if the new position is valid
		if newRow >= 0 && newRow < len(board) && 
		   newCol >= 0 && newCol < len(board[newRow]) && 
		   board[newRow][newCol] == 'X' && 
		   !visited[newRow][newCol] {
			// Continue DFS from this new position
			if dfsX(board, visited, newRow, newCol) {
				return true
			}
		}
	}

	return false
}

// dfsO performs depth-first search for player O (top to bottom)
func dfsO(board [][]rune, visited [][]bool, row, col int) bool {
	// Check if we've reached the bottom edge
	if row == len(board)-1 {
		return true
	}

	// Mark current cell as visited
	visited[row][col] = true

	// Define possible directions (including hexagonal adjacency)
	directions := [][]int{
		{-1, 0}, {-1, 1}, // Up-left, Up-right
		{0, -1}, {0, 1},  // Left, Right
		{1, -1}, {1, 0},  // Down-left, Down-right
	}

	// Check all adjacent cells
	for _, dir := range directions {
		newRow, newCol := row+dir[0], col+dir[1]

		// Check if the new position is valid
		if newRow >= 0 && newRow < len(board) && 
		   newCol >= 0 && newCol < len(board[newRow]) && 
		   board[newRow][newCol] == 'O' && 
		   !visited[newRow][newCol] {
			// Continue DFS from this new position
			if dfsO(board, visited, newRow, newCol) {
				return true
			}
		}
	}

	return false
}
