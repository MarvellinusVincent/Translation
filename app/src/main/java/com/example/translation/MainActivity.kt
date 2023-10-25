package com.example.translation

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.translation.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.mlkit.nl.translate.TranslateLanguage
import java.util.Locale

/**
 * This is the main activity class
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel

    /**
     * This is the onCreate function for the class
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /**
         * This puts the edit text fragment to the fragment container in the main activity class
         */
        if (savedInstanceState == null) {
            val editTextFragment = EditTextFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, editTextFragment)
                .commit()
        }

        /**
         * This accesses the shared view model to change the text view of the translation to the actual translation
         */
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.translatedText.observe(this){text->
            binding.translatedTextView.text = text
        }
        val languagesList = loadLanguages().toMutableList()
        languagesList.add(0, Languages("", "Select Source Language"))
        languagesList.add(1, Languages("", "Select Target Language"))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languagesList.map { it.languageTitle })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val sourceLanguageSpinner = findViewById<Spinner>(R.id.sourceLanguageSpinner)
        val targetLanguageSpinner = findViewById<Spinner>(R.id.targetLanguageSpinner)
        val switchButton = findViewById<MaterialButton>(R.id.buttonSwitchLang)
        sourceLanguageSpinner.adapter = adapter
        targetLanguageSpinner.adapter = adapter
        sourceLanguageSpinner.setSelection(0, false)
        targetLanguageSpinner.setSelection(1, false)
        sourceLanguageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguageCode = languagesList[position].languageCode
                Log.d("MainActivity", "sourceLanguageCode = $selectedLanguageCode")
                sharedViewModel.sourceLanguage = selectedLanguageCode
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        targetLanguageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguageCode = languagesList[position].languageCode
                sharedViewModel.targetLanguage = selectedLanguageCode
                Log.d("MainActivity", "targetLanguageCode = $selectedLanguageCode")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        switchButton.setOnClickListener {
            val currentSourceLanguage = sourceLanguageSpinner.selectedItemPosition
            val currentTargetLanguage = targetLanguageSpinner.selectedItemPosition
            sourceLanguageSpinner.setSelection(currentTargetLanguage, true)
            targetLanguageSpinner.setSelection(currentSourceLanguage, true)
        }
    }
    private fun loadLanguages(): List<Languages> {
        val languages = ArrayList<Languages>()
        val languageCodeList = TranslateLanguage.getAllLanguages()
        for (languageCode in languageCodeList) {
            val languageTitle = Locale(languageCode).displayLanguage
            Log.d(TAG, "loadAvailableLanguages: languageCode: $languageCode")
            Log.d(TAG, "loadAvailableLanguages: languageTitle: $languageTitle")
            val modelLanguage = Languages(languageCode, languageTitle)
            languages.add(modelLanguage)
        }
        return languages
    }

}