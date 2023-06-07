package pjwstk.edu.pl.resistor_calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ResistanceCalculatorActivity : AppCompatActivity() {

    private lateinit var editTextResistors: EditText
    private lateinit var buttonCalculateSeries: Button
    private lateinit var buttonCalculateParallel: Button
    private lateinit var textViewResult: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resistance_calculator)

        // Inicjalizacja elementów interfejsu użytkownika
        editTextResistors = findViewById(R.id.editTextResistors)
        buttonCalculateSeries = findViewById(R.id.buttonCalculateSeries)
        buttonCalculateParallel = findViewById(R.id.buttonCalculateParallel)
        textViewResult = findViewById(R.id.textViewResult)

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
        textViewResult.text = "Rezystancja dla połączenia szeregowego: $seriesResistance"
    }
    // Obliczanie rezystancji dla połączenia równoległego
    private fun calculateParallelResistance() {
        val resistors = parseResistors()
        val calculator = ResistorCalculator()
        val parallelResistance = calculator.calculateParallelResistance(resistors)
        textViewResult.text = "Rezystancja dla połączenia równoległego: $parallelResistance"
    }
}
