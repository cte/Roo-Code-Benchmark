package octal

import (
	"errors"
	"unicode"
)

// ParseOctal converts an octal string to its decimal equivalent
func ParseOctal(input string) (int64, error) {
	if input == "" {
		return 0, errors.New("empty input")
	}

	var result int64

	for _, char := range input {
		// Check if the character is a valid octal digit (0-7)
		if !unicode.IsDigit(char) || char > '7' {
			return 0, errors.New("invalid octal digit")
		}

		// Convert the character to its numeric value
		digitValue := int64(char - '0')

		// Multiply the current result by 8 and add the new digit
		result = result*8 + digitValue
	}

	return result, nil
}
