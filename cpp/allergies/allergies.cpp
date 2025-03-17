#include "allergies.h"

namespace allergies {

// Define the static map of allergen values
const std::unordered_map<std::string, int> allergy_test::allergen_values_ = {
    {"eggs", 1},
    {"peanuts", 2},
    {"shellfish", 4},
    {"strawberries", 8},
    {"tomatoes", 16},
    {"chocolate", 32},
    {"pollen", 64},
    {"cats", 128}
};

// Constructor
allergy_test::allergy_test(int score) : score_(score & 255) {
    // We only care about the lowest 8 bits (values 1-128)
    // as per the instructions, we ignore components with values > 255
}

// Check if allergic to a specific allergen
bool allergy_test::is_allergic_to(const std::string& allergen) const {
    auto it = allergen_values_.find(allergen);
    if (it == allergen_values_.end()) {
        return false; // Unknown allergen
    }
    
    // Check if the bit corresponding to this allergen is set in the score
    return (score_ & it->second) != 0;
}

// Get all allergens the person is allergic to
std::unordered_set<std::string> allergy_test::get_allergies() const {
    std::unordered_set<std::string> result;
    
    // Check each allergen
    for (const auto& [allergen, value] : allergen_values_) {
        if (score_ & value) {
            result.insert(allergen);
        }
    }
    
    return result;
}

}  // namespace allergies
