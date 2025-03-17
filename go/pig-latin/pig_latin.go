package piglatin

import (
	"strings"
)

func Sentence(sentence string) string {
	words := strings.Fields(sentence)
	var result []string

	for _, word := range words {
		result = append(result, translateWord(word))
	}

	return strings.Join(result, " ")
}

func translateWord(word string) string {
	// Rule 1: If a word begins with a vowel, or starts with "xr" or "yt", add "ay" to the end
	if isVowel(word[0]) || strings.HasPrefix(word, "xr") || strings.HasPrefix(word, "yt") {
		return word + "ay"
	}

	// Rule 3: Handle words beginning with "qu" or consonants followed by "qu"
	quIndex := strings.Index(word, "qu")
	if quIndex >= 0 && quIndex <= 1 {
		// If "qu" is at the beginning or after one consonant
		return word[quIndex+2:] + word[:quIndex+2] + "ay"
	}

	// Rule 4: Handle words with consonants followed by "y"
	// First check if "y" is not the first letter and is preceded by consonants
	yIndex := strings.IndexRune(word, 'y')
	if yIndex > 0 {
		// Check if all characters before "y" are consonants
		allConsonantsBefore := true
		for i := 0; i < yIndex; i++ {
			if isVowel(word[i]) {
				allConsonantsBefore = false
				break
			}
		}
		
		if allConsonantsBefore {
			return word[yIndex:] + word[:yIndex] + "ay"
		}
	}

	// Rule 2: If a word begins with consonants, move them to the end and add "ay"
	// Find the index of the first vowel (including "y" if not at the beginning)
	firstVowelIndex := len(word)
	for i, c := range word {
		if isVowel(byte(c)) || (c == 'y' && i > 0) {
			firstVowelIndex = i
			break
		}
	}

	if firstVowelIndex < len(word) {
		return word[firstVowelIndex:] + word[:firstVowelIndex] + "ay"
	}

	// Default case (shouldn't reach here with the given rules)
	return word + "ay"
}

func isVowel(c byte) bool {
	return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
}
