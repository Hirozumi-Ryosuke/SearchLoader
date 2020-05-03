package com.example.searchloader.loderspack.loader

import android.R.color.*
import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.view.animation.*
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import com.example.searchloader.R
import com.example.searchloader.R.styleable
import com.example.searchloader.R.styleable.*
import com.example.searchloader.loderspack.basicviews.MagnifyingGlassView
import com.example.searchloader.loderspack.contract.LoaderContract

class SearchLoader : RelativeLayout, LoaderContract {

    var lensRadius = 50
    var lensBorderWidth = 20

    var lensHandleLength = 80

    var lensColor = getColor(context, holo_green_light)

    var xRangeToSearch = 400
    var yRangeToSearch = 400

    var defaultStartLoading = true

    private lateinit var magnifyingGlassView: MagnifyingGlassView

    private var xCor = 0.0f
    private var yCor = 0.0f

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
        initView()
    }

    constructor(context: Context?,
                lensRadius: Int, lensBorderWidth: Int, lensHandleLength: Int, lensColor: Int, xRangeToSearch: Int, yRangeToSearch: Int,
                defaultStartLoading: Boolean
    ) : super(context) {
        this.lensRadius = lensRadius
        this.lensBorderWidth = lensBorderWidth
        this.lensHandleLength = lensHandleLength
        this.lensColor = lensColor
        this.xRangeToSearch = xRangeToSearch
        this.yRangeToSearch = yRangeToSearch
        this.defaultStartLoading = defaultStartLoading
        initView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(xRangeToSearch, yRangeToSearch)
    }

    override fun initAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, SearchLoader, 0, 0)

        this.lensRadius = typedArray
            .getDimensionPixelSize(SearchLoader_search_lensRadius, 50)

        this.lensBorderWidth = typedArray
            .getDimensionPixelSize(SearchLoader_search_lensBorderWidth, 20)

        this.lensHandleLength = typedArray
            .getDimensionPixelSize(SearchLoader_search_lensHandleLength, 80)


        this.lensColor = typedArray
            .getColor(SearchLoader_search_lensColor, getColor(context, holo_green_light))

        this.xRangeToSearch = typedArray
            .getDimensionPixelSize(SearchLoader_search_xRangeToSearch, 400)

        this.yRangeToSearch = typedArray
            .getDimensionPixelSize(SearchLoader_search_yRangeToSearch, 400)

        this.defaultStartLoading = typedArray
            .getBoolean(SearchLoader_search_defaultStartLoading, true)

        typedArray.recycle()
    }

    private fun initView() {

        val viewTreeObserver = this.viewTreeObserver
        val loaderView = this

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                startLoading(null)

                val vto = loaderView.viewTreeObserver
                vto.removeOnGlobalLayoutListener(this)
            }
        })
    }

    fun startLoading(animation: Animation?) {

        var translateAnim = animation

        if (translateAnim == null) {
            translateAnim = getTransAnimation()

            translateAnim.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                    startLoading(null)
                }

                override fun onAnimationStart(animation: Animation?) = Unit

            })
        }
        magnifyingGlassView.startAnimation(translateAnim)
    }

    private fun getTransAnimation(): TranslateAnimation {

        val animDuration = (300..1000).random()

        val maxX = xRangeToSearch - magnifyingGlassView.width
        val maxY = yRangeToSearch - magnifyingGlassView.height

        val toXCor = (0..if (maxX > 0) maxX else magnifyingGlassView.width).random().toFloat()
        val toYCor = (0..if (maxY > 0) maxX else magnifyingGlassView.width).random().toFloat()

        val anim = TranslateAnimation(xCor, toXCor, yCor, toYCor)

        anim.duration = animDuration.toLong()
        anim.repeatCount = 0
        anim.fillAfter = true

        val random = (0..3).random()
        when (random) {
            0 -> anim.interpolator = LinearInterpolator()
            1 -> anim.interpolator = AccelerateInterpolator()
            2 -> anim.interpolator = DecelerateInterpolator()
            3 -> anim.interpolator = AccelerateDecelerateInterpolator()
        }

        xCor = toXCor
        yCor = toYCor

        return anim
    }
}