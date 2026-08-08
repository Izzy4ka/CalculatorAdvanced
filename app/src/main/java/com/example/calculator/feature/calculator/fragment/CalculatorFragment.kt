package com.example.calculator.feature.calculator.fragment

import android.os.Bundle
import android.util.TypedValue
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calculator.R
import com.example.calculator.core.utils.TextSafeTransition
import com.example.calculator.databinding.FragmentCalculatorBinding
import com.example.calculator.feature.calculator.viewmodel.CalculatorViewModel
import com.example.calculator.feature.calculator.adapter.CalculateAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding is null" }

    private val adapter by lazy { CalculateAdapter() }
    private val viewModel by viewModels<CalculatorViewModel>()

    private val isExpandableMode by lazy {
        resources.getBoolean(R.bool.is_keyboard_expandable)
    }

    private val defaultTextSizes = mutableMapOf<Int, Float>()
    private val defaultIconSizes = mutableMapOf<Int, Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupHistory()
        setupListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        if (!isExpandableMode) {
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isKeyboardExpanded.collect(::updateKeyboardState)
            }
        }
    }

    private fun setupListeners() {
        setupKeyboardListeners()

        if (!isExpandableMode) {
            return
        }

        setupExpandableListeners()
    }

    private fun setupExpandableListeners() {
        binding.btnExpand.setOnClickListener {
            viewModel.toggleKeyboardExpansion()
        }
    }

    private fun setupKeyboardListeners() {
        with(binding) {
            btn0.setOnClickListener {

            }
        }
    }

    private fun setupHistory() {
        binding.rvHistory.adapter = adapter
    }

    private fun updateKeyboardState(isExpanded: Boolean) {
        if (binding.groupScientific.isVisible == isExpanded) return

        prepareKeyboardTransition()

        applyKeyboardConfiguration(isExpanded)
    }

    private fun prepareKeyboardTransition() {
        val transition = TransitionSet().apply {

            addTransition(TextSafeTransition())

            addTransition(Fade())

            duration = ANIMATION_DURATION_MS
            interpolator = AccelerateDecelerateInterpolator()
        }
        TransitionManager.beginDelayedTransition(binding.root, transition)
    }

    private fun applyKeyboardConfiguration(isExpanded: Boolean) {
        binding.groupScientific.isVisible = isExpanded

        binding.keyboardFlow.apply {
            setMaxElementsWrap(
                if (isExpanded) EXPANDED_FLOW_WRAP_COUNT else COLLAPSED_FLOW_WRAP_COUNT
            )

            (layoutParams as ConstraintLayout.LayoutParams).apply {
                dimensionRatio = if (isExpanded) EXPANDED_RATIO else COLLAPSED_RATIO
            }
        }

        updateButtonContentSizes(isExpanded)
    }

    private fun updateButtonContentSizes(isExpanded: Boolean) {
        binding.keyboardFlow.referencedIds.forEach { id ->
            val button = binding.root.findViewById<MaterialButton>(id)
                ?: return@forEach

            val originalTextSize = defaultTextSizes.getOrPut(id) {
                button.textSize
            }

            button.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                if (isExpanded) {
                    originalTextSize * CONTENT_SCALE_RATIO
                } else {
                    originalTextSize
                },
            )

            button.icon?.let {
                val originalIconSize = defaultIconSizes.getOrPut(id) {
                    button.iconSize
                }

                button.iconSize = if (isExpanded) {
                    (originalIconSize * CONTENT_SCALE_RATIO).toInt()
                } else {
                    originalIconSize
                }
            }
        }
    }

    private companion object {
        const val ANIMATION_DURATION_MS: Long = 300
        const val CONTENT_SCALE_RATIO = 0.8f

        const val COLLAPSED_FLOW_WRAP_COUNT = 4
        const val EXPANDED_FLOW_WRAP_COUNT = 5

        const val COLLAPSED_RATIO = "4:5"
        const val EXPANDED_RATIO = "5:7"
    }
}