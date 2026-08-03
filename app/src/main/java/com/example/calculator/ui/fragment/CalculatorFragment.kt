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
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.calculator.databinding.FragmentCalculatorBinding
import com.example.calculator.ui.adapter.CalculateAdapter

class CalculatorFragment : Fragment() {
    private lateinit var binding: FragmentCalculatorBinding
    private val adapter by lazy { CalculateAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
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

        val transition =
            TransitionSet().apply {
                addTransition(ChangeBounds())
                addTransition(Fade())
                duration = ANIMATION_DURATION_MS
                interpolator = AccelerateDecelerateInterpolator()
            }

        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)

        binding.groupScientific.isVisible = isCurrentlyHidden
        binding.keyboardFlow.setMaxElementsWrap(
            if (isCurrentlyHidden) {
                EXPANDED_FLOW_WRAP_COUNT
            } else {
                COLLAPSED_FLOW_WRAP_COUNT
            },
        )
        (binding.keyboardFlow.layoutParams as ConstraintLayout.LayoutParams).apply {
            matchConstraintPercentHeight =
                if (isCurrentlyHidden) EXPANDED_FLOW_HEIGHT_PERCENT else COLLAPSED_FLOW_HEIGHT_PERCENT
        }
    }

    private fun setupInitialUi() {
        binding.rvHistory.adapter = adapter
    }

    private companion object {
        const val ANIMATION_DURATION_MS: Long = 300

        const val COLLAPSED_FLOW_WRAP_COUNT = 4
        const val EXPANDED_FLOW_WRAP_COUNT = 5

        const val COLLAPSED_FLOW_HEIGHT_PERCENT = 0.55f
        const val EXPANDED_FLOW_HEIGHT_PERCENT = 0.65f
    }
}
