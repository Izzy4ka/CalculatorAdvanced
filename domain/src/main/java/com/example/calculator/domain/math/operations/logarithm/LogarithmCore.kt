package com.example.calculator.domain.math.operations.logarithm

import com.example.calculator.domain.math.numbers.MathNumber

interface LogarithmCore<T : MathNumber> {
    fun ln(value: T): MathNumber.RealNumber
    fun log(base: T, value: T): MathNumber.RealNumber
}