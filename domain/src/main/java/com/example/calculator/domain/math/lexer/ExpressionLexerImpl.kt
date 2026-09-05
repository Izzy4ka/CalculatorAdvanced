package com.example.calculator.domain.math.lexer

class ExpressionLexerImpl : ExpressionLexer {
    private val functionNames = Token.Function.entries.map { it.name.lowercase() }.toTypedArray()
    private val functionTokens = Token.Function.entries.toTypedArray()

    private val constantNames = Token.Constant.entries.map { it.name.lowercase() }.toTypedArray()
    private val constantTokens = Token.Constant.entries.toTypedArray()

    override fun tokenize(expression: String): List<Token> {
        val tokens = ArrayList<Token>(expression.length / 2)
        var position = 0

        while (position < expression.length) {
            val currentChar = expression[position]

            when {
                currentChar.isWhitespace() -> position++

                currentChar == '+' -> {
                    tokens.add(Token.Operator.Plus); position++
                }

                currentChar == '-' -> {
                    tokens.add(Token.Operator.Minus); position++
                }

                currentChar == '*' -> {
                    tokens.add(Token.Operator.Multiply); position++
                }

                currentChar == '/' -> {
                    tokens.add(Token.Operator.Divide); position++
                }

                currentChar == '^' -> {
                    tokens.add(Token.Operator.Power); position++
                }

                currentChar == '(' -> {
                    tokens.add(Token.Bracket.Left); position++
                }

                currentChar == ')' -> {
                    tokens.add(Token.Bracket.Right); position++
                }

                currentChar.isDigit() || currentChar == '.' -> {
                    position = parseNumber(expression, position, tokens)
                }

                currentChar.isLetter() -> {
                    position = parseIdentifier(expression, position, tokens)
                }

                else -> throw IllegalArgumentException("Unknown character: $currentChar at position $position")
            }
        }
        return tokens
    }

    private fun parseNumber(
        expression: String,
        startPosition: Int,
        tokens: MutableList<Token>
    ): Int {
        var position = startPosition
        var hasDot = false

        while (position < expression.length) {
            val ch = expression[position]

            if (ch.isDigit()) {
                position++
            } else if (ch == '.') {
                if (hasDot) throw IllegalArgumentException("Invalid number format (multiple dots)")
                hasDot = true
                position++
            } else if (ch == 'e' || ch == 'E') {
                val isScientific = position + 1 < expression.length &&
                        (expression[position + 1].isDigit() || expression[position + 1] in "+-.")

                if (isScientific) {
                    val baseNumber = expression.substring(startPosition, position)

                    if (baseNumber == ".") {
                        throw IllegalArgumentException("Invalid scientific format: missing base")
                    }

                    tokens.add(Token.Number(baseNumber))
                    tokens.add(Token.Operator.Multiply)
                    tokens.add(Token.Number("10"))
                    tokens.add(Token.Operator.Power)

                    return position + 1
                } else {
                    break
                }
            } else {
                break
            }
        }

        if (position > startPosition) {
            val finalNumber = expression.substring(startPosition, position)

            if (finalNumber == ".") {
                throw IllegalArgumentException("Invalid number format: standalone dot")
            }

            tokens.add(Token.Number(finalNumber))
        }

        return position
    }

    private fun parseIdentifier(
        expression: String,
        startPosition: Int,
        tokens: MutableList<Token>
    ): Int {
        var endPosition = startPosition
        while (endPosition < expression.length && expression[endPosition].isLetter()) {
            endPosition++
        }

        var currentPos = startPosition

        while (currentPos < endPosition) {
            var matchFound = false

            for (i in functionNames.indices) {
                val name = functionNames[i]
                if (expression.regionMatches(currentPos, name, 0, name.length, ignoreCase = true)) {
                    tokens.add(functionTokens[i])
                    currentPos += name.length
                    matchFound = true
                    break
                }
            }

            if (matchFound) continue

            for (i in constantNames.indices) {
                val name = constantNames[i]
                if (expression.regionMatches(currentPos, name, 0, name.length, ignoreCase = true)) {
                    tokens.add(constantTokens[i])
                    currentPos += name.length
                    matchFound = true
                    break
                }
            }

            if (!matchFound) {
                val errorSnippet = expression.substring(currentPos, endPosition)
                throw IllegalArgumentException("Unknown function or constant near: $errorSnippet")
            }
        }

        return endPosition
    }
}