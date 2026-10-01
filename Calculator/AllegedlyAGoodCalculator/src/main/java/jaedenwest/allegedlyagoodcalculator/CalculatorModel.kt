package jaedenwest.allegedlyagoodcalculator


//enum having mathematical operators, also being associated with a string symbol
enum class OperatorT(val symbol: String){
    NONE(""),
    PLUS("+"),
    MINUS("-"),
    TIMES("x"),
    DIVIDE("/")
}
/**
* The model of the calculator application
* Takes care of the operations, or the calculations and nothing else
*/
class CalculatorModel {

    //first number of the operation
    var operand1: Float = 0.0f

    //second number of the operations
    var operand2: Float = 0.0f

    //the operator to be used in the operation
    var operator: OperatorT = OperatorT.NONE


    //function performing the actual calculation using operand1 and 2
    fun doCalculation(): Float? {
        return when(operator){
            OperatorT.NONE -> null
            OperatorT.PLUS -> operand1+operand2
            OperatorT.MINUS -> operand1-operand2
            OperatorT.TIMES -> operand1*operand2
            OperatorT.DIVIDE -> {
                if(operand2 == 0.0f){
                    null
                }else{
                    operand1/operand2
                }
            }
        }
    }
}
