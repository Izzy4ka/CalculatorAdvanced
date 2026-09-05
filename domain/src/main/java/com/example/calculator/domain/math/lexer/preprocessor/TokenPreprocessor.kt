package com.example.calculator.domain.math.lexer.preprocessor

import com.example.calculator.domain.math.lexer.Token

interface TokenPreprocessor {
    fun process(tokens: List<Token>): List<Token>
}