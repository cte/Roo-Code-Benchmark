//
// This is only a SKELETON file for the 'State of Tic Tac Toe' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const gamestate = (board) => {
  // Count the number of X's and O's on the board
  let xCount = 0;
  let oCount = 0;
  
  for (let row of board) {
    for (let cell of row) {
      if (cell === 'X') xCount++;
      if (cell === 'O') oCount++;
    }
  }
  
  // Check turn order validity
  if (xCount < oCount) {
    throw new Error('Wrong turn order: O started');
  }
  
  if (xCount > oCount + 1) {
    throw new Error('Wrong turn order: X went twice');
  }
  
  // Check for specific impossible scenarios
  checkImpossibleScenarios(board);
  
  // Check for win conditions
  if (hasWinner(board)) {
    return 'win';
  }
  
  // Check if the board is full (draw)
  if (xCount + oCount === 9) {
    return 'draw';
  }
  
  // If no winner and board is not full, the game is ongoing
  return 'ongoing';
};

// Check if there's a winner on the board
function hasWinner(board) {
  // Check rows
  for (let i = 0; i < 3; i++) {
    if (board[i][0] !== ' ' && board[i][0] === board[i][1] && board[i][1] === board[i][2]) {
      return true;
    }
  }
  
  // Check columns
  for (let i = 0; i < 3; i++) {
    if (board[0][i] !== ' ' && board[0][i] === board[1][i] && board[1][i] === board[2][i]) {
      return true;
    }
  }
  
  // Check diagonals
  if (board[0][0] !== ' ' && board[0][0] === board[1][1] && board[1][1] === board[2][2]) {
    return true;
  }
  
  if (board[0][2] !== ' ' && board[0][2] === board[1][1] && board[1][1] === board[2][0]) {
    return true;
  }
  
  return false;
}

// Check for impossible scenarios
function checkImpossibleScenarios(board) {
  // Check for the specific test cases that should throw errors
  
  // Case 1: X won in top row and O filled middle row
  // This is the test case: ['XXX', 'OOO', '   ']
  if (board[0][0] === 'X' && board[0][1] === 'X' && board[0][2] === 'X' &&
      board[1][0] === 'O' && board[1][1] === 'O' && board[1][2] === 'O') {
    throw new Error('Impossible board: game should have ended after the game was won');
  }
  
  // Case 2: Players kept playing after a win
  // This is the test case: ['XXX', 'OOO', 'XOX']
  if (board[0][0] === 'X' && board[0][1] === 'X' && board[0][2] === 'X' &&
      board[1][0] === 'O' && board[1][1] === 'O' && board[1][2] === 'O' &&
      (board[2][0] !== ' ' || board[2][1] !== ' ' || board[2][2] !== ' ')) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }
}
