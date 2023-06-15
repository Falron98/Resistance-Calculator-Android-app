package pjwstk.edu.pl.resistor_calculator
import android.graphics.Color
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import java.io.*

class ChooseStripesActivity : AppCompatActivity() {
    private lateinit var stripeNumberSpinner: Spinner
    private lateinit var stripeSpinnersContainer: LinearLayout
    private lateinit var submitButton: Button
    private lateinit var saveButton: Button
    private lateinit var loadButton: Button
    private lateinit var deleteButton: Button
    private lateinit var configurationsSpinner: Spinner
    private lateinit var configurationNameEditText: EditText
    private val colorOptions = arrayOf(
        "None", "Black", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Gray", "White", "Silver", "Gold"
    )

    private val savedConfigurations = mutableListOf<Pair<String, List<Int>>>()

    @SuppressLint("MissingInflatedId")
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
        saveButton = findViewById(R.id.saveButton)
        loadButton = findViewById(R.id.loadButton)
        deleteButton = findViewById(R.id.deleteButton)
        configurationsSpinner = findViewById(R.id.configurationsSpinner)
        configurationNameEditText = findViewById(R.id.configurationNameEditText)
        stripeNumberSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedStripeOption = stripeOptions[position]
                val numStripes = selectedStripeOption.split(" ")[0].toInt()
                stripeSpinnersContainer.removeAllViews()
                for (i in 1..numStripes) {
                    val stripeSpinner = Spinner(this@ChooseStripesActivity)
                    val stripeAdapter = ArrayAdapter(
                        this@ChooseStripesActivity,
                        android.R.layout.simple_spinner_item,
                        colorOptions
                    )
                    stripeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    stripeSpinner.adapter = stripeAdapter
                    stripeSpinnersContainer.addView(stripeSpinner)
                    stripeSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {

                                checkSubmitButtonState()
                                val selectedColor = stripeAdapter.getItem(position).toString()
                                val backgroundColor = getColorForStripe(selectedColor)
                                stripeSpinner.setBackgroundColor(backgroundColor)
                                Toast.makeText(
                                    applicationContext,
                                    "Selected Color: $selectedColor",
                                    Toast.LENGTH_SHORT
                                ).show()
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
        saveButton.setOnClickListener {
            val selectedStripes = mutableListOf<Int>()

            for (i in 0 until stripeSpinnersContainer.childCount) {
                val stripeSpinner = stripeSpinnersContainer.getChildAt(i) as Spinner
                val selectedColorIndex = stripeSpinner.selectedItemPosition
                selectedStripes.add(selectedColorIndex)
            }

            val configurationName = configurationNameEditText.text.toString()
            savedConfigurations.add(Pair(configurationName, selectedStripes.toList()))
            updateConfigurationsSpinner()
            saveConfigurationsToFile()
        }

        deleteButton.setOnClickListener {
            val selectedConfigurationIndex = configurationsSpinner.selectedItemPosition
            if (selectedConfigurationIndex != AdapterView.INVALID_POSITION) {
                savedConfigurations.removeAt(selectedConfigurationIndex)
                updateConfigurationsSpinner()
                saveConfigurationsToFile()
                Toast.makeText(this, "Configuration deleted", Toast.LENGTH_SHORT).show()
            }
        }

        loadButton.setOnClickListener {
            val stripeOptions = arrayOf("3 Stripes", "4 Stripes", "5 Stripes", "6 Stripes")
            val selectedConfigurationIndex = configurationsSpinner.selectedItemPosition
            if (selectedConfigurationIndex != AdapterView.INVALID_POSITION) {
                val selectedConfiguration = savedConfigurations[selectedConfigurationIndex]
                val numStripes = selectedConfiguration.second.size
                val stripeOption = "$numStripes Stripes"
                val stripeOptionIndex = stripeOptions.indexOf(stripeOption)
                stripeNumberSpinner.setSelection(stripeOptionIndex)

                val colors = selectedConfiguration.second
                setStripeColors(colors)
            }
        }


        loadConfigurationsFromFile()
    }
    private fun loadConfigurationColors() {
        val selectedConfigurationIndex = configurationsSpinner.selectedItemPosition
        if (selectedConfigurationIndex != AdapterView.INVALID_POSITION) {
            val selectedConfiguration = savedConfigurations[selectedConfigurationIndex]
            val colors = selectedConfiguration.second
            setStripeColors(colors)
        }
    }
    private fun getColorForStripe(color: String): Int {
        return when (color) {
            "None" -> Color.TRANSPARENT
            "Black" -> ContextCompat.getColor(this, R.color.black)
            "Brown" -> ContextCompat.getColor(this, R.color.brown)
            "Red" -> ContextCompat.getColor(this, R.color.red)
            "Orange" -> ContextCompat.getColor(this, R.color.orange)
            "Yellow" -> ContextCompat.getColor(this, R.color.yellow)
            "Green" -> ContextCompat.getColor(this, R.color.green)
            "Blue" -> ContextCompat.getColor(this, R.color.blue)
            "Purple" -> ContextCompat.getColor(this, R.color.purple)
            "Gray" -> ContextCompat.getColor(this, R.color.gray)
            "White" -> ContextCompat.getColor(this, R.color.white)
            "Silver" -> ContextCompat.getColor(this, R.color.silver)
            "Gold" -> ContextCompat.getColor(this, R.color.gold)
            else -> Color.TRANSPARENT
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



    private fun updateConfigurationsSpinner() {
        // Pobierz nazwy konfiguracji
        val configurationNames = savedConfigurations.map { it.first }
        // Utwórz adapter dla spinnera
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, configurationNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Ustaw adapter dla spinnera konfiguracji
        configurationsSpinner.adapter = adapter
    }

    private fun setStripeColors(colors: List<Int>) {
        if (stripeSpinnersContainer.childCount != colors.size) {
            return
        }

        for (i in 0 until stripeSpinnersContainer.childCount) {
            val stripeSpinner = stripeSpinnersContainer.getChildAt(i) as Spinner
            val colorIndex = colors[i]
            if (colorIndex >= 0 && colorIndex < colorOptions.size) {
                stripeSpinner.setSelection(colorIndex)
            } else {
                stripeSpinner.setSelection(0)
            }
        }
    }


    private fun saveConfigurationsToFile() {
        // Zapisz konfiguracje do pliku
        val file = File(applicationContext.filesDir, "configurations.txt")
        val fileOutputStream = FileOutputStream(file)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)

        objectOutputStream.writeObject(savedConfigurations)

        objectOutputStream.close()
        fileOutputStream.close()
    }

    private fun loadConfigurationsFromFile() {
        // Wczytaj konfiguracje z pliku
        val file = File(applicationContext.filesDir, "configurations.txt")

        if (file.exists()) {
            val fileInputStream = FileInputStream(file)
            val objectInputStream = ObjectInputStream(fileInputStream)

            val configurations =
                objectInputStream.readObject() as MutableList<Pair<String, List<Int>>>

            // Wyczyść bieżące konfiguracje i dodaj wczytane
            savedConfigurations.clear()
            savedConfigurations.addAll(configurations)

            objectInputStream.close()
            fileInputStream.close()
        }

        updateConfigurationsSpinner()
    }
}