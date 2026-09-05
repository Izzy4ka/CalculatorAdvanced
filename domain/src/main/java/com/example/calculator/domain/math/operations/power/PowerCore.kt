package com.example.calculator.domain.math.operations.power

import com.example.calculator.domain.math.numbers.MathNumber

interface PowerCore<T : MathNumber> {
    fun pow(base: T, exponent: T): MathNumber
    fun sqrt(value: T): MathNumber
}