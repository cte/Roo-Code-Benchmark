export class ComplexNumber {
  constructor(real, imag) {
    this._real = real || 0;
    this._imag = imag || 0;
  }

  get real() {
    return this._real;
  }

  get imag() {
    return this._imag;
  }

  add(other) {
    return new ComplexNumber(
      this._real + other.real,
      this._imag + other.imag
    );
  }

  sub(other) {
    return new ComplexNumber(
      this._real - other.real,
      this._imag - other.imag
    );
  }

  mul(other) {
    // (a + bi) * (c + di) = (ac - bd) + (bc + ad)i
    return new ComplexNumber(
      this._real * other.real - this._imag * other.imag,
      this._imag * other.real + this._real * other.imag
    );
  }

  div(other) {
    // (a + bi) / (c + di) = ((ac + bd)/(c^2 + d^2)) + ((bc - ad)/(c^2 + d^2))i
    const denominator = other.real ** 2 + other.imag ** 2;
    return new ComplexNumber(
      (this._real * other.real + this._imag * other.imag) / denominator,
      (this._imag * other.real - this._real * other.imag) / denominator
    );
  }

  get abs() {
    // |a + bi| = sqrt(a^2 + b^2)
    return Math.sqrt(this._real ** 2 + this._imag ** 2);
  }

  get conj() {
    // (a + bi)* = a - bi
    return new ComplexNumber(this._real, -this._imag);
  }

  get exp() {
    // e^(a + bi) = e^a * (cos(b) + i*sin(b))
    const expReal = Math.exp(this._real);
    return new ComplexNumber(
      expReal * Math.cos(this._imag),
      expReal * Math.sin(this._imag)
    );
  }
}
