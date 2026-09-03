package com.example.calculator.domain.math.parser

import com.example.calculator.domain.math.ast.ExpressionNode
import com.example.calculator.domain.math.lexer.Token
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ExpressionParserImplTest {

    private val parser: ExpressionParser = ExpressionParserImpl()

    @Test
    fun parse_singleNumber_returnsNumberNode() {
        val tokens = listOf(Token.Number(5.0))
        val expectedAst = ExpressionNode.NumberNode(5.0)

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_simpleAddition_returnsBinaryNode() {
        val tokens = listOf(
            Token.Number(2.0),
            Token.Operator.Plus,
            Token.Number(3.0)
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.NumberNode(2.0),
            operator = Token.Operator.Plus,
            right = ExpressionNode.NumberNode(3.0)
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_multiplicationAndAddition_buildsCorrectPriorityTree() {
        val tokens = listOf(
            Token.Number(2.0),
            Token.Operator.Plus,
            Token.Number(3.0),
            Token.Operator.Multiply,
            Token.Number(4.0)
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.NumberNode(2.0),
            operator = Token.Operator.Plus,
            right = ExpressionNode.BinaryNode(
                left = ExpressionNode.NumberNode(3.0),
                operator = Token.Operator.Multiply,
                right = ExpressionNode.NumberNode(4.0)
            )
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_missingRightBracket_throwsIllegalArgumentException() {
        val tokens = listOf(
            Token.Number(2.0),
            Token.Operator.Plus,
            Token.Number(3.0),
            Token.Bracket.Left
        )

        assertThrows(IllegalArgumentException::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun parse_multiplicationAndBracket_buildCorrectPriorityTree() {
        val tokens = listOf(
            Token.Bracket.Left,
            Token.Number(2.0),
            Token.Operator.Plus,
            Token.Number(3.0),
            Token.Bracket.Right,
            Token.Operator.Multiply,
            Token.Number(4.0)
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.BinaryNode(
                left = ExpressionNode.NumberNode(2.0),
                operator = Token.Operator.Plus,
                right = ExpressionNode.NumberNode(3.0)
            ),
            operator = Token.Operator.Multiply,
            right = ExpressionNode.NumberNode(4.0)
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_PowerAndUnary_buildCorrectPriorityTree() {
        val tokens = listOf(
            Token.Operator.Minus,
            Token.Number(2.0),
            Token.Operator.Power,
            Token.Number(3.0)
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.UnaryNode(
                operator = Token.Operator.Minus,
                operand = ExpressionNode.NumberNode(2.0)
            ),
            operator = Token.Operator.Power,
            right = ExpressionNode.NumberNode(3.0)
        )
        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }
}