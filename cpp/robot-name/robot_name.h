#if !defined(ROBOT_NAME_H)
#define ROBOT_NAME_H

#include <string>
#include <unordered_set>

namespace robot_name {

class robot {
public:
    robot();
    std::string name() const;
    void reset();

private:
    std::string generate_name();
    std::string current_name;
    static std::unordered_set<std::string> used_names;
};

}  // namespace robot_name

#endif // ROBOT_NAME_H