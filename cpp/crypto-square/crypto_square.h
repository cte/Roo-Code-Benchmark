#if !defined(CRYPTO_SQUARE_H)
#define CRYPTO_SQUARE_H

#include <string>

namespace crypto_square {

class cipher {
public:
    cipher(const std::string& text);
    std::string normalized_cipher_text() const;

private:
    std::string normalize(const std::string& text) const;
    std::string plaintext_;
};

}  // namespace crypto_square

#endif // CRYPTO_SQUARE_H