package com.example.calculator.domain.math.ast

import com.example.calculator.domain.math.numbers.MathNumber

sealed interface ExpressionNode {
    data class NumberNode(val number: MathNumber) : ExpressionNode
    data class BinaryNode(
        val left: ExpressionNode,
        val operator: MathOperation,
        val right: ExpressionNode,
    ) : ExpressionNode

    data class UnaryNode(val operator: MathOperation, val operand: ExpressionNode) :
        ExpressionNode

    data class FunctionNode(val function: MathFunction, val argument: ExpressionNode) :
        ExpressionNode
}
