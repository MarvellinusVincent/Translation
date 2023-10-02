package com.example.translation

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

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                okayToTranslate = true
            }
            .addOnFailureListener {
            }
        if (okayToTranslate) {
            translator.translate(inputText)
                .addOnSuccessListener { translatedText ->
                    this.translatedText.postValue(translatedText)
                }
                .addOnFailureListener { exception ->
                }
        }
    }
}