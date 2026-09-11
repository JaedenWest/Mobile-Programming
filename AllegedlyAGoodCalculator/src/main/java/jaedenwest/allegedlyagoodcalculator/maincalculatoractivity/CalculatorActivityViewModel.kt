package jaedenwest.allegedlyagoodcalculator.maincalculatoractivity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import jaedenwest.allegedlyagoodcalculator.CalculatorModel
import jaedenwest.allegedlyagoodcalculator.OperatorT
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow


/**
 * The ViewModel layer for the calculator application
 * Coordinates between both the view and the calculator math operations
 * The class maintains the calculations state and also handlers user interactions like button clicks
 */
class CalculatorActivityViewModel: ViewModel() {
    //Core mathematical model for calculations
    private val calculatorModel: CalculatorModel = CalculatorModel()
    //backing state for the result displayed
    private val _result:MutableState<String> = mutableStateOf("0")
    //public read only state of the current calculation, we use _result to be able to change
    val result: State<String> = _result

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    /**
     * Handles logic for when a mathematical operator button is pressed
     * Manages operand updates and a running total
     */
    suspend fun onOperatorClicked(op: OperatorT) {
        // 1. If we are already showing a result (e.g., "10 + 5 = 15"),
        // we just need to update the operator and keep going.
        if (_result.value.contains("=")) {
            calculatorModel.operator = op
            _result.value = "0"
            return
        }

        // 2. If an operator is already set, calculate the running total now
        if (calculatorModel.operator != OperatorT.NONE) {
            calculatorModel.operand2 = _result.value.toFloatOrNull() ?: 0.0f
            val intermediateResult = calculatorModel.doCalculation()

            if (intermediateResult != null) {
                calculatorModel.operand1 = intermediateResult
                _toastEvent.emit("Running total: $intermediateResult")
            } else {
                _result.value = "Undefined"
                return
            }
        } else {
            calculatorModel.operand1 = _result.value.toFloatOrNull() ?: 0.0f
        }

        // Set the new operator and clear the screen for the next number
        calculatorModel.operator = op
        _result.value = "0"
    }

    //If the clear button is clicked how to clear the screen
    //As well as clearing the actual state
    fun onClearClicked() {
        _result.value = "0"

        calculatorModel.operand1 = 0.0f
        calculatorModel.operand2 = 0.0f
        calculatorModel.operator = OperatorT.NONE

    }

    //Triggers the final calculation as well as formatting the output to be smaller if needed
    //Handles specific formatting for scientific notation
    suspend fun onEqualClicked() {
        calculatorModel.operand2 = _result.value.toFloatOrNull() ?: 0.0f
        val calcResult: Float? = calculatorModel.doCalculation()
        //properly format the result string
        val formattedResult = when {
            calcResult == null -> "Undefined"
            //test 6 scientific notation
            calcResult >= 1e7f || calcResult <= -1e7f -> "%.2e".format(calcResult) // scientific notation
            //clean whole numbers
            calcResult % 1.0f == 0.0f -> calcResult.toInt().toString() // making floats clean when possible
            else -> "%.2f".format(calcResult).trimEnd('0').trimEnd('.')

        }
        if(calcResult == null){
            _result.value = "Undefined"
            _toastEvent.emit("Error: Divide by 0")
        }else {
            val opSymbol = calculatorModel.operator.symbol
            _result.value = "${calculatorModel.operand1} $opSymbol ${calculatorModel.operand2}\n = $formattedResult"
            calculatorModel.operand1 = calcResult
        }
    }

    /**
     * Processes clicks for digits, decimal points, and other buttons such as backspace
     */
    fun onNumberClicked(symbol: String) {
        val isShowingResult = _result.value.contains("=")

        //Handles multiple decimal points
        if (symbol == "." && (_result.value.contains(".") && !isShowingResult)) {
            return
        }
        //Toggle plus minus sign
        if (symbol == "+/-") {
            if (_result.value != "0" && !isShowingResult && _result.value != "Undefined") {
                _result.value = if (_result.value.startsWith("-")) {
                    _result.value.removePrefix("-")
                } else {
                    "-" + _result.value
                }
            }
            return
        }

        // Backspace button
        if (symbol == "<-") {
            if (_result.value == "Undefined" || isShowingResult) {
                _result.value = "0"
            } else {
                _result.value = if (_result.value.length > 1) _result.value.dropLast(1) else "0"
            }
            return
        }

        // Handle numbers and decimal point placement
        if (isShowingResult || _result.value == "0") {
            _result.value = if (symbol == ".") "0." else symbol
        } else {
            _result.value += symbol
        }
    }

}