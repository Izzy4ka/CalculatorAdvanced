package com.example.calculator.feature.calculator.fragment

import android.os.Bundle
import android.util.TypedValue
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
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.example.calculator.R
import com.example.calculator.core.utils.TextSafeTransition
import com.example.calculator.databinding.FragmentCalculatorBinding
import com.example.calculator.feature.calculator.adapter.CalculateAdapter
import com.example.calculator.feature.calculator.view.PullToHistoryLayout
import com.example.calculator.feature.calculator.viewmodel.CalculatorViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions", "ktlint:standard:backing-property-naming")
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

    private val colorHintGray by lazy { requireContext().getColor(R.color.hint_gray) }
    private val colorHintActive by lazy { requireContext().getColor(R.color.hint_active_orange) }
    private val textPullToHistory by lazy { getString(R.string.pull_to_history) }
    private val textReleaseForHistory by lazy { getString(R.string.release_for_history) }
    private val hintStartOffsetPx by lazy {
        -resources.getDimension(R.dimen.hint_start_offset)
    }

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

        if (isExpandableMode) {
            setupExpandableFeatures()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupExpandableFeatures() {
        setupExpandableListeners()
        setupPullToHistoryLogic()
        observeExpandableViewModel()
    }

    private fun observeExpandableViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isKeyboardExpanded.collect(::updateKeyboardState)
            }
        }
    }

    private fun setupPullToHistoryLogic() {
        val historyView = binding.rvHistory ?: return

        val displayHeightPx = resources.displayMetrics.heightPixels.toFloat()
        val hintRevealPx = displayHeightPx * HINT_REVEAL_HEIGHT_RATIO
        val dragThresholdPx = displayHeightPx * DRAG_THRESHOLD_HEIGHT_RATIO

        val config =
            PullToHistoryLayout.Config(
                scrollTarget = historyView,
                dragThresholdPx = dragThresholdPx,
                onDragUpdate = { currentTranslation ->
                    applyDragUiState(currentTranslation, hintRevealPx, dragThresholdPx)
                },
                onDragActionFired = {
                },
            )

        binding.pullToHistoryLayout?.setup(config)
    }

    private fun setupExpandableListeners() {
        binding.btnExpand.setOnClickListener {
            viewModel.toggleKeyboardExpansion()
        }
    }

    private fun setupHistory() {
        binding.rvHistory?.adapter = adapter
    }

    private fun updateKeyboardState(isExpanded: Boolean) {
        if (binding.groupScientific.isVisible == isExpanded) {
            return
        }

        prepareKeyboardTransition()

        applyKeyboardConfiguration(isExpanded)
    }

    private fun prepareKeyboardTransition() {
        val transition =
            TransitionSet().apply {
                addTransition(TextSafeTransition())

                addTransition(Fade())

                duration = resources.getInteger(R.integer.calc_animation_duration).toLong()
                interpolator = AccelerateDecelerateInterpolator()
            }
        TransitionManager.beginDelayedTransition(binding.root, transition)
    }

    private fun applyKeyboardConfiguration(isExpanded: Boolean) {
        binding.groupScientific.isVisible = isExpanded

        val wrapCountRes = if (isExpanded) {
            R.integer.calc_flow_wrap_count_expanded
        } else {
            R.integer.calc_flow_wrap_count_collapsed
        }

        val ratioRes = if (isExpanded) {
            R.string.calc_flow_ratio_expanded
        } else {
            R.string.calc_flow_ratio_collapsed
        }

        binding.keyboardFlow.apply {
            setMaxElementsWrap(resources.getInteger(wrapCountRes))

            (layoutParams as ConstraintLayout.LayoutParams).apply {
                val ratioValue = getString(ratioRes)
                dimensionRatio = ratioValue.ifEmpty { null }
            }
        }

        updateButtonContentSizes(isExpanded)
    }

    private fun updateButtonContentSizes(isExpanded: Boolean) {
        binding.keyboardFlow.referencedIds.forEach { id ->
            val button =
                binding.root.findViewById<MaterialButton>(id)
                    ?: return@forEach

            val originalTextSize =
                defaultTextSizes.getOrPut(id) {
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
                val originalIconSize =
                    defaultIconSizes.getOrPut(id) {
                        button.iconSize
                    }

                button.iconSize =
                    if (isExpanded) {
                        (originalIconSize * CONTENT_SCALE_RATIO).toInt()
                    } else {
                        originalIconSize
                    }
            }
        }
    }

    private fun applyDragUiState(
        totalTranslation: Float,
        hintRevealPx: Float,
        dragThresholdPx: Float
    ) {
        if (totalTranslation <= hintRevealPx) {
            val progress = totalTranslation / hintRevealPx

            binding.tvPullHint?.translationY = hintStartOffsetPx * (1f - progress)
            binding.tvPullHint?.alpha = progress.coerceIn(0f, 1f)

            setPullTranslation(0f, applyToHint = false)
            setTvPullHint(textPullToHistory, colorHintGray)
            return
        }

        val extraScroll = totalTranslation - hintRevealPx
        val isThresholdReached = totalTranslation > dragThresholdPx

        binding.tvPullHint?.alpha = 1f
        setPullTranslation(extraScroll, applyToHint = true)

        if (isThresholdReached) {
            setTvPullHint(textReleaseForHistory, colorHintActive)
        } else {
            setTvPullHint(textPullToHistory, colorHintGray)
        }
    }

    private fun setTvPullHint(
        text: String,
        color: Int,
    ) {
        if (binding.tvPullHint?.text != text) {
            binding.tvPullHint?.text = text
        }

        if (binding.tvPullHint?.currentTextColor != color) {
            binding.tvPullHint?.setTextColor(color)
        }
    }

    private fun setPullTranslation(
        value: Float,
        applyToHint: Boolean,
    ) {
        binding.rvHistory?.translationY = value
        binding.tvPreResult.translationY = value
        binding.tvExpression.translationY = value

        if (applyToHint) {
            binding.tvPullHint?.translationY = value
        }
    }

    private companion object {
        private const val HINT_REVEAL_HEIGHT_RATIO = 0.05f
        private const val DRAG_THRESHOLD_HEIGHT_RATIO = 0.2f
        const val CONTENT_SCALE_RATIO = 0.8f
    }
}
