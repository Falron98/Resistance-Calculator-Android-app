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
        // Example input and expected output
        val selectedStripes = listOf(2, 3, 5)
        val expectedResistance = "Resistance: 120 KΩ, Tolerance: ±20%"

        // Calculate the resistance using the activity's method
        val actualResistance = activity.calculateResistance(selectedStripes)

        // Verify the calculated resistance matches the expected result
        assertEquals(expectedResistance, actualResistance)
    }

    @Test
    fun testCalculateResistanceWithFourStripes() {
        // Example input and expected output
        val selectedStripes = listOf(3, 4, 6, 7)
        val expectedResistance = "Resistance: 2.3 MΩ, Tolerance: ±0.25%"

        // Calculate the resistance using the activity's method
        val actualResistance = activity.calculateResistance(selectedStripes)

        // Verify the calculated resistance matches the expected result
        assertEquals(expectedResistance, actualResistance)
    }

    @Test
    fun testCalculateResistanceWithFiveStripes() {
        // Example input and expected output
        val selectedStripes = listOf(1, 2, 3, 5, 9)
        val expectedResistance = "Resistance: 120 KΩ, Tolerance: ±0.01%"

        // Calculate the resistance using the activity's method
        val actualResistance = activity.calculateResistance(selectedStripes)

        // Verify the calculated resistance matches the expected result
        assertEquals(expectedResistance, actualResistance)
    }

}
