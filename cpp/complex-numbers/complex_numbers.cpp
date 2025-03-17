#include "complex_numbers.h"
#include <cmath>

namespace complex_numbers {

// Constructor
Complex::Complex(double real, double imaginary)
    : real_part(real), imaginary_part(imaginary) {}

// Accessor methods
double Complex::real() const {
    return real_part;
}

double Complex::imag() const {
    return imaginary_part;
}

// Absolute value: |z| = sqrt(a^2 + b^2)
double Complex::abs() const {
    return std::sqrt(real_part * real_part + imaginary_part * imaginary_part);
}

// Complex conjugate: (a + bi) -> (a - bi)
Complex Complex::conj() const {
    return Complex(real_part, -imaginary_part);
}

// Exponential function: e^(a + bi) = e^a * (cos(b) + i*sin(b))
Complex Complex::exp() const {
    double e_to_real = std::exp(real_part);
    return Complex(e_to_real * std::cos(imaginary_part), 
                   e_to_real * std::sin(imaginary_part));
}

// Addition: (a + bi) + (c + di) = (a + c) + (b + d)i
Complex Complex::operator+(const Complex& other) const {
    return Complex(real_part + other.real_part, 
                   imaginary_part + other.imaginary_part);
}

// Subtraction: (a + bi) - (c + di) = (a - c) + (b - d)i
Complex Complex::operator-(const Complex& other) const {
    return Complex(real_part - other.real_part, 
                   imaginary_part - other.imaginary_part);
}

// Multiplication: (a + bi) * (c + di) = (ac - bd) + (bc + ad)i
Complex Complex::operator*(const Complex& other) const {
    return Complex(real_part * other.real_part - imaginary_part * other.imaginary_part,
                   imaginary_part * other.real_part + real_part * other.imaginary_part);
}

// Division: (a + bi) / (c + di) = ((ac + bd) + (bc - ad)i) / (c^2 + d^2)
Complex Complex::operator/(const Complex& other) const {
    double denominator = other.real_part * other.real_part + other.imaginary_part * other.imaginary_part;
    
    return Complex(
        (real_part * other.real_part + imaginary_part * other.imaginary_part) / denominator,
        (imaginary_part * other.real_part - real_part * other.imaginary_part) / denominator
    );
}

// Operations with real numbers
Complex Complex::operator+(double real) const {
    return Complex(real_part + real, imaginary_part);
}

Complex Complex::operator-(double real) const {
    return Complex(real_part - real, imaginary_part);
}

Complex Complex::operator*(double real) const {
    return Complex(real_part * real, imaginary_part * real);
}

Complex Complex::operator/(double real) const {
    return Complex(real_part / real, imaginary_part / real);
}

// Non-member operators for operations where real number is on the left
Complex operator+(double real, const Complex& complex) {
    return Complex(real + complex.real(), complex.imag());
}

Complex operator-(double real, const Complex& complex) {
    return Complex(real - complex.real(), -complex.imag());
}

Complex operator*(double real, const Complex& complex) {
    return Complex(real * complex.real(), real * complex.imag());
}

Complex operator/(double real, const Complex& complex) {
    double denominator = complex.real() * complex.real() + complex.imag() * complex.imag();
    
    return Complex(
        (real * complex.real()) / denominator,
        (-real * complex.imag()) / denominator
    );
}

}  // namespace complex_numbers
