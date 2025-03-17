package transpose

func Transpose(input []string) []string {
	if len(input) == 0 {
		return []string{}
	}

	// Find the maximum length of any row
	maxLen := 0
	for _, row := range input {
		if len(row) > maxLen {
			maxLen = len(row)
		}
	}

	// Create the result slice with the appropriate number of rows
	result := make([]string, maxLen)

	// Build each row of the result
	for i := 0; i < maxLen; i++ {
		var transposedRow []rune
		for j, row := range input {
			// If this position exists in the original row, add the character
			// Otherwise, add a space (but only if there are more rows with characters at this position)
			if i < len(row) {
				transposedRow = append(transposedRow, rune(row[i]))
			} else {
				// Check if we need to add a space
				needSpace := false
				for k := j + 1; k < len(input); k++ {
					if i < len(input[k]) {
						needSpace = true
						break
					}
				}
				if needSpace {
					transposedRow = append(transposedRow, ' ')
				}
			}
		}
		result[i] = string(transposedRow)
	}

	return result
}
