package com.example.calculator.ui.fragment

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.calculator.databinding.FragmentCalculatorBinding
import com.example.calculator.ui.adapter.CalculateAdapter
import androidx.core.view.isGone
import androidx.core.view.isVisible


class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorBinding
    private val adapter by lazy { CalculateAdapter() }

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
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnExpand.setOnClickListener {
            toggleScientificKeyboard()
        }
    }

    private fun toggleScientificKeyboard() {
        val isCurrentlyHidden = binding.groupScientific.isGone

        val transition = TransitionSet().apply {
            addTransition(ChangeBounds())
            addTransition(Fade())
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
        }

        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)

        binding.groupScientific.isVisible = isCurrentlyHidden
        binding.keyboardFlow.setMaxElementsWrap(if (isCurrentlyHidden) 5 else 4)
        (binding.keyboardFlow.layoutParams as ConstraintLayout.LayoutParams).apply {
            matchConstraintPercentHeight = if (isCurrentlyHidden) 0.65f else 0.45f
        }
    }

    private fun setupInitialUi() {
        binding.rvHistory.adapter = adapter
    }
}