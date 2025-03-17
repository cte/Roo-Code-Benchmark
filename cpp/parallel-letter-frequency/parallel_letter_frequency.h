#if !defined(PARALLEL_LETTER_FREQUENCY_H)
#define PARALLEL_LETTER_FREQUENCY_H

#include <string_view>
#include <vector>
#include <unordered_map>

namespace parallel_letter_frequency {
    std::unordered_map<char, int> frequency(const std::vector<std::string_view>& texts);
}

#endif
