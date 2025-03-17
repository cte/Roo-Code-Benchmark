#include "robot_name.h"
#include <random>
#include <ctime>

namespace robot_name {

// Initialize static member
std::unordered_set<std::string> robot::used_names;

// Constructor
robot::robot() {
    current_name = generate_name();
}

// Return the robot's name
std::string robot::name() const {
    return current_name;
}

// Reset the robot's name
void robot::reset() {
    // Generate a new name (don't remove the old one from used_names)
    current_name = generate_name();
}

// Generate a unique random name
std::string robot::generate_name() {
    // Use a random device for better seeding
    static std::random_device rd;
    static std::mt19937 rng(rd());
    static std::uniform_int_distribution<> letter_dist(0, 25);  // A-Z (26 letters)
    static std::uniform_int_distribution<> digit_dist(0, 9);    // 0-9 (10 digits)
    
    std::string new_name;
    
    do {
        new_name.clear();
        
        // Generate 2 uppercase letters
        for (int i = 0; i < 2; ++i) {
            new_name += static_cast<char>('A' + letter_dist(rng));
        }
        
        // Generate 3 digits
        for (int i = 0; i < 3; ++i) {
            new_name += static_cast<char>('0' + digit_dist(rng));
        }
        
    } while (used_names.count(new_name) > 0);
    
    // Add the new name to the set of used names
    used_names.insert(new_name);
    
    return new_name;
}

}  // namespace robot_name
