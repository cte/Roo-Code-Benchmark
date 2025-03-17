package bottlesong

func Recite(startBottles, takeDown int) []string {
	var lyrics []string
	
	for i := 0; i < takeDown; i++ {
		currentBottles := startBottles - i
		nextBottles := currentBottles - 1
		
		// Add verse
		lyrics = append(lyrics, formatBottleCount(currentBottles, true)+" hanging on the wall,")
		lyrics = append(lyrics, formatBottleCount(currentBottles, true)+" hanging on the wall,")
		lyrics = append(lyrics, "And if one green bottle should accidentally fall,")
		lyrics = append(lyrics, "There'll be "+formatBottleCount(nextBottles, false)+" hanging on the wall.")
		
		// Add empty line between verses, except after the last verse
		if i < takeDown-1 {
			lyrics = append(lyrics, "")
		}
	}
	
	return lyrics
}

// formatBottleCount formats the bottle count with proper grammar
// capitalize determines whether to capitalize the first letter
func formatBottleCount(count int, capitalize bool) string {
	if count == 0 {
		return "no green bottles"
	} else if count == 1 {
		if capitalize {
			return "One green bottle"
		} else {
			return "one green bottle"
		}
	} else {
		// Convert number to word
		words := []string{"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"}
		word := words[count]
		
		if capitalize {
			// Capitalize first letter
			if len(word) > 0 {
				word = string(word[0]-32) + word[1:]
			}
		}
		
		return word + " green bottles"
	}
}
