#include "sublist.h"
#include <algorithm>

namespace sublist {

// Helper function to check if list_one is a subsequence of list_two
bool is_subsequence(const std::vector<int>& list_one, const std::vector<int>& list_two) {
    if (list_one.empty()) {
        return true; // Empty list is a subsequence of any list
    }
    
    if (list_one.size() > list_two.size()) {
        return false; // First list can't be a subsequence if it's longer
    }
    
    // Check for subsequence at each possible starting position in list_two
    for (size_t i = 0; i <= list_two.size() - list_one.size(); ++i) {
        bool found = true;
        for (size_t j = 0; j < list_one.size(); ++j) {
            if (list_one[j] != list_two[i + j]) {
                found = false;
                break;
            }
        }
        if (found) {
            return true;
        }
    }
    
    return false;
}

List_comparison sublist(const std::vector<int>& list_one, const std::vector<int>& list_two) {
    // Check for equality
    if (list_one == list_two) {
        return List_comparison::equal;
    }
    
    // Check if list_one is a sublist of list_two
    if (is_subsequence(list_one, list_two)) {
        return List_comparison::sublist;
    }
    
    // Check if list_two is a sublist of list_one (i.e., list_one is a superlist)
    if (is_subsequence(list_two, list_one)) {
        return List_comparison::superlist;
    }
    
    // If none of the above, the lists are unequal
    return List_comparison::unequal;
}

}  // namespace sublist
