package pjwstk.edu.pl.resistor_calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class ResistanceCalculatorParallelAndSeriesActivityTest {

    @Test
    fun testCalculateSeriesResistance() {
        // Create an instance of the ResistorCalculator
        val calculator = ResistanceCalculatorParallelAndSeriesActivity.ResistorCalculator()

        // Define the resistors in series and the expected total resistance
        val resistors = listOf(10.0, 20.0, 30.0)
        val expectedResistance = 60.0

        // Calculate the total series resistance using the ResistorCalculator
        val result = calculator.calculateSeriesResistance(resistors)

        // Verify that the calculated series resistance matches the expected result
        assertEquals(expectedResistance, result, 0.0001)
    }

    @Test
    fun testCalculateParallelResistance() {
        // Create an instance of the ResistorCalculator
        val calculator = ResistanceCalculatorParallelAndSeriesActivity.ResistorCalculator()

        // Define the resistors in parallel and the expected total resistance
        val resistors = listOf(10.0, 20.0, 30.0)
        val expectedResistance = 5.4545

        // Calculate the total parallel resistance using the ResistorCalculator
        val result = calculator.calculateParallelResistance(resistors)

        // Verify that the calculated parallel resistance matches the expected result
        assertEquals(expectedResistance, result, 0.0001)
    }

}
