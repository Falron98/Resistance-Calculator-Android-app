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
    private lateinit var radioButtonKiloohms: RadioButton
    private lateinit var radioButtonMegaohms: RadioButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resistance_calculator_parallel_series)

        // Inicjalizacja elementów interfejsu użytkownika
        editTextResistors = findViewById(R.id.editTextResistors)
        buttonCalculateSeries = findViewById(R.id.buttonCalculateSeries)
        buttonCalculateParallel = findViewById(R.id.buttonCalculateParallel)
        textViewResult = findViewById(R.id.textViewResult)
        radioButtonKiloohms = findViewById(R.id.radioButtonKiloohms)
        radioButtonMegaohms = findViewById(R.id.radioButtonMegaohms)

        // Ustawienie obsługi kliknięcia przycisków
        buttonCalculateSeries.setOnClickListener {
            calculateSeriesResistance()
        }

        buttonCalculateParallel.setOnClickListener {
            calculateParallelResistance()
        }
    }

    // Parsowanie wartości rezystorów wprowadzonych przez użytkownika
    private fun parseResistors(): List<Double> {
        val resistorsText = editTextResistors.text.toString()
        val resistorsArray = resistorsText.split(",").map { it.toDouble() }
        return resistorsArray
    }

    // Klasa do obliczania rezystancji
    class ResistorCalculator {

        // Obliczanie rezystancji dla połączenia szeregowego
        fun calculateSeriesResistance(resistors: List<Double>): Double {
            return resistors.sum()
        }

        // Obliczanie rezystancji dla połączenia równoległego
        fun calculateParallelResistance(resistors: List<Double>): Double {
            return 1 / resistors.sumOf { 1 / it }
        }
    }

    // Obliczanie rezystancji dla połączenia szeregowego
    private fun calculateSeriesResistance() {
        val resistors = parseResistors()
        val calculator = ResistorCalculator()
        val seriesResistance = calculator.calculateSeriesResistance(resistors)
        val formattedResistance = formatResistanceValue(seriesResistance)
        textViewResult.text = getString(R.string.ResultSeries) + " " + formattedResistance
    }

    // Obliczanie rezystancji dla połączenia równoległego
    private fun calculateParallelResistance() {
        val resistors = parseResistors()
        val calculator = ResistorCalculator()
        val parallelResistance = calculator.calculateParallelResistance(resistors)
        val formattedResistance = formatResistanceValue(parallelResistance)
        textViewResult.text = getString(R.string.ResultParallel) + " " + formattedResistance
    }

    // Formatowanie wartości rezystancji na podstawie wybranej jednostki
    private fun formatResistanceValue(resistance: Double): String {
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

    // Pobieranie wybranej jednostki rezystancji
    private fun getSelectedResistanceUnit(): String {
        return when {
            radioButtonKiloohms.isChecked -> "kΩ"
            radioButtonMegaohms.isChecked -> "MΩ"
            else -> "Ω"
        }
    }
}
