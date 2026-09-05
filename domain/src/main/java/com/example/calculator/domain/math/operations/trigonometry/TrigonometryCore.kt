package com.example.calculator.domain.math.operations.trigonometry

import com.example.calculator.domain.math.numbers.MathNumber

interface TrigonometryCore<T : MathNumber> {
    fun sin(value: T): MathNumber.RealNumber
    fun cos(value: T): MathNumber.RealNumber
    fun tan(value: T): MathNumber.RealNumber
    fun cot(value: T): MathNumber.RealNumber
}
