#include "bank_account.h"

namespace Bankaccount {

Bankaccount::Bankaccount() : balance_(0), is_open_(false) {}

void Bankaccount::open() {
    std::lock_guard<std::mutex> lock(mutex_);
    if (is_open_) {
        throw std::runtime_error("Cannot open an already opened account");
    }
    is_open_ = true;
    balance_ = 0;
}

void Bankaccount::close() {
    std::lock_guard<std::mutex> lock(mutex_);
    if (!is_open_) {
        throw std::runtime_error("Cannot close an account that was not opened");
    }
    is_open_ = false;
}

int Bankaccount::balance() const {
    std::lock_guard<std::mutex> lock(mutex_);
    if (!is_open_) {
        throw std::runtime_error("Cannot check balance of closed account");
    }
    return balance_;
}

void Bankaccount::deposit(int amount) {
    std::lock_guard<std::mutex> lock(mutex_);
    if (!is_open_) {
        throw std::runtime_error("Cannot deposit into unopened account");
    }
    if (amount < 0) {
        throw std::runtime_error("Cannot deposit negative amount");
    }
    balance_ += amount;
}

void Bankaccount::withdraw(int amount) {
    std::lock_guard<std::mutex> lock(mutex_);
    if (!is_open_) {
        throw std::runtime_error("Cannot withdraw from closed account");
    }
    if (amount < 0) {
        throw std::runtime_error("Cannot withdraw negative amount");
    }
    if (amount > balance_) {
        throw std::runtime_error("Cannot withdraw more than deposited");
    }
    balance_ -= amount;
}

}  // namespace Bankaccount