package com.example.translation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.translation.databinding.FragmentEditTextBinding

/**
 * This is the edit text fragment for the user to type the text
 */
class EditText : Fragment() {

    lateinit var sharedViewModel: SharedViewModel
    private var _binding: FragmentEditTextBinding? = null
    private val binding get() = _binding!!

    /**
     * This is to initialize the edit text fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTextBinding.inflate(inflater, container, false)
        val view = binding.root
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val editText = binding.editText

        /**
         * This is called when the user inputs a text
         */
        editText.addTextChangedListener(object:TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedViewModel.translateText(binding.editText.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        return view
    }

    /**
     * This is to destroy the view
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}