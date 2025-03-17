package kindergarten

import (
	"errors"
	"sort"
	"strings"
)

// Garden represents a kindergarten garden with children and their assigned plants
type Garden struct {
	childToPlants map[string][]string
}

// The diagram argument starts each row with a '\n'.  This allows Go's
// raw string literals to present diagrams in source code nicely as two
// rows flush left, for example,
//
//     diagram := `
//     VVCCGG
//     VVCCGG`

func NewGarden(diagram string, children []string) (*Garden, error) {
	// Validate diagram format
	if !strings.HasPrefix(diagram, "\n") {
		return nil, errors.New("diagram must start with a newline")
	}

	// Parse diagram into rows
	rows := strings.Split(strings.TrimPrefix(diagram, "\n"), "\n")
	if len(rows) != 2 {
		return nil, errors.New("diagram must have exactly two rows")
	}

	// Validate row lengths are equal
	if len(rows[0]) != len(rows[1]) {
		return nil, errors.New("rows must have the same length")
	}

	// Validate even number of cups
	if len(rows[0])%2 != 0 {
		return nil, errors.New("number of cups must be even")
	}

	// Validate plant codes
	for _, row := range rows {
		for _, plant := range row {
			if plant != 'V' && plant != 'R' && plant != 'C' && plant != 'G' {
				return nil, errors.New("invalid plant code")
			}
		}
	}

	// Check for duplicate names
	nameSet := make(map[string]bool)
	for _, child := range children {
		if nameSet[child] {
			return nil, errors.New("duplicate child name")
		}
		nameSet[child] = true
	}

	// Create a copy of children and sort alphabetically
	sortedChildren := make([]string, len(children))
	copy(sortedChildren, children)
	sort.Strings(sortedChildren)

	// Map plant codes to full names
	plantNames := map[rune]string{
		'V': "violets",
		'R': "radishes",
		'C': "clover",
		'G': "grass",
	}

	// Assign plants to children
	childToPlants := make(map[string][]string)
	for i, child := range sortedChildren {
		// Each child gets 2 cups in each row, so 4 cups total
		startPos := i * 2
		if startPos+1 >= len(rows[0]) {
			break // Not enough cups for all children
		}

		plants := []string{
			plantNames[rune(rows[0][startPos])],
			plantNames[rune(rows[0][startPos+1])],
			plantNames[rune(rows[1][startPos])],
			plantNames[rune(rows[1][startPos+1])],
		}
		childToPlants[child] = plants
	}

	return &Garden{childToPlants: childToPlants}, nil
}

func (g *Garden) Plants(child string) ([]string, bool) {
	plants, ok := g.childToPlants[child]
	return plants, ok
}
