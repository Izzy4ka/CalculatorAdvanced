package com.example.calculator.feature.main.model

enum class MainTabs(
    val position: Int,
) {
    CALCULATOR(0),
    CONVERTER(1),
    ;

    companion object {
        fun fromPosition(position: Int) =
            entries.find { it.position == position }
                ?: throw IllegalArgumentException("Invalid position: $position")
    }
}
