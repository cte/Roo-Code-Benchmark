package trinary

import (
	"errors"
	"math"
)

// ParseTrinary converts a trinary string to its decimal equivalent
func ParseTrinary(arg string) (int64, error) {
	// Check if the string is empty
	if len(arg) == 0 {
		return 0, errors.New("empty string")
	}

	var result int64 = 0
	
	// Iterate through each character in the string
	for _, digit := range arg {
		// Check if the digit is valid (0, 1, or 2)
		if digit < '0' || digit > '2' {
			return 0, errors.New("invalid trinary digit")
		}
		
		// Convert the digit to its numeric value
		digitValue := int64(digit - '0')
		
		// Check for potential overflow before multiplying
		if result > math.MaxInt64/3 {
			return 0, errors.New("trinary value overflows int64")
		}
		
		// Multiply the current result by 3 (shift left in trinary) and add the new digit
		result = result*3 + digitValue
		
		// Check if the result is negative (overflow occurred)
		if result < 0 {
			return 0, errors.New("trinary value overflows int64")
		}
	}
	
	return result, nil
}
