package com.eg.app.weight.countdown

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.allen.library.SuperButton

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 验证码倒计时
 */
class CountdownView : SuperButton, Runnable {
    /** 倒计时秒数  */
    private var mTotalSecond = 60

    /** 当前秒数  */
    private var mCurrentSecond = 0

    /** 记录原有的文本  */
    private var mRecordText: CharSequence? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 设置倒计时总秒数
     */
    fun setTotalTime(totalTime: Int) {
        mTotalSecond = totalTime
    }

    /**
     * 开始倒计时
     */
    fun start() {
        mRecordText = text
        isEnabled = false
        mCurrentSecond = mTotalSecond
        post(this)
    }

    /**
     * 结束倒计时
     */
    fun stop() {
        text = mRecordText
        isEnabled = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 移除延迟任务，避免内存泄露
        removeCallbacks(this)
    }

    @SuppressLint("SetTextI18n")
    override fun run() {
        if (mCurrentSecond == 0) {
            stop()
            return
        }
        mCurrentSecond--
        text = "$mCurrentSecond $TIME_UNIT"
        postDelayed(this, 1000)
    }

    companion object {
        /** 秒数单位文本  */
        private const val TIME_UNIT = "S"
    }
}