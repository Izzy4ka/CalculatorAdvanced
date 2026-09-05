package com.example.calculator.domain.math.parser

import com.example.calculator.domain.math.ast.MathConstant
import com.example.calculator.domain.math.numbers.MathNumber
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext

object MathNumberResolver {
    private val MATH_CONTEXT = MathContext.DECIMAL128

    fun resolve(rawValue: String): MathNumber {
        val isReal = rawValue.contains('.') || rawValue.contains('e', ignoreCase = true)

        return if (isReal) {
            MathNumber.RealNumber(BigDecimal(rawValue, MATH_CONTEXT))
        } else {
            MathNumber.ExactInteger(BigInteger(rawValue))
        }
    }

    fun resolveConstant(constant: MathConstant): MathNumber {
        return when (constant) {
            MathConstant.PI -> MathNumber.RealNumber(
                BigDecimal("3.1415926535897932384626433832795028841971693993751", MATH_CONTEXT)
            )
            MathConstant.E -> MathNumber.RealNumber(
                BigDecimal("2.7182818284590452353602874713526624977572470936999", MATH_CONTEXT)
            )
        }
    }
}