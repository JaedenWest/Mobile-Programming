package edu.uark.ahnelson.allegedlyabadcalculator

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule // v2 non-deprecated import
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import edu.uark.ahnelson.allegedlyabadcalculator.MainCalculatorActivity.CalculatorActivity
import org.junit.Rule
import org.junit.Test

class CalculatorActivityTest {

    // Uses the v2 createAndroidComposeRule factory
    @get:Rule
    val composeTestRule = createAndroidComposeRule<CalculatorActivity>()

    @Test
    fun testKeypadAdditionFlow() {
        // Tap '7'
        composeTestRule.onNodeWithText("7").performClick()

        // Tap '+'
        composeTestRule.onNodeWithText("+").performClick()

        // Tap '5'
        composeTestRule.onNodeWithText("5").performClick()

        // Verify intermediate display string before pressing '='
        composeTestRule.onNodeWithText("7 + 5").assertIsDisplayed()

        // Tap '='
        composeTestRule.onNodeWithText("=").performClick()

        // Assert evaluated result is displayed
        composeTestRule.onNode(hasTestTag("calculator_display") and hasText("12"))
            .assertIsDisplayed()
    }

    @Test
    fun testClearButtonResetsDisplay() {
        // Build active expression: '9', 'x', '3'
        composeTestRule.onNodeWithText("9").performClick()
        composeTestRule.onNodeWithText("x").performClick()
        composeTestRule.onNodeWithText("3").performClick()

        // Verify active expression on screen
        composeTestRule.onNodeWithText("9 x 3").assertIsDisplayed()

        // Tap Clear 'C'
        composeTestRule.onNodeWithText("C").performClick()

        // Assert screen resets to initial state ('0')
        composeTestRule.onNode(hasTestTag("calculator_display") and hasText("0"))
            .assertIsDisplayed()
    }

    @Test
    fun testAllNumberInputs() {
        // Enter '12'
        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("2").performClick()
        composeTestRule.onNodeWithText("3").performClick()
        composeTestRule.onNodeWithText("4").performClick()
        composeTestRule.onNodeWithText("5").performClick()
        composeTestRule.onNodeWithText("6").performClick()
        composeTestRule.onNodeWithText("7").performClick()
        composeTestRule.onNodeWithText("8").performClick()
        composeTestRule.onNodeWithText("9").performClick()
        composeTestRule.onNodeWithText("0").performClick()

        // Assert result is 8
        composeTestRule.onNode(hasTestTag("calculator_display") and hasText("1234567890"))
            .assertIsDisplayed()
    }
}