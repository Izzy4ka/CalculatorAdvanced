package com.example.calculator.feature.main.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.calculator.feature.main.model.TabMode
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val currentTab: StateFlow<TabMode> =
        savedStateHandle.getStateFlow(KEY_CURRENT_TAB, TabMode.CALCULATOR)

    private fun setCurrentTab(tab: TabMode) {
        if (tab == currentTab.value) {
            return
        }

        savedStateHandle[KEY_CURRENT_TAB] = tab
    }

    fun selectCalculatorTab() {
        setCurrentTab(TabMode.CALCULATOR)
    }

    fun selectConverterTab() {
        setCurrentTab(TabMode.CONVERTER)
    }

    companion object {
        private const val KEY_CURRENT_TAB = "current_tab"
    }
}
