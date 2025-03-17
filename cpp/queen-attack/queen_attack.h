#if !defined(QUEEN_ATTACK_H)
#define QUEEN_ATTACK_H

#include <utility>
#include <stdexcept>

namespace queen_attack {

class chess_board {
public:
    // Constructor takes positions of white and black queens
    chess_board(std::pair<int, int> white_position, std::pair<int, int> black_position);
    
    // Return positions of queens
    std::pair<int, int> white() const;
    std::pair<int, int> black() const;
    
    // Check if queens can attack each other
    bool can_attack() const;
    
private:
    std::pair<int, int> white_pos_;
    std::pair<int, int> black_pos_;
    
    // Helper method to validate a position
    void validate_position(const std::pair<int, int>& position) const;
};

}  // namespace queen_attack

#endif // QUEEN_ATTACK_H