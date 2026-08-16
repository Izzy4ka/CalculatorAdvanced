package com.example.calculator.feature.main.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.calculator.R
import com.example.calculator.databinding.FragmentMainNavHostBinding
import com.example.calculator.feature.main.adapter.MainPagerAdapter
import com.example.calculator.feature.main.model.MainTabs

class MainHostFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentMainNavHostBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding is null" }

    private val activeColor by lazy { requireContext().getColor(R.color.tab_text_active) }
    private val inactiveColor by lazy { requireContext().getColor(R.color.tab_text_inactive) }

    private val calcMatrix = Matrix()
    private val convMatrix = Matrix()

    private var calcShader: LinearGradient? = null
    private var convShader: LinearGradient? = null

    private var calcWidth = 0f
    private var convWidth = 0f

    private var colorAnimator: ValueAnimator? = null
    private var isColorAnimating = false
    private var currentColorOffset = 0f

    private val onPageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                if (calcWidth == 0f || convWidth == 0f) {
                    return
                }

                if (isColorAnimating) {
                    return
                }

                currentColorOffset = position + positionOffset
                if (currentColorOffset > 1f) {
                    currentColorOffset = 1f
                }

                applyColorOffset(currentColorOffset)
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    colorAnimator?.cancel()
                    isColorAnimating = false
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainNavHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        colorAnimator?.cancel()
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding.viewPager.adapter = null
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initTabListener()
        initShaders()
    }

    private fun initShaders() {
        binding.appBar.doOnLayout {
            calcWidth = binding.tvCalculator.width.toFloat()
            convWidth = binding.tvConverter.width.toFloat()

            val positions = floatArrayOf(0f, SHADER_GRADIENT_START, SHADER_GRADIENT_END, 1f)

            val calcColors = intArrayOf(inactiveColor, inactiveColor, activeColor, activeColor)
            calcShader =
                LinearGradient(
                    0f,
                    0f,
                    calcWidth * 2,
                    0f,
                    calcColors,
                    positions,
                    Shader.TileMode.CLAMP,
                )
            binding.tvCalculator.paint.shader = calcShader

            val convColors = intArrayOf(activeColor, activeColor, inactiveColor, inactiveColor)
            convShader =
                LinearGradient(
                    0f,
                    0f,
                    convWidth * 2,
                    0f,
                    convColors,
                    positions,
                    Shader.TileMode.CLAMP,
                )
            binding.tvConverter.paint.shader = convShader

            applyColorOffset(0f)
        }
    }

    private fun initTabListener() {
        binding.tvCalculator.setOnClickListener {
            switchTabWithAnimation(MainTabs.CALCULATOR)
        }
        binding.tvConverter.setOnClickListener {
            switchTabWithAnimation(MainTabs.CONVERTER)
        }
    }

    private fun initViewPager() {
        binding.viewPager.adapter =
            MainPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private fun switchTabWithAnimation(targetTab: MainTabs) {
        if (binding.viewPager.currentItem == targetTab.position) {
            return
        }

        moveViewPagerTo(targetTab.position)
        animateColorsTo(targetTab.position.toFloat())
    }

    private fun moveViewPagerTo(position: Int) {
        isColorAnimating = true
        binding.viewPager.setCurrentItem(position, true)
    }

    private fun animateColorsTo(targetOffset: Float) {
        colorAnimator?.cancel()
        colorAnimator =
            ValueAnimator.ofFloat(currentColorOffset, targetOffset).apply {
                duration = resources.getInteger(R.integer.calc_animation_duration).toLong()
                addUpdateListener { animator ->
                    currentColorOffset = animator.animatedValue as Float
                    applyColorOffset(currentColorOffset)
                }
                addListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            isColorAnimating = false
                        }
                    },
                )
                start()
            }
    }

    private fun applyColorOffset(offset: Float) {
        val txCalc = -calcWidth * (1.0f - offset)
        val txConv = -convWidth * (1.0f - offset)

        calcMatrix.setTranslate(txCalc, 0f)
        convMatrix.setTranslate(txConv, 0f)

        calcShader?.setLocalMatrix(calcMatrix)
        convShader?.setLocalMatrix(convMatrix)

        binding.tvCalculator.invalidate()
        binding.tvConverter.invalidate()
    }

    companion object {
        private const val SHADER_GRADIENT_START = 0.49f
        private const val SHADER_GRADIENT_END = 0.51f
    }
}
