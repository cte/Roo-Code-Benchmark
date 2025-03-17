package wordsearch

import (
	"errors"
	"fmt"
)

// Direction represents a direction to search for words in the puzzle
type Direction struct {
	dx, dy int
}

// Solve finds the given words in the puzzle and returns their coordinates
func Solve(words []string, puzzle []string) (map[string][2][2]int, error) {
	result := make(map[string][2][2]int)
	notFound := false

	// Initialize all words with not found coordinates
	for _, word := range words {
		result[word] = [2][2]int{{-1, -1}, {-1, -1}}
	}

	// Define all 8 possible directions to search
	directions := []Direction{
		{1, 0},   // right
		{-1, 0},  // left
		{0, 1},   // down
		{0, -1},  // up
		{1, 1},   // down-right
		{-1, -1}, // up-left
		{-1, 1},  // up-right
		{1, -1},  // down-left
	}

	// Get puzzle dimensions
	if len(puzzle) == 0 {
		return result, errors.New("empty puzzle")
	}
	height := len(puzzle)
	width := len(puzzle[0])

	// Search for each word
	for _, word := range words {
		found := false

		// Try each starting position
		for y := 0; y < height; y++ {
			for x := 0; x < width; x++ {
				// Try each direction
				for _, dir := range directions {
					// Check if the word fits in this direction from this position
					if x+(len(word)-1)*dir.dx < 0 || x+(len(word)-1)*dir.dx >= width ||
						y+(len(word)-1)*dir.dy < 0 || y+(len(word)-1)*dir.dy >= height {
						continue
					}

					// Check if the word matches in this direction
					match := true
					for i := 0; i < len(word); i++ {
						cx := x + i*dir.dx
						cy := y + i*dir.dy
						if puzzle[cy][cx] != word[i] {
							match = false
							break
						}
					}

					if match {
						// Word found, store coordinates
						startX, startY := x, y
						endX, endY := x+(len(word)-1)*dir.dx, y+(len(word)-1)*dir.dy
						result[word] = [2][2]int{{startX, startY}, {endX, endY}}
						found = true
						break
					}
				}
				if found {
					break
				}
			}
			if found {
				break
			}
		}

		if !found {
			notFound = true
		}
	}

	if notFound {
		return result, fmt.Errorf("some words were not found in the puzzle")
	}

	return result, nil
}
