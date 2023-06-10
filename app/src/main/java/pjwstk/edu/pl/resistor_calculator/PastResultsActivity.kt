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
import kotlin.math.pow

class PastResultsActivity : AppCompatActivity() {
    private lateinit var configurationSpinner1: Spinner
    private lateinit var configurationSpinner2: Spinner
    private lateinit var resultTextView1: TextView
    private lateinit var resultTextView2: TextView

    private val savedConfigurations = mutableListOf<Pair<String, List<String>>>()
    private val resultData = mutableMapOf<Pair<String, List<String>>, String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_results)

        // Wczytaj konfiguracje z pliku
        val configurationsLoaded = loadConfigurationsFromFile()
        val numberOfConfigurations = savedConfigurations.size

        if (configurationsLoaded && numberOfConfigurations >= 2) {
            // Przykładowe dane - dodaj swoje zapisane wyniki
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

            // Ustaw domyślnie różne konfiguracje na spinnerach
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
                        val result = resultData[selectedConfiguration]

                        displayResult(selectedConfiguration, result ?: "", resultTextView1)
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
                        val result = resultData[selectedConfiguration]

                        displayResult(selectedConfiguration, result ?: "", resultTextView2)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
        } else {
            // Brak konfiguracji lub plik nie istnieje - przejdź do ChooseStripesActivity
            val intent = Intent(this, ChooseStripesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun displayResult(
        configuration: Pair<String, List<String>>,
        result: String,
        textView: TextView
    ) {
        val configurationName = configuration.first
        val selectedStripes = configuration.second

        val resistanceResult = calculateResistance(selectedStripes)

        val resultText = buildString {
            append("Config name: $configurationName\n")
            append("Stripes colors: ${selectedStripes.joinToString(", ")}\n")
            append("Result: $resistanceResult")
        }

        textView.text = resultText
    }

    private fun calculateResistance(selectedStripes: List<String>): String {
        val colorsToValues = mapOf(
            "Black" to 0,
            "Brown" to 1,
            "Red" to 2,
            "Orange" to 3,
            "Yellow" to 4,
            "Green" to 5,
            "Blue" to 6,
            "Purple" to 7,
            "Gray" to 8,
            "White" to 9
        )

        var resistance = 0L
        var multiplier = 1L

        // Pobierz wartość rezystancji z pierwszych dwóch kolorów
        for (i in 0..1) {
            val color = selectedStripes[i]
            val value = colorsToValues[color]
            resistance = resistance * 10 + value!!
        }

        // Pobierz mnożnik z trzeciego koloru
        val multiplierColor = selectedStripes[2]
        val multiplierValue = colorsToValues[multiplierColor]
        multiplier = 10.0.pow(multiplierValue!!.toDouble()).toLong()

        // Oblicz wartość rezystancji
        val resistanceValue = resistance * multiplier

        // Jeśli istnieje czwarty kolor, to jest to tolerancja
        if (selectedStripes.size > 3) {
            val toleranceColor = selectedStripes[3]
            val toleranceValues = mapOf(
                "Brown" to "±1%",
                "Red" to "±2%",
                "Orange" to "±3%",
                "Yellow" to "±4%",
                "Green" to "±0.5%",
                "Blue" to "±0.25%",
                "Purple" to "±0.10%",
                "Gray" to "±0.05%",
                "Gold" to "±5%",
                "Silver" to "±10%"
            )
            val toleranceValue = toleranceValues[toleranceColor]

            // Jeśli istnieje piąty kolor, to jest to PPM
            if (selectedStripes.size > 5) {
                val ppmColor = selectedStripes[5]
                val ppmValues = mapOf(
                    "Black" to "250 ppm/°C",
                    "Brown" to "100 ppm/°C",
                    "Red" to "50 ppm/°C",
                    "Orange" to "15 ppm/°C",
                    "Yellow" to "25 ppm/°C",
                    "Green" to "20 ppm/°C",
                    "Blue" to "10 ppm/°C",
                    "Purple" to "5 ppm/°C",
                    "Gray" to "1 ppm/°C"
                )
                val ppmValue = ppmValues[ppmColor]
                return "Resistance: $resistanceValue, Tolerance: $toleranceValue, PPM: $ppmValue"
            } else {
                return "Resistance: $resistanceValue, Tolerance: $toleranceValue"
            }
        } else {
            return "Resistance: $resistanceValue"
        }
    }

    private fun loadConfigurationsFromFile(): Boolean {
        // Wczytaj konfiguracje z pliku
        val file = File(applicationContext.filesDir, "configurations.txt")

        if (file.exists()) {
            val fileInputStream = FileInputStream(file)
            val objectInputStream = ObjectInputStream(fileInputStream)

            val configurations =
                objectInputStream.readObject() as MutableList<Pair<String, List<String>>>

            // Wyczyść bieżące konfiguracje i dodaj wczytane z pliku
            savedConfigurations.clear()
            savedConfigurations.addAll(configurations)

            objectInputStream.close()
            fileInputStream.close()

            return true
        } else {
            return false
        }
    }
}
