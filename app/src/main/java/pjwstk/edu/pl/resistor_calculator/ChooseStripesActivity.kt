package pjwstk.edu.pl.resistor_calculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class ChooseStripesActivity : AppCompatActivity() {

    private lateinit var stripeNumberSpinner: Spinner
    private lateinit var stripeSpinnersContainer: LinearLayout
    private lateinit var submitButton: Button

    private val normalColorOptions = arrayOf(
        "None", "Black", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Violet", "Gray", "White"
    )

    private val multiplierColorOptions = arrayOf(
        "None", "Black", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Violet", "Gray", "White", "Silver", "Gold"
    )

    private val toleranceColorOptions = arrayOf(
        "None", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Violet", "Gray", "Silver", "Gold"
    )

    private val temperatureColorOptions = arrayOf(
        "None", "Black", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Violet", "Gray"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_stripes)

        stripeNumberSpinner = findViewById(R.id.stripeNumberSpinner)
        val stripeOptions = arrayOf("3 Stripes", "4 Stripes", "5 Stripes", "6 Stripes")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stripeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stripeNumberSpinner.adapter = adapter

        stripeSpinnersContainer = findViewById(R.id.stripeSpinnersContainer)
        submitButton = findViewById(R.id.submitButton)

        stripeNumberSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedStripeOption = stripeOptions[position]
                val numStripes = selectedStripeOption.split(" ")[0].toInt()

                stripeSpinnersContainer.removeAllViews()

                for (i in 1..numStripes) {
                    val stripeSpinner = Spinner(this@ChooseStripesActivity)
                    val stripeAdapter = ArrayAdapter(this@ChooseStripesActivity, android.R.layout.simple_spinner_item, getStripeColorOptions(i, numStripes))
                    stripeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    stripeSpinner.adapter = stripeAdapter

                    stripeSpinnersContainer.addView(stripeSpinner)

                    stripeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            checkSubmitButtonState()
                            val selectedColor = stripeAdapter.getItem(position).toString()
                            Toast.makeText(applicationContext, "Selected Color: $selectedColor", Toast.LENGTH_SHORT).show()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Do nothing
                        }
                    }
                }

                checkSubmitButtonState()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        submitButton.setOnClickListener {
            val selectedStripes = mutableListOf<String>()

            for (i in 0 until stripeSpinnersContainer.childCount) {
                val stripeSpinner = stripeSpinnersContainer.getChildAt(i) as Spinner
                val selectedColor = stripeSpinner.selectedItem.toString()
                selectedStripes.add(selectedColor)
            }

            val intent = Intent(this, CalculateResistanceActivity::class.java)
            intent.putStringArrayListExtra("stripes", ArrayList(selectedStripes))
            startActivity(intent)
        }
    }

    private fun checkSubmitButtonState() {
        var isSubmitButtonEnabled = true

        for (i in 0 until stripeSpinnersContainer.childCount) {
            val stripeSpinner = stripeSpinnersContainer.getChildAt(i) as Spinner
            val selectedColor = stripeSpinner.selectedItem.toString()
            if (selectedColor == "None") {
                isSubmitButtonEnabled = false
                break
            }
        }

        submitButton.isEnabled = isSubmitButtonEnabled

        if (isSubmitButtonEnabled) {
            submitButton.alpha = 1.0f
        } else {
            submitButton.alpha = 0.5f
        }
    }

    private fun getStripeColorOptions(position: Int, numStripes: Int): Array<String> {
        return when (numStripes) {
            3 -> {
                when (position) {
                    1, 2 -> normalColorOptions
                    3 -> multiplierColorOptions
                    else -> emptyArray()
                }
            }
            4 -> {
                when (position) {
                    1, 2 -> normalColorOptions
                    3 -> multiplierColorOptions
                    4 -> toleranceColorOptions
                    else -> emptyArray()
                }
            }
            5 -> {
                when (position) {
                    1, 2, 3 -> normalColorOptions
                    4 -> multiplierColorOptions
                    5 -> toleranceColorOptions
                    else -> emptyArray()
                }
            }
            6 -> {
                when (position) {
                    1, 2, 3 -> normalColorOptions
                    4 -> multiplierColorOptions
                    5 -> toleranceColorOptions
                    6 -> temperatureColorOptions
                    else -> emptyArray()
                }
            }
            else -> emptyArray()
        }
    }
}
