#pragma once
#include <array>
#include <string>
#include <vector>

namespace kindergarten_garden {

enum class Plants {
    clover,
    grass,
    violets,
    radishes
};

std::array<Plants, 4> plants(const std::string& diagram, const std::string& student);

}  // namespace kindergarten_garden
