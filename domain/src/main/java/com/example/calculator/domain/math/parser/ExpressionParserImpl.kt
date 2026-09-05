package com.example.calculator.domain.math.parser

import com.example.calculator.domain.math.ast.ExpressionNode
import com.example.calculator.domain.math.lexer.Token

class ExpressionParserImpl : ExpressionParser {
    private var tokens: List<Token> = emptyList()
    private var position: Int = 0

    override fun parse(tokens: List<Token>): ExpressionNode {
        this.tokens = tokens
        this.position = 0

        val result = parseExpression()

        if (position < tokens.size) {
            throw IllegalArgumentException("Unexpected token after expression: ${peek()}")
        }

        return result
    }

    private fun peek(): Token? {
        if (position >= tokens.size) return null
        return tokens[position]
    }

    private fun parseExpression(): ExpressionNode {
        return parseAddition()
    }

    private fun parsePrimary(): ExpressionNode {
        val numberToken = match<Token.Number>()
        if (numberToken != null) {
            val result = MathNumberResolver.resolve(numberToken.value)
            return ExpressionNode.NumberNode(result)
        }

        val leftBracket = match<Token.Bracket.Left>()

        if (leftBracket != null) {
            val innerNode = parseExpression()

            match<Token.Bracket.Right>()
                ?: throw IllegalArgumentException("Expected closing parenthesis ')'")

            return innerNode
        }

        val functionToken = match<Token.Function>()

        if (functionToken != null) {
            match<Token.Bracket.Left>()
                ?: throw IllegalArgumentException("Expected opening parenthesis '('")
            val innerNode = parseExpression()
            match<Token.Bracket.Right>()
                ?: throw IllegalArgumentException("Expected closing parenthesis ')'")
            return ExpressionNode.FunctionNode(functionToken.toDomain(), innerNode)
        }

        val constantToken = match<Token.Constant>()

        if (constantToken != null) {
            val result = MathNumberResolver.resolveConstant(constantToken.toDomain())
            return ExpressionNode.NumberNode(result)
        }

        throw IllegalArgumentException("Expected a number or opening parenthesis, but found: ${peek()}")
    }

    private fun parseMultiplication(): ExpressionNode {
        var left = parsePower()
        while (true) {
            val operator = match<Token.Operator.Multiply>()
                ?: match<Token.Operator.Divide>()
                ?: break

            val right = parsePower()
            left = ExpressionNode.BinaryNode(left, operator.toDomain(), right)
        }

        return left
    }

    private fun parseAddition(): ExpressionNode {
        var left = parseMultiplication()

        while (true) {
            val operator = match<Token.Operator.Plus>()
                ?: match<Token.Operator.Minus>()
                ?: break

            val right = parseMultiplication()
            left = ExpressionNode.BinaryNode(left, operator.toDomain(), right)
        }

        return left
    }

    private fun parsePower(): ExpressionNode {
        val left = parseUnary()

        val operator = match<Token.Operator.Power>()
        if (operator != null) {
            val right = parsePower()
            return ExpressionNode.BinaryNode(left, operator.toDomain(), right)
        }

        return left
    }

    private fun parseUnary(): ExpressionNode {
        val operator =
            match<Token.Operator.Minus>() ?: match<Token.Operator.Plus>() ?: return parsePrimary()
        val operand = parseUnary()
        return ExpressionNode.UnaryNode(operator.toDomain(), operand)
    }


    private inline fun <reified T : Token> match(): T? {
        val currentToken = peek()
        if (currentToken is T) {
            position++
            return currentToken
        }
        return null
    }
}
