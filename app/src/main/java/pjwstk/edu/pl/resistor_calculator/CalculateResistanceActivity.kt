package pjwstk.edu.pl.resistor_calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculateResistanceActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_resistance)

        val stripes = intent.getStringArrayListExtra("stripes")

        val resistanceValueTextView: TextView = findViewById(R.id.resistanceValueTextView)
        val toleranceTextView: TextView = findViewById(R.id.toleranceTextView)
        val temperatureTextView: TextView = findViewById(R.id.temperatureTextView)

        // Calculate resistance based on the number of stripes
        val resistanceValue: Double = when (stripes?.size) {
            3 -> calculateThreeBandResistance(stripes)
            4 -> calculateFourBandResistance(stripes)
            5 -> calculateFiveBandResistance(stripes)
            6 -> calculateSixBandResistance(stripes)
            else -> 0.0
        }

        // Round the resistance value and convert it to a string
        val roundedResistanceValue = "%.0f".format(resistanceValue)

        // Set the resistance value in the text view
        resistanceValueTextView.text = roundedResistanceValue

        // Set the tolerance value based on the number of stripes
        val toleranceValue = getToleranceValue(stripes)

        // Set the tolerance value in the text view
        toleranceTextView.text = toleranceValue

        if (stripes?.size == 6) {
            val temperatureCoefficientValue = getTemperatureCoefficientValue(stripes)
            temperatureTextView.text = temperatureCoefficientValue
        } else {
            temperatureTextView.text = "N/A"
        }
    }

    private fun calculateThreeBandResistance(stripes: List<String>?): Double {
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

    private fun calculateFourBandResistance(stripes: List<String>?): Double {
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

    private fun calculateFiveBandResistance(stripes: List<String>?): Double {
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

    private fun calculateSixBandResistance(stripes: List<String>?): Double {
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

    private fun getToleranceValue(stripes: List<String>?): String {
        val toleranceIndex = stripes?.size?.minus(1) ?: 0
        val tolerance = stripes?.get(toleranceIndex)

        return when {
            stripes?.size == 3 -> "±20%" // Fixed tolerance of 20% for three-band resistors
            tolerance == "Brown" -> "±1%"
            tolerance == "Red" -> "±2%"
            tolerance == "Orange" -> "±3%"
            tolerance == "Yellow" -> "±4%"
            tolerance == "Green" -> "±0.5%"
            tolerance == "Blue" -> "±0.25%"
            tolerance == "Purple" -> "±0.10%"
            tolerance == "Gray" -> "±0.05%"
            tolerance == "Gold" -> "±5%"
            tolerance == "Silver" -> "±10%"
            else -> "N/A"
        }
    }

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

    private fun getMultiplierValue(color: String?): Int? {
        return when (color) {
            "Black" -> 1
            "Brown" -> 10
            "Red" -> 100
            "Orange" -> 1000
            "Yellow" -> 10000
            "Green" -> 100000
            "Blue" -> 1000000
            "Purple" -> 10000000
            "Gray" -> 100000000
            "White" -> 1000000000
            "Gold" -> 1/10
            "Silver" -> 1/100
            else -> null
        }
    }

    private fun getTemperatureCoefficientValue(stripes: List<String>?): String {
        val temperatureCoefficientIndex = stripes?.size?.minus(1) ?: 0
        val temperatureCoefficientColor = stripes?.get(temperatureCoefficientIndex)

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
}
