//
// This is only a SKELETON file for the 'Rectangles' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export function count(diagram) {
  // Handle empty input cases
  if (!diagram.length || !diagram[0].length) {
    return 0;
  }

  const height = diagram.length;
  const width = diagram[0].length;
  
  // Find all '+' characters (potential corners)
  const corners = [];
  for (let y = 0; y < height; y++) {
    for (let x = 0; x < width; x++) {
      if (diagram[y][x] === '+') {
        corners.push({ x, y });
      }
    }
  }
  
  let rectangleCount = 0;
  
  // Check each pair of corners to see if they can form a rectangle
  for (let i = 0; i < corners.length; i++) {
    const topLeft = corners[i];
    
    for (let j = 0; j < corners.length; j++) {
      // Find potential bottom right corners
      const bottomRight = corners[j];
      
      // Skip if not diagonally positioned (need x2 > x1 and y2 > y1)
      if (bottomRight.x <= topLeft.x || bottomRight.y <= topLeft.y) {
        continue;
      }
      
      // Check if the other two corners exist
      const topRight = corners.find(c => c.x === bottomRight.x && c.y === topLeft.y);
      const bottomLeft = corners.find(c => c.x === topLeft.x && c.y === bottomRight.y);
      
      if (!topRight || !bottomLeft) {
        continue;
      }
      
      // Check if all edges are valid
      if (
        isValidHorizontalEdge(diagram, topLeft.x, topRight.x, topLeft.y) &&
        isValidHorizontalEdge(diagram, bottomLeft.x, bottomRight.x, bottomLeft.y) &&
        isValidVerticalEdge(diagram, topLeft.y, bottomLeft.y, topLeft.x) &&
        isValidVerticalEdge(diagram, topRight.y, bottomRight.y, topRight.x)
      ) {
        rectangleCount++;
      }
    }
  }
  
  return rectangleCount;
}

// Check if a horizontal edge is valid (consists of '-' or '+' characters)
function isValidHorizontalEdge(diagram, x1, x2, y) {
  for (let x = x1 + 1; x < x2; x++) {
    if (diagram[y][x] !== '-' && diagram[y][x] !== '+') {
      return false;
    }
  }
  return true;
}

// Check if a vertical edge is valid (consists of '|' or '+' characters)
function isValidVerticalEdge(diagram, y1, y2, x) {
  for (let y = y1 + 1; y < y2; y++) {
    if (diagram[y][x] !== '|' && diagram[y][x] !== '+') {
      return false;
    }
  }
  return true;
}
