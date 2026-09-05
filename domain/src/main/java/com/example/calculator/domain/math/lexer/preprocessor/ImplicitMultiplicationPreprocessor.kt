package com.example.calculator.domain.math.lexer.preprocessor

import com.example.calculator.domain.math.lexer.Token

class ImplicitMultiplicationPreprocessor : TokenPreprocessor {
    override fun process(tokens: List<Token>): List<Token> {
        if (tokens.isEmpty()) return tokens

        if (!requiresProcessing(tokens)) {
            return tokens
        }

        val newTokens = ArrayList<Token>(tokens.size + 5)

        newTokens.add(tokens[0])

        for (i in 1 until tokens.size) {
            val left = tokens[i - 1]
            val right = tokens[i]

            if (requiresMultiplicationBetween(left, right)) {
                newTokens.add(Token.Operator.Multiply)
            }

            newTokens.add(right)
        }

        return newTokens
    }

    private fun requiresProcessing(tokens: List<Token>): Boolean {
        for (i in 1 until tokens.size) {
            if (requiresMultiplicationBetween(tokens[i - 1], tokens[i])) {
                return true
            }
        }
        return false
    }

    private fun requiresMultiplicationBetween(left: Token, right: Token): Boolean {
        return isLeftMultiplier(left) && isRightMultiplier(right)
    }

    private fun isLeftMultiplier(token: Token): Boolean {
        return token is Token.Number ||
                token is Token.Constant ||
                token is Token.Bracket.Right
    }

    private fun isRightMultiplier(token: Token): Boolean {
        return token is Token.Number ||
                token is Token.Constant ||
                token is Token.Function ||
                token is Token.Bracket.Left
    }
}