package jaedenwest.allegedlyagoodcalculator.maincalculatoractivity

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import jaedenwest.allegedlyagoodcalculator.OperatorT
import jaedenwest.allegedlyagoodcalculator.ui.theme.AllegedlyAGoodCalculatorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 THE VIEW LAYER
* Responsible for listening to what happens on the screen and telling the viewmodel
* It does not understand what its receiving just tells the viewmodel that it happened
* Class that creates the Activity
*/
 class CalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllegedlyAGoodCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * Main Composable for the calculator
 * Implements the layout for both Portrait and Landscape
 */
@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculatorActivityViewModel = viewModel()
) {
    val resultText by viewModel.result
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    //Detect orientation
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    //List for events from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { toastMessage ->
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    if (isLandscape) {
        //Landscape layout
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                CalculatorDisplay(resultText)
            }
            
            // Keypad on the right in Landscape
            KeypadGrid(
                viewModel = viewModel,
                coroutineScope = coroutineScope,
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight()
            )
        }
        //Portrait layout
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display at the top (1/3 of the screen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                CalculatorDisplay(resultText)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Keypad at the bottom (2/3 of the screen)
            KeypadGrid(
                viewModel = viewModel,
                coroutineScope = coroutineScope,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )
        }
    }
}

/**
 * Renders the calculator output screen
 * Automatically adjusts the font size when to many digits will make it look bad
 */
@Composable
fun CalculatorDisplay(text: String) {
    Text(
        text = text.ifEmpty { "0" },
        fontSize = if (text.length > 15) 26.sp else 36.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End,
        modifier = Modifier.fillMaxWidth().testTag("calculator_display")
    )
}

/**
 * Generates the grid of calculator buttons
 * Routes user clicks to the actual viewModel func
 */
@Composable
fun KeypadGrid(
    viewModel: CalculatorActivityViewModel,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    //list of buttons
    val buttonRows = listOf(
        listOf("C", "+/-", "<-", "/"),
        listOf("7", "8", "9", "x"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf(".", "0", "=")
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttonRows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //what to do when buttons clicked
                row.forEach { symbol ->
                    Button(
                        onClick = {
                            when (symbol) {
                                "C" -> viewModel.onClearClicked()
                                "=" -> coroutineScope.launch { viewModel.onEqualClicked() }
                                "+" -> coroutineScope.launch { viewModel.onOperatorClicked(OperatorT.PLUS) }
                                "-" -> coroutineScope.launch { viewModel.onOperatorClicked(OperatorT.MINUS) }
                                "x" -> coroutineScope.launch { viewModel.onOperatorClicked(OperatorT.TIMES) }
                                "/" -> coroutineScope.launch { viewModel.onOperatorClicked(OperatorT.DIVIDE) }
                                else -> viewModel.onNumberClicked(symbol)
                            }
                        },
                        modifier = Modifier
                            .weight(if (symbol == "0" && row.size == 3) 2f else 1f)
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(0.dp),
                        colors = when (symbol) {
                            "=" -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            "+", "-", "x", "/" -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            "C" -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            else -> ButtonDefaults.buttonColors()
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = symbol,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalculatorScreenPreview() {
    AllegedlyAGoodCalculatorTheme {
        CalculatorScreen()
    }
}
