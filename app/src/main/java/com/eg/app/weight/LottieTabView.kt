package com.eg.app.weight

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.AndroidCharacter.mirror
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieOnCompositionLoadedListener
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.SimpleLottieValueCallback
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.SizeUtils
import com.eg.R


class LottieTabView : FrameLayout {
    private var mTextNormalColor = 0
    private var mTextSelectColor = 0
    private var mTextSize = 0f
    private var mTabName: String? = null
    private var mIconNormal: Drawable? = null
    private var mAnimationPath: String? = null
    private lateinit var mLottieView: LottieAnimationView
    private lateinit var mTabNameView: TextView
    private lateinit var mMsgView: TextView
    private var mIsSelected = false


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LottieTabView)
        mTextNormalColor = ta.getColor(R.styleable.LottieTabView_text_normal_color, Color.BLACK)
        mTextSelectColor = ta.getColor(R.styleable.LottieTabView_text_selected_color, Color.BLUE)
        mTextSize = ta.getDimension(R.styleable.LottieTabView_text_size, SizeUtils.dp2px(5f).toFloat())
        mIconNormal = ta.getDrawable(R.styleable.LottieTabView_icon_normal)
        mAnimationPath = ta.getString(R.styleable.LottieTabView_lottie_path)
        mTabName = ta.getString(R.styleable.LottieTabView_tab_name)
        mIsSelected = ta.getBoolean(R.styleable.LottieTabView_tab_selected, false)
        ta.recycle()
        initView(context)
    }

    @SuppressLint("RestrictedApi")
    private fun initView(context: Context) {
        val containView = LayoutInflater.from(context).inflate(R.layout.lottie_tab_view, null, false)
        mLottieView = containView.findViewById(R.id.animation_view)
        mLottieView.repeatCount = 0
        mTabNameView = containView.findViewById(R.id.tab_name)
        mTabNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
        mTabNameView.setTextColor(mTextNormalColor)
        mTabNameView.text = mTabName
        mMsgView = containView.findViewById(R.id.msg_view)
        this.addView(containView)
        if (mIsSelected) selected() else unSelected()

    }

    /**
     * 选中状态
     */
    fun selected() {
        if (TextUtils.isEmpty(mAnimationPath)) {
            throw NullPointerException("ainmation path must be not empty")
        } else {
            mLottieView.setAnimation(mAnimationPath)
            mLottieView.playAnimation()
            mTabNameView.setTextColor(mTextSelectColor)
        }
    }

    /**
     * 未选中
     */
    fun unSelected() {
        mTabNameView.setTextColor(mTextNormalColor)
        mLottieView.clearAnimation()
        mLottieView.setImageDrawable(mIconNormal)
    }

    /**
     * 显示红点消息数，默认不显示
     * @param num
     */
    fun showMsg(num: Int) {
        if (num in 1..99) {
            mMsgView.visibility = VISIBLE
            mMsgView.text = num.toString()
        } else if (num > 99) {
            mMsgView.visibility = VISIBLE
            mMsgView.text = "99+"
        } else {
            mMsgView.visibility = INVISIBLE
        }
    }
}