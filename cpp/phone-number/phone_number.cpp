#include "phone_number.h"
#include <algorithm>
#include <cctype>
#include <regex>

namespace phone_number {

phone_number::phone_number(const std::string& input) {
    // Remove all non-digit characters
    std::string digits;
    std::copy_if(input.begin(), input.end(), std::back_inserter(digits),
                 [](char c) { return std::isdigit(c); });
    
    // Check if the number has valid length
    if (digits.length() == 11) {
        if (digits[0] != '1') {
            throw std::domain_error("11-digit number must start with 1");
        }
        // Remove the country code
        digits = digits.substr(1);
    } else if (digits.length() != 10) {
        throw std::domain_error("Phone number must be 10 or 11 digits");
    }
    
    // Validate area code and exchange code
    if (digits[0] == '0' || digits[0] == '1') {
        throw std::domain_error("Area code cannot start with 0 or 1");
    }
    
    if (digits[3] == '0' || digits[3] == '1') {
        throw std::domain_error("Exchange code cannot start with 0 or 1");
    }
    
    cleaned_number = digits;
}

std::string phone_number::number() const {
    return cleaned_number;
}

}  // namespace phone_number
