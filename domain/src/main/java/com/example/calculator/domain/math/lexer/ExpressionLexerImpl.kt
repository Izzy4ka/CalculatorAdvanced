package com.example.calculator.domain.math.lexer

class ExpressionLexerImpl : ExpressionLexer {
    private val functionsMap = Token.Function.entries.associateBy { it.name.lowercase() }
    private val constantsMap = Token.Constant.entries.associateBy { it.name.lowercase() }

    override fun tokenize(expression: String): List<Token> {
        val tokens = mutableListOf<Token>()
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
                    if (baseNumber.isEmpty() || baseNumber == ".") {
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
            tokens.add(Token.Number(expression.substring(startPosition, position)))
        }

        return position
    }

    private fun parseIdentifier(
        expression: String,
        startPosition: Int,
        tokens: MutableList<Token>
    ): Int {
        var position = startPosition
        while (position < expression.length && expression[position].isLetter()) {
            position++
        }

        val word = expression.substring(startPosition, position).lowercase()

        val token = functionsMap[word] ?: constantsMap[word]

        if (token != null) {
            tokens.add(token)
        } else {
            throw IllegalArgumentException("Unknown function or constant: $word")
        }

        return position
    }
}