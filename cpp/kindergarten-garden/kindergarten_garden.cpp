#include "kindergarten_garden.h"
#include <map>
#include <vector>

namespace kindergarten_garden {

std::array<Plants, 4> plants(const std::string& diagram, const std::string& student) {
    // Define the list of students in alphabetical order
    const std::vector<std::string> students = {
        "Alice", "Bob", "Charlie", "David", "Eve", "Fred", 
        "Ginny", "Harriet", "Ileana", "Joseph", "Kincaid", "Larry"
    };
    
    // Map plant codes to Plants enum values
    const std::map<char, Plants> plant_map = {
        {'C', Plants::clover},
        {'G', Plants::grass},
        {'V', Plants::violets},
        {'R', Plants::radishes}
    };
    
    // Find the student's position in the alphabetical list
    auto student_pos = std::find(students.begin(), students.end(), student);
    int index = std::distance(students.begin(), student_pos);
    
    // Each student gets 2 plants per row, starting from their position * 2
    int start_pos = index * 2;
    
    // Split the diagram into rows
    size_t newline_pos = diagram.find('\n');
    std::string row1 = diagram.substr(0, newline_pos);
    std::string row2 = diagram.substr(newline_pos + 1);
    
    // Get the student's plants
    std::array<Plants, 4> result;
    result[0] = plant_map.at(row1[start_pos]);
    result[1] = plant_map.at(row1[start_pos + 1]);
    result[2] = plant_map.at(row2[start_pos]);
    result[3] = plant_map.at(row2[start_pos + 1]);
    
    return result;
}

}  // namespace kindergarten_garden
