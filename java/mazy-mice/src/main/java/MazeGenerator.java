import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    // Box drawing characters
    private static final char TOP_LEFT = '┌';
    private static final char TOP_RIGHT = '┐';
    private static final char BOTTOM_LEFT = '└';
    private static final char BOTTOM_RIGHT = '┘';
    private static final char HORIZONTAL = '─';
    private static final char VERTICAL = '│';
    private static final char TOP_JUNCTION = '┬';
    private static final char BOTTOM_JUNCTION = '┴';
    private static final char LEFT_JUNCTION = '├';
    private static final char RIGHT_JUNCTION = '┤';
    private static final char CROSS_JUNCTION = '┼';
    private static final char ARROW = '⇨';
    private static final char EMPTY = ' ';

    // Directions for maze generation
    private enum Direction {
        NORTH(0, -1),
        EAST(1, 0),
        SOUTH(0, 1),
        WEST(-1, 0);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int dx() {
            return dx;
        }

        public int dy() {
            return dy;
        }
    }

    public char[][] generatePerfectMaze(int rows, int columns) {
        validateDimensions(rows, columns);
        return generateMaze(rows, columns, new Random().nextInt());
    }

    public char[][] generatePerfectMaze(int rows, int columns, int seed) {
        validateDimensions(rows, columns);
        return generateMaze(rows, columns, seed);
    }

    private void validateDimensions(int rows, int columns) {
        if (rows < 5 || rows > 100) {
            throw new IllegalArgumentException("Rows must be between 5 and 100");
        }
        if (columns < 5 || columns > 100) {
            throw new IllegalArgumentException("Columns must be between 5 and 100");
        }
    }

    private char[][] generateMaze(int rows, int columns, int seed) {
        Random random = new Random(seed);
        
        // Create a grid where each cell is a wall (false) or a passage (true)
        // The grid size is (2*rows+1) x (2*columns+1) to account for walls
        char[][] maze = new char[2 * rows + 1][2 * columns + 1];
        
        // Initialize the maze with all walls
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                maze[y][x] = (y % 2 == 0 || x % 2 == 0) ? VERTICAL : EMPTY;
            }
        }
        
        // Generate the maze using recursive backtracker algorithm
        generateMazeRecursive(maze, random);
        
        // Fix the walls and junctions
        fixWallsAndJunctions(maze);
        
        // Add entrance and exit
        addEntranceAndExit(maze, random);
        
        return maze;
    }

    private void generateMazeRecursive(char[][] maze, Random random) {
        int height = maze.length;
        int width = maze[0].length;
        
        // Create a visited grid to track which cells have been visited
        boolean[][] visited = new boolean[height][width];
        
        // Start at a random cell (must be at odd coordinates to be a cell, not a wall)
        int startY = 1 + 2 * random.nextInt((height - 1) / 2);
        int startX = 1 + 2 * random.nextInt((width - 1) / 2);
        
        // Mark the starting cell as visited
        visited[startY][startX] = true;
        
        // Stack for backtracking
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});
        
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int x = current[0];
            int y = current[1];
            
            // Get unvisited neighbors
            List<Direction> unvisitedNeighbors = new ArrayList<>();
            
            for (Direction dir : Direction.values()) {
                int nx = x + 2 * dir.dx();
                int ny = y + 2 * dir.dy();
                
                // Check if the neighbor is within bounds and unvisited
                if (nx > 0 && nx < width && ny > 0 && ny < height && !visited[ny][nx]) {
                    unvisitedNeighbors.add(dir);
                }
            }
            
            if (!unvisitedNeighbors.isEmpty()) {
                // Choose a random unvisited neighbor
                Direction dir = unvisitedNeighbors.get(random.nextInt(unvisitedNeighbors.size()));
                
                // Remove the wall between the current cell and the chosen neighbor
                int wallX = x + dir.dx();
                int wallY = y + dir.dy();
                maze[wallY][wallX] = EMPTY;
                
                // Mark the neighbor as visited
                int nx = x + 2 * dir.dx();
                int ny = y + 2 * dir.dy();
                visited[ny][nx] = true;
                
                // Push the neighbor onto the stack
                stack.push(new int[]{nx, ny});
            } else {
                // Backtrack
                stack.pop();
            }
        }
    }

    private void fixWallsAndJunctions(char[][] maze) {
        int height = maze.length;
        int width = maze[0].length;
        
        // Fix horizontal walls
        for (int y = 0; y < height; y += 2) {
            for (int x = 0; x < width; x++) {
                if (maze[y][x] == VERTICAL) {
                    maze[y][x] = HORIZONTAL;
                }
            }
        }
        
        // Fix corners and junctions
        for (int y = 0; y < height; y += 2) {
            for (int x = 0; x < width; x += 2) {
                if (y == 0 && x == 0) {
                    maze[y][x] = TOP_LEFT;
                } else if (y == 0 && x == width - 1) {
                    maze[y][x] = TOP_RIGHT;
                } else if (y == height - 1 && x == 0) {
                    maze[y][x] = BOTTOM_LEFT;
                } else if (y == height - 1 && x == width - 1) {
                    maze[y][x] = BOTTOM_RIGHT;
                } else if (y == 0) {
                    maze[y][x] = TOP_JUNCTION;
                } else if (y == height - 1) {
                    maze[y][x] = BOTTOM_JUNCTION;
                } else if (x == 0) {
                    maze[y][x] = LEFT_JUNCTION;
                } else if (x == width - 1) {
                    maze[y][x] = RIGHT_JUNCTION;
                } else {
                    // Determine the appropriate junction character based on surrounding walls
                    boolean north = y > 0 && maze[y - 1][x] != EMPTY;
                    boolean east = x < width - 1 && maze[y][x + 1] != EMPTY;
                    boolean south = y < height - 1 && maze[y + 1][x] != EMPTY;
                    boolean west = x > 0 && maze[y][x - 1] != EMPTY;
                    
                    if (north && east && south && west) {
                        maze[y][x] = CROSS_JUNCTION;
                    } else if (north && east && south) {
                        maze[y][x] = LEFT_JUNCTION;
                    } else if (north && east && west) {
                        maze[y][x] = BOTTOM_JUNCTION;
                    } else if (north && south && west) {
                        maze[y][x] = RIGHT_JUNCTION;
                    } else if (east && south && west) {
                        maze[y][x] = TOP_JUNCTION;
                    } else if (north && south) {
                        maze[y][x] = VERTICAL;
                    } else if (east && west) {
                        maze[y][x] = HORIZONTAL;
                    } else if (north && east) {
                        maze[y][x] = BOTTOM_LEFT;
                    } else if (north && west) {
                        maze[y][x] = BOTTOM_RIGHT;
                    } else if (south && east) {
                        maze[y][x] = TOP_LEFT;
                    } else if (south && west) {
                        maze[y][x] = TOP_RIGHT;
                    } else if (north) {
                        maze[y][x] = VERTICAL;
                    } else if (south) {
                        maze[y][x] = VERTICAL;
                    } else if (east) {
                        maze[y][x] = HORIZONTAL;
                    } else if (west) {
                        maze[y][x] = HORIZONTAL;
                    }
                }
            }
        }
    }

    private void addEntranceAndExit(char[][] maze, Random random) {
        int height = maze.length;
        int width = maze[0].length;
        
        // Find valid entrance positions (left side)
        List<Integer> validEntranceRows = new ArrayList<>();
        for (int y = 1; y < height - 1; y += 2) {
            if (maze[y][1] == EMPTY) {
                validEntranceRows.add(y);
            }
        }
        
        // Find valid exit positions (right side)
        List<Integer> validExitRows = new ArrayList<>();
        for (int y = 1; y < height - 1; y += 2) {
            if (maze[y][width - 2] == EMPTY) {
                validExitRows.add(y);
            }
        }
        
        // Choose random entrance and exit
        int entranceRow = validEntranceRows.get(random.nextInt(validEntranceRows.size()));
        int exitRow = validExitRows.get(random.nextInt(validExitRows.size()));
        
        // Place entrance and exit
        maze[entranceRow][0] = ARROW;
        maze[exitRow][width - 1] = ARROW;
    }
}
