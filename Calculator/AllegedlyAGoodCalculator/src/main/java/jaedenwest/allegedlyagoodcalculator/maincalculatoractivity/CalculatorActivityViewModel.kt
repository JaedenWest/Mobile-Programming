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

    private var isResultShown = false

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    /**
     * Helper to format numbers for display, removing unnecessary .0
     */
    private fun formatNumber(value: Float): String {
        return if (value % 1.0f == 0.0f) value.toInt().toString() else value.toString()
    }

    /**
     * Handles logic for when a mathematical operator button is pressed
     * Manages operand updates and a running total
     */
    suspend fun onOperatorClicked(op: OperatorT) {
        val currentText = _result.value
        isResultShown = false

        // 1. If we are already showing a result (e.g., "15"),
        // use it as the new operand1 and start a new expression.
        if (currentText.contains("=") || calculatorModel.operator == OperatorT.NONE && currentText != "0") {
            val baseValue = if (currentText.contains("=")) {
                currentText.substringAfter("=").trim()
            } else {
                currentText
            }
            calculatorModel.operand1 = baseValue.toFloatOrNull() ?: 0f
            calculatorModel.operator = op
            _result.value = "${formatNumber(calculatorModel.operand1)} ${op.symbol} "
            return
        }

        // 2. If an operator is already set, calculate the running total now
        if (calculatorModel.operator != OperatorT.NONE) {
            val parts = currentText.split(" ")
            if (parts.size >= 3) {
                calculatorModel.operand2 = parts[2].toFloatOrNull() ?: 0.0f
                val intermediateResult = calculatorModel.doCalculation()

                if (intermediateResult != null) {
                    calculatorModel.operand1 = intermediateResult
                    calculatorModel.operator = op
                    val formatted = formatNumber(intermediateResult)
                    _result.value = "$formatted ${op.symbol} "
                    _toastEvent.emit("Running total: $formatted")
                } else {
                    _result.value = "Undefined"
                }
            } else {
                // Just changing the operator (e.g., "7 + " to "7 * ")
                calculatorModel.operator = op
                val operand1Formatted = formatNumber(calculatorModel.operand1)
                _result.value = "$operand1Formatted ${op.symbol} "
            }
        }
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
        val currentText = _result.value
        if (calculatorModel.operator == OperatorT.NONE) return

        val parts = currentText.split(" ")
        if (parts.size >= 3) {
            calculatorModel.operand2 = parts[2].toFloatOrNull() ?: 0.0f
        } else {
            // If only one operand and operator (e.g., "7 + "), use operand1 as operand2
            calculatorModel.operand2 = calculatorModel.operand1
        }

        val calcResult: Float? = calculatorModel.doCalculation()
        //properly format the result string
        val formattedResult = when {
            calcResult == null -> "Undefined"
            calcResult >= 1e7f || calcResult <= -1e7f -> "%.2e".format(calcResult)
            calcResult % 1.0f == 0.0f -> calcResult.toInt().toString()
            else -> "%.2f".format(calcResult).trimEnd('0').trimEnd('.')
        }

        if (calcResult == null) {
            _result.value = "Undefined"
            _toastEvent.emit("Error: Divide by 0")
        } else {
            // Update display to just the result to satisfy the test expectations
            _result.value = formattedResult
            calculatorModel.operand1 = calcResult
            calculatorModel.operator = OperatorT.NONE
            isResultShown = true
        }
    }

    /**
     * Processes clicks for digits, decimal points, and other buttons such as backspace
     */
    fun onNumberClicked(symbol: String) {
        val currentText = _result.value
        val hasOperator = calculatorModel.operator != OperatorT.NONE
        val parts = currentText.split(" ")

        // If we just finished a calculation and a number is clicked, start fresh
        if (isResultShown && symbol != "." && symbol != "+/-" && symbol != "<-") {
             _result.value = symbol
             isResultShown = false
             return
        }

        // Toggle plus minus sign
        if (symbol == "+/-") {
            if (currentText == "0" || currentText == "Undefined") return
            
            if (!hasOperator) {
                _result.value = if (currentText.startsWith("-")) currentText.removePrefix("-") else "-$currentText"
            } else if (parts.size >= 3) {
                val operand2 = parts[2]
                val toggled = if (operand2.startsWith("-")) operand2.removePrefix("-") else "-$operand2"
                _result.value = "${parts[0]} ${parts[1]} $toggled"
            }
            return
        }

        // Backspace button
        if (symbol == "<-") {
            if (currentText == "Undefined" || currentText == "0") {
                _result.value = "0"
            } else if (currentText.endsWith(" ")) {
                // Remove operator and space: "7 + " -> "7"
                _result.value = parts[0]
                calculatorModel.operator = OperatorT.NONE
            } else {
                _result.value = if (currentText.length > 1) currentText.dropLast(1) else "0"
            }
            return
        }

        // Decimal point logic
        if (symbol == ".") {
            if (!hasOperator) {
                if (!currentText.contains(".")) _result.value += "."
            } else {
                if (parts.size < 3) {
                    _result.value += "0."
                } else if (!parts[2].contains(".")) {
                    _result.value += "."
                }
            }
            return
        }

        // Handle numbers
        if (currentText == "0") {
            _result.value = symbol
        } else {
            _result.value += symbol
        }
    }

}