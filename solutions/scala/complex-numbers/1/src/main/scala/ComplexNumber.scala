case class ComplexNumber(real: Double = 0, imaginary: Double = 0):
    def +(other: ComplexNumber): ComplexNumber =
        ComplexNumber(
            real + other.real,
            imaginary + other.imaginary
        )

    def -(other: ComplexNumber): ComplexNumber =
        ComplexNumber(
            real - other.real,
            imaginary - other.imaginary
        )

    def *(other: ComplexNumber): ComplexNumber =
        ComplexNumber(
            real * other.real - imaginary * other.imaginary,
            imaginary * other.real + real * other.imaginary
        )

    def /(other: ComplexNumber): ComplexNumber =
        val denominator =
            other.real * other.real + other.imaginary * other.imaginary

        ComplexNumber(
            (real * other.real + imaginary * other.imaginary) / denominator,
            (imaginary * other.real - real * other.imaginary) / denominator
        )

    def conjugate: ComplexNumber =
        ComplexNumber(real, -imaginary)

    def abs: Double =
        Math.sqrt(real * real + imaginary * imaginary)

object ComplexNumber:
    def exp(c: ComplexNumber): ComplexNumber =
        val expReal = Math.exp(c.real)
        ComplexNumber(
            expReal * Math.cos(c.imaginary),
            expReal * Math.sin(c.imaginary)
        )