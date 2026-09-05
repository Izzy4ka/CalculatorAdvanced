package com.example.calculator.domain.math.numbers

import java.math.BigDecimal
import java.math.BigInteger

sealed interface MathNumber {
    data class ExactInteger(val value: BigInteger) : MathNumber

    data class RationalNumber(
        val numerator: BigInteger,
        val denominator: BigInteger
    ) : MathNumber

    data class RealNumber(val value: BigDecimal) : MathNumber
}