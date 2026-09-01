package com.example.calculator.feature.converter.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.calculator.R

enum class ConverterType {
    LENGTH, AREA, MASS, VOLUME, CURRENCY, TIME
}

data class ConverterCategoryUiModel(
    val type: ConverterType,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int
)

val converterCategories = listOf(
    ConverterCategoryUiModel(
        ConverterType.LENGTH,
        R.string.category_length,
        R.drawable.ic_category_length
    ),
    ConverterCategoryUiModel(
        ConverterType.AREA,
        R.string.category_area,
        R.drawable.ic_category_area
    ),
    ConverterCategoryUiModel(
        ConverterType.MASS,
        R.string.category_mass,
        R.drawable.ic_cagetory_mass
    ),
    ConverterCategoryUiModel(
        ConverterType.VOLUME,
        R.string.category_volume,
        R.drawable.ic_category_volume
    ),
    ConverterCategoryUiModel(
        ConverterType.CURRENCY,
        R.string.category_currency,
        R.drawable.ic_category_currency
    ),
    ConverterCategoryUiModel(
        ConverterType.TIME,
        R.string.category_time,
        R.drawable.ic_category_time
    )
)