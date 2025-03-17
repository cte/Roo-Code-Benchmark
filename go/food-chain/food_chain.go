package foodchain

import (
	"fmt"
	"strings"
)

// Define the animals and their corresponding special lines
var animals = []string{
	"", // Index 0 is empty to align with verse numbers starting at 1
	"fly",
	"spider",
	"bird",
	"cat",
	"dog",
	"goat",
	"cow",
	"horse",
}

var specialLines = []string{
	"", // Index 0 is empty to align with verse numbers starting at 1
	"I don't know why she swallowed the fly. Perhaps she'll die.",
	"It wriggled and jiggled and tickled inside her.",
	"How absurd to swallow a bird!",
	"Imagine that, to swallow a cat!",
	"What a hog, to swallow a dog!",
	"Just opened her throat and swallowed a goat!",
	"I don't know how she swallowed a cow!",
	"She's dead, of course!",
}

// Verse returns the verse for the given verse number
func Verse(v int) string {
	if v < 1 || v > 8 {
		return ""
	}

	var lines []string
	
	// First line is always the same pattern
	lines = append(lines, fmt.Sprintf("I know an old lady who swallowed a %s.", animals[v]))
	
	// Add the special line for this animal
	lines = append(lines, specialLines[v])
	
	// For the horse (verse 8), we're done after the special line
	if v == 8 {
		return strings.Join(lines, "\n")
	}
	
	// For verse 1 (fly), we're also done after the special line
	if v == 1 {
		return strings.Join(lines, "\n")
	}
	
	// For other verses, add the cumulative "she swallowed X to catch Y" lines
	for i := v; i > 1; i-- {
		// Special case for the spider
		if i == 3 {
			lines = append(lines, fmt.Sprintf("She swallowed the %s to catch the %s that wriggled and jiggled and tickled inside her.", animals[i], animals[i-1]))
		} else {
			lines = append(lines, fmt.Sprintf("She swallowed the %s to catch the %s.", animals[i], animals[i-1]))
		}
	}
	
	// Last line is always the same for verses 2-7
	lines = append(lines, specialLines[1])
	
	return strings.Join(lines, "\n")
}

// Verses returns a string containing the verses from start to end, inclusive
func Verses(start, end int) string {
	var verses []string
	for i := start; i <= end; i++ {
		verses = append(verses, Verse(i))
	}
	return strings.Join(verses, "\n\n")
}

// Song returns the entire song (verses 1-8)
func Song() string {
	return Verses(1, 8)
}
