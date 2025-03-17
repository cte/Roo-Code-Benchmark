package say

import (
	"strings"
)

// Say converts an integer to its English representation
func Say(n int64) (string, bool) {
	// Check if the number is within the valid range
	if n < 0 || n >= 1000000000000 {
		return "", false
	}

	// Special case for zero
	if n == 0 {
		return "zero", true
	}

	// Convert the number to English
	return sayNumber(n), true
}

// sayNumber converts a positive number to its English representation
func sayNumber(n int64) string {
	// Arrays for special cases
	ones := []string{"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
		"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"}
	tens := []string{"", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"}
	scales := []string{"", "thousand", "million", "billion"}

	// Handle numbers less than 100
	if n < 20 {
		return ones[n]
	}
	if n < 100 {
		if n%10 == 0 {
			return tens[n/10]
		}
		return tens[n/10] + "-" + ones[n%10]
	}

	// Handle numbers less than 1000
	if n < 1000 {
		if n%100 == 0 {
			return ones[n/100] + " hundred"
		}
		return ones[n/100] + " hundred " + sayNumber(n%100)
	}

	// Break the number into chunks of thousands
	var result strings.Builder
	var chunks []int64

	// Extract chunks of up to 3 digits
	temp := n
	for temp > 0 {
		chunks = append(chunks, temp%1000)
		temp /= 1000
	}

	// Build the result string from the chunks
	for i := len(chunks) - 1; i >= 0; i-- {
		if chunks[i] > 0 {
			if result.Len() > 0 {
				result.WriteString(" ")
			}
			result.WriteString(sayNumber(chunks[i]))
			if i > 0 { // Add scale word if not the last chunk
				result.WriteString(" " + scales[i])
			}
		}
	}

	return result.String()
}
