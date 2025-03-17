#include "grade_school.h"
#include <algorithm>

namespace grade_school {

bool school::add(const std::string& name, int grade) {
    // Check if the student already exists in any grade
    for (auto& [grade_num, students] : school_roster) {
        if (std::find(students.begin(), students.end(), name) != students.end()) {
            return false; // Student already exists
        }
    }
    
    // Add the student to the specified grade
    school_roster[grade].push_back(name);
    
    // Sort the students in the grade alphabetically
    std::sort(school_roster[grade].begin(), school_roster[grade].end());
    
    return true;
}

std::vector<std::string> school::grade(int grade) const {
    // If the grade exists, return its students, otherwise return an empty vector
    if (school_roster.find(grade) != school_roster.end()) {
        return school_roster.at(grade);
    }
    return {};
}

std::map<int, std::vector<std::string>> school::roster() const {
    return school_roster;
}

}  // namespace grade_school
