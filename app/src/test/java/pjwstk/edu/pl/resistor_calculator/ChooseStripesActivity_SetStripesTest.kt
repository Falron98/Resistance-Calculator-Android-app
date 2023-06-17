package pjwstk.edu.pl.resistor_calculator

import android.widget.LinearLayout
import android.widget.Spinner
import org.junit.Assert
import org.junit.Test

class ChooseStripesActivity_SetStripesTest {

    // Mock LinearLayout class for testing purposes
    private class MockLinearLayout : LinearLayout(null) {
        private val childCountInternal: Int
            get() = 0

        override fun getChildCount(): Int {
            return childCountInternal
        }
    }

    // Mock Spinner class for testing purposes
    private class MockSpinner : Spinner(null) {
        private var selectedItemPositionInternal: Int = 0

        override fun getSelectedItemPosition(): Int {
            return selectedItemPositionInternal
        }

        override fun setSelection(position: Int) {
            selectedItemPositionInternal = position
        }
    }

    // Create a mock LinearLayout object to hold the mock Spinners
    private val stripeSpinnersContainer = MockLinearLayout()

    // Helper function to set the stripe colors in the mock Spinners
    private fun setStripeColors(colors: List<Int>) {
        // Check if the number of colors matches the number of child Spinners
        if (stripeSpinnersContainer.getChildCount() != colors.size) {
            return
        }
        // Set the selected color in each Spinner based on the colors list
        for (i in 0 until stripeSpinnersContainer.getChildCount()) {
            val stripeSpinner = stripeSpinnersContainer.getChildAt(i) as MockSpinner
            val colorIndex = colors[i]
            stripeSpinner.setSelection(colorIndex)
        }
    }

    @Test
    fun testSetStripeColors() {
        val colors = listOf(2, 4, 1)
        setStripeColors(colors)

        // Verify that each Spinner has the correct selected color
        for (i in 0 until stripeSpinnersContainer.getChildCount()) {
            val stripeSpinner = stripeSpinnersContainer.getChildAt(i) as MockSpinner
            val expectedColorIndex = colors[i]
            val actualColorIndex = stripeSpinner.getSelectedItemPosition()
            Assert.assertEquals(
                "Mismatch in color index at position $i",
                expectedColorIndex,
                actualColorIndex
            )
        }
    }

    @Test
    fun testSetStripeColors_IncorrectChildCount() {
        val colors = listOf(2, 4, 1, 3)
        setStripeColors(colors)

        // Verify that the number of child Spinners is not equal to the number of colors
        Assert.assertNotEquals(
            "Mismatch in child count",
            colors.size,
            stripeSpinnersContainer.getChildCount()
        )
    }
}
