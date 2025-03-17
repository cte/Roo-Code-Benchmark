#if !defined(BANK_ACCOUNT_H)
#define BANK_ACCOUNT_H

#include <mutex>
#include <stdexcept>

namespace Bankaccount {
class Bankaccount {
public:
    Bankaccount();
    
    void open();
    void close();
    int balance() const;
    void deposit(int amount);
    void withdraw(int amount);

private:
    mutable std::mutex mutex_;
    int balance_;
    bool is_open_;
};  // class Bankaccount

}  // namespace Bankaccount

#endif  // BANK_ACCOUNT_H