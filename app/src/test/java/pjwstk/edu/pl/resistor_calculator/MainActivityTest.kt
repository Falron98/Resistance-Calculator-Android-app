package pjwstk.edu.pl.resistor_calculator

import androidx.appcompat.app.AppCompatDelegate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class MainActivityTest {

    @Test
    fun testUpdateNightMode() {
        // Create an instance of MainActivity to access its functions
        val mainActivity = MainActivity()

        // Set the night mode enabled
        mainActivity.updateNightMode(true)

        // Verify that the night mode is set to MODE_NIGHT_YES
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, AppCompatDelegate.getDefaultNightMode())

        // Set the night mode disabled
        mainActivity.updateNightMode(false)

        // Verify that the night mode is set to MODE_NIGHT_NO
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, AppCompatDelegate.getDefaultNightMode())
    }
}
