package com.applehealth.androidApp.views.switches

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.applehealth.androidApp.R

class SwitchButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var srcLegacy: Drawable = drawable
    private var srcEnabledLegacy: Drawable

    init {
        val style = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
        srcEnabledLegacy = style.getDrawable(R.styleable.SwitchButton_srcCompatEnabled) ?: srcLegacy
        style.recycle()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        when (selected) {
            true -> setImageDrawable(srcEnabledLegacy)
            false -> setImageDrawable(srcLegacy)
        }
    }
}