//
// This is only a SKELETON file for the 'Connect' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Board {
  constructor(board) {
    this.board = board;
    this.height = board.length;
    this.width = board.length > 0 ? board[0].split(' ').length : 0;
    
    // Parse the board into a 2D grid for easier manipulation
    this.grid = this.parseBoard();
  }

  parseBoard() {
    const grid = [];
    
    for (let row = 0; row < this.height; row++) {
      const rowCells = this.board[row].trim().split(' ');
      grid.push(rowCells);
    }
    
    return grid;
  }

  winner() {
    // Check if player X has won (connected left to right)
    if (this.hasPlayerWon('X')) {
      return 'X';
    }
    
    // Check if player O has won (connected top to bottom)
    if (this.hasPlayerWon('O')) {
      return 'O';
    }
    
    // No winner
    return '';
  }

  hasPlayerWon(player) {
    if (player === 'X') {
      // Player X wins by connecting left to right
      // Find all X cells on the left edge
      const startCells = [];
      for (let row = 0; row < this.height; row++) {
        if (this.grid[row][0] === 'X') {
          startCells.push([row, 0]);
        }
      }
      
      // Check if any of these cells can reach the right edge
      for (const [row, col] of startCells) {
        const visited = new Set();
        if (this.dfs(row, col, 'X', visited, (r, c) => c === this.grid[r].length - 1)) {
          return true;
        }
      }
    } else if (player === 'O') {
      // Player O wins by connecting top to bottom
      // Find all O cells on the top edge
      const startCells = [];
      if (this.grid.length > 0) {
        for (let col = 0; col < this.grid[0].length; col++) {
          if (this.grid[0][col] === 'O') {
            startCells.push([0, col]);
          }
        }
      }
      
      // Check if any of these cells can reach the bottom edge
      for (const [row, col] of startCells) {
        const visited = new Set();
        if (this.dfs(row, col, 'O', visited, (r, c) => r === this.height - 1)) {
          return true;
        }
      }
    }
    
    return false;
  }

  dfs(row, col, player, visited, isDestination) {
    // Create a unique key for the cell
    const key = `${row},${col}`;
    
    // If we've already visited this cell, return false
    if (visited.has(key)) {
      return false;
    }
    
    // Mark the cell as visited
    visited.add(key);
    
    // If this cell is a destination, return true
    if (isDestination(row, col)) {
      return true;
    }
    
    // Get all adjacent cells
    const adjacentCells = this.getAdjacentCells(row, col);
    
    // Check if any adjacent cell can reach the destination
    for (const [nextRow, nextCol] of adjacentCells) {
      // Only consider cells that contain the player's stone
      if (this.isValidCell(nextRow, nextCol) && this.grid[nextRow][nextCol] === player) {
        if (this.dfs(nextRow, nextCol, player, visited, isDestination)) {
          return true;
        }
      }
    }
    
    return false;
  }

  getAdjacentCells(row, col) {
    // In a hexagonal grid, each cell has 6 adjacent cells
    // The exact directions depend on the representation
    // Based on the test cases, it seems like the adjacent cells are:
    // (row-1, col), (row-1, col+1), (row, col-1), (row, col+1), (row+1, col-1), (row+1, col)
    return [
      [row - 1, col],     // top
      [row - 1, col + 1], // top-right
      [row, col - 1],     // left
      [row, col + 1],     // right
      [row + 1, col - 1], // bottom-left
      [row + 1, col]      // bottom
    ];
  }

  isValidCell(row, col) {
    return row >= 0 && row < this.height && col >= 0 && col < this.grid[row].length;
  }
}
