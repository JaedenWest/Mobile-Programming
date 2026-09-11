package jaedenwest.allegedlyagoodcalculator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class CalculatorModelTest {

    private lateinit var calculatorModel: CalculatorModel

    @BeforeEach
    fun setUp(){
        calculatorModel = CalculatorModel()
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_plus() {
        calculatorModel.operand1 = 23f
        calculatorModel.operand2 = 45f
        calculatorModel.operator = OperatorT.PLUS
        val result = calculatorModel.doCalculation()

        assertEquals(68f, result)
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_minus() {
        calculatorModel.operand1 = 50f
        calculatorModel.operand2 = 20f
        calculatorModel.operator = OperatorT.MINUS
        val result = calculatorModel.doCalculation()

        assertEquals(30f, result)
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_times() {
        calculatorModel.operand1 = 6f
        calculatorModel.operand2 = 7f
        calculatorModel.operator = OperatorT.TIMES
        val result = calculatorModel.doCalculation()

        assertEquals(42f, result)
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_divide() {
        calculatorModel.operand1 = 20f
        calculatorModel.operand2 = 4f
        calculatorModel.operator = OperatorT.DIVIDE
        val result = calculatorModel.doCalculation()

        assertEquals(5f, result)
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_divideByZero() {
        calculatorModel.operand1 = 10f
        calculatorModel.operand2 = 0f
        calculatorModel.operator = OperatorT.DIVIDE
        val result = calculatorModel.doCalculation()

        assertNull(result)
    }

}
