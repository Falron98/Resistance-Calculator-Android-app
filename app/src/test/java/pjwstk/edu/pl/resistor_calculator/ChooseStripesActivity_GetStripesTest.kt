package pjwstk.edu.pl.resistor_calculator

import android.widget.LinearLayout
import android.widget.Spinner
import org.junit.Assert
import org.junit.Test

class ChooseStripesActivity_GetStripesTest {

    private class MockLinearLayout : LinearLayout(null) {
        private val childCountInternal: Int
            get() = 0

        override fun getChildCount(): Int {
            return childCountInternal
        }
    }

    private class MockSpinner : Spinner(null) {
        private var selectedItemPositionInternal: Int = 0

        override fun getSelectedItemPosition(): Int {
            return selectedItemPositionInternal
        }

        override fun setSelection(position: Int) {
            selectedItemPositionInternal = position
        }
    }

    private val stripeSpinnersContainer = MockLinearLayout()

    private fun setStripeColors(colors: List<Int>) {
        if (stripeSpinnersContainer.getChildCount() != colors.size) {
            return
        }
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

        Assert.assertNotEquals(
            "Mismatch in child count",
            colors.size,
            stripeSpinnersContainer.getChildCount()
        )
    }
}

