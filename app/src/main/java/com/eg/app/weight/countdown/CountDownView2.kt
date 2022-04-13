package com.eg.app.weight.countdown

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.CountDownTimer
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.eg.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


class CountDownView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(CLOCKWISE_FROM_EXIST, CLOCKWISE_FROM_NOTHING, ANTICLOCKWISE_FROM_EXIST, ANTICLOCKWISE_FROM_NOTHING)
    private annotation class ProgressMode

    private val DEFAULT_PADDING: Int

    /**
     * 背景颜色。
     */
    private var mBackgroundColor = 0

    /**
     * 倒计时的监听。
     */
    private var mOnFinishListener: OnFinishListener? = null

    /**
     * 一个范围为：0~360的小数，用来记录当前的进度。
     */
    private var mProgress = 0f

    /**
     * 一个范围为：0~360的小数，做动画时从多少结束。
     */
    private var mProgressMax: Long = 0

    /**
     * 边框的宽度。
     */
    private var mProgressBarWidth: Int

    /**
     * 边框的颜色。
     */
    private var mProgressBarColor = 0

    /**
     * 要显示的文字。
     */
    private var mContentText: CharSequence? = null

    /**
     * 字体颜色。
     */
    private var mTextColor = 0

    /**
     * 每行文字的长度，用作换号的依据。
     */
    private var mLineTextLength = 4

    /**
     * 显示时长。
     */
    private var duration = 0

    /**
     * 用来画圆的画笔。
     */
    private val mCirclePaint: Paint

    /**
     * 用来画进度条的画笔。
     */
    private val mProgressBarPaint: Paint

    /**
     * 用来绘制字体的画笔。
     */
    private val mTextPaint: TextPaint

    /**
     * 绘制进度条时需要用到的矩形，为了避免在onDraw的时候重复new，所以在这里直接创建了。
     */
    private val mRect = RectF()

    /**
     * 用来绘制文字的工具。
     */
    private lateinit var mStaticLayout: StaticLayout

    /**
     * 倒计时工具。
     */
    private var mCD: CD? = null

    /**
     * 用来记录当前的进度条模式。
     */
    private var mProgressBarMode = 0

    /**
     * 当前圆的半径。
     */
    private var mRadios = 0
    private fun createPaint(color: Int, textSize: Float, strokeWidth: Float, style: Paint.Style?, align: Paint.Align?): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = color
        if (textSize > 0) {
            paint.textSize = textSize
        }
        if (strokeWidth > 0) {
            paint.strokeWidth = strokeWidth
        }
        if (style != null) {
            paint.style = style
        }
        if (align != null) {
            paint.textAlign = align
        }
        return paint
    }

    /**
     * 设置进度条模式,必须在[.start]方法被调用前调用。
     *
     * @param mProgressMode 要设置的模式。分别为：[.CLOCKWISE_FROM_EXIST]、[.CLOCKWISE_FROM_NOTHING]、[.ANTICLOCKWISE_FROM_EXIST]、[.ANTICLOCKWISE_FROM_NOTHING]。
     */
    fun setProgressBarMode(@ProgressMode mProgressMode: Int): CountDownView2 {
        checkIsStartedAndThrow()
        mProgressBarMode = mProgressMode
        return this
    }

    /**
     * 设置进度条宽度,必须在[.start]方法被调用前调用。
     *
     * @param widthPx 要设置的宽度，单位为px。
     */
    fun setProgressBarWidth(widthPx: Int): CountDownView2 {
        checkIsStartedAndThrow()
        mProgressBarWidth = widthPx
        return this
    }

    /**
     * 设置进度条颜色,必须在[.start]方法被调用前调用。
     *
     * @param color 要设置的颜色。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun setProgressColor(color: Color): CountDownView2 {
        checkIsStartedAndThrow()
        mProgressBarColor = color.toArgb()
        return this
    }

    /**
     * 设置进度条颜色,必须在[.start]方法被调用前调用。
     *
     * @param color 要设置的颜色。
     */
    fun setProgressColor(@ColorInt color: Int): CountDownView2 {
        checkIsStartedAndThrow()
        mProgressBarColor = color
        return this
    }

    /**
     * 设置进度条颜色,必须在[.start]方法被调用前调用。
     *
     * @param color 要设置的颜色。
     */
    fun setProgressColorResource(@ColorRes color: Int): CountDownView2 {
        mProgressBarColor = ContextCompat.getColor(context, color)
        return this
    }

    /**
     * 设置圆形背景颜色。
     *
     * @param color 要设置的颜色。
     */
    override fun setBackgroundColor(@ColorInt color: Int) {
        mBackgroundColor = color
        mCirclePaint.color = color
        invalidate()
    }

    /**
     * 设置圆形背景颜色。
     *
     * @param color 要设置的颜色。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun setBackgroundColor(color: Color): CountDownView2 {
        mBackgroundColor = color.toArgb()
        mCirclePaint.color = mBackgroundColor
        invalidate()
        return this
    }

    /**
     * 设置圆形背景颜色。
     *
     * @param color 要设置的颜色。
     */
    fun setBackgroundColorResource(@ColorRes color: Int): CountDownView2 {
        mBackgroundColor = ContextCompat.getColor(context, color)
        mCirclePaint.color = mBackgroundColor
        invalidate()
        return this
    }

    /**
     * 设置文字,必须在[.start]方法被调用前调用。
     *
     * @param text 要设置的文字的内容。
     */
    fun setText(text: CharSequence?): CountDownView2 {
        checkIsStartedAndThrow()
        mContentText = text
        createStaticLayout()
        return this
    }

    private fun createStaticLayout() {
        mStaticLayout = StaticLayout(mContentText, mTextPaint, mTextPaint.measureText(mContentText!!.subSequence(0, mLineTextLength).toString()).toInt(), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
//        StaticLayout.Builder.obtain(mContentText!!, mTextPaint, mTextPaint.measureText(mContentText!!.subSequence(0, mLineTextLength).toString()).toInt(), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

    }

    /**
     * 设置字体颜色,必须在[.start]方法被调用前调用。
     *
     * @param color 要设置的颜色。
     */
    fun setTextColor(@ColorInt color: Int): CountDownView2 {
        checkIsStartedAndThrow()
        mTextColor = color
        mTextPaint.color = color
        createStaticLayout()
        return this
    }

    /**
     * 设置字体颜色,必须在[.start]方法被调用前调用。
     *
     * @param color 要设置的颜色。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun setTextColor(color: Color): CountDownView2 {
        checkIsStartedAndThrow()
        mTextColor = color.toArgb()
        mTextPaint.color = mTextColor
        createStaticLayout()
        return this
    }

    /**
     * 设置字体颜色,必须在[.start]方法被调用前调用。
     *
     * @param color 要设置的颜色。
     */
    fun setTextColorResource(@ColorRes color: Int): CountDownView2 {
        checkIsStartedAndThrow()
        mTextColor = ContextCompat.getColor(context, color)
        mTextPaint.color = mTextColor
        createStaticLayout()
        return this
    }

    /**
     * 设置单行文字个数的最大值,必须在[.start]方法被调用前调用。
     *
     * @param lineTextLength 要设置的单行文字个数。
     */
    fun setLineTextLength( lineTextLength: Int): CountDownView2 {
        checkIsStartedAndThrow()
        if (lineTextLength > 0 && lineTextLength < mContentText!!.length) {
            mLineTextLength = lineTextLength
        }
        createStaticLayout()
        return this
    }

    /**
     * 设置时长，单位为毫秒,必须在[.start]方法被调用前调用。
     *
     * @param duration 要设置的时长，取值范围：1000 ~ 20000(1秒至20秒)。
     */
    fun setDuration( duration: Int): CountDownView2 {
        checkIsStartedAndThrow()
        require(duration >= 1000) {
            // 这里做小于1秒的判断是因为如果小于1秒假如是200毫秒的话就会导致➗360的时候得不到整数，
            //而且如果小于一秒也没有倒计时的必要了，我是这么认为的。所以加了这个判断。
            //至于没有判断大于20秒，是考虑到有可能你真的需要显示20秒，虽然我建议不要超过20秒，但还是不要抛出异常的好，
            //我加了 @IntRange 注解只是想在超这个范围的时候在代码中有个警告。
            "the duration must be ≥ 1000 and must be ≤ 20000!"
        }
        this.duration = duration
        return this
    }

    protected fun checkIsStartedAndThrow() {
        check(!isStarted) { "The countDownView is started，You must call before the start method call." }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val w = mStaticLayout.width + paddingLeft + paddingRight + DEFAULT_PADDING
        val h = mStaticLayout.height + paddingTop + paddingBottom + DEFAULT_PADDING
        mRadios = ((Math.sqrt(Math.pow(w.toDouble(), 2.0) + Math.pow(h.toDouble(), 2.0)) + 0.5f) / 2).toInt()
        val width: Int = if (widthMode != MeasureSpec.EXACTLY) {
            (mRadios shl 1) + mProgressBarWidth
        } else {
            MeasureSpec.getSize(widthMeasureSpec)
        }
        val height: Int = if (heightMode != MeasureSpec.EXACTLY) {
            (mRadios shl 1) + mProgressBarWidth
        } else {
            MeasureSpec.getSize(heightMeasureSpec)
        }
        setMeasuredDimension(width, height)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val cx = measuredWidth ushr 1
        val cy = measuredHeight ushr 1
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadios.toFloat(), mCirclePaint)
        mRect.left = (cx - mRadios).toFloat()
        mRect.top = (cy - mRadios).toFloat()
        mRect.right = (cx + mRadios).toFloat()
        mRect.bottom = (cy + mRadios).toFloat()
        canvas.drawArc(mRect, -90f, mProgress, false, mProgressBarPaint)
        canvas.translate(cx.toFloat(), (cy - (mStaticLayout.height ushr 1)).toFloat())
        mStaticLayout.draw(canvas)
    }

    fun start() {
        check(mCD == null) { "The countdown has begun!" }
        val unit = duration / 360
        mCD = CD(mProgressMax * unit, unit.toLong())
        mCD!!.startCountDown()
    }

    val isStarted: Boolean
        get() = mCD != null && mCD!!.isStarted

    /**
     * 判断是否是顺时针进度条。
     *
     * @return 如果是返回true，否则返回false。
     */
    private fun hasClockwiseProgress(): Boolean {
        return mProgressBarMode and 2 != 0
    }

    /**
     * 判断是否是从有到无绘制。
     *
     * @return 如果是返回true，否则返回false。
     */
    private fun hasFromExistProgress(): Boolean {
        return mProgressBarMode and 1 != 0
    }

    override fun performClick(): Boolean {
        if (mCD != null) {
            mCD!!.cancelCountDown()
            mCD!!.onFinish()
        }
        return super.performClick()
    }

    fun setOnFinishListener(listener: OnFinishListener?): CountDownView2 {
        mOnFinishListener = listener
        return this
    }

    interface OnFinishListener {
        /**
         * 倒计时完成或空间被点击后执行。
         */
        fun onFinish()
    }

    private inner class CD
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to [.start] until the countdown is done and [.onFinish]
     * is called.
     * @param countDownInterval The interval along the way to receive
     * [.onTick] callbacks.
     */ internal constructor(millisInFuture: Long, private val mCountDownInterval: Long) : CountDownTimer(millisInFuture, mCountDownInterval) {
        var isStarted = false
            private set

        override fun onTick(millisUntilFinished: Long) {
            val scale = millisUntilFinished / mCountDownInterval
            mProgress = if (hasFromExistProgress()) {
                if (hasClockwiseProgress()) scale.inv().toFloat() else scale.toFloat()
            } else {
                if (hasClockwiseProgress()) (mProgressMax - scale).toFloat() else (mProgressMax - scale).inv().toFloat()
            }
            invalidate()
        }

        override fun onFinish() {
            mProgress = if (hasFromExistProgress()) 0f else if (hasClockwiseProgress()) mProgressMax.toFloat() else mProgressMax.inv().toFloat()
            invalidate()
            if (mOnFinishListener != null) {
                mOnFinishListener!!.onFinish()
            }
            mCD = null
            isStarted = false
        }

        fun startCountDown() {
            start()
            isStarted = true
        }

        fun cancelCountDown() {
            cancel()
            isStarted = false
        }
    }

    companion object {
        private const val DEFAULT_TEXT = ""

        /**
         * 表示进度条模式为顺时针从无到有。
         */
        const val CLOCKWISE_FROM_NOTHING = 2

        /**
         * 表示进度条模式为顺时针从有到无。
         */
        const val CLOCKWISE_FROM_EXIST = 3

        /**
         * 表示进度条模式为逆时针从无到有。
         */
        const val ANTICLOCKWISE_FROM_NOTHING = 0

        /**
         * 表示进度条模式为逆时针从有到无。
         */
        const val ANTICLOCKWISE_FROM_EXIST = 1
    }

    init {
        isClickable = true
        val fontScale = getContext().resources.displayMetrics.scaledDensity
        var textSize: Float = (0x0000000E * fontScale + 0.5).toInt().toFloat()
        mProgressBarWidth = (2 * context.resources.displayMetrics.density + 0.5f).toInt()
        val roundStrokeCap: Boolean
        //获取自定义属性。
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView)
        if (attrs != null) {
            mBackgroundColor = ta.getColor(R.styleable.CountDownView_backgroundColor, -0x99999a)
            mProgressBarWidth = (ta.getDimension(R.styleable.CountDownView_progressBarWidth, mProgressBarWidth.toFloat()) + 0.9).toInt()
            mProgressBarColor = ta.getColor(R.styleable.CountDownView_progressBarColor, -0x994120)
            textSize = ta.getDimension(R.styleable.CountDownView_android_textSize, textSize)
            mTextColor = ta.getColor(R.styleable.CountDownView_android_textColor, -0x1)
            mProgress = 360 * ta.getFloat(R.styleable.CountDownView_progress, 0f)
            mProgressMax = (360 * ta.getFloat(R.styleable.CountDownView_progressMax, 1f)).toLong()
            mProgressBarMode = ta.getInt(R.styleable.CountDownView_progressBarMode, CLOCKWISE_FROM_EXIST)
            if (ta.getString(R.styleable.CountDownView_android_text).also { mContentText = it } == null) {
                mContentText = DEFAULT_TEXT
            }
            var length = mContentText!!.length
            if (length > 4) {
                if (length and 1 != 0) {
                    length++
                }
                length = length shr 1
            }
            roundStrokeCap = ta.getBoolean(R.styleable.CountDownView_roundStrokeCap, false)
            mLineTextLength = ta.getInteger(R.styleable.CountDownView_lineTextLength, length)
            setDuration(ta.getInteger(R.styleable.CountDownView_duration, 3000))
            ta.recycle()
        } else {
            roundStrokeCap = false
            mBackgroundColor = -0x99999a
            mProgressBarColor = -0x10000
            mTextColor = -0x1
            mLineTextLength = 4
            mProgressBarMode = CLOCKWISE_FROM_EXIST
            mContentText = DEFAULT_TEXT
            setDuration(3000)
        }
        DEFAULT_PADDING = (3 * context.resources.displayMetrics.density + 0.5f).toInt()
        //创建用来画圆的画笔。
        mCirclePaint = createPaint(mBackgroundColor, 0f, 0f, Paint.Style.FILL, null)

        //创建用来画进度条的画笔。
        mProgressBarPaint = createPaint(mProgressBarColor, 0f, mProgressBarWidth.toFloat(), Paint.Style.STROKE, null)
        if (roundStrokeCap) {
            mProgressBarPaint.strokeCap = Paint.Cap.ROUND
        }

        //创建用来画文字的画笔以及给文字排版的工具。
        mTextPaint = TextPaint(createPaint(mTextColor, textSize, 0f, null, Paint.Align.CENTER))
        setText(mContentText)
    }
}