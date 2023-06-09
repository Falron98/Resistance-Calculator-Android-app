package pjwstk.edu.pl.resistor_calculator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnCalculateResistance: Button
    private lateinit var btnShowPastResults: Button
    private lateinit var btnSettings: Button
    private lateinit var btnCalculateParallelAndSeries: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize buttons
        btnCalculateResistance = findViewById(R.id.btnCalculateResistance)
        btnShowPastResults = findViewById(R.id.btnShowPastResults)
        btnSettings = findViewById(R.id.btnSettings)
        btnCalculateParallelAndSeries = findViewById(R.id.btnCalculateParallelAndSeries)

        // Set click listeners for buttons
        btnCalculateResistance.setOnClickListener(this)
        btnShowPastResults.setOnClickListener(this)
        btnSettings.setOnClickListener(this)
        btnCalculateParallelAndSeries.setOnClickListener(this)

        // Get night mode settings from SharedPreferences
        val sharedPreferences = getSharedPreferences("DarkMode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)

        // Set night mode based on settings
        updateNightMode(nightMode)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnCalculateResistance -> {
                    // Start resistance calculator activity
                    val intent = Intent(this, ChooseStripesActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnCalculateParallelAndSeries -> {
                    // Start resistance calculate parallel and series
                    val intent = Intent(this, ResistanceCalculatorParallelAndSeriesActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnShowPastResults -> {
                    // Start past results activity
                    val intent = Intent(this, PastResultsActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnSettings -> {
                    // Start settings activity
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
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
