class WordSearch {
  constructor(grid) {
    this.grid = grid;
    this.rows = grid.length;
    this.cols = grid[0].length;
  }

  find(words) {
    const results = {};
    
    for (const word of words) {
      results[word] = this.findWord(word);
    }
    
    return results;
  }
  
  findWord(word) {
    // Check all possible directions
    const directions = [
      [0, 1],   // right
      [0, -1],  // left
      [1, 0],   // down
      [-1, 0],  // up
      [1, 1],   // down-right
      [-1, -1], // up-left
      [1, -1],  // down-left
      [-1, 1]   // up-right
    ];
    
    // Check each cell as a potential starting point
    for (let row = 0; row < this.rows; row++) {
      for (let col = 0; col < this.cols; col++) {
        // Check each direction from this cell
        for (const [dRow, dCol] of directions) {
          const result = this.checkDirection(word, row, col, dRow, dCol);
          if (result) {
            return result;
          }
        }
      }
    }
    
    return undefined;
  }
  
  checkDirection(word, startRow, startCol, dRow, dCol) {
    const wordLength = word.length;
    
    // Check if the word would go out of bounds in this direction
    const endRow = startRow + (wordLength - 1) * dRow;
    const endCol = startCol + (wordLength - 1) * dCol;
    
    if (
      endRow < 0 || 
      endRow >= this.rows || 
      endCol < 0 || 
      endCol >= this.cols
    ) {
      return null;
    }
    
    // Check if the word matches in this direction
    for (let i = 0; i < wordLength; i++) {
      const row = startRow + i * dRow;
      const col = startCol + i * dCol;
      
      if (this.grid[row][col] !== word[i]) {
        return null;
      }
    }
    
    // Word found, return the start and end positions (1-indexed)
    return {
      start: [startRow + 1, startCol + 1],
      end: [endRow + 1, endCol + 1]
    };
  }
}

export default WordSearch;
