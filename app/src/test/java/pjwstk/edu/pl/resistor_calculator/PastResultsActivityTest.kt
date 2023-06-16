import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pjwstk.edu.pl.resistor_calculator.PastResultsActivity

@RunWith(AndroidJUnit4::class)
class PastResultsActivityTest {
    private lateinit var activity: PastResultsActivity

    @Before
    fun setUp() {
        activity = PastResultsActivity()
    }

    @Test
    fun testCalculateResistanceWithThreeStripes() {
        val selectedStripes = listOf(2, 3, 5) // Example input
        val expectedResistance = "Resistance: 120 KΩ, Tolerance: ±20%"

        val actualResistance = activity.calculateResistance(selectedStripes)

        assertEquals(expectedResistance, actualResistance)
    }

    @Test
    fun testCalculateResistanceWithFourStripes() {
        val selectedStripes = listOf(3, 4, 6, 7) // Example input
        val expectedResistance = "Resistance: 2.3 MΩ, Tolerance: ±0.25%"

        val actualResistance = activity.calculateResistance(selectedStripes)

        assertEquals(expectedResistance, actualResistance)
    }

    @Test
    fun testCalculateResistanceWithFiveStripes() {
        val selectedStripes = listOf(1, 2, 3, 5, 9) // Example input
        val expectedResistance = "Resistance: 120 KΩ, Tolerance: ±0.01%"

        val actualResistance = activity.calculateResistance(selectedStripes)

        assertEquals(expectedResistance, actualResistance)
    }

    // Add more tests for other scenarios and methods as needed
}
