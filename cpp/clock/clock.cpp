#include "clock.h"
#include <iomanip>
#include <sstream>

namespace date_independent {

// Constructor
clock::clock(int hours, int minutes) : hours_(hours), minutes_(minutes) {
    normalize();
}

// Static factory method
clock clock::at(int hour, int minute) {
    return clock(hour, minute);
}

// Normalize the time
void clock::normalize() {
    // Handle minutes overflow and underflow
    int total_minutes = hours_ * 60 + minutes_;
    
    // Handle negative total minutes
    // Add enough days to make it positive
    while (total_minutes < 0) {
        total_minutes += 24 * 60; // Add a full day
    }
    
    // Calculate hours and minutes, ensuring they wrap correctly
    hours_ = (total_minutes / 60) % 24;
    minutes_ = total_minutes % 60;
}

// Add minutes to the clock
clock clock::plus(int minutes) const {
    return clock(hours_, minutes_ + minutes);
}

// Equality operators
bool clock::operator==(const clock& other) const {
    return hours_ == other.hours_ && minutes_ == other.minutes_;
}

bool clock::operator!=(const clock& other) const {
    return !(*this == other);
}

// String conversion
clock::operator std::string() const {
    std::ostringstream oss;
    oss << std::setfill('0') << std::setw(2) << hours_ << ":"
        << std::setfill('0') << std::setw(2) << minutes_;
    return oss.str();
}

}  // namespace date_independent
