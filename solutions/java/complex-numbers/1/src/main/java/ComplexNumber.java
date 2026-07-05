record ComplexNumber(double real, double imaginary) {

    double abs() {
        return Math.hypot(this.real, this.imaginary);
    }

    ComplexNumber add(final ComplexNumber other) {
        return new ComplexNumber(
                this.real + other.real,
                this.imaginary + other.imaginary
        );
    }

    ComplexNumber subtract(final ComplexNumber other) {
        return new ComplexNumber(
                this.real - other.real,
                this.imaginary - other.imaginary
        );
    }

    ComplexNumber multiply(final ComplexNumber other) {
        return new ComplexNumber(
                this.real * other.real - this.imaginary * other.imaginary,
                this.imaginary * other.real + this.real * other.imaginary
        );
    }

    ComplexNumber divide(final ComplexNumber other) {
        final double denominator = other.real * other.real
                                   + other.imaginary * other.imaginary;

        final double realNumerator = this.real * other.real
                                     + this.imaginary * other.imaginary;

        final double imaginaryNumerator = this.imaginary * other.real
                                          - this.real * other.imaginary;

        return new ComplexNumber(
                realNumerator / denominator,
                imaginaryNumerator / denominator
        );
    }

    ComplexNumber conjugate() {
        return new ComplexNumber(this.real, -this.imaginary);
    }

    ComplexNumber exponentialOf() {
        final double exponentialReal = Math.exp(this.real);

        return new ComplexNumber(
                exponentialReal * Math.cos(this.imaginary),
                exponentialReal * Math.sin(this.imaginary)
        );
    }
}