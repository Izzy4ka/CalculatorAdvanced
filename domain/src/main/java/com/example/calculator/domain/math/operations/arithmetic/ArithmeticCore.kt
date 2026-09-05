package com.example.calculator.domain.math.operations.arithmetic

import com.example.calculator.domain.math.numbers.MathNumber

interface ArithmeticCore<T : MathNumber> {
    fun add(left: T, right: T): T
    fun subtract(left: T, right: T): T
    fun multiply(left: T, right: T): T
    fun divide(left: T, right: T): MathNumber
}