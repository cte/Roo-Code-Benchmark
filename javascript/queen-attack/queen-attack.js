//
// This is only a SKELETON file for the 'Queen Attack' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class QueenAttack {
  constructor({
    black: [blackRow, blackColumn] = [0, 3],
    white: [whiteRow, whiteColumn] = [7, 3],
  } = {}) {
    // Validate that queens are on the board (0-7 for both row and column)
    if (
      blackRow < 0 || blackRow > 7 || blackColumn < 0 || blackColumn > 7 ||
      whiteRow < 0 || whiteRow > 7 || whiteColumn < 0 || whiteColumn > 7
    ) {
      throw new Error('Queen must be placed on the board');
    }

    // Validate that queens don't occupy the same space
    if (blackRow === whiteRow && blackColumn === whiteColumn) {
      throw new Error('Queens cannot share the same space');
    }

    this.black = [blackRow, blackColumn];
    this.white = [whiteRow, whiteColumn];
  }

  toString() {
    // Create an 8x8 board with all empty spaces
    const board = Array(8).fill().map(() => Array(8).fill('_'));

    // Place the queens on the board
    const [whiteRow, whiteColumn] = this.white;
    const [blackRow, blackColumn] = this.black;
    
    board[whiteRow][whiteColumn] = 'W';
    board[blackRow][blackColumn] = 'B';

    // Convert the board to a string
    return board.map(row => row.join(' ')).join('\n');
  }

  get canAttack() {
    const [whiteRow, whiteColumn] = this.white;
    const [blackRow, blackColumn] = this.black;

    // Queens can attack if they are on the same row
    if (whiteRow === blackRow) {
      return true;
    }

    // Queens can attack if they are on the same column
    if (whiteColumn === blackColumn) {
      return true;
    }

    // Queens can attack if they are on the same diagonal
    // This happens when the absolute difference between rows equals the absolute difference between columns
    const rowDiff = Math.abs(whiteRow - blackRow);
    const colDiff = Math.abs(whiteColumn - blackColumn);
    
    return rowDiff === colDiff;
  }
}
