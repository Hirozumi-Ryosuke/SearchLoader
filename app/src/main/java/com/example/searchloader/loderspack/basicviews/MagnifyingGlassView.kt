package com.example.searchloader.loderspack.basicviews

import android.R.color.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.*
import android.graphics.Paint.Cap.*
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import com.example.searchloader.R
import com.example.searchloader.R.styleable
import com.example.searchloader.R.styleable.*


class MagnifyingGlassView : View {
    var glassRadius = 50
    var glassBorderWidth = 20

    var glassHandleLength = 80

    var glassColor = getColor(context, holo_green_light)

    private val paint = Paint()

    private var circleCenterPoint = 0.0f
    private var handleStartPoint = 0.0f
    private var handleEndPoint = 0.0f

    private val sin45 = 0.7075f

    constructor(context: Context) : super(context) {
        initValues()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(attrs)
        initValues()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
        initValues()
    }

    constructor(context: Context?, glassRadius: Int, glassBorderWidth: Int, glassHandleLength: Int, glassColor: Int) : super(context) {
        this.glassRadius = glassRadius
        this.glassBorderWidth = glassBorderWidth
        this.glassHandleLength = glassHandleLength
        this.glassColor = glassColor
        initValues()
    }

    fun initAttributes(attrs: AttributeSet) {

        val typeArray = context.obtainStyledAttributes(attrs, MagnifyingGlassView, 0, 0)

        this.glassRadius = typeArray.getDimensionPixelSize(MagnifyingGlassView_glassRadius, 50)
        this.glassBorderWidth = typeArray.getDimensionPixelSize(MagnifyingGlassView_glassBorderWidth, 20)

        this.glassHandleLength = typeArray.getDimensionPixelSize(
            MagnifyingGlassView_glassHandleLength, 80)

        this.glassColor = typeArray.getColor(MagnifyingGlassView_glassColor, getColor(context, holo_green_light))

        typeArray.recycle()
    }

    private fun initValues() {

        paint.color = glassColor
        paint.isAntiAlias = true
        paint.strokeWidth = glassBorderWidth.toFloat()

        paint.strokeCap = ROUND

        circleCenterPoint = (glassRadius + glassBorderWidth / 2).toFloat()
        handleStartPoint = circleCenterPoint + (glassRadius * sin45)

        handleEndPoint = handleStartPoint + (glassHandleLength * sin45)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthHeight = ((2 * glassRadius) + glassBorderWidth
                + (glassHandleLength * sin45) - (0.29 * glassRadius)).toInt()

        setMeasuredDimension(widthHeight, widthHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.style = STROKE
        canvas.drawCircle(circleCenterPoint, circleCenterPoint, glassRadius.toFloat(), paint)

        paint.style = FILL

        canvas.drawLine(handleStartPoint, handleStartPoint, handleEndPoint, handleEndPoint, paint)
    }
}