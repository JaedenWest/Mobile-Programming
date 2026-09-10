package edu.uark.ahnelson.allegedlyabadcalculator

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
        calculatorModel.operand1 = 23
        calculatorModel.operand2 = 45
        calculatorModel.operator = OperatorT.PLUS
        val result = calculatorModel.doCalculation()

        assertEquals(68, result)
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_minus() {
        calculatorModel.operand1 = 50
        calculatorModel.operand2 = 20
        calculatorModel.operator = OperatorT.MINUS
        val result = calculatorModel.doCalculation()

        assertEquals(30, result)
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_times() {
        calculatorModel.operand1 = 6
        calculatorModel.operand2 = 7
        calculatorModel.operator = OperatorT.TIMES
        val result = calculatorModel.doCalculation()

        assertEquals(42, result)
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_divide() {
        calculatorModel.operand1 = 20
        calculatorModel.operand2 = 4
        calculatorModel.operator = OperatorT.DIVIDE
        val result = calculatorModel.doCalculation()

        assertEquals(5, result)
    }

    @org.junit.jupiter.api.Test
    fun doCalculation_divideByZero() {
        calculatorModel.operand1 = 10
        calculatorModel.operand2 = 0
        calculatorModel.operator = OperatorT.DIVIDE
        val result = calculatorModel.doCalculation()

        assertNull(result)
    }

}