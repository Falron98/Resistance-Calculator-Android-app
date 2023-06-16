package pjwstk.edu.pl.resistor_calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class ResistanceCalculatorParallelAndSeriesActivityTest {

    @Test
    fun testCalculateSeriesResistance() {
        val calculator = ResistanceCalculatorParallelAndSeriesActivity.ResistorCalculator()
        val resistors = listOf(10.0, 20.0, 30.0)
        val expectedResistance = 60.0

        val result = calculator.calculateSeriesResistance(resistors)

        assertEquals(expectedResistance, result, 0.0001)
    }

    @Test
    fun testCalculateParallelResistance() {
        val calculator = ResistanceCalculatorParallelAndSeriesActivity.ResistorCalculator()
        val resistors = listOf(10.0, 20.0, 30.0)
        val expectedResistance = 5.4545

        val result = calculator.calculateParallelResistance(resistors)

        assertEquals(expectedResistance, result, 0.0001)
    }

}
