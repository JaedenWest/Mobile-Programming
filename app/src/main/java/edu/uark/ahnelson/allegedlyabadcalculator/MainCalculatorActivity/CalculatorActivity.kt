package edu.uark.ahnelson.allegedlyabadcalculator.MainCalculatorActivity

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
import edu.uark.ahnelson.allegedlyabadcalculator.OperatorT
import edu.uark.ahnelson.allegedlyabadcalculator.ui.theme.AllegedlyABadCalculatorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllegedlyABadCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculatorActivityViewModel = viewModel()
) {
    val resultText by viewModel.result
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { toastMessage ->
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    if (isLandscape) {
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
                isLandscape = true,
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight()
            )
        }
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
                isLandscape = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )
        }
    }
}

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

@Composable
fun KeypadGrid(
    viewModel: CalculatorActivityViewModel,
    coroutineScope: CoroutineScope,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
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
                row.forEach { symbol ->
                    Button(
                        onClick = {
                            when (symbol) {
                                "C" -> coroutineScope.launch { viewModel.onClearClicked() }
                                "=" -> coroutineScope.launch { viewModel.onEqualClicked() }
                                "+" -> coroutineScope.launch { viewModel.onOperatorClicked(OperatorT.PLUS) }
                                "-" -> coroutineScope.launch { viewModel.onOperatorClicked(OperatorT.MINUS) }
                                "x" -> coroutineScope.launch { viewModel.onOperatorClicked(OperatorT.TIMES) }
                                "/" -> coroutineScope.launch { viewModel.onOperatorClicked(OperatorT.DIVIDE) }
                                else -> coroutineScope.launch { viewModel.onNumberClicked(symbol) }
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
    AllegedlyABadCalculatorTheme {
        CalculatorScreen()
    }
}
