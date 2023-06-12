package pjwstk.edu.pl.resistor_calculator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize night mode switch
        val switch = findViewById<SwitchCompat>(R.id.switch1)

        // Get night mode settings from SharedPreferences
        val sharedPreferences = getSharedPreferences("DarkMode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night", false)

        // Set switch state based on settings
        switch.isChecked = nightMode

        // Listen for switch state changes
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            // Save the state of the switch in SharedPreferences
            editor.putBoolean("night", isChecked)
            editor.apply()

            // Update the night mode in MainActivity
            updateNightMode(isChecked)
        }
    }

    private fun updateNightMode(nightModeEnabled: Boolean) {
        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
