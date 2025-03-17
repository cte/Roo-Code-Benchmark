package hexadecimal

import (
	"errors"
	"strings"
)

// ParseHex converts a hexadecimal string to its decimal equivalent
func ParseHex(s string) (int64, error) {
	if s == "" {
		return 0, errors.New("syntax error: empty string")
	}

	var result int64 = 0
	for _, char := range s {
		// Shift result left by 4 bits (multiply by 16)
		result <<= 4

		// Convert hex character to decimal value
		var val int64
		if char >= '0' && char <= '9' {
			val = int64(char - '0')
		} else if char >= 'a' && char <= 'f' {
			val = int64(char - 'a' + 10)
		} else if char >= 'A' && char <= 'F' {
			val = int64(char - 'A' + 10)
		} else {
			return 0, errors.New("syntax error: invalid character")
		}

		// Check for overflow before adding
		if result > (1<<63-1-val)/16 {
			return 0, errors.New("range error: value too large for int64")
		}

		// Add the value
		result += val
	}

	return result, nil
}

// HandleErrors takes a list of inputs for ParseHex and returns a matching list
// of error cases
func HandleErrors(inputs []string) []string {
	results := make([]string, len(inputs))
	
	for i, input := range inputs {
		_, err := ParseHex(input)
		if err == nil {
			results[i] = "none"
		} else if strings.Contains(strings.ToLower(err.Error()), "syntax") {
			results[i] = "syntax"
		} else if strings.Contains(strings.ToLower(err.Error()), "range") {
			results[i] = "range"
		}
	}
	
	return results
}
