# Define constants for players
WHITE = "W"
BLACK = "B"
NONE = ""

class Board:
    """Count territories of each player in a Go game

    Args:
        board (list[str]): A two-dimensional Go board
    """

    def __init__(self, board):
        self.board = board
        self.height = len(board)
        self.width = len(board[0]) if self.height > 0 else 0
        # Cache for territories to avoid recalculating
        self._territories = None

    def territory(self, x, y):
        """Find the owner and the territories given a coordinate on
           the board

        Args:
            x (int): Column on the board
            y (int): Row on the board

        Returns:
            (str, set): A tuple, the first element being the owner
                        of that area.  One of "W", "B", "".  The
                        second being a set of coordinates, representing
                        the owner's territories.
        """
        # Check if coordinates are valid
        if not (0 <= x < self.width and 0 <= y < self.height):
            raise ValueError("Invalid coordinate")

        # If the position is not empty, it's not a territory
        if self.board[y][x] != " ":
            return NONE, set()

        # Use BFS to find all connected empty intersections
        territory = set()
        queue = [(x, y)]
        visited = {(x, y)}
        
        while queue:
            curr_x, curr_y = queue.pop(0)
            territory.add((curr_x, curr_y))
            
            # Check all four adjacent positions
            for dx, dy in [(0, 1), (1, 0), (0, -1), (-1, 0)]:
                nx, ny = curr_x + dx, curr_y + dy
                
                # Skip if out of bounds
                if not (0 <= nx < self.width and 0 <= ny < self.height):
                    continue
                
                # If empty and not visited, add to queue
                if self.board[ny][nx] == " " and (nx, ny) not in visited:
                    queue.append((nx, ny))
                    visited.add((nx, ny))
        
        # Determine the owner of the territory
        owner = self._determine_owner(territory)
        
        return owner, territory

    def _determine_owner(self, territory):
        """Determine the owner of a territory.
        
        A territory belongs to a player if all its borders are that player's stones.
        """
        borders = set()
        
        # Find all stones bordering the territory
        for x, y in territory:
            for dx, dy in [(0, 1), (1, 0), (0, -1), (-1, 0)]:
                nx, ny = x + dx, y + dy
                
                # Skip if out of bounds
                if not (0 <= nx < self.width and 0 <= ny < self.height):
                    continue
                
                # If it's a stone (not empty), add to borders
                if self.board[ny][nx] != " ":
                    borders.add(self.board[ny][nx])
        
        # If all borders are the same player, that player owns the territory
        if len(borders) == 1:
            return borders.pop()
        
        # If mixed or no borders, it's neutral
        return NONE

    def territories(self):
        """Find the owners and the territories of the whole board

        Args:
            none

        Returns:
            dict(str, set): A dictionary whose key being the owner
                        , i.e. "W", "B", "".  The value being a set
                        of coordinates owned by the owner.
        """
        # If we've already calculated territories, return the cached result
        if self._territories is not None:
            return self._territories
        
        # Initialize result dictionary
        result = {WHITE: set(), BLACK: set(), NONE: set()}
        
        # Keep track of visited positions
        visited = set()
        
        # Iterate through all positions on the board
        for y in range(self.height):
            for x in range(self.width):
                # Skip if not empty or already visited
                if self.board[y][x] != " " or (x, y) in visited:
                    continue
                
                # Find the territory and its owner
                owner, territory = self.territory(x, y)
                
                # Add to result and mark as visited
                result[owner].update(territory)
                visited.update(territory)
        
        # Cache the result
        self._territories = result
        
        return result
