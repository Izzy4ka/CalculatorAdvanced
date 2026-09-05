package com.example.calculator.domain.math.parser

import com.example.calculator.domain.math.ast.MathConstant
import com.example.calculator.domain.math.numbers.MathNumber
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

class MathNumberResolverTest {
    @Test
    fun resolve_stringWithOnlyDigits_returnsExactInteger() {
        val tokens = "12345"

        val expectedNumber = MathNumber.ExactInteger(BigInteger("12345"))

        val actual = MathNumberResolver.resolve(tokens)

        assertEquals(expectedNumber, actual)
    }

    @Test
    fun resolve_stringWithDot_returnsRealNumber() {
        val tokens = ".5"

        val expectedNumber = MathNumber.RealNumber(BigDecimal("0.5"))

        val actual = MathNumberResolver.resolve(tokens)

        assertEquals(expectedNumber, actual)
    }

    @Test
    fun resolve_domainConstantPI_returnsRealNumberWithHighPrecision() {
        val tokens = MathConstant.PI
        val expectedNumber =
            MathNumber.RealNumber(BigDecimal("3.141592653589793238462643383279503"))
        val actual = MathNumberResolver.resolveConstant(tokens)
        assertEquals(expectedNumber, actual)
    }
}