package cryptosquare

import (
	"math"
	"strings"
	"unicode"
)

func Encode(pt string) string {
	// Handle empty input
	if len(pt) == 0 {
		return ""
	}

	// Normalize the input: remove spaces and punctuation, convert to lowercase
	normalized := normalize(pt)
	if len(normalized) == 0 {
		return ""
	}

	// Calculate the dimensions of the rectangle
	c, r := calculateDimensions(len(normalized))

	// If we only have one character, return it as is
	if len(normalized) == 1 {
		return normalized
	}

	// Create the rectangle
	rectangle := createRectangle(normalized, r, c)

	// Read down the columns to create the encoded message
	return encodeRectangle(rectangle, r, c)
}

// normalize removes spaces and punctuation, and converts to lowercase
func normalize(s string) string {
	var result strings.Builder
	for _, ch := range s {
		if unicode.IsLetter(ch) || unicode.IsDigit(ch) {
			result.WriteRune(unicode.ToLower(ch))
		}
	}
	return result.String()
}

// calculateDimensions calculates the dimensions of the rectangle
func calculateDimensions(length int) (c, r int) {
	// Find the smallest possible integer c such that:
	// - r * c >= length
	// - c >= r
	// - c - r <= 1
	c = int(math.Ceil(math.Sqrt(float64(length))))
	r = int(math.Ceil(float64(length) / float64(c)))
	return c, r
}

// createRectangle arranges the normalized text into a rectangle
func createRectangle(text string, rows, cols int) [][]rune {
	rectangle := make([][]rune, rows)
	for i := range rectangle {
		rectangle[i] = make([]rune, cols)
		// Initialize with spaces
		for j := range rectangle[i] {
			rectangle[i][j] = ' '
		}
	}

	// Fill the rectangle with the text
	for i, ch := range text {
		row := i / cols
		col := i % cols
		if row < rows && col < cols {
			rectangle[row][col] = ch
		}
	}

	return rectangle
}

// encodeRectangle reads down the columns to create the encoded message
func encodeRectangle(rectangle [][]rune, rows, cols int) string {
	var result strings.Builder
	
	for col := 0; col < cols; col++ {
		if col > 0 {
			result.WriteRune(' ')
		}
		
		for row := 0; row < rows; row++ {
			if row < len(rectangle) && col < len(rectangle[row]) {
				result.WriteRune(rectangle[row][col])
			}
		}
	}
	
	return result.String()
}
