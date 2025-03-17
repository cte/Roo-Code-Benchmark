package alphametics

import (
	"errors"
	"fmt"
	"strings"
)

// Solve takes an alphametics puzzle and returns a map of letters to digits
// that satisfies the equation, or an error if no solution exists.
func Solve(puzzle string) (map[string]int, error) {
	// Parse the puzzle into terms and result
	terms, result, err := parsePuzzle(puzzle)
	if err != nil {
		return nil, err
	}

	// Get all unique letters in the puzzle
	letters := getUniqueLetters(terms, result)

	// Get the first letter of each term and the result
	firstLetters := getFirstLetters(terms, result)

	// Try all possible digit assignments
	solution, found := findSolution(letters, firstLetters, terms, result)
	if !found {
		return nil, errors.New("no solution found")
	}

	return solution, nil
}

// parsePuzzle parses the puzzle string into terms and result
func parsePuzzle(puzzle string) ([]string, string, error) {
	// Remove spaces and split by ==
	parts := strings.Split(strings.ReplaceAll(puzzle, " ", ""), "==")
	if len(parts) != 2 {
		return nil, "", fmt.Errorf("invalid puzzle format: %s", puzzle)
	}

	// Split the left side by +
	terms := strings.Split(parts[0], "+")
	result := parts[1]

	return terms, result, nil
}

// getUniqueLetters returns all unique letters in the puzzle
func getUniqueLetters(terms []string, result string) []rune {
	letterMap := make(map[rune]bool)

	// Add letters from terms
	for _, term := range terms {
		for _, char := range term {
			letterMap[char] = true
		}
	}

	// Add letters from result
	for _, char := range result {
		letterMap[char] = true
	}

	// Convert map to slice
	letters := make([]rune, 0, len(letterMap))
	for letter := range letterMap {
		letters = append(letters, letter)
	}

	return letters
}

// getFirstLetters returns the first letter of each term and the result
func getFirstLetters(terms []string, result string) map[rune]bool {
	firstLetters := make(map[rune]bool)

	// Add first letter of each term
	for _, term := range terms {
		if len(term) > 0 {
			firstLetters[rune(term[0])] = true
		}
	}

	// Add first letter of result
	if len(result) > 0 {
		firstLetters[rune(result[0])] = true
	}

	return firstLetters
}

// evaluateExpression calculates the value of a term given a letter-to-digit mapping
func evaluateExpression(expr string, mapping map[rune]int) (int, bool) {
	if len(expr) == 0 {
		return 0, true
	}

	// Check if the first digit is 0 (invalid for multi-digit numbers)
	if mapping[rune(expr[0])] == 0 && len(expr) > 1 {
		return 0, false
	}

	// Calculate the value
	value := 0
	for _, char := range expr {
		value = value*10 + mapping[char]
	}

	return value, true
}

// findSolution tries all possible digit assignments to find a solution
func findSolution(letters []rune, firstLetters map[rune]bool, terms []string, result string) (map[string]int, bool) {
	// If there are more than 10 letters, no solution is possible
	if len(letters) > 10 {
		return nil, false
	}

	// Create a map to store the current assignment
	assignment := make(map[rune]int)
	used := make([]bool, 10) // Track which digits are already used

	// Try all possible assignments recursively
	if backtrack(letters, 0, firstLetters, assignment, used, terms, result) {
		// Convert rune map to string map for the result
		solution := make(map[string]int)
		for letter, digit := range assignment {
			solution[string(letter)] = digit
		}
		return solution, true
	}

	return nil, false
}

// backtrack recursively tries all possible digit assignments
func backtrack(letters []rune, index int, firstLetters map[rune]bool, assignment map[rune]int, used []bool, terms []string, result string) bool {
	// If all letters have been assigned, check if the equation is satisfied
	if index == len(letters) {
		return checkEquation(terms, result, assignment)
	}

	letter := letters[index]
	isFirstLetter := firstLetters[letter]

	// Try each possible digit for this letter
	start := 0
	if isFirstLetter {
		start = 1 // First letters can't be 0
	}

	for digit := start; digit <= 9; digit++ {
		// Skip if this digit is already used
		if used[digit] {
			continue
		}

		// Assign this digit to the letter
		assignment[letter] = digit
		used[digit] = true

		// Recursively try to assign the next letter
		if backtrack(letters, index+1, firstLetters, assignment, used, terms, result) {
			return true
		}

		// Backtrack
		delete(assignment, letter)
		used[digit] = false
	}

	return false
}

// checkEquation checks if the current assignment satisfies the equation
func checkEquation(terms []string, result string, assignment map[rune]int) bool {
	sum := 0

	// Calculate the sum of all terms
	for _, term := range terms {
		value, valid := evaluateExpression(term, assignment)
		if !valid {
			return false
		}
		sum += value
	}

	// Calculate the value of the result
	resultValue, valid := evaluateExpression(result, assignment)
	if !valid {
		return false
	}

	// Check if the equation is satisfied
	return sum == resultValue
}
