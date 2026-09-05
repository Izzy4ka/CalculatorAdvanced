package com.example.calculator.domain.math.parser

import com.example.calculator.domain.math.ast.MathConstant
import com.example.calculator.domain.math.ast.MathFunction
import com.example.calculator.domain.math.ast.MathOperation
import com.example.calculator.domain.math.lexer.Token

fun Token.Operator.toDomain(): MathOperation = when (this) {
    is Token.Operator.Plus -> MathOperation.PLUS
    Token.Operator.Divide -> MathOperation.DIVIDE
    Token.Operator.Minus -> MathOperation.MINUS
    Token.Operator.Multiply -> MathOperation.MULTIPLY
    Token.Operator.Power -> MathOperation.POWER
}

fun Token.Function.toDomain(): MathFunction = when (this) {
    Token.Function.SIN -> MathFunction.SIN
    Token.Function.COS -> MathFunction.COS
    Token.Function.TAN -> MathFunction.TAN
    Token.Function.LOG -> MathFunction.LOG
    Token.Function.LN -> MathFunction.LN
    Token.Function.SQRT -> MathFunction.SQRT
}

fun Token.Constant.toDomain(): MathConstant = when (this) {
    Token.Constant.PI -> MathConstant.PI
    Token.Constant.E -> MathConstant.E
}