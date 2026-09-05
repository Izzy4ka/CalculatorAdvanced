package com.example.calculator.domain.math.parser

import com.example.calculator.domain.math.ast.ExpressionNode
import com.example.calculator.domain.math.ast.MathConstant
import com.example.calculator.domain.math.ast.MathFunction
import com.example.calculator.domain.math.ast.MathOperation
import com.example.calculator.domain.math.lexer.Token
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ExpressionParserImplTest {

    private val parser: ExpressionParser = ExpressionParserImpl()

    @Test
    fun parse_singleNumber_returnsNumberNode() {
        val tokens = listOf(Token.Number("5.0"))
        val expectedAst = ExpressionNode.NumberNode(MathNumberResolver.resolve("5.0"))

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_simpleAddition_returnsBinaryNode() {
        val tokens = listOf(
            Token.Number("2.0"),
            Token.Operator.Plus,
            Token.Number("3.0")
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.NumberNode(MathNumberResolver.resolve("2.0")),
            operator = MathOperation.PLUS,
            right = ExpressionNode.NumberNode(MathNumberResolver.resolve("3.0"))
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_multiplicationAndAddition_buildsCorrectPriorityTree() {
        val tokens = listOf(
            Token.Number("2.0"),
            Token.Operator.Plus,
            Token.Number("3.0"),
            Token.Operator.Multiply,
            Token.Number("4.0")
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.NumberNode(MathNumberResolver.resolve("2.0")),
            operator = MathOperation.PLUS,
            right = ExpressionNode.BinaryNode(
                left = ExpressionNode.NumberNode(MathNumberResolver.resolve("3.0")),
                operator = MathOperation.MULTIPLY,
                right = ExpressionNode.NumberNode(MathNumberResolver.resolve("4.0"))
            )
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_missingRightBracket_throwsIllegalArgumentException() {
        val tokens = listOf(
            Token.Number("2.0"),
            Token.Operator.Plus,
            Token.Number("3.0"),
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
            Token.Number("2.0"),
            Token.Operator.Plus,
            Token.Number("3.0"),
            Token.Bracket.Right,
            Token.Operator.Multiply,
            Token.Number("4.0")
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.BinaryNode(
                left = ExpressionNode.NumberNode(MathNumberResolver.resolve("2.0")),
                operator = MathOperation.PLUS,
                right = ExpressionNode.NumberNode(MathNumberResolver.resolve("3.0"))
            ),
            operator = MathOperation.MULTIPLY,
            right = ExpressionNode.NumberNode(MathNumberResolver.resolve("4.0"))
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_PowerAndUnary_buildCorrectPriorityTree() {
        val tokens = listOf(
            Token.Operator.Minus,
            Token.Number("2.0"),
            Token.Operator.Power,
            Token.Number("3.0")
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.UnaryNode(
                operator = MathOperation.MINUS,
                operand = ExpressionNode.NumberNode(MathNumberResolver.resolve("2.0"))
            ),
            operator = MathOperation.POWER,
            right = ExpressionNode.NumberNode(MathNumberResolver.resolve("3.0"))
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_constantToken_returnsNumberNodeWithResolvedConstant() {
        val tokens = listOf(Token.Constant.PI)
        val expectedAst =
            ExpressionNode.NumberNode(MathNumberResolver.resolveConstant(MathConstant.PI))

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_scientificNotationSequence_buildsCorrectAst() {
        val tokens = listOf(
            Token.Number("15"),
            Token.Operator.Multiply,
            Token.Number("10"),
            Token.Operator.Power,
            Token.Operator.Minus,
            Token.Number("3")
        )

        val expectedAst = ExpressionNode.BinaryNode(
            left = ExpressionNode.NumberNode(MathNumberResolver.resolve("15")),
            operator = MathOperation.MULTIPLY,
            right = ExpressionNode.BinaryNode(
                left = ExpressionNode.NumberNode(MathNumberResolver.resolve("10")),
                operator = MathOperation.POWER,
                right = ExpressionNode.UnaryNode(
                    operator = MathOperation.MINUS,
                    operand = ExpressionNode.NumberNode(MathNumberResolver.resolve("3"))
                )
            )
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }

    @Test
    fun parse_unexpectedTokenAtEnd_throwsException() {
        val tokens = listOf(
            Token.Number("2.0"),
            Token.Operator.Plus,
            Token.Number("3.0"),
            Token.Operator.Multiply
        )

        assertThrows(IllegalArgumentException::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun parse_functionWithConstantArgument_buildsCorrectAst() {
        val tokens = listOf(
            Token.Function.SIN,
            Token.Bracket.Left,
            Token.Constant.PI,
            Token.Bracket.Right
        )

        val expectedAst = ExpressionNode.FunctionNode(
            function = MathFunction.SIN,
            argument = ExpressionNode.NumberNode(MathNumberResolver.resolveConstant(MathConstant.PI))
        )

        val actualAst = parser.parse(tokens)

        assertEquals(expectedAst, actualAst)
    }
}