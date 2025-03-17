package dndcharacter

type Character struct {
	Strength     int
	Dexterity    int
	Constitution int
	Intelligence int
	Wisdom       int
	Charisma     int
	Hitpoints    int
}

// Modifier calculates the ability modifier for a given ability score
func Modifier(score int) int {
	return (score - 10) / 2
}

// Ability uses randomness to generate the score for an ability
func Ability() int {
	// Roll four 6-sided dice
	rolls := make([]int, 4)
	for i := 0; i < 4; i++ {
		rolls[i] = rand.Intn(6) + 1 // 1-6
	}
	
	// Find the smallest roll to discard
	minIndex := 0
	for i := 1; i < 4; i++ {
		if rolls[i] < rolls[minIndex] {
			minIndex = i
		}
	}
	
	// Sum the three highest dice
	sum := 0
	for i, roll := range rolls {
		if i != minIndex {
			sum += roll
		}
	}
	
	return sum
}

// GenerateCharacter creates a new Character with random scores for abilities
func GenerateCharacter() Character {
	character := Character{
		Strength:     Ability(),
		Dexterity:    Ability(),
		Constitution: Ability(),
		Intelligence: Ability(),
		Wisdom:       Ability(),
		Charisma:     Ability(),
	}
	
	// Calculate hitpoints: 10 + constitution modifier
	character.Hitpoints = 10 + Modifier(character.Constitution)
	
	return character
}
