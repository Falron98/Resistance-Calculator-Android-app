package pjwstk.edu.pl.resistor_calculator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var btnCalculateResistance: Button
    private lateinit var btnShowPastResults: Button
    private lateinit var btnSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize buttons
        btnCalculateResistance = findViewById(R.id.btnCalculateResistance)
        btnShowPastResults = findViewById(R.id.btnShowPastResults)
        btnSettings = findViewById(R.id.btnSettings)

        // Set click listeners for buttons
        btnCalculateResistance.setOnClickListener(this)
        btnShowPastResults.setOnClickListener(this)
        btnSettings.setOnClickListener(this)

        // Initialize night mode switch
        val switch = findViewById<SwitchCompat>(R.id.switch1)

        // Get night mode settings from SharedPreferences
        val sharedPreferences = getSharedPreferences("DarkMode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night", false)

        // Set switch state and night mode based on settings
        if (nightMode) {
            switch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Listen for switch state changes
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                // Day mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()
            } else {
                // Night mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
                editor.apply()
            }
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnCalculateResistance -> {
                    // Start resistance calculator activity
                    val intent = Intent(this, ChooseStripesActivity::class.java)
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
}
