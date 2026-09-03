package com.example.calculator.domain.math.ast

import com.example.calculator.domain.math.lexer.Token

sealed interface ExpressionNode {
    data class NumberNode(val number: Double) : ExpressionNode
    data class BinaryNode(
        val left: ExpressionNode,
        val operator: Token.Operator,
        val right: ExpressionNode,
    ) : ExpressionNode

    data class UnaryNode(val operator: Token.Operator, val operand: ExpressionNode) :
        ExpressionNode

    data class FunctionNode(val function: Token.Function, val argument: ExpressionNode) :
        ExpressionNode
}
