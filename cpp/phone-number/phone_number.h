#if !defined(PHONE_NUMBER_H)
#define PHONE_NUMBER_H

#include <string>
#include <stdexcept>

namespace phone_number {

class phone_number {
public:
    phone_number(const std::string& input);
    std::string number() const;

private:
    std::string cleaned_number;
};

}  // namespace phone_number

#endif // PHONE_NUMBER_H