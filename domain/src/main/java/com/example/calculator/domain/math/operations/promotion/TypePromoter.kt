package com.example.calculator.domain.math.operations.promotion

import com.example.calculator.domain.math.numbers.MathNumber
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext

object TypePromoter {
    fun promoteToCommon(left: MathNumber, right: MathNumber): Pair<MathNumber, MathNumber> {
        if (left::class == right::class) return Pair(left, right)

        if (left is MathNumber.RealNumber || right is MathNumber.RealNumber) {
            return Pair(promoteToReal(left), promoteToReal(right))
        }

        if (left is MathNumber.RationalNumber || right is MathNumber.RationalNumber) {
            return Pair(promoteToRational(left), promoteToRational(right))
        }

        throw IllegalArgumentException("Unknown types for conversion: \${left::class} and \${right::class}")
    }

    private fun promoteToReal(number: MathNumber): MathNumber.RealNumber = when (number) {
        is MathNumber.RealNumber -> number
        is MathNumber.ExactInteger -> MathNumber.RealNumber(BigDecimal(number.value))
        is MathNumber.RationalNumber -> {
            val decimalNumerator = BigDecimal(number.numerator)
            val decimalDenominator = BigDecimal(number.denominator)
            MathNumber.RealNumber(
                decimalNumerator.divide(
                    decimalDenominator,
                    MathContext.DECIMAL128
                )
            )
        }
    }

    private fun promoteToRational(number: MathNumber): MathNumber.RationalNumber = when (number) {
        is MathNumber.RationalNumber -> number
        is MathNumber.ExactInteger -> MathNumber.RationalNumber(
            numerator = number.value,
            denominator = BigInteger.ONE
        )

        is MathNumber.RealNumber -> throw IllegalStateException("Narrowing conversion from Real to Rational is not allowed")
    }
}