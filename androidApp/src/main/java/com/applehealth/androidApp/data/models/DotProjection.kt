package com.applehealth.androidApp.data.models

import android.graphics.Point
import androidx.annotation.Keep

@Keep
data class DotProjection(var point: Point, val order: Int, var isClicked: Boolean)