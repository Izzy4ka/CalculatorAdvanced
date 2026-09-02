package com.example.calculator.feature.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.calculator.feature.calculator.fragment.CalculatorFragment
import com.example.calculator.feature.converter.fragment.ConverterMenuFragment
import com.example.calculator.feature.main.model.MainTabs

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment =
        when (MainTabs.fromPosition(position)) {
            MainTabs.CALCULATOR -> CalculatorFragment()
            MainTabs.CONVERTER -> ConverterMenuFragment()
        }

    override fun getItemCount(): Int = MainTabs.entries.size
}
