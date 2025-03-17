#include "perfect_numbers.h"
#include <stdexcept>

namespace perfect_numbers {
    classification classify(int number) {
        // Check if the number is positive
        if (number <= 0) {
            throw std::domain_error("Classification is only defined for positive integers.");
        }
        
        // Calculate the aliquot sum (sum of factors excluding the number itself)
        int aliquot_sum = 0;
        
        // Special case for 1, which has no factors other than itself
        if (number == 1) {
            return classification::deficient;
        }
        
        // Find all factors and sum them
        for (int i = 1; i <= number / 2; ++i) {
            if (number % i == 0) {
                aliquot_sum += i;
            }
        }
        
        // Determine the classification based on the aliquot sum
        if (aliquot_sum == number) {
            return classification::perfect;
        } else if (aliquot_sum > number) {
            return classification::abundant;
        } else {
            return classification::deficient;
        }
    }
}  // namespace perfect_numbers
