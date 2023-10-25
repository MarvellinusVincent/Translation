package com.example.translation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

/**
 * This is the shared view model class between the edit text fragment and the main activity
 */
class SharedViewModel: ViewModel() {

    /**
     * Initializing the variables
     */
    var sourceLanguage: String = ""
    var targetLanguage: String = ""
    var translatedText: MutableLiveData<String> = MutableLiveData()
    private var okayToTranslate: Boolean = false

    /**
     * Function to translate the text from the source language to the targeted language
     */
    fun translateText(inputText: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()
        val translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        Log.d("SharedViewModel", "Download check, $sourceLanguage")
        Log.d("SharedViewModel", "Download check, $targetLanguage")
        // Download the required models for the new source and target languages if needed
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Log.d("SharedViewModel", "Download successful, $sourceLanguage")
                Log.d("SharedViewModel", "Download successful, $targetLanguage")
                // Model downloaded successfully, it's okay to translate
                okayToTranslate = true

                // Once the model is downloaded, perform the translation
                translator.translate(inputText)
                    .addOnSuccessListener { translatedText ->
                        this.translatedText.postValue(translatedText)
                    }
                    .addOnFailureListener { exception ->
                        // Handle translation failure
                    }
            }
            .addOnFailureListener {
                Log.d("SharedViewModel", "Download failed, $sourceLanguage")
                Log.d("SharedViewModel", "Download failed, $targetLanguage")
                // Handle model download failure
            }
    }

}