package com.example.calculator.domain.math.parser

import com.example.calculator.domain.math.ast.ExpressionNode
import com.example.calculator.domain.math.lexer.Token

interface ExpressionParser {
    fun parse(tokens: List<Token>): ExpressionNode
}