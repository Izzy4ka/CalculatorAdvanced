package com.example.calculator.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calculator.databinding.FragmentCalculatorBinding
import com.example.calculator.ui.adapter.CalculateAdapter
import androidx.core.view.isGone


class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorBinding
    private lateinit var adapter: CalculateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CalculateAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInitialUi()
        setupBtnListeners()
    }

    private fun setupBtnListeners() {
        binding.btnTransform.setOnClickListener {
            if (binding.groupScientific.isGone) {
                binding.groupScientific.visibility = View.VISIBLE
                binding.flowCalculate.setMaxElementsWrap(5)
            } else {
                binding.groupScientific.visibility = View.GONE
                binding.flowCalculate.setMaxElementsWrap(4)
            }
        }
    }

    private fun setupInitialUi() {
        binding.recyclerHistory.adapter = adapter
    }
}