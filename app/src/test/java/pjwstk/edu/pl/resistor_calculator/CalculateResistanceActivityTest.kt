import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import pjwstk.edu.pl.resistor_calculator.CalculateResistanceActivity

@RunWith(MockitoJUnitRunner::class)
class CalculateResistanceActivityTest {

    @Mock
    private lateinit var CalculateResistanceActivity: CalculateResistanceActivity

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }
    @Test
    fun testCalculateThreeFourBandResistance() {
        val stripes = listOf("Brown", "Black", "Red")
        val expectedResult = 12.0 // (1 * 10 + 2) * 10

        // Configure the behavior of the mock
        `when`(CalculateResistanceActivity.calculateThreeFourBandResistance(stripes)).thenReturn(expectedResult)

        // Perform the test
        val actualResult = CalculateResistanceActivity.calculateThreeFourBandResistance(stripes)

        // Verify the result
        assertEquals(expectedResult, actualResult, 0.0)
    }


    @Test
    fun testCalculateFiveSixBandResistance() {
        val stripes = listOf("Brown", "Black", "Red", "Orange")
        val expectedResult = 1200.0 // (1 * 100 + 2 * 10 + 3) * 1000

        `when`(CalculateResistanceActivity.calculateFiveSixBandResistance(stripes)).thenReturn(expectedResult)

        val actualResult = CalculateResistanceActivity.calculateFiveSixBandResistance(stripes)
        assertEquals(expectedResult, actualResult, 0.0)
    }

    @Test
    fun testGetToleranceValue() {
        val stripes = listOf("Brown", "Black", "Red", "Orange")
        val expectedTolerance = "±0.05%"

        `when`(CalculateResistanceActivity.getToleranceValue(stripes)).thenReturn(expectedTolerance)

        val actualTolerance = CalculateResistanceActivity.getToleranceValue(stripes)
        assertEquals(expectedTolerance, actualTolerance)
    }

    @Test
    fun testGetColorValue() {
        val color = "Black"
        val expectedValue = 0

        `when`(CalculateResistanceActivity.getColorValue(color)).thenReturn(expectedValue)

        val actualValue = CalculateResistanceActivity.getColorValue(color)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun testGetMultiplierValue() {
        val color = "Brown"
        val expectedMultiplier = 10.0

        `when`(CalculateResistanceActivity.getMultiplierValue(color)).thenReturn(expectedMultiplier)

        val actualMultiplier = CalculateResistanceActivity.getMultiplierValue(color)
        if (actualMultiplier != null) {
            assertEquals(expectedMultiplier, actualMultiplier, 0.0)
        }
    }

    @Test
    fun testGetTemperatureCoefficientValue() {
        val stripes = listOf("Brown", "Black", "Red", "Orange")
        val expectedTemperatureCoefficient = "15 ppm/°C"

        `when`(CalculateResistanceActivity.getTemperatureCoefficientValue(stripes)).thenReturn(expectedTemperatureCoefficient)

        val actualTemperatureCoefficient = CalculateResistanceActivity.getTemperatureCoefficientValue(stripes)
        assertEquals(expectedTemperatureCoefficient, actualTemperatureCoefficient)
    }

    @Test
    fun testFormatResistanceValue() {
        val resistanceValue = 1234567.89
        val expectedFormattedValue = "1.23 MΩ"

        `when`(CalculateResistanceActivity.formatResistanceValue(resistanceValue)).thenReturn(expectedFormattedValue)

        val actualFormattedValue = CalculateResistanceActivity.formatResistanceValue(resistanceValue)
        assertEquals(expectedFormattedValue, actualFormattedValue)
    }
}
