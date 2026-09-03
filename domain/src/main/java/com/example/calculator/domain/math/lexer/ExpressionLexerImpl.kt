package com.example.calculator.domain.math.lexer

class ExpressionLexerImpl : ExpressionLexer {
    override fun tokenize(expression: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var position = 0
        val builder = StringBuilder()

        while (position < expression.length) {
            val currentChar = expression[position]
            when {
                currentChar.isWhitespace() -> {
                    position++
                }

                currentChar.isDigit() || currentChar == '.' -> {
                    var firstFindDot = false
                    while (position < expression.length && (expression[position].isDigit() || expression[position] == '.')) {
                        if (expression[position] == '.' && firstFindDot) {
                            throw IllegalArgumentException("Invalid number format: $expression")
                        }

                        if (expression[position] == '.') {
                            firstFindDot = true
                        }

                        builder.append(expression[position])
                        position++
                    }

                    val number = builder.toString().toDouble()
                    tokens.add(Token.Number(number))
                    builder.clear()
                }

                currentChar.isLetter() -> {
                    while (position < expression.length && expression[position].isLetter()) {
                        builder.append(expression[position])
                        position++
                    }

                    val result = builder.toString()

                    val functionToken = Token.Function.entries.find {
                        it.name.equals(result, ignoreCase = true)
                    }

                    if (functionToken != null) {
                        tokens.add(functionToken)
                    } else {
                        throw IllegalArgumentException("Unknown function: $result")
                    }

                    builder.clear()
                }

                currentChar == '+' -> {
                    tokens.add(Token.Operator.Plus)
                    position++
                }

                currentChar == '-' -> {
                    tokens.add(Token.Operator.Minus)
                    position++
                }

                currentChar == '*' -> {
                    tokens.add(Token.Operator.Multiply)
                    position++
                }

                currentChar == '/' -> {
                    tokens.add(Token.Operator.Divide)
                    position++
                }

                currentChar == '^' -> {
                    tokens.add(Token.Operator.Power)
                    position++
                }

                currentChar == '(' -> {
                    tokens.add(Token.Bracket.Left)
                    position++
                }

                currentChar == ')' -> {
                    tokens.add(Token.Bracket.Right)
                    position++
                }

                else -> {
                    throw IllegalArgumentException("Unknown character: $currentChar at position $position")
                }
            }
        }

        return tokens
    }
}