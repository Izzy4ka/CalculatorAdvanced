package com.example.calculator.feature.calculator.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.constraintlayout.widget.ConstraintLayout

class PullToHistoryLayout
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        data class Config(
            val scrollTarget: View,
            val dragThresholdPx: Float,
            val dragFriction: Float = 0.4f,
            val resetAnimationDuration: Long = 250L,
            val onDragUpdate: (Float) -> Unit,
            val onDragActionFired: () -> Unit,
        )

        private var config: Config? = null

        private var initialY = 0f
        private var isDragging = false
        private var currentDragValue = 0f
        private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

        fun setup(configuration: Config) {
            this.config = configuration
        }

        override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
            val currentConfig = config ?: return super.onInterceptTouchEvent(ev)

            if (currentConfig.scrollTarget.canScrollVertically(-1)) {
                return false
            }

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = ev.rawY
                    isDragging = false
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaY = ev.rawY - initialY
                    if (deltaY > touchSlop) {
                        isDragging = true
                        return true
                    }
                }
            }

            return super.onInterceptTouchEvent(ev)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val currentConfig = config ?: return super.onTouchEvent(event)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = event.rawY
                    return true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.rawY - initialY
                    if (deltaY > 0) {
                        currentDragValue = deltaY * currentConfig.dragFriction

                        currentConfig.onDragUpdate(currentDragValue)
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (!isDragging) {
                        performClick()
                        return false
                    }

                    isDragging = false

                    if (currentDragValue > currentConfig.dragThresholdPx) {
                        currentConfig.onDragActionFired()
                    }
                    resetDragAnimation(currentConfig)
                }
            }
            return true
        }

        private fun resetDragAnimation(currentConfig: Config) {
            ValueAnimator.ofFloat(currentDragValue, 0f).apply {
                duration = currentConfig.resetAnimationDuration
                addUpdateListener { animator ->
                    currentDragValue = animator.animatedValue as Float
                    currentConfig.onDragUpdate(currentDragValue)
                }
                start()
            }
        }

        override fun performClick(): Boolean {
            super.performClick()
            return true
        }
    }
