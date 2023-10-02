package com.example.translation

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.translation.databinding.ActivityMainBinding

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
            val editTextFragment = EditText()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, editTextFragment)
                .commit()
        }

        /**
         * This accesses the shared view model to change the text view of the translation to the actual translation
         */
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.translatedText.observe(this){text->
              binding.translationTextView.text = text
        }

        /**
         * This puts the chosen source language to the shared view model
         */
        binding.Language.setOnCheckedChangeListener() { _, checkedSourceId ->
            val sourceLanguage = when(view.findViewById<RadioButton>(checkedSourceId).text) {
                "English" -> "en"
                "Spanish" -> "es"
                "German" -> "de"
                else -> "es"
            }
            sharedViewModel.sourceLanguage = sourceLanguage
        }

        /**
         * This puts the chosen targeted language to the shared view model
         */
        binding.Translation.setOnCheckedChangeListener() { _, checkedTranslationId ->
            val targetLanguage = when(view.findViewById<RadioButton>(checkedTranslationId).text) {
                "English" -> "en"
                "Spanish" -> "es"
                "German" -> "de"
                else -> "es"
            }
            sharedViewModel.targetLanguage = targetLanguage
        }
    }
}