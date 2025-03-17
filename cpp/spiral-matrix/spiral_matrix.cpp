#include "spiral_matrix.h"

namespace spiral_matrix {
    std::vector<std::vector<uint32_t>> spiral_matrix(int size) {
        // Handle edge case: size 0
        if (size <= 0) {
            return {};
        }
        
        // Initialize matrix with zeros
        std::vector<std::vector<uint32_t>> matrix(size, std::vector<uint32_t>(size, 0));
        
        // Define direction vectors: right, down, left, up
        const int dx[] = {0, 1, 0, -1};
        const int dy[] = {1, 0, -1, 0};
        
        int x = 0;    // Current row
        int y = 0;    // Current column
        int dir = 0;  // Current direction (0: right, 1: down, 2: left, 3: up)
        
        // Fill the matrix with values from 1 to size*size
        for (uint32_t num = 1; num <= static_cast<uint32_t>(size * size); ++num) {
            matrix[x][y] = num;
            
            // Calculate next position
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            
            // Check if we need to change direction
            if (nx < 0 || nx >= size || ny < 0 || ny >= size || matrix[nx][ny] != 0) {
                // Change direction (right -> down -> left -> up -> right)
                dir = (dir + 1) % 4;
                nx = x + dx[dir];
                ny = y + dy[dir];
            }
            
            // Move to next position
            x = nx;
            y = ny;
        }
        
        return matrix;
    }
}  // namespace spiral_matrix
