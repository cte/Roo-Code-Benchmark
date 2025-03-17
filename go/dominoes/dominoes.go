package dominoes

// Domino represents a domino tile with two values
type Domino [2]int

// MakeChain arranges dominoes in a valid chain where adjacent values match
// and the first and last values match, forming a loop
func MakeChain(input []Domino) ([]Domino, bool) {
	// Handle empty input
	if len(input) == 0 {
		return []Domino{}, true
	}

	// Handle singleton input
	if len(input) == 1 {
		// For a single domino, both ends must match to form a loop
		if input[0][0] == input[0][1] {
			return input, true
		}
		return nil, false
	}

	// Create a copy of the input
	dominoes := make([]Domino, len(input))
	copy(dominoes, input)

	// Try to find a valid chain
	var solution []Domino
	found := false

	// Helper function to solve the problem using backtracking
	var solve func(chain []Domino, remaining []Domino)
	solve = func(chain []Domino, remaining []Domino) {
		// If we've already found a solution, return early
		if found {
			return
		}

		// If no dominoes remain, check if the chain is valid
		if len(remaining) == 0 {
			// Check if the chain forms a loop
			if chain[0][0] == chain[len(chain)-1][1] {
				// We found a valid solution
				solution = make([]Domino, len(chain))
				copy(solution, chain)
				found = true
			}
			return
		}

		// Get the value we need to match
		needToMatch := chain[len(chain)-1][1]

		// Try each remaining domino
		for i, domino := range remaining {
			// Create a new slice of remaining dominoes without the current one
			newRemaining := make([]Domino, 0, len(remaining)-1)
			newRemaining = append(newRemaining, remaining[:i]...)
			newRemaining = append(newRemaining, remaining[i+1:]...)

			// Try the domino as-is
			if domino[0] == needToMatch {
				newChain := make([]Domino, len(chain)+1)
				copy(newChain, chain)
				newChain[len(chain)] = domino
				solve(newChain, newRemaining)
			}

			// Try the domino flipped
			if domino[1] == needToMatch {
				newChain := make([]Domino, len(chain)+1)
				copy(newChain, chain)
				newChain[len(chain)] = Domino{domino[1], domino[0]}
				solve(newChain, newRemaining)
			}
		}
	}

	// Try each domino as the starting point
	for i, domino := range dominoes {
		// Create a new slice of remaining dominoes without the current one
		remaining := make([]Domino, 0, len(dominoes)-1)
		remaining = append(remaining, dominoes[:i]...)
		remaining = append(remaining, dominoes[i+1:]...)

		// Try the domino as-is
		solve([]Domino{domino}, remaining)
		if found {
			return solution, true
		}

		// Try the domino flipped
		solve([]Domino{Domino{domino[1], domino[0]}}, remaining)
		if found {
			return solution, true
		}
	}

	return nil, false
}
