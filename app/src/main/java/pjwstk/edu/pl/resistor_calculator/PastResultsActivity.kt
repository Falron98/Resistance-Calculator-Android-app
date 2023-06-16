package pjwstk.edu.pl.resistor_calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.text.DecimalFormat

class PastResultsActivity : AppCompatActivity() {
    private lateinit var configurationSpinner1: Spinner
    private lateinit var configurationSpinner2: Spinner
    private lateinit var resultTextView1: TextView
    private lateinit var resultTextView2: TextView
    val decimalFormat = DecimalFormat("#.##")
    private val savedConfigurations = mutableListOf<Pair<String, List<Int>>>()
    private val resultData = mutableMapOf<Pair<String, List<Int>>, String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_results)

        // Load config from file
        val configurationsLoaded = loadConfigurationsFromFile()
        val numberOfConfigurations = savedConfigurations.size

        if (configurationsLoaded && numberOfConfigurations >= 2) {
            // Example data - add yours results
            resultData[savedConfigurations[0]] = "Wynik 1"
            resultData[savedConfigurations[1]] = "Wynik 2"

            configurationSpinner1 = findViewById(R.id.configurationSpinner1)
            configurationSpinner2 = findViewById(R.id.configurationSpinner2)
            resultTextView1 = findViewById(R.id.resultTextView1)
            resultTextView2 = findViewById(R.id.resultTextView2)

            val configurationNames = savedConfigurations.map { it.first }
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, configurationNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            configurationSpinner1.adapter = adapter
            configurationSpinner2.adapter = adapter

            // Set default values for spinners
            configurationSpinner1.setSelection(0)
            configurationSpinner2.setSelection(1)

            configurationSpinner1.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedConfiguration = savedConfigurations[position]
                        resultData[selectedConfiguration]

                        displayResult(selectedConfiguration, resultTextView1)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }

            configurationSpinner2.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedConfiguration = savedConfigurations[position]
                        resultData[selectedConfiguration]

                        displayResult(selectedConfiguration, resultTextView2)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
        } else {
            // No configuration or file doesnt exist
            val intent = Intent(this, ChooseStripesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun displayResult(
        configuration: Pair<String, List<Int>>,
        textView: TextView
    ) {
        val configurationName = configuration.first
        val selectedStripes = configuration.second

        val resistanceResult = calculateResistance(selectedStripes)

        val resultText = buildString {
            append("Config name: $configurationName\n")
            append(
                "Stripes colors: ${
                    selectedStripes.map { getColorName(it) }.joinToString(", ")
                }\n"
            )
            append("Result: $resistanceResult")
        }

        textView.text = resultText
    }

    private fun getColorName(color: Int): String {
        val colorsToNames = mapOf(
            0 to "None",
            1 to "Black",
            2 to "Brown",
            3 to "Red",
            4 to "Orange",
            5 to "Yellow",
            6 to "Green",
            7 to "Blue",
            8 to "Purple",
            9 to "Gray",
            10 to "White",
            11 to "Silver",
            12 to "Gold"
        )

        return colorsToNames[color] ?: ""
    }

    fun calculateResistance(selectedStripes: List<Int>): String {
        val colorsToValues = mapOf(
            "None" to 0,
            "Black" to 1,
            "Brown" to 2,
            "Red" to 3,
            "Orange" to 4,
            "Yellow" to 5,
            "Green" to 6,
            "Blue" to 7,
            "Purple" to 8,
            "Gray" to 9,
            "White" to 10,
            "Silver" to 11,
            "Gold" to 12
        )


        if (selectedStripes.size == 3) {
            // Configuration with 3 stripes: Basic values, Mulitplier
            val firstNumber = (colorsToValues.values.elementAt(selectedStripes[0]) - 1).toDouble()
            val secondNumber = (colorsToValues.values.elementAt(selectedStripes[1]) - 1).toDouble()
            val multiplierColor = selectedStripes[2]
            val multiplierValues = mapOf(
                "Black" to 1.0,
                "Brown" to 10.0,
                "Red" to 100.0,
                "Orange" to 1000.0,
                "Yellow" to 10000.0,
                "Green" to 100000.0,
                "Blue" to 1000000.0,
                "Purple" to 10000000.0,
                "Gray" to 100000000.0,
                "White" to 1000000000.0,
                "Gold" to 0.1,
                "Silver" to 0.01
            )
            val multiplierValue = multiplierValues[getColorName(multiplierColor)] ?: 0.0

            val resistanceValue = (firstNumber * 10 + secondNumber * 1) * multiplierValue

            val formattedResistance = when {
                resistanceValue >= 1e9 -> "${decimalFormat.format(resistanceValue / 1e9)} GΩ"
                resistanceValue >= 1e6 -> "${decimalFormat.format(resistanceValue / 1e6)} MΩ"
                resistanceValue >= 1e3 -> "${decimalFormat.format(resistanceValue / 1e3)} KΩ"
                resistanceValue > 0 -> "${decimalFormat.format(resistanceValue)} Ω"
                else -> "0 Ω"
            }

            return "Resistance: $formattedResistance, Tolerance: ±20%"
        } else if (selectedStripes.size == 4) {
            // Configuration with 4 stripes: Basic values, Multiplier, Tolerance
            val firstNumber = colorsToValues.values.elementAt(selectedStripes[0]) - 1
            val secondNumber = colorsToValues.values.elementAt(selectedStripes[1]) - 1
            val multiplierColor = selectedStripes[2]
            val toleranceColor = selectedStripes[3]
            val toleranceValues = mapOf(
                "None" to "N/a",
                "Black" to "N/a",
                "Brown" to "±1%",
                "Red" to "±2%",
                "Orange" to "±0.05%",
                "Yellow" to "±0.02%",
                "Green" to "±0.5%",
                "Blue" to "±0.25%",
                "Purple" to "±0.1%",
                "Gray" to "±0.01%",
                "White" to "N/a",
                "Silver" to "±10%",
                "Gold" to "±5%",
            )
            val multiplierValues = mapOf(
                "Black" to 1.0,
                "Brown" to 10.0,
                "Red" to 100.0,
                "Orange" to 1000.0,
                "Yellow" to 10000.0,
                "Green" to 100000.0,
                "Blue" to 1000000.0,
                "Purple" to 10000000.0,
                "Gray" to 100000000.0,
                "White" to 1000000000.0,
                "Gold" to 0.1,
                "Silver" to 0.01
            )
            val multiplierValue = multiplierValues[getColorName(multiplierColor)] ?: 0.0

            val toleranceValue = toleranceValues[getColorName(toleranceColor)]

            val resistanceValue = (firstNumber * 10 + secondNumber * 1) * multiplierValue

            val formattedResistance = when {
                resistanceValue >= 1e9 -> "${decimalFormat.format(resistanceValue / 1e9)} GΩ"
                resistanceValue >= 1e6 -> "${decimalFormat.format(resistanceValue / 1e6)} MΩ"
                resistanceValue >= 1e3 -> "${decimalFormat.format(resistanceValue / 1e3)} KΩ"
                resistanceValue > 0 -> "${decimalFormat.format(resistanceValue)} Ω"
                else -> "0 Ω"
            }

            return "Resistance: $formattedResistance, Tolerance: $toleranceValue"


        } else if (selectedStripes.size == 5) {
            // Configuration with 5 stripes: Basic values, Multiplier, Tolerance
            val firstNumber = colorsToValues.values.elementAt(selectedStripes[0]) - 1
            val secondNumber = colorsToValues.values.elementAt(selectedStripes[1]) - 1
            val thirdNumber = colorsToValues.values.elementAt(selectedStripes[2]) - 1
            val multiplierColor = selectedStripes[3]
            val toleranceColor = selectedStripes[4]
            val toleranceValues = mapOf(
                "None" to "N/a",
                "Black" to "N/a",
                "Brown" to "±1%",
                "Red" to "±2%",
                "Orange" to "±0.05%",
                "Yellow" to "±0.02%",
                "Green" to "±0.5%",
                "Blue" to "±0.25%",
                "Purple" to "±0.1%",
                "Gray" to "±0.01%",
                "White" to "N/a",
                "Silver" to "±10%",
                "Gold" to "±5%",
            )
            val multiplierValues = mapOf(
                "Black" to 1.0,
                "Brown" to 10.0,
                "Red" to 100.0,
                "Orange" to 1000.0,
                "Yellow" to 10000.0,
                "Green" to 100000.0,
                "Blue" to 1000000.0,
                "Purple" to 10000000.0,
                "Gray" to 100000000.0,
                "White" to 1000000000.0,
                "Gold" to 0.1,
                "Silver" to 0.01
            )
            val multiplierValue = multiplierValues[getColorName(multiplierColor)] ?: 0.0
            val toleranceValue = toleranceValues[getColorName(toleranceColor)]
            val resistanceValue =
                (firstNumber * 100 + secondNumber * 10 + thirdNumber) * multiplierValue
            val formattedResistance = when {
                resistanceValue >= 1e9 -> "${decimalFormat.format(resistanceValue / 1e9)} GΩ"
                resistanceValue >= 1e6 -> "${decimalFormat.format(resistanceValue / 1e6)} MΩ"
                resistanceValue >= 1e3 -> "${decimalFormat.format(resistanceValue / 1e3)} KΩ"
                resistanceValue > 0 -> "${decimalFormat.format(resistanceValue)} Ω"
                else -> "0 Ω"
            }

            return "Resistance: $formattedResistance, Tolerance: $toleranceValue"

        } else if (selectedStripes.size == 6) {
            // Configuration with 6 stripes: Basic values, Multiplier, Tolerance, Temperature Coefficient
            val firstNumber = colorsToValues.values.elementAt(selectedStripes[0]) - 1
            val secondNumber = colorsToValues.values.elementAt(selectedStripes[1]) - 1
            val thirdNumber = colorsToValues.values.elementAt(selectedStripes[2]) - 1
            val multiplierColor = selectedStripes[3]
            val multiplierValues = mapOf(
                "Black" to 1.0,
                "Brown" to 10.0,
                "Red" to 100.0,
                "Orange" to 1000.0,
                "Yellow" to 10000.0,
                "Green" to 100000.0,
                "Blue" to 1000000.0,
                "Purple" to 10000000.0,
                "Gray" to 100000000.0,
                "White" to 1000000000.0,
                "Gold" to 0.1,
                "Silver" to 0.01
            )
            val multiplierValue = multiplierValues[getColorName(multiplierColor)] ?: 0.0
            val toleranceColor = selectedStripes[4]
            val toleranceValues = mapOf(
                "None" to "N/a",
                "Black" to "N/a",
                "Brown" to "±1%",
                "Red" to "±2%",
                "Orange" to "±0.05%",
                "Yellow" to "±0.02%",
                "Green" to "±0.5%",
                "Blue" to "±0.25%",
                "Purple" to "±0.1%",
                "Gray" to "±0.01%",
                "White" to "N/a",
                "Silver" to "±10%",
                "Gold" to "±5%",
            )
            val toleranceValue = toleranceValues[getColorName(toleranceColor)]
            val ppmColor = selectedStripes[5]
            val ppmValues = mapOf(
                "None" to "N/a",
                "Black" to "250 ppm",
                "Brown" to "100 ppm",
                "Red" to "50 ppm",
                "Orange" to "15 ppm",
                "Yellow" to "25 ppm",
                "Green" to "20 ppm",
                "Blue" to "10 ppm",
                "Purple" to "5 ppm",
                "Gray" to "1 ppm",
                "White" to "N/a",
                "Silver" to "N/a",
                "Gold" to "N/a"
            )
            val ppmValue = ppmValues[getColorName(ppmColor)]

            val resistanceValue =
                (firstNumber * 100 + secondNumber * 10 + thirdNumber) * multiplierValue
            val formattedResistance = when {
                resistanceValue >= 1e9 -> "${decimalFormat.format(resistanceValue / 1e9)} GΩ"
                resistanceValue >= 1e6 -> "${decimalFormat.format(resistanceValue / 1e6)} MΩ"
                resistanceValue >= 1e3 -> "${decimalFormat.format(resistanceValue / 1e3)} KΩ"
                resistanceValue > 0 -> "${decimalFormat.format(resistanceValue)} Ω"
                else -> "0 Ω"
            }
            return "Resistance: $formattedResistance, Tolerance: $toleranceValue, PPM: $ppmValue"
        }

        return "N/a"
    }

    private fun loadConfigurationsFromFile(): Boolean {
        val file = File(filesDir, "configurations.txt")

        if (file.exists()) {
            FileInputStream(file).use { fileInputStream ->
                ObjectInputStream(fileInputStream).use { objectInputStream ->
                    val configurations =
                        objectInputStream.readObject() as List<Pair<String, List<Int>>>
                    savedConfigurations.addAll(configurations)
                }
            }
            return true
        }

        return false
    }
}
