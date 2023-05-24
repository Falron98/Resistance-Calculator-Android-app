package pjwstk.edu.pl.resistor_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var btnCalculateResistance: Button
    private lateinit var btnShowPastResults: Button
    private lateinit var btnSettings: Button
    private lateinit var tvPastResults: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCalculateResistance = findViewById(R.id.btnCalculateResistance)
        btnShowPastResults = findViewById(R.id.btnShowPastResults)
        btnSettings = findViewById(R.id.btnSettings)

        btnCalculateResistance.setOnClickListener(this)
        btnShowPastResults.setOnClickListener(this)
        btnSettings.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnCalculateResistance -> {
                    val intent = Intent(this, ResistanceCalculatorActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnShowPastResults -> {
                    val intent = Intent(this, PastResultsActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnSettings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}