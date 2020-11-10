package com.applehealth.androidApp.views.volumeKnob

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.applehealth.androidApp.R
import kotlin.math.atan2


class VolumeButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val rotationDuration = 250L
    private val rotationInterpolator = AccelerateDecelerateInterpolator()

    private lateinit var angleListener: AngleListener

    init {
        background = ContextCompat.getDrawable(context, R.drawable.ic_volume_knub)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val centerX = x + width / 2
        val centerY = y + height / 2
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> { }
            MotionEvent.ACTION_UP -> {
                if (rotation > -45 && rotation <= 45f) setAngle(VolumeMeasure.ANGLE_0)
                if (rotation > 45f && rotation <= 135f) setAngle(VolumeMeasure.ANGLE_90)
                if (rotation > 135f || rotation <= -135f) setAngle(VolumeMeasure.ANGLE_180)
                if (rotation > -135f && rotation <= -45) setAngle(VolumeMeasure.ANGLE_270)
                if (rotation > 315f) set360()
            }
            MotionEvent.ACTION_MOVE -> {
                val angle = atan2(event.rawX - centerX, centerY - event.rawY)
                val newAngle = Math.toDegrees(angle.toDouble()).toFloat()
                rotation = newAngle
            }
        }
        return true
    }

    fun setAngle(volumeMeasure: VolumeMeasure) {
        animate().rotation(
            when (volumeMeasure) {
                VolumeMeasure.ANGLE_0 -> 0f
                VolumeMeasure.ANGLE_90 -> if (rotation >= 0) 90f else -270f
                VolumeMeasure.ANGLE_180 -> if (rotation > 0) 180f else -180f
                VolumeMeasure.ANGLE_270 -> if (rotation > 0) 270f else -90f
            }
        ).
        apply {
            duration = rotationDuration
            interpolator = rotationInterpolator
        }.withEndAction {
            angleListener.onAngleChanged(volumeMeasure)
        }.start()
    }

    private fun set360() {
        val anim = ObjectAnimator.ofFloat(rotation, 360f)
        anim.addUpdateListener {
            rotation = it.animatedValue as Float
        }
        anim.doOnEnd {
            rotation = 0f
        }
        anim.start()
    }

    fun setCustomObjectListener(listener: AngleListener) {
        angleListener = listener
    }
}

interface AngleListener {
    fun onAngleChanged(volumeMeasure: VolumeMeasure)
}