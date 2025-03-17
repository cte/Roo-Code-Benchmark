package zebra

type Solution struct {
	DrinksWater string
	OwnsZebra   string
}

// House represents a house in the puzzle with its attributes
type House struct {
	Position   int
	Color      string
	Nationality string
	Pet        string
	Drink      string
	Cigarette  string
}

func SolvePuzzle() Solution {
	// Create all possible combinations for each attribute
	colors := []string{"red", "green", "ivory", "yellow", "blue"}
	nationalities := []string{"Englishman", "Spaniard", "Ukrainian", "Norwegian", "Japanese"}
	pets := []string{"dog", "snails", "fox", "horse", "zebra"}
	drinks := []string{"coffee", "tea", "milk", "orange juice", "water"}
	cigarettes := []string{"Old Gold", "Kools", "Chesterfields", "Lucky Strike", "Parliaments"}

	// Try all possible permutations of houses
	for _, colorPerm := range permutations(colors) {
		for _, nationalityPerm := range permutations(nationalities) {
			for _, petPerm := range permutations(pets) {
				for _, drinkPerm := range permutations(drinks) {
					for _, cigarettePerm := range permutations(cigarettes) {
						// Create houses with the current permutation
						houses := make([]House, 5)
						for i := 0; i < 5; i++ {
							houses[i] = House{
								Position:    i,
								Color:       colorPerm[i],
								Nationality: nationalityPerm[i],
								Pet:         petPerm[i],
								Drink:       drinkPerm[i],
								Cigarette:   cigarettePerm[i],
							}
						}

						// Check if all constraints are satisfied
						if checkConstraints(houses) {
							// Find who drinks water and who owns the zebra
							var drinksWater, ownsZebra string
							for _, house := range houses {
								if house.Drink == "water" {
									drinksWater = house.Nationality
								}
								if house.Pet == "zebra" {
									ownsZebra = house.Nationality
								}
							}
							return Solution{DrinksWater: drinksWater, OwnsZebra: ownsZebra}
						}
					}
				}
			}
		}
	}

	// This should never happen if the puzzle has a solution
	panic("No solution found")
}

// checkConstraints checks if all the puzzle constraints are satisfied
func checkConstraints(houses []House) bool {
	// 2. The Englishman lives in the red house.
	if !checkNationalityColor(houses, "Englishman", "red") {
		return false
	}

	// 3. The Spaniard owns the dog.
	if !checkNationalityPet(houses, "Spaniard", "dog") {
		return false
	}

	// 4. Coffee is drunk in the green house.
	if !checkColorDrink(houses, "green", "coffee") {
		return false
	}

	// 5. The Ukrainian drinks tea.
	if !checkNationalityDrink(houses, "Ukrainian", "tea") {
		return false
	}

	// 6. The green house is immediately to the right of the ivory house.
	if !checkRightOf(houses, "green", "ivory") {
		return false
	}

	// 7. The Old Gold smoker owns snails.
	if !checkCigarettePet(houses, "Old Gold", "snails") {
		return false
	}

	// 8. Kools are smoked in the yellow house.
	if !checkColorCigarette(houses, "yellow", "Kools") {
		return false
	}

	// 9. Milk is drunk in the middle house.
	if !checkMiddleHouseDrink(houses, "milk") {
		return false
	}

	// 10. The Norwegian lives in the first house.
	if !checkFirstHouseNationality(houses, "Norwegian") {
		return false
	}

	// 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
	if !checkNextTo(houses, func(h House) bool { return h.Cigarette == "Chesterfields" },
		func(h House) bool { return h.Pet == "fox" }) {
		return false
	}

	// 12. Kools are smoked in the house next to the house where the horse is kept.
	if !checkNextTo(houses, func(h House) bool { return h.Cigarette == "Kools" },
		func(h House) bool { return h.Pet == "horse" }) {
		return false
	}

	// 13. The Lucky Strike smoker drinks orange juice.
	if !checkCigaretteDrink(houses, "Lucky Strike", "orange juice") {
		return false
	}

	// 14. The Japanese smokes Parliaments.
	if !checkNationalityCigarette(houses, "Japanese", "Parliaments") {
		return false
	}

	// 15. The Norwegian lives next to the blue house.
	if !checkNextTo(houses, func(h House) bool { return h.Nationality == "Norwegian" },
		func(h House) bool { return h.Color == "blue" }) {
		return false
	}

	return true
}

