#include "diamond.h"
#include <vector>
#include <string>

namespace diamond {
    std::vector<std::string> rows(char letter) {
        // Calculate the size of the diamond
        int size = letter - 'A' + 1;
        int width = 2 * size - 1;
        std::vector<std::string> result;
        
        // Generate the top half of the diamond (including the middle row)
        for (int i = 0; i < size; i++) {
            char current_letter = 'A' + i;
            std::string row(width, ' ');
            
            // Place the letters in the row
            row[size - 1 - i] = current_letter;
            if (i > 0) {
                row[size - 1 + i] = current_letter;
            }
            
            result.push_back(row);
        }
        
        // Generate the bottom half of the diamond (excluding the middle row)
        for (int i = size - 2; i >= 0; i--) {
            result.push_back(result[i]);
        }
        
        return result;
    }
}  // namespace diamond
