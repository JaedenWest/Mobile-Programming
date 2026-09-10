package edu.uark.ahnelson.allegedlyabadcalculator.MainCalculatorActivity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import edu.uark.ahnelson.allegedlyabadcalculator.CalculatorModel
import edu.uark.ahnelson.allegedlyabadcalculator.OperatorT
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CalculatorActivityViewModel: ViewModel() {
    private val calculatorModel: CalculatorModel = CalculatorModel()

    private val _result:MutableState<String> = mutableStateOf("0")
    val result: State<String> = _result

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()


    suspend fun onOperatorClicked(op: OperatorT) {
        // 1. If we are already showing a result (e.g., "10 + 5 = 15"),
        // we just need to update the operator and keep going.
        if (_result.value.contains("=")) {
            calculatorModel.operator = op
            _result.value = "0"
            return
        }

        // 2. If an operator is ALREADY set, calculate the running total now
        // Example: User typed 10 + 5 and just hit 'x'
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
            // 3. This is the very first operator (+, -, etc.)
            calculatorModel.operand1 = _result.value.toFloatOrNull() ?: 0.0f
        }

        // Set the new operator and clear the screen for the next number
        calculatorModel.operator = op
        _result.value = "0"
    }

    suspend fun onClearClicked() {
        _result.value = "0"

        calculatorModel.operand1 = 0.0f
        calculatorModel.operand2 = 0.0f
        calculatorModel.operator = OperatorT.NONE

    }

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

    suspend fun onNumberClicked(symbol: String) {
        val isShowingResult = _result.value.contains("=")

        // Handle specific symbols first
        if (symbol == "." && (_result.value.contains(".") && !isShowingResult)) {
            return
        }

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