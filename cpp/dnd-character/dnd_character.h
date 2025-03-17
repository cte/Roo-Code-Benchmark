#pragma once

namespace dnd_character {

// Calculate ability modifier from ability score
int modifier(int score);

// Roll ability score (4d6, drop lowest)
int ability();

// Character class with abilities and hitpoints
struct Character {
    int strength;
    int dexterity;
    int constitution;
    int intelligence;
    int wisdom;
    int charisma;
    int hitpoints;

    // Constructor to generate a random character
    Character();
};

}  // namespace dnd_character
