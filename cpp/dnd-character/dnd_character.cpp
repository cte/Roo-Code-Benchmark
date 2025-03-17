#include "dnd_character.h"
#include <algorithm>
#include <array>
#include <cmath>
#include <random>

namespace dnd_character {

// Calculate ability modifier from ability score
int modifier(int score) {
    return static_cast<int>(std::floor((score - 10) / 2.0));
}

// Roll ability score (4d6, drop lowest)
int ability() {
    // Create random number generator
    static std::random_device rd;
    static std::mt19937 gen(rd());
    static std::uniform_int_distribution<> die(1, 6);
    
    // Roll 4 dice
    std::array<int, 4> rolls = {die(gen), die(gen), die(gen), die(gen)};
    
    // Sort the rolls to find the lowest
    std::sort(rolls.begin(), rolls.end());
    
    // Sum the highest 3 rolls (discard the lowest)
    return rolls[1] + rolls[2] + rolls[3];
}

// Constructor to generate a random character
Character::Character() {
    // Generate ability scores
    strength = ability();
    dexterity = ability();
    constitution = ability();
    intelligence = ability();
    wisdom = ability();
    charisma = ability();
    
    // Calculate hitpoints
    hitpoints = 10 + modifier(constitution);
}

}  // namespace dnd_character
