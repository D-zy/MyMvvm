package com.eg.app.weight

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.eg.R


class XEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val mText: String
    private val mHint: String
    private val mIsPwd: Boolean
    private val mIsNum: Boolean
    private val mMaxLength: Int

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.XEditText)
        mText = ta.getString(R.styleable.XEditText_xText) ?: ""
        mHint = ta.getString(R.styleable.XEditText_xHint) ?: ""
        mIsPwd = ta.getBoolean(R.styleable.XEditText_xIsPwd, false)
        mIsNum = ta.getBoolean(R.styleable.XEditText_xIsNum, false)
        mMaxLength = ta.getInteger(R.styleable.XEditText_xMaxLength, Int.MAX_VALUE)
        ta.recycle()
        initData(context)
    }

    fun getEtText(): String {
        return mEt.text.toString().trim()
    }

    fun setEtText(context: String) {
        mEt.setText(context)
    }

    lateinit var mEt: EditText

    private fun initData(context: Context) {
        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_xet, this)
        mEt = inflate.findViewById<EditText>(R.id.et)
        val mIv = inflate.findViewById<ImageView>(R.id.iv)
        val mCb = inflate.findViewById<CheckBox>(R.id.cb)
        val mLl = inflate.findViewById<LinearLayout>(R.id.ll)
        mEt.setText(mText)
        mEt.hint = mHint

        //是否是数字
        if (mIsNum) mEt.inputType = InputType.TYPE_CLASS_NUMBER
        //是否是密码
        if (mIsPwd) mEt.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        mLl.visibility = if (mIsPwd) VISIBLE else GONE

        //设置最大长度
        mEt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(mMaxLength))
        //点击清除
        mIv.setOnClickListener { mEt.setText("") }
        mEt.typeface = Typeface.DEFAULT
        //密码显示
        mCb.setOnCheckedChangeListener { _, p1 ->
            mEt.inputType = if (p1) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            mEt.typeface = Typeface.DEFAULT
            mEt.setSelection(mEt.text.length)
//            if (p1) mEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
//            else mEt.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        mEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable) {
                mIv.visibility = if (hasFocus() && p0.toString().isNotEmpty()) VISIBLE else GONE
            }
        })

        mEt.setOnFocusChangeListener { v, hasFocus ->
            mIv.visibility = if (hasFocus && mEt.text.isNotEmpty()) VISIBLE else GONE
        }
    }

}
