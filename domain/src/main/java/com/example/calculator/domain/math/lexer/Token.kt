package com.example.calculator.domain.math.lexer

sealed interface Token {
    data class Number(val value: Double) : Token

    sealed interface Operator : Token {
        data object Plus : Operator
        data object Minus : Operator
        data object Multiply : Operator
        data object Divide : Operator
        data object Power : Operator
    }

    sealed interface Bracket : Token {
        data object Left : Bracket
        data object Right : Bracket
    }

    enum class Function : Token {
        SIN, COS, TAN, LOG, LN, SQRT;
    }
}