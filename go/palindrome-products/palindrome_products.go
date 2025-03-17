package palindrome

import (
	"errors"
	"strconv"
)

// Product represents a palindrome product with its factorizations
type Product struct {
	// Value is the palindrome product
	Value int
	// Factorizations is a list of factor pairs that multiply to give the palindrome product
	Factorizations [][2]int
}

// Products returns the smallest and largest palindrome products in the given range,
// along with their factorizations
func Products(fmin, fmax int) (pmin Product, pmax Product, err error) {
	// Check if fmin > fmax
	if fmin > fmax {
		return Product{}, Product{}, errors.New("fmin > fmax")
	}

	// Initialize variables to track min and max palindromes
	minPalindrome := -1
	maxPalindrome := -1
	minFactors := make([][2]int, 0)
	maxFactors := make([][2]int, 0)

	// Iterate through all possible factor pairs
	for i := fmin; i <= fmax; i++ {
		for j := i; j <= fmax; j++ { // Start from i to avoid duplicate factor pairs
			product := i * j

			// Check if the product is a palindrome
			if isPalindrome(product) {
				// If this is the first palindrome found or it's smaller than the current minimum
				if minPalindrome == -1 || product < minPalindrome {
					minPalindrome = product
					minFactors = [][2]int{{i, j}}
				} else if product == minPalindrome {
					// If we found another factorization for the same minimum palindrome
					minFactors = append(minFactors, [2]int{i, j})
				}

				// If this is the first palindrome found or it's larger than the current maximum
				if maxPalindrome == -1 || product > maxPalindrome {
					maxPalindrome = product
					maxFactors = [][2]int{{i, j}}
				} else if product == maxPalindrome {
					// If we found another factorization for the same maximum palindrome
					maxFactors = append(maxFactors, [2]int{i, j})
				}
			}
		}
	}

	// Check if no palindromes were found
	if minPalindrome == -1 {
		return Product{}, Product{}, errors.New("no palindromes")
	}

	// Create and return the Product objects
	pmin = Product{minPalindrome, minFactors}
	pmax = Product{maxPalindrome, maxFactors}
	return pmin, pmax, nil
}

// isPalindrome checks if a number is a palindrome (reads the same forward and backward)
func isPalindrome(n int) bool {
	// Convert the number to a string
	str := strconv.Itoa(n)
	
	// Check if the string reads the same forward and backward
	for i, j := 0, len(str)-1; i < j; i, j = i+1, j-1 {
		if str[i] != str[j] {
			return false
		}
	}
	
	return true
}
