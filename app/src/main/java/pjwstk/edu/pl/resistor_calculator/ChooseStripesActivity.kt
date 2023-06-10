package pjwstk.edu.pl.resistor_calculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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
    private val normalColorOptions = arrayOf(
        "None",
        "Black",
        "Brown",
        "Red",
        "Orange",
        "Yellow",
        "Green",
        "Blue",
        "Violet",
        "Gray",
        "White"
    )
    private val multiplierColorOptions = arrayOf(
        "None",
        "Black",
        "Brown",
        "Red",
        "Orange",
        "Yellow",
        "Green",
        "Blue",
        "Violet",
        "Gray",
        "White",
        "Silver",
        "Gold"
    )
    private val toleranceColorOptions = arrayOf(
        "None",
        "Brown",
        "Red",
        "Orange",
        "Yellow",
        "Green",
        "Blue",
        "Violet",
        "Gray",
        "Silver",
        "Gold"
    )
    private val temperatureColorOptions = arrayOf(
        "None", "Black", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Violet", "Gray"
    )
    private val savedConfigurations = mutableListOf<Pair<String, List<String>>>()

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
                        getStripeColorOptions(i, numStripes)
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
            val selectedStripes = mutableListOf<String>()
            for (i in 0 until stripeSpinnersContainer.childCount) {
                val stripeSpinner = stripeSpinnersContainer.getChildAt(i) as Spinner
                val selectedColor = stripeSpinner.selectedItem.toString()
                selectedStripes.add(selectedColor)
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
            val selectedConfigurationIndex = configurationsSpinner.selectedItemPosition
            if (selectedConfigurationIndex != AdapterView.INVALID_POSITION) {
                val selectedStripes = savedConfigurations[selectedConfigurationIndex].second
                val numSelectedStripes = selectedStripes.size
                val numStripeSpinners = stripeSpinnersContainer.childCount
                val numExpectedStripes = when (numStripeSpinners) {
                    5 -> numStripeSpinners + 1 // Dodaj 1 dla paska temperaturowego
                    else -> numStripeSpinners
                }
                if (numSelectedStripes == numExpectedStripes) {
                    setStripeColors(selectedStripes)
                } else {
                    Toast.makeText(this, "Invalid configuration", Toast.LENGTH_SHORT).show()
                }
            }
        }
        loadConfigurationsFromFile()
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

    private fun updateConfigurationsSpinner() {
        // Pobierz nazwy konfiguracji
        val configurationNames = savedConfigurations.map { it.first }
        // Utwórz adapter dla spinnera
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, configurationNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Ustaw adapter dla spinnera konfiguracji
        configurationsSpinner.adapter = adapter
    }

    private fun setStripeColors(colors: List<String>) {
        // Ustaw kolory dla poszczególnych spinnerów
        for (i in 0 until stripeSpinnersContainer.childCount) {
            val stripeSpinner = stripeSpinnersContainer.getChildAt(i) as Spinner
            val colorIndex = getColorIndex(colors[i], i + 1)
            stripeSpinner.setSelection(colorIndex)
        }
    }

    private fun getColorIndex(color: String, position: Int): Int {
        return when (position) {
            1, 2, 3 -> normalColorOptions.indexOf(color)
            4 -> multiplierColorOptions.indexOf(color)
            5 -> toleranceColorOptions.indexOf(color)
            6 -> temperatureColorOptions.indexOf(color)
            else -> -1
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
                objectInputStream.readObject() as MutableList<Pair<String, List<String>>>
            // Wyczyść bieżące konfiguracje i dodaj wczytane
            savedConfigurations.clear()
            savedConfigurations.addAll(configurations)
            objectInputStream.close()
            fileInputStream.close()
            // Zaktualizuj spinner konfiguracji
            updateConfigurationsSpinner()
        }
    }
}