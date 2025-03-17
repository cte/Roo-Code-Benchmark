#include "all_your_base.h"
#include <algorithm>
#include <cmath>

namespace all_your_base {

std::vector<unsigned int> convert(int input_base, const std::vector<unsigned int>& input_digits, int output_base) {
    // Validate bases
    if (input_base < 2) {
        throw std::invalid_argument("Input base must be >= 2");
    }
    if (output_base < 2) {
        throw std::invalid_argument("Output base must be >= 2");
    }

    // Handle empty input
    if (input_digits.empty()) {
        return {};
    }

    // Convert input to decimal
    unsigned int decimal_value = 0;
    bool non_zero_found = false;

    for (unsigned int digit : input_digits) {
        // Validate digit
        if (digit >= static_cast<unsigned int>(input_base)) {
            throw std::invalid_argument("All digits must be < input base");
        }

        // Skip leading zeros
        if (digit == 0 && !non_zero_found) {
            continue;
        }

        non_zero_found = true;
        decimal_value = decimal_value * input_base + digit;
    }

    // Handle case where input is all zeros
    if (!non_zero_found) {
        return {};
    }

    // Convert decimal to output base
    std::vector<unsigned int> output_digits;
    
    // Special case for decimal_value = 0
    if (decimal_value == 0) {
        return {};
    }

    // Convert to output base
    while (decimal_value > 0) {
        output_digits.push_back(decimal_value % output_base);
        decimal_value /= output_base;
    }

    // Reverse the digits to get the correct order
    std::reverse(output_digits.begin(), output_digits.end());

    return output_digits;
}

}  // namespace all_your_base
