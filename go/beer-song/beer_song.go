package beer

import (
	"errors"
	"fmt"
	"strings"
)

// Song returns the entire "99 Bottles of Beer on the Wall" song
func Song() string {
	lyrics, _ := Verses(99, 0)
	return lyrics
}

// Verses returns a range of verses from the song
func Verses(start, stop int) (string, error) {
	if start > 99 {
		return "", errors.New("start verse cannot be greater than 99")
	}
	if stop < 0 {
		return "", errors.New("stop verse cannot be less than 0")
	}
	if start < stop {
		return "", errors.New("start verse must be greater than or equal to stop verse")
	}

	var verses []string
	for i := start; i >= stop; i-- {
		verse, err := Verse(i)
		if err != nil {
			return "", err
		}
		verses = append(verses, verse)
	}

	// Add an extra newline at the end to match the expected output
	return strings.Join(verses, "\n") + "\n", nil
}

// Verse returns a single verse from the song
func Verse(n int) (string, error) {
	if n < 0 || n > 99 {
		return "", errors.New("verse number must be between 0 and 99")
	}

	switch n {
	case 0:
		return "No more bottles of beer on the wall, no more bottles of beer.\n" +
			"Go to the store and buy some more, 99 bottles of beer on the wall.\n", nil
	case 1:
		return "1 bottle of beer on the wall, 1 bottle of beer.\n" +
			"Take it down and pass it around, no more bottles of beer on the wall.\n", nil
	case 2:
		return "2 bottles of beer on the wall, 2 bottles of beer.\n" +
			"Take one down and pass it around, 1 bottle of beer on the wall.\n", nil
	default:
		return fmt.Sprintf("%d bottles of beer on the wall, %d bottles of beer.\n"+
			"Take one down and pass it around, %d bottles of beer on the wall.\n", n, n, n-1), nil
	}
}
