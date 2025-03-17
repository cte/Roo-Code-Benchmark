//
// This is only a SKELETON file for the 'Go Counting' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GoCounting {
  constructor(board) {
    this.board = board;
    this.height = board.length;
    this.width = board[0].length;
  }

  getTerritory(x, y) {
    // Check if coordinates are valid
    if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
      return { error: 'Invalid coordinate' };
    }

    // Check if the position is a stone
    if (this.board[y][x] !== ' ') {
      return { owner: 'NONE', territory: [] };
    }

    // Find all connected empty intersections using flood fill
    const territory = [];
    const visited = new Set();
    const queue = [[x, y]];
    
    // Track neighboring stones to determine territory owner
    let blackNeighbors = false;
    let whiteNeighbors = false;

    while (queue.length > 0) {
      const [currentX, currentY] = queue.shift();
      const key = `${currentX},${currentY}`;
      
      if (visited.has(key)) continue;
      visited.add(key);

      // If it's an empty intersection, add to territory and queue its neighbors
      if (this.board[currentY][currentX] === ' ') {
        territory.push([currentX, currentY]);
        
        // Check all four directions (up, right, down, left)
        const directions = [
          [0, -1], [1, 0], [0, 1], [-1, 0]
        ];
        
        for (const [dx, dy] of directions) {
          const newX = currentX + dx;
          const newY = currentY + dy;
          
          // Skip if out of bounds
          if (newX < 0 || newX >= this.width || newY < 0 || newY >= this.height) {
            continue;
          }
          
          const cell = this.board[newY][newX];
          
          if (cell === ' ') {
            // Add empty intersection to queue
            queue.push([newX, newY]);
          } else if (cell === 'B') {
            // Found a black stone
            blackNeighbors = true;
          } else if (cell === 'W') {
            // Found a white stone
            whiteNeighbors = true;
          }
        }
      }
    }

    // Determine the owner of the territory
    let owner = 'NONE';
    if (blackNeighbors && !whiteNeighbors) {
      owner = 'BLACK';
    } else if (whiteNeighbors && !blackNeighbors) {
      owner = 'WHITE';
    }

    // Sort territory coordinates for consistent results
    territory.sort((a, b) => {
      if (a[0] !== b[0]) return a[0] - b[0];
      return a[1] - b[1];
    });

    return { owner, territory };
  }

  getTerritories() {
    const territoryBlack = [];
    const territoryWhite = [];
    const territoryNone = [];
    const visited = new Set();

    // Scan the entire board
    for (let y = 0; y < this.height; y++) {
      for (let x = 0; x < this.width; x++) {
        const key = `${x},${y}`;
        
        // Skip if already visited or not an empty intersection
        if (visited.has(key) || this.board[y][x] !== ' ') {
          continue;
        }

        // Get territory information for this position
        const { owner, territory } = this.getTerritory(x, y);
        
        // Add all coordinates in this territory to the visited set
        for (const [tx, ty] of territory) {
          visited.add(`${tx},${ty}`);
        }

        // Add territory to the appropriate category
        if (owner === 'BLACK') {
          territoryBlack.push(...territory);
        } else if (owner === 'WHITE') {
          territoryWhite.push(...territory);
        } else {
          territoryNone.push(...territory);
        }
      }
    }

    // Sort all territory coordinates for consistent results
    const sortCoordinates = (coords) => {
      return coords.sort((a, b) => {
        if (a[0] !== b[0]) return a[0] - b[0];
        return a[1] - b[1];
      });
    };

    return {
      territoryBlack: sortCoordinates(territoryBlack),
      territoryWhite: sortCoordinates(territoryWhite),
      territoryNone: sortCoordinates(territoryNone),
    };
  }
}
