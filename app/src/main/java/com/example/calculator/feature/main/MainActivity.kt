package com.example.calculator.feature.main

import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.R
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.feature.main.model.TabMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setupWindowInsets()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateTabUi(tabMode: TabMode) {
        val colorWhite = ContextCompat.getColor(this, R.color.white)
        val colorGray = ContextCompat.getColor(this, R.color.gray)

        when (tabMode) {
            TabMode.CALCULATOR -> {
                animateTextColor(binding.tvCalculator, colorWhite)
                animateTextColor(binding.tvConverter, colorGray)
            }

            TabMode.CONVERTER -> {
                animateTextColor(binding.tvConverter, colorWhite)
                animateTextColor(binding.tvCalculator, colorGray)
            }
        }
    }

    private fun animateTextColor(
        view: TextView,
        targetColor: Int,
    ) {
        val currentColor = view.currentTextColor

        if (currentColor == targetColor) return

        ValueAnimator.ofArgb(currentColor, targetColor).apply {
            duration = ANIMATION_DURATION_MS
            addUpdateListener { animator ->
                view.setTextColor(animator.animatedValue as Int)
            }
            start()
        }
    }

    private companion object {
        const val ANIMATION_DURATION_MS: Long = 300
    }
}
