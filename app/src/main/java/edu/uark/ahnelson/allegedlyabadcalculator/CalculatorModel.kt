package edu.uark.ahnelson.allegedlyabadcalculator

import kotlinx.coroutines.flow.MutableStateFlow

enum class OperatorT(val symbol: String){
    NONE(""),
    PLUS("+"),
    MINUS("-"),
    TIMES("x"),
    DIVIDE("/")
}
class CalculatorModel {
    var operand1: Float = 0.0f
    var operand2: Float = 0.0f
    var operator: OperatorT = OperatorT.NONE


    fun doCalculation(): Float? {
        when(operator){
            OperatorT.NONE -> return null
            OperatorT.PLUS -> return operand1+operand2
            OperatorT.MINUS -> return operand1-operand2
            OperatorT.TIMES -> return operand1*operand2
            OperatorT.DIVIDE -> {
                if(operand2 == 0.0f){
                    //TODO Handle Divide by 0
                    return null;
                }else{
                    return operand1/operand2
                }
            }
        }
    }
}
