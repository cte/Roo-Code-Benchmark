package wordy

import (
	"strconv"
	"strings"
)

func Answer(question string) (int, bool) {
	// Check if the question starts with "What is" and ends with "?"
	if !strings.HasPrefix(question, "What is") || !strings.HasSuffix(question, "?") {
		return 0, false
	}

	// Remove "What is" from the beginning and "?" from the end
	question = strings.TrimPrefix(question, "What is")
	question = strings.TrimSuffix(question, "?")
	question = strings.TrimSpace(question)

	// If there's nothing left, it's an invalid question
	if question == "" {
		return 0, false
	}

	// Split the question into tokens
	tokens := tokenize(question)
	if len(tokens) == 0 {
		return 0, false
	}

	// Parse the first number
	result, err := strconv.Atoi(tokens[0])
	if err != nil {
		return 0, false
	}

	// Process the operations
	for i := 1; i < len(tokens); i += 2 {
		// If we've reached the end of the tokens but still expect an operation and a number,
		// the question is invalid
		if i+1 >= len(tokens) {
			return 0, false
		}

		operation := tokens[i]
		operand, err := strconv.Atoi(tokens[i+1])
		if err != nil {
			return 0, false
		}

		switch operation {
		case "plus":
			result += operand
		case "minus":
			result -= operand
		case "multiplied by":
			result *= operand
		case "divided by":
			result /= operand
		default:
			return 0, false
		}
	}

	return result, true
}

func tokenize(question string) []string {
	// Replace operations with placeholders
	question = strings.ReplaceAll(question, "multiplied by", "MULTIPLIED_BY")
	question = strings.ReplaceAll(question, "divided by", "DIVIDED_BY")

	// Split by spaces
	parts := strings.Fields(question)

	// Process each part
	var tokens []string
	for i := 0; i < len(parts); i++ {
		part := parts[i]

		// Restore operations
		if part == "MULTIPLIED_BY" {
			tokens = append(tokens, "multiplied by")
		} else if part == "DIVIDED_BY" {
			tokens = append(tokens, "divided by")
		} else if part == "plus" || part == "minus" {
			tokens = append(tokens, part)
		} else {
			// Try to parse as a number
			_, err := strconv.Atoi(part)
			if err == nil {
				tokens = append(tokens, part)
			} else {
				// If it's not a recognized operation or a number, the question is invalid
				return nil
			}
		}
	}

	// Validate the token sequence (number, operation, number, operation, ...)
	for i := 0; i < len(tokens); i++ {
		if i%2 == 0 {
			// Even positions should be numbers
			_, err := strconv.Atoi(tokens[i])
			if err != nil {
				return nil
			}
		} else {
			// Odd positions should be operations
			if tokens[i] != "plus" && tokens[i] != "minus" && tokens[i] != "multiplied by" && tokens[i] != "divided by" {
				return nil
			}
		}
	}

	return tokens
}
