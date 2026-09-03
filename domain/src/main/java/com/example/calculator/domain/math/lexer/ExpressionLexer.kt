package com.example.calculator.domain.math.lexer

interface ExpressionLexer {
    fun tokenize(expression: String): List<Token>
}