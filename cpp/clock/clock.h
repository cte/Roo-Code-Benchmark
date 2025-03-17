#if !defined(CLOCK_H)
#define CLOCK_H

#include <string>

namespace date_independent {

class clock {
private:
    int hours_;
    int minutes_;
    
    // Normalize the time to ensure hours are in [0,23] and minutes are in [0,59]
    void normalize();

public:
    // Constructor
    clock(int hours, int minutes);
    
    // Static factory method
    static clock at(int hour, int minute);
    
    // Add minutes to the clock
    clock plus(int minutes) const;
    
    // Equality operators
    bool operator==(const clock& other) const;
    bool operator!=(const clock& other) const;
    
    // String conversion
    operator std::string() const;
};

}  // namespace date_independent

#endif // CLOCK_H