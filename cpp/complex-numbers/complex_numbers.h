#if !defined(COMPLEX_NUMBERS_H)
#define COMPLEX_NUMBERS_H

#include <cmath>

namespace complex_numbers {

class Complex {
private:
    double real_part;
    double imaginary_part;

public:
    Complex(double real, double imaginary);
    
    double real() const;
    double imag() const;
    double abs() const;
    Complex conj() const;
    Complex exp() const;
    
    // Arithmetic operators for complex numbers
    Complex operator+(const Complex& other) const;
    Complex operator-(const Complex& other) const;
    Complex operator*(const Complex& other) const;
    Complex operator/(const Complex& other) const;
    
    // Arithmetic operators with real numbers
    Complex operator+(double real) const;
    Complex operator-(double real) const;
    Complex operator*(double real) const;
    Complex operator/(double real) const;
};

// Non-member operators for operations where real number is on the left
Complex operator+(double real, const Complex& complex);
Complex operator-(double real, const Complex& complex);
Complex operator*(double real, const Complex& complex);
Complex operator/(double real, const Complex& complex);

}  // namespace complex_numbers

#endif  // COMPLEX_NUMBERS_H
