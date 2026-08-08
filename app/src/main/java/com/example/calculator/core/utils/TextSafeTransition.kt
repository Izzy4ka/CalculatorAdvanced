package com.example.calculator.core.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

class TextSafeTransition : Transition() {
    private fun captureValues(transitionValues: TransitionValues?) {
        if (transitionValues == null) {
            return
        }

        val view = transitionValues.view
        transitionValues.values[PROP_BOUNDS] = Rect(view.left, view.top, view.right, view.bottom)
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?,
    ): Animator? {
        if (startValues == null || endValues == null) {
            return null
        }

        val startBounds = startValues.values[PROP_BOUNDS] as Rect
        val endBounds = endValues.values[PROP_BOUNDS] as Rect

        if (startBounds == endBounds) {
            return null
        }

        val view = endValues.view

        val startWidth = startBounds.width().toFloat()
        val startHeight = startBounds.height().toFloat()
        val endWidth = endBounds.width().toFloat()
        val endHeight = endBounds.height().toFloat()

        val scaleX = if (endWidth != 0f) startWidth / endWidth else 1f
        val scaleY = if (endHeight != 0f) startHeight / endHeight else 1f

        val deltaX = startBounds.exactCenterX() - endBounds.exactCenterX()
        val deltaY = startBounds.exactCenterY() - endBounds.exactCenterY()

        view.translationX = deltaX
        view.translationY = deltaY
        view.scaleX = scaleX
        view.scaleY = scaleY

        val moveX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, deltaX, 0f)
        val moveY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, deltaY, 0f)
        val scaleXAnim = ObjectAnimator.ofFloat(view, View.SCALE_X, scaleX, 1f)
        val scaleYAnim = ObjectAnimator.ofFloat(view, View.SCALE_Y, scaleY, 1f)

        return AnimatorSet().apply {
            playTogether(moveX, moveY, scaleXAnim, scaleYAnim)
        }
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private companion object {
        private const val PROP_BOUNDS = "TextSafeTransition:bounds"
    }
}
