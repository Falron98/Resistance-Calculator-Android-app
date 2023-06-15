package pjwstk.edu.pl.resistor_calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class CalculateResistanceActivity : AppCompatActivity() {

    // Create a decimal format instance to format resistance values
    private val decimalFormat = DecimalFormat("#.##")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_resistance)

        // Retrieve the stripe colors from the intent
        val stripes = intent.getStringArrayListExtra("stripes")

        // Initialize the text views
        val resistanceValueTextView: TextView = findViewById(R.id.resistanceValueTextView)
        val toleranceTextView: TextView = findViewById(R.id.toleranceTextView)
        val temperatureTextView: TextView = findViewById(R.id.temperatureTextView)

        // Calculate the resistance based on the number of stripes
        val resistanceValue: Double = when (stripes?.size) {
            3 -> calculateThreeFourBandResistance(stripes)
            4 -> calculateThreeFourBandResistance(stripes)
            5 -> calculateFiveSixBandResistance(stripes)
            6 -> calculateFiveSixBandResistance(stripes)
            else -> 0.0
        }

        // Format the resistance value
        val formattedResistanceValue = formatResistanceValue(resistanceValue)

        // Set the resistance value in the text view
        resistanceValueTextView.text = formattedResistanceValue

        // Set the tolerance value based on the number of stripes
        val toleranceValue = getToleranceValue(stripes)

        // Set the tolerance value in the text view
        toleranceTextView.text = toleranceValue

        // Set the temperature coefficient value if it is a six-band resistor, otherwise show "N/A"
        if (stripes?.size == 6) {
            val temperatureCoefficientValue = getTemperatureCoefficientValue(stripes)
            temperatureTextView.text = temperatureCoefficientValue
        } else {
            temperatureTextView.text = "N/A"
        }
    }

    // Function to calculate the resistance value for a three- and four-band resistors
    private fun calculateThreeFourBandResistance(stripes: List<String>?): Double {
        // Extract the color values for the first digit, second digit, and multiplier
        val firstDigit = getColorValue(stripes?.get(0))
        val secondDigit = getColorValue(stripes?.get(1))
        val multiplier = getMultiplierValue(stripes?.get(2))

        // Calculate the resistance value: (first digit * 10 + second digit) * multiplier
        return if (firstDigit != null && secondDigit != null && multiplier != null) {
            (firstDigit * 10 + secondDigit) * multiplier.toDouble()
        } else {
            0.0
        }
    }


    // Function to calculate the resistance value for a five- or six-band resistor
    private fun calculateFiveSixBandResistance(stripes: List<String>?): Double {
        val firstDigit = getColorValue(stripes?.get(0))
        val secondDigit = getColorValue(stripes?.get(1))
        val thirdDigit = getColorValue(stripes?.get(2))
        val multiplier = getMultiplierValue(stripes?.get(3))

        // Calculate the resistance value: (first digit * 100 + second digit * 10 + third digit) * multiplier
        return if (firstDigit != null && secondDigit != null && thirdDigit != null && multiplier != null) {
            (firstDigit * 100 + secondDigit * 10 + thirdDigit) * multiplier.toDouble()
        } else {
            0.0
        }
    }

    // Function to get the tolerance value based on the stripe colors
    private fun getToleranceValue(stripes: List<String>?): String {
        // Determine the index of the tolerance color based on the number of stripes
        val toleranceIndex = stripes?.size?.minus(2) ?: 0
        val tolerance = stripes?.get(toleranceIndex)

        // Return the tolerance value based on the color
        return when {
            stripes?.size == 3 -> "±20%" // Fixed tolerance of 20% for three-band resistors
            tolerance == "Brown" -> "±1%"
            tolerance == "Red" -> "±2%"
            tolerance == "Orange" -> "±0.05%"
            tolerance == "Yellow" -> "±0.02%"
            tolerance == "Green" -> "±0.5%"
            tolerance == "Blue" -> "±0.25%"
            tolerance == "Purple" -> "±0.1%"
            tolerance == "Gray" -> "±0.01%"
            tolerance == "Gold" -> "±5%"
            tolerance == "Silver" -> "±10%"
            else -> "N/A"
        }
    }

    // Function to map the color name to its corresponding value
    private fun getColorValue(color: String?): Int? {
        return when (color) {
            "Black" -> 0
            "Brown" -> 1
            "Red" -> 2
            "Orange" -> 3
            "Yellow" -> 4
            "Green" -> 5
            "Blue" -> 6
            "Purple" -> 7
            "Gray" -> 8
            "White" -> 9
            else -> null
        }
    }

    // Function to map the color name to its corresponding multiplier value
    private fun getMultiplierValue(color: String?): Double? {
        return when (color) {
            "Black" -> 1.0
            "Brown" -> 10.0
            "Red" -> 100.0
            "Orange" -> 1000.0
            "Yellow" -> 10000.0
            "Green" -> 100000.0
            "Blue" -> 1000000.0
            "Purple" -> 10000000.0
            "Gray" -> 100000000.0
            "White" -> 1000000000.0
            "Gold" -> 0.1
            "Silver" -> 0.01
            else -> null
        }
    }

    // Function to get the temperature coefficient value based on the stripe colors
    private fun getTemperatureCoefficientValue(stripes: List<String>?): String {
        // Determine the index of the temperature coefficient color based on the number of stripes
        val temperatureCoefficientIndex = stripes?.size?.minus(1) ?: 0
        val temperatureCoefficientColor = stripes?.get(temperatureCoefficientIndex)

        // Return the temperature coefficient value based on the color
        return when (temperatureCoefficientColor) {
            "Black" -> "250 ppm/°C"
            "Brown" -> "100 ppm/°C"
            "Red" -> "50 ppm/°C"
            "Orange" -> "15 ppm/°C"
            "Yellow" -> "25 ppm/°C"
            "Green" -> "20 ppm/°C"
            "Blue" -> "10 ppm/°C"
            "Purple" -> "5 ppm/°C"
            "Gray" -> "1 ppm/°C"
            else -> "N/A"
        }
    }

    // Function to format the resistance value based on its magnitude
    private fun formatResistanceValue(resistanceValue: Double): String {
        return when {
            resistanceValue >= 1e9 -> "${decimalFormat.format(resistanceValue / 1e9)} GΩ"
            resistanceValue >= 1e6 -> "${decimalFormat.format(resistanceValue / 1e6)} MΩ"
            resistanceValue >= 1e3 -> "${decimalFormat.format(resistanceValue / 1e3)} KΩ"
            resistanceValue > 0 -> "${decimalFormat.format(resistanceValue)} Ω"
            else -> "0 Ω"
        }
    }
}