// Helper functions to check specific constraints

func checkNationalityColor(houses []House, nationality, color string) bool {
	for _, h := range houses {
		if h.Nationality == nationality && h.Color != color {
			return false
		}
		if h.Color == color && h.Nationality != nationality {
			return false
		}
	}
	return true
}

func checkNationalityPet(houses []House, nationality, pet string) bool {
	for _, h := range houses {
		if h.Nationality == nationality && h.Pet != pet {
			return false
		}
		if h.Pet == pet && h.Nationality != nationality {
			return false
		}
	}
	return true
}

func checkColorDrink(houses []House, color, drink string) bool {
	for _, h := range houses {
		if h.Color == color && h.Drink != drink {
			return false
		}
		if h.Drink == drink && h.Color != color {
			return false
		}
	}
	return true
}

func checkNationalityDrink(houses []House, nationality, drink string) bool {
	for _, h := range houses {
		if h.Nationality == nationality && h.Drink != drink {
			return false
		}
		if h.Drink == drink && h.Nationality != nationality {
			return false
		}
	}
	return true
}

func checkRightOf(houses []House, rightColor, leftColor string) bool {
	for i := 0; i < len(houses)-1; i++ {
		if houses[i].Color == leftColor && houses[i+1].Color == rightColor {
			return true
		}
	}
	return false
}

func checkCigarettePet(houses []House, cigarette, pet string) bool {
	for _, h := range houses {
		if h.Cigarette == cigarette && h.Pet != pet {
			return false
		}
		if h.Pet == pet && h.Cigarette != cigarette {
			return false
		}
	}
	return true
}

func checkColorCigarette(houses []House, color, cigarette string) bool {
	for _, h := range houses {
		if h.Color == color && h.Cigarette != cigarette {
			return false
		}
		if h.Cigarette == cigarette && h.Color != color {
			return false
		}
	}
	return true
}

func checkMiddleHouseDrink(houses []House, drink string) bool {
	return houses[2].Drink == drink
}

func checkFirstHouseNationality(houses []House, nationality string) bool {
	return houses[0].Nationality == nationality
}

func checkNextTo(houses []House, pred1, pred2 func(House) bool) bool {
	for i := 0; i < len(houses); i++ {
		if pred1(houses[i]) {
			if (i > 0 && pred2(houses[i-1])) || (i < len(houses)-1 && pred2(houses[i+1])) {
				return true
			}
			return false
		}
	}
	return false
}

func checkCigaretteDrink(houses []House, cigarette, drink string) bool {
	for _, h := range houses {
		if h.Cigarette == cigarette && h.Drink != drink {
			return false
		}
		if h.Drink == drink && h.Cigarette != cigarette {
			return false
		}
	}
	return true
}

func checkNationalityCigarette(houses []House, nationality, cigarette string) bool {
	for _, h := range houses {
		if h.Nationality == nationality && h.Cigarette != cigarette {
			return false
		}
		if h.Cigarette == cigarette && h.Nationality != nationality {
			return false
		}
	}
	return true
}

// Generate all permutations of a slice
func permutations(arr []string) [][]string {
	var result [][]string
	generatePermutations(arr, 0, &result)
	return result
}

func generatePermutations(arr []string, start int, result *[][]string) {
	if start == len(arr)-1 {
		temp := make([]string, len(arr))
		copy(temp, arr)
		*result = append(*result, temp)
		return
	}

	for i := start; i < len(arr); i++ {
		// Swap
		arr[start], arr[i] = arr[i], arr[start]
		// Recurse
		generatePermutations(arr, start+1, result)
		// Backtrack
		arr[start], arr[i] = arr[i], arr[start]
	}
}
