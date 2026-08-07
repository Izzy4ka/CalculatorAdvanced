package com.example.calculator.feature.calculator.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel : ViewModel() {
    private val _isKeyboardExpanded = MutableStateFlow(false)

    val isKeyboardExpanded: StateFlow<Boolean> = _isKeyboardExpanded.asStateFlow()

    fun toggleKeyboardExpansion() {
        _isKeyboardExpanded.value = !_isKeyboardExpanded.value
    }
}
