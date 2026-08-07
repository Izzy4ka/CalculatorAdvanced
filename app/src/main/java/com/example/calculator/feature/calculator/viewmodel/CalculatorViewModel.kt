package com.example.calculator.feature.calculator.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class CalculatorViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isKeyboardExpanded: StateFlow<Boolean> =
        savedStateHandle.getStateFlow(KEY_IS_EXPANDED, false)

    fun toggleKeyboardExpansion() {
        savedStateHandle[KEY_IS_EXPANDED] = !isKeyboardExpanded.value
    }

    companion object {
        private const val KEY_IS_EXPANDED = "is_keyboard_expanded"
    }
}
