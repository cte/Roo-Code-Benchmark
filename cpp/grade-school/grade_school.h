#if !defined(GRADE_SCHOOL_H)
#define GRADE_SCHOOL_H

#include <string>
#include <vector>
#include <map>

namespace grade_school {

class school {
public:
    // Add a student to a grade
    bool add(const std::string& name, int grade);
    
    // Get a sorted list of all students in a specific grade
    std::vector<std::string> grade(int grade) const;
    
    // Get a sorted map of all grades and students
    std::map<int, std::vector<std::string>> roster() const;

private:
    std::map<int, std::vector<std::string>> school_roster;
};

}  // namespace grade_school

#endif // GRADE_SCHOOL_H