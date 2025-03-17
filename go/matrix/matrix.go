package matrix

import (
	"errors"
	"strconv"
	"strings"
)

// Matrix represents a matrix of integers
type Matrix [][]int

// New creates a new matrix from a string representation
func New(s string) (Matrix, error) {
	// Handle empty string
	if s == "" {
		return nil, errors.New("empty matrix")
	}

	// Split the string into rows
	rows := strings.Split(s, "\n")
	
	// Check for empty rows
	for i, row := range rows {
		if row == "" {
			return nil, errors.New("empty row at index " + strconv.Itoa(i))
		}
	}

	// Create the matrix
	matrix := make(Matrix, len(rows))
	
	// Parse each row
	for i, row := range rows {
		// Split the row into numbers
		numStrs := strings.Fields(row)
		
		// Check if this is the first row or if all rows have the same number of columns
		if i > 0 && len(numStrs) != len(matrix[0]) {
			return nil, errors.New("uneven rows")
		}
		
		// Parse each number in the row
		matrix[i] = make([]int, len(numStrs))
		for j, numStr := range numStrs {
			num, err := strconv.Atoi(numStr)
			if err != nil {
				return nil, errors.New("invalid number: " + numStr)
			}
			matrix[i][j] = num
		}
	}
	
	return matrix, nil
}

// Rows returns a copy of the rows of the matrix
func (m Matrix) Rows() [][]int {
	// Create a deep copy of the matrix
	rows := make([][]int, len(m))
	for i, row := range m {
		rows[i] = make([]int, len(row))
		copy(rows[i], row)
	}
	return rows
}

// Cols returns a copy of the columns of the matrix
func (m Matrix) Cols() [][]int {
	if len(m) == 0 {
		return [][]int{}
	}
	
	// Create a slice for each column
	cols := make([][]int, len(m[0]))
	for i := range cols {
		cols[i] = make([]int, len(m))
		
		// Fill each column with values from the corresponding row positions
		for j := range m {
			cols[i][j] = m[j][i]
		}
	}
	
	return cols
}

// Set sets the value at the specified row and column
func (m Matrix) Set(row, col, val int) bool {
	// Check if the row and column are valid
	if row < 0 || row >= len(m) || col < 0 || col >= len(m[0]) {
		return false
	}
	
	// Set the value
	m[row][col] = val
	return true
}
