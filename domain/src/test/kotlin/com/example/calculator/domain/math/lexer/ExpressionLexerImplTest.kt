package com.example.calculator.domain.math.lexer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class ExpressionLexerImplTest {
    private lateinit var lexer: ExpressionLexer

    @Before
    fun setUp() {
        lexer = ExpressionLexerImpl()
    }

    @Test
    fun tokenize_simpleAddition_returnsCorrectTokens() {
        val input = "2 + 2"
        val expected = listOf(
            Token.Number("2"),
            Token.Operator.Plus,
            Token.Number("2")
        )

        val actual = lexer.tokenize(input)

        assertEquals(expected, actual)
    }

    @Test
    fun tokenize_decimalNumbersAndSpaces_returnsCorrectTokens() {
        val input = "  12.5   *  0.4 "
        val expected = listOf(
            Token.Number("12.5"),
            Token.Operator.Multiply,
            Token.Number("0.4")
        )

        val actual = lexer.tokenize(input)

        assertEquals(expected, actual)
    }

    @Test
    fun tokenize_functionCaseInsensitive_returnsFunctionToken() {
        val input = "SiN(30)"
        val expected = listOf(
            Token.Function.SIN,
            Token.Bracket.Left,
            Token.Number("30"),
            Token.Bracket.Right
        )

        val actual = lexer.tokenize(input)

        assertEquals(expected, actual)
    }

    @Test
    fun tokenize_twoDotsInNumber_throwsIllegalArgumentException() {
        val input = "12.5.5"

        assertThrows(IllegalArgumentException::class.java) {
            lexer.tokenize(input)
        }
    }

    @Test
    fun tokenize_unknownCharacter_throwsIllegalArgumentException() {
        val input = "2 + $ 3"

        assertThrows(IllegalArgumentException::class.java) {
            lexer.tokenize(input)
        }
    }

    @Test
    fun tokenize_singleDot_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException::class.java) {
            lexer.tokenize(".")
        }
    }

    @Test
    fun tokenize_leadingDot_returnsCorrectNumber() {
        val tokens = lexer.tokenize(".5 + 1")
        assertEquals(Token.Number(".5"), tokens[0])
    }

    @Test
    fun tokenize_emptyOrWhitespace_returnsEmptyList() {
        val tokens = lexer.tokenize("    ")
        assertEquals(emptyList<Token>(), tokens)
    }
}