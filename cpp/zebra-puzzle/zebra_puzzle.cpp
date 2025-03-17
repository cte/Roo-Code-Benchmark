#include "zebra_puzzle.h"
#include <array>
#include <algorithm>
#include <vector>

namespace zebra_puzzle {

// Define the solve function that will return the solution
Solution solve() {
    // Define the constants for positions
    const int FIRST = 0;
    const int MIDDLE = 2;
    
    // Define the constants for colors
    const int RED = 0;
    const int GREEN = 1;
    const int IVORY = 2;
    const int YELLOW = 3;
    const int BLUE = 4;
    
    // Define the constants for nationalities
    const int ENGLISHMAN = 0;
    const int SPANIARD = 1;
    const int UKRAINIAN = 2;
    const int NORWEGIAN = 3;
    const int JAPANESE = 4;
    
    // Define the constants for drinks
    const int COFFEE = 0;
    const int TEA = 1;
    const int MILK = 2;
    const int ORANGE_JUICE = 3;
    const int WATER = 4;
    
    // Define the constants for pets
    const int DOG = 0;
    const int SNAIL = 1;
    const int FOX = 2;
    const int HORSE = 3;
    const int ZEBRA = 4;
    
    // Define the constants for hobbies
    const int DANCING = 0;
    const int PAINTING = 1;
    const int READING = 2;
    const int FOOTBALL = 3;
    const int CHESS = 4;
    
    // Arrays to map constants to strings for the final output
    const std::array<std::string, 5> nationalityNames = {
        "Englishman", "Spaniard", "Ukrainian", "Norwegian", "Japanese"
    };
    
    // Arrays to store the solution
    std::array<int, 5> colors;     // colors[i] = color of house i
    std::array<int, 5> nations;    // nations[i] = nationality of person in house i
    std::array<int, 5> drinks;     // drinks[i] = drink of person in house i
    std::array<int, 5> pets;       // pets[i] = pet of person in house i
    std::array<int, 5> hobbies;    // hobbies[i] = hobby of person in house i
    
    // Initialize arrays with values 0-4
    for (int i = 0; i < 5; i++) {
        colors[i] = i;
        nations[i] = i;
        drinks[i] = i;
        pets[i] = i;
        hobbies[i] = i;
    }
    
    // We'll use a brute force approach with permutations
    // and check all constraints for each permutation
    
    // Start with some fixed constraints:
    
    // 9. The person in the middle house drinks milk.
    drinks[MIDDLE] = MILK;
    
    // 10. The Norwegian lives in the first house.
    nations[FIRST] = NORWEGIAN;
    
    // Generate all permutations of colors, nations, drinks, pets, and hobbies
    // and check if they satisfy all constraints
    
    do {
        // 2. The Englishman lives in the red house.
        if (colors[std::find(nations.begin(), nations.end(), ENGLISHMAN) - nations.begin()] != RED) continue;
        
        // 3. The Spaniard owns the dog.
        if (pets[std::find(nations.begin(), nations.end(), SPANIARD) - nations.begin()] != DOG) continue;
        
        // 4. The person in the green house drinks coffee.
        if (drinks[std::find(colors.begin(), colors.end(), GREEN) - colors.begin()] != COFFEE) continue;
        
        // 5. The Ukrainian drinks tea.
        if (drinks[std::find(nations.begin(), nations.end(), UKRAINIAN) - nations.begin()] != TEA) continue;
        
        // 6. The green house is immediately to the right of the ivory house.
        {
            auto ivoryPos = std::find(colors.begin(), colors.end(), IVORY) - colors.begin();
            auto greenPos = std::find(colors.begin(), colors.end(), GREEN) - colors.begin();
            if (greenPos != ivoryPos + 1) continue;
        }
        
        // 7. The snail owner likes to go dancing.
        if (hobbies[std::find(pets.begin(), pets.end(), SNAIL) - pets.begin()] != DANCING) continue;
        
        // 8. The person in the yellow house is a painter.
        if (hobbies[std::find(colors.begin(), colors.end(), YELLOW) - colors.begin()] != PAINTING) continue;
        
        // 11. The person who enjoys reading lives in the house next to the person with the fox.
        {
            auto readerPos = std::find(hobbies.begin(), hobbies.end(), READING) - hobbies.begin();
            auto foxPos = std::find(pets.begin(), pets.end(), FOX) - pets.begin();
            if (abs(static_cast<int>(readerPos) - static_cast<int>(foxPos)) != 1) continue;
        }
        
        // 12. The painter's house is next to the house with the horse.
        {
            auto painterPos = std::find(hobbies.begin(), hobbies.end(), PAINTING) - hobbies.begin();
            auto horsePos = std::find(pets.begin(), pets.end(), HORSE) - pets.begin();
            if (abs(static_cast<int>(painterPos) - static_cast<int>(horsePos)) != 1) continue;
        }
        
        // 13. The person who plays football drinks orange juice.
        if (drinks[std::find(hobbies.begin(), hobbies.end(), FOOTBALL) - hobbies.begin()] != ORANGE_JUICE) continue;
        
        // 14. The Japanese person plays chess.
        if (hobbies[std::find(nations.begin(), nations.end(), JAPANESE) - nations.begin()] != CHESS) continue;
        
        // 15. The Norwegian lives next to the blue house.
        {
            auto norwegianPos = std::find(nations.begin(), nations.end(), NORWEGIAN) - nations.begin();
            auto bluePos = std::find(colors.begin(), colors.end(), BLUE) - colors.begin();
            if (abs(static_cast<int>(norwegianPos) - static_cast<int>(bluePos)) != 1) continue;
        }
        
        // If we've reached here, all constraints are satisfied
        // Find who drinks water and who owns the zebra
        int waterDrinker = std::find(drinks.begin(), drinks.end(), WATER) - drinks.begin();
        int zebraOwner = std::find(pets.begin(), pets.end(), ZEBRA) - pets.begin();
        
        return Solution{
            nationalityNames[nations[waterDrinker]],
            nationalityNames[nations[zebraOwner]]
        };
        
    } while (std::next_permutation(colors.begin(), colors.end()) ||
             std::next_permutation(nations.begin() + 1, nations.end()) || // Skip Norwegian (fixed at first house)
             std::next_permutation(drinks.begin(), drinks.end()) ||
             std::next_permutation(pets.begin(), pets.end()) ||
             std::next_permutation(hobbies.begin(), hobbies.end()));
    
    // If no solution is found (shouldn't happen with a valid puzzle)
    return Solution{"Unknown", "Unknown"};
}

}  // namespace zebra_puzzle