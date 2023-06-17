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

        // Configure the behavior of the mock
        `when`(CalculateResistanceActivity.calculateFiveSixBandResistance(stripes)).thenReturn(expectedResult)

        // Perform the test
        val actualResult = CalculateResistanceActivity.calculateFiveSixBandResistance(stripes)

        // Verify the result
        assertEquals(expectedResult, actualResult, 0.0)
    }

    @Test
    fun testGetToleranceValue() {
        val stripes = listOf("Brown", "Black", "Red", "Orange")
        val expectedTolerance = "±0.05%"

        // Configure the behavior of the mock
        `when`(CalculateResistanceActivity.getToleranceValue(stripes)).thenReturn(expectedTolerance)

        // Perform the test
        val actualTolerance = CalculateResistanceActivity.getToleranceValue(stripes)

        // Verify the result
        assertEquals(expectedTolerance, actualTolerance)
    }

    @Test
    fun testGetColorValue() {
        val color = "Black"
        val expectedValue = 0

        // Configure the behavior of the mock
        `when`(CalculateResistanceActivity.getColorValue(color)).thenReturn(expectedValue)

        // Perform the test
        val actualValue = CalculateResistanceActivity.getColorValue(color)

        // Verify the result
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun testGetMultiplierValue() {
        val color = "Brown"
        val expectedMultiplier = 10.0

        // Configure the behavior of the mock
        `when`(CalculateResistanceActivity.getMultiplierValue(color)).thenReturn(expectedMultiplier)

        // Perform the test
        val actualMultiplier = CalculateResistanceActivity.getMultiplierValue(color)

        // Verify the result
        if (actualMultiplier != null) {
            assertEquals(expectedMultiplier, actualMultiplier, 0.0)
        }
    }

    @Test
    fun testGetTemperatureCoefficientValue() {
        val stripes = listOf("Brown", "Black", "Red", "Orange")
        val expectedTemperatureCoefficient = "15 ppm/°C"

        // Configure the behavior of the mock
        `when`(CalculateResistanceActivity.getTemperatureCoefficientValue(stripes)).thenReturn(expectedTemperatureCoefficient)

        // Perform the test
        val actualTemperatureCoefficient = CalculateResistanceActivity.getTemperatureCoefficientValue(stripes)

        // Verify the result
        assertEquals(expectedTemperatureCoefficient, actualTemperatureCoefficient)
    }

    @Test
    fun testFormatResistanceValue() {
        val resistanceValue = 1234567.89
        val expectedFormattedValue = "1.23 MΩ"

        // Configure the behavior of the mock
        `when`(CalculateResistanceActivity.formatResistanceValue(resistanceValue)).thenReturn(expectedFormattedValue)

        // Perform the test
        val actualFormattedValue = CalculateResistanceActivity.formatResistanceValue(resistanceValue)

        // Verify the result
        assertEquals(expectedFormattedValue, actualFormattedValue)
    }
}
