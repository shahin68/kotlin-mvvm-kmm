package com.applehealth.androidApp.views.dotPuzzleView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.applehealth.androidApp.R
import com.applehealth.androidApp.data.models.DotProjection

class DotPuzzle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint: Paint = Paint()
    private var mPoints = arrayListOf<DotProjection>()
    private val radius = resources.getDimension(R.dimen.dimen14)
    private val lineWidth = resources.getDimension(R.dimen.dimen8)
    private val textSize = resources.getDimension(R.dimen.text14)
    private val neutralColor = Color.GRAY
    private val correctColor = Color.GREEN
    private val wrongColor = Color.RED

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val sortedClickedItems = mPoints.filter { it.isClicked }.sortedWith(compareBy { it.order })
        if (sortedClickedItems.size > 1) {
            for (i in 1 until sortedClickedItems.size) {
                mPaint.reset()
                mPaint.style = Paint.Style.FILL_AND_STROKE
                mPaint.strokeWidth = lineWidth
                val prevItem = mPoints.find { it.order == sortedClickedItems[i].order - 1 }
                if (prevItem == null) {
                    mPaint.color = correctColor
                } else {
                    mPaint.color = if (prevItem.isClicked) correctColor else wrongColor
                }
                canvas?.drawLine(
                    sortedClickedItems[i - 1].point.x.toFloat(),
                    sortedClickedItems[i - 1].point.y.toFloat(),
                    sortedClickedItems[i].point.x.toFloat(),
                    sortedClickedItems[i].point.y.toFloat(),
                    mPaint
                )
            }
        }

        for (i in 0 until mPoints.size) {
            val circleCenterX = mPoints[i].point.x.toFloat()
            val circleCenterY = mPoints[i].point.y.toFloat()

            mPaint.reset()
            mPaint.style = Paint.Style.FILL
            mPaint.color =
                if (mPoints[i].isClicked)
                    when {
                        mPoints[i].order == 0 -> correctColor
                        mPoints.find { it.order == mPoints[i].order - 1 }?.isClicked == true -> correctColor
                        else -> wrongColor
                    }
                else neutralColor
            mPaint.isDither = true
            mPaint.isAntiAlias = true
            canvas?.drawCircle(circleCenterX, circleCenterY, radius, mPaint)

            mPaint.reset()
            mPaint.color = if (mPoints[i].isClicked) Color.BLACK else Color.WHITE
            mPaint.textSize = textSize
            mPaint.typeface = Typeface.DEFAULT_BOLD
            mPaint.textAlign = Paint.Align.CENTER
            canvas?.drawText(
                (mPoints[i].order + 1).toString(),
                circleCenterX,
                circleCenterY + radius / 2.5f,
                mPaint
            )
        }

    }

    fun addPoint(DotProjection: DotProjection) {
        val pointList = mPoints.map { it.point }
        val point = pointList.find {
            it.x == DotProjection.point.x && it.y == DotProjection.point.y
        }
        if (point == null) {
            mPoints.add(DotProjection(DotProjection.point, DotProjection.order, DotProjection.isClicked))
        } else {
            var pos = -1
            mPoints.forEachIndexed { index, project ->
                if (project.point.x == point.x && project.point.y == point.y) {
                    pos = index
                }
            }
            if (pos != -1) {
                mPoints.removeAt(pos)
                mPoints.add(DotProjection(DotProjection.point, DotProjection.order, DotProjection.isClicked))
            }
        }
        invalidate()
    }

    fun clear() {
        mPoints.clear()
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val clickedOnes = mPoints.filter { it.isClicked }
                val DotProjection = mPoints.find {
                    event.x in it.point.x - radius..it.point.x + radius &&
                            event.y in it.point.y - radius..it.point.y + radius
                }
                if (DotProjection != null) {
                    clickedOnes.forEach { clickedDotProjection ->
                        if (clickedDotProjection.order > DotProjection.order) {
                            var pos = -1
                            mPoints.forEachIndexed { index, project ->
                                if (project.point.x == clickedDotProjection.point.x && project.point.y == clickedDotProjection.point.y) {
                                    pos = index
                                }
                            }
                            if (pos != -1) {
                                mPoints.removeAt(pos)
                                mPoints.add(
                                    DotProjection(
                                        clickedDotProjection.point,
                                        clickedDotProjection.order,
                                        isClicked = false
                                    )
                                )
                            }
                        }
                    }
                    var pos = -1
                    mPoints.forEachIndexed { index, project ->
                        if (project.point.x == DotProjection.point.x && project.point.y == DotProjection.point.y) {
                            pos = index
                        }
                    }
                    if (pos != -1) {
                        mPoints.removeAt(pos)
                        mPoints.add(
                            DotProjection(
                                DotProjection.point,
                                DotProjection.order,
                                isClicked = true
                            )
                        )
                        invalidate()
                    }
                }
            }
        }
        return true
    }
}

