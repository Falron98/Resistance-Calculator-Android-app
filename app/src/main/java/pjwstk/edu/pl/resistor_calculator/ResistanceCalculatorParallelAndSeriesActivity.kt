package pjwstk.edu.pl.resistor_calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView

class ResistanceCalculatorParallelAndSeriesActivity : AppCompatActivity() {

    private lateinit var editTextResistors: EditText
    private lateinit var buttonCalculateSeries: Button
    private lateinit var buttonCalculateParallel: Button
    private lateinit var textViewResult: TextView
    lateinit var radioButtonKiloohms: RadioButton
    private lateinit var radioButtonMegaohms: RadioButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resistance_calculator_parallel_series)

        // Initialization of user interface elements
        editTextResistors = findViewById(R.id.editTextResistors)
        buttonCalculateSeries = findViewById(R.id.buttonCalculateSeries)
        buttonCalculateParallel = findViewById(R.id.buttonCalculateParallel)
        textViewResult = findViewById(R.id.textViewResult)
        radioButtonKiloohms = findViewById(R.id.radioButtonKiloohms)
        radioButtonMegaohms = findViewById(R.id.radioButtonMegaohms)

        // Button click handler
        buttonCalculateSeries.setOnClickListener {
            calculateSeriesResistance()
        }

        buttonCalculateParallel.setOnClickListener {
            calculateParallelResistance()
        }
    }

    // Parsing user-entered resistor values
    private fun parseResistors(): List<Double> {
        val resistorsText = editTextResistors.text.toString()
        val resistorsArray = resistorsText.split(",").map { it.toDouble() }
        return resistorsArray
    }

    // Class for calculating resistance
    class ResistorCalculator {

        // Calculation of resistance for series connection
        fun calculateSeriesResistance(resistors: List<Double>): Double {
            return resistors.sum()
        }

        // Calculation of resistance for parallel connection
        fun calculateParallelResistance(resistors: List<Double>): Double {
            return 1 / resistors.sumOf { 1 / it }
        }
    }

    // Calculation of resistance for a series connection
    @SuppressLint("SetTextI18n")
    private fun calculateSeriesResistance() {
        val resistors = parseResistors()
        val calculator = ResistorCalculator()
        val seriesResistance = calculator.calculateSeriesResistance(resistors)
        val formattedResistance = formatResistanceValue(seriesResistance)
        textViewResult.text = getString(R.string.ResultSeries) + " " + formattedResistance
    }

    // Calculation of resistance for parallel connection
    @SuppressLint("SetTextI18n")
    private fun calculateParallelResistance() {
        val resistors = parseResistors()
        val calculator = ResistorCalculator()
        val parallelResistance = calculator.calculateParallelResistance(resistors)
        val formattedResistance = formatResistanceValue(parallelResistance)
        textViewResult.text = getString(R.string.ResultParallel) + " " + formattedResistance
    }

    // Formatting the resistance value based on the selected unit
    fun formatResistanceValue(resistance: Double): String {
        val selectedUnit = getSelectedResistanceUnit()
        val formattedValue: String

        formattedValue = when (selectedUnit) {
            "kΩ" -> {
                val valueInKiloohms = resistance / 1000
                "%.5f $selectedUnit".format(valueInKiloohms)
            }
            "MΩ" -> {
                val valueInMegaohms = resistance / 1000000
                "%.5f $selectedUnit".format(valueInMegaohms)
            }
            else -> "%.5f Ω".format(resistance)
        }

        return formattedValue
    }

    // Choose resistance unit
    fun getSelectedResistanceUnit(): String {
        return when {
            radioButtonKiloohms.isChecked -> "kΩ"
            radioButtonMegaohms.isChecked -> "MΩ"
            else -> "Ω"
        }
    }
}
