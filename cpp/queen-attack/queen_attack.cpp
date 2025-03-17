#include "queen_attack.h"
#include <cmath>

namespace queen_attack {

chess_board::chess_board(std::pair<int, int> white_position, std::pair<int, int> black_position)
    : white_pos_(white_position), black_pos_(black_position) {
    
    // Validate both positions
    validate_position(white_pos_);
    validate_position(black_pos_);
    
    // Check that queens are in different positions
    if (white_pos_ == black_pos_) {
        throw std::domain_error("Queens cannot be placed in the same position");
    }
}

std::pair<int, int> chess_board::white() const {
    return white_pos_;
}

std::pair<int, int> chess_board::black() const {
    return black_pos_;
}

bool chess_board::can_attack() const {
    // Queens can attack if they are in the same row
    if (white_pos_.first == black_pos_.first) {
        return true;
    }
    
    // Queens can attack if they are in the same column
    if (white_pos_.second == black_pos_.second) {
        return true;
    }
    
    // Queens can attack if they are on the same diagonal
    // This happens when the absolute difference in rows equals the absolute difference in columns
    int row_diff = std::abs(white_pos_.first - black_pos_.first);
    int col_diff = std::abs(white_pos_.second - black_pos_.second);
    
    return row_diff == col_diff;
}

void chess_board::validate_position(const std::pair<int, int>& position) const {
    // Check that row and column are non-negative
    if (position.first < 0) {
        throw std::domain_error("Row position cannot be negative");
    }
    if (position.second < 0) {
        throw std::domain_error("Column position cannot be negative");
    }
    
    // Check that row and column are on the board (less than 8)
    if (position.first >= 8) {
        throw std::domain_error("Row position must be on the board (0-7)");
    }
    if (position.second >= 8) {
        throw std::domain_error("Column position must be on the board (0-7)");
    }
}

}  // namespace queen_attack
