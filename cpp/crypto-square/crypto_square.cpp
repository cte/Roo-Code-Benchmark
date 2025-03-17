#include "crypto_square.h"
#include <algorithm>
#include <cctype>
#include <cmath>
#include <sstream>

namespace crypto_square {

cipher::cipher(const std::string& text) : plaintext_(normalize(text)) {}

std::string cipher::normalize(const std::string& text) const {
    std::string normalized;
    for (char c : text) {
        if (std::isalnum(c)) {
            normalized.push_back(std::tolower(c));
        }
    }
    return normalized;
}

std::string cipher::normalized_cipher_text() const {
    if (plaintext_.empty()) {
        return "";
    }

    // Calculate the dimensions of the rectangle
    int length = plaintext_.length();
    int c = std::ceil(std::sqrt(length)); // number of columns
    int r = std::ceil(static_cast<double>(length) / c); // number of rows

    // Ensure c >= r and c - r <= 1
    if (c < r) {
        c = r;
    }

    // Create the encoded text by reading down the columns
    std::stringstream result;
    
    // For each column position
    for (int col = 0; col < c; ++col) {
        // For each row in this column
        for (int row = 0; row < r; ++row) {
            int index = row * c + col;
            if (index < length) {
                result << plaintext_[index];
            } else {
                // Add a space if we're in the padding area
                result << " ";
            }
        }
        
        // Add a space between chunks, but not after the last chunk
        if (col < c - 1) {
            result << " ";
        }
    }

    return result.str();
}

}  // namespace crypto_square
