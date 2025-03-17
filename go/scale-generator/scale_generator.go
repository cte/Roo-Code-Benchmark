package scale

import (
	"strings"
)

// Scale generates a musical scale starting with the given tonic and following the specified interval pattern.
// If interval is empty, it generates a chromatic scale.
func Scale(tonic, interval string) []string {
	// Normalize tonic to uppercase for consistency
	normalizedTonic := strings.ToUpper(tonic[:1]) + tonic[1:]
	
	// Define the chromatic scales with sharps and flats
	sharps := []string{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"}
	flats := []string{"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"}
	
	// Determine whether to use sharps or flats based on the tonic
	var chromaticScale []string
	
	// Check if we should use sharps or flats based on the tonic
	// According to the instructions:
	// Sharp keys: G, D, A, E, B, F#, C# and their relative minors: e, b, f#, c#, g#, d#, a#
	// Flat keys: F, Bb, Eb, Ab, Db, Gb and their relative minors: d, g, c, f, bb, eb
	// Natural keys: C, a - use sharps when ascending
	
	// For uppercase tonics (major keys)
	majorSharpTonics := map[string]bool{
		"C": true, "G": true, "D": true, "A": true, "E": true, "B": true, "F#": true, "C#": true,
	}
	
	// For lowercase tonics (minor keys)
	minorSharpTonics := map[string]bool{
		"a": true, "e": true, "b": true, "f#": true, "c#": true, "g#": true, "d#": true,
	}
	
	// Determine which scale to use
	if majorSharpTonics[tonic] || minorSharpTonics[tonic] {
		chromaticScale = sharps
	} else {
		chromaticScale = flats
	}
	
	// Find the index of the tonic in the chromatic scale
	tonicIndex := -1
	for i, note := range chromaticScale {
		if strings.EqualFold(note, normalizedTonic) {
			tonicIndex = i
			break
		}
	}
	
	// Reorder the chromatic scale to start with the tonic
	reorderedScale := append(chromaticScale[tonicIndex:], chromaticScale[:tonicIndex]...)
	
	// If no interval is provided, return the full chromatic scale
	if interval == "" {
		return reorderedScale
	}
	
	// Generate the scale based on the interval pattern
	result := []string{reorderedScale[0]}
	currentIndex := 0
	
	for _, step := range interval {
		switch step {
		case 'm': // Minor second (half step)
			currentIndex += 1
		case 'M': // Major second (whole step)
			currentIndex += 2
		case 'A': // Augmented second
			currentIndex += 3
		}
		result = append(result, reorderedScale[currentIndex%12])
	}
	
	return result
}
