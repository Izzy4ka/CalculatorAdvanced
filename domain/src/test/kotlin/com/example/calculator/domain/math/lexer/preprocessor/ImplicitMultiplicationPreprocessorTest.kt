package com.example.calculator.domain.math.lexer.preprocessor

import com.example.calculator.domain.math.lexer.Token
import org.junit.Assert.assertEquals
import org.junit.Test

class ImplicitMultiplicationPreprocessorTest {

    private val preprocessor = ImplicitMultiplicationPreprocessor()

    @Test
    fun process_noImplicitMultiplication_returnsOriginalList() {
        val tokens = listOf(
            Token.Number("1"),
            Token.Operator.Plus,
            Token.Number("1")
        )

        val result = preprocessor.process(tokens)

        assert(result == tokens)
    }

    @Test
    fun process_emptyList_returnsEmptyList() {
        val tokens = emptyList<Token>()

        val result = preprocessor.process(tokens)

        assert(result.isEmpty())
    }

    @Test
    fun process_numberAndConstant_insertsMultiply() {
        val tokens = listOf(
            Token.Number("2"),
            Token.Constant.PI
        )

        val expectedTokens = listOf(
            Token.Number("2"),
            Token.Operator.Multiply,
            Token.Constant.PI
        )

        val result = preprocessor.process(tokens)

        assertEquals(expectedTokens, result)
    }

    @Test
    fun process_constantAndFunction_insertsMultiply() {
        val tokens = listOf(
            Token.Constant.PI,
            Token.Function.SIN,
            Token.Bracket.Left,
            Token.Number("2"),
            Token.Bracket.Right
        )

        val expectedTokens = listOf(
            Token.Constant.PI,
            Token.Operator.Multiply,
            Token.Function.SIN,
            Token.Bracket.Left,
            Token.Number("2"),
            Token.Bracket.Right
        )

        val result = preprocessor.process(tokens)

        assertEquals(expectedTokens, result)
    }

    @Test
    fun process_rightBracketAndLeftBracket_insertsMultiply() {
        val tokens = listOf(
            Token.Bracket.Right,
            Token.Bracket.Left
        )

        val expectedTokens = listOf(
            Token.Bracket.Right,
            Token.Operator.Multiply,
            Token.Bracket.Left
        )

        val result = preprocessor.process(tokens)

        assertEquals(expectedTokens, result)
    }

    @Test
    fun process_multipleImplicitMultiplications_insertsMultipleMultiply() {
        val tokens = listOf(
            Token.Number("2"),
            Token.Constant.PI,
            Token.Bracket.Left,
            Token.Number("3"),
            Token.Bracket.Right
        )

        val expectedTokens = listOf(
            Token.Number("2"),
            Token.Operator.Multiply,
            Token.Constant.PI,
            Token.Operator.Multiply,
            Token.Bracket.Left,
            Token.Number("3"),
            Token.Bracket.Right
        )

        val result = preprocessor.process(tokens)

        assertEquals(expectedTokens, result)
    }
}