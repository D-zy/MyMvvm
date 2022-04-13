package com.eg.data.bindadapter

import android.os.SystemClock
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.allen.library.SuperTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.eg.app.weight.XEditText
import me.hgj.jetpackmvvm.ext.view.textString


/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 自定义 BindingAdapter
 */
object CustomBindAdapter {

    @BindingAdapter(value = ["marginTop"])
    fun setLayoutMarginLeft(view: View, marginTop: Float) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(p.leftMargin, marginTop.toInt(), p.rightMargin, p.bottomMargin)
            view.requestLayout()
        }
    }

    //-----------------------------------------------------------

    @BindingAdapter(value = ["xText"])
    @JvmStatic
    fun setEtContext(view: XEditText, content: String) {
        val newContent = view.getEtText()
        if (content.isNotEmpty()) {
            if (content != newContent) {
                view.setEtText(content)
                // 设置光标位置
                view.mEt.setSelection(view.mEt.length());
            }
        }
    }

    @InverseBindingAdapter(attribute = "xText", event = "xTextAttrChanged")
    @JvmStatic
    fun getEtContext(view: XEditText): String {
        return view.getEtText()
    }


    @BindingAdapter(value = ["xTextAttrChanged"], requireAll = false)
    @JvmStatic
    fun setChangeListener(view: XEditText, listener: InverseBindingListener?) {
        if (listener != null) {
            view.mEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    listener.onChange()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
    }


    //-----------------------------------------------------------
    @BindingAdapter(value = ["sSwitchIsChecked"])
    @JvmStatic
    fun setSwitchIsChecked(view: SuperTextView, value: Boolean) {
        val isChecked = view.switchIsChecked
        if (isChecked != value) {
            view.switchIsChecked = value
        }
    }

    @InverseBindingAdapter(attribute = "sSwitchIsChecked", event = "sSwitchIsCheckedAttrChanged")
    @JvmStatic
    fun getSwitchIsChecked(view: SuperTextView): Boolean {
        return view.switchIsChecked
    }

    @BindingAdapter(value = ["sSwitchIsCheckedAttrChanged"], requireAll = false)
    @JvmStatic
    fun setSwitchIsCheckedListener(view: SuperTextView, listener: InverseBindingListener?) {
        if (listener != null) {
            for (i in 0..view.childCount) {
                val childView = view.getChildAt(i)
                if (childView is Switch) {
                    childView.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                        listener.onChange()
                    })
                }
            }
        }
    }

    //-----------------------------------------------------------
    @BindingAdapter(value = ["sIsClickable"])
    @JvmStatic
    fun setCheckBoxIsClickable(view: SuperTextView, value: Boolean) {
        val isClickable = view.isClickable
        if (isClickable != value) {
            view.checkBox.isClickable = value
        }
    }

    @InverseBindingAdapter(attribute = "sIsClickable", event = "sIsClickableAttrChanged")
    @JvmStatic
    fun getSwitchIsClickable(view: SuperTextView): Boolean {
        return view.checkBox.isClickable
    }

    @BindingAdapter(value = ["sIsClickableAttrChanged"], requireAll = false)
    @JvmStatic
    fun setCheckBoxIsClickableListener(view: SuperTextView, listener: InverseBindingListener?) {
        if (listener != null) {
            for (i in 0..view.childCount) {
                val childView = view.getChildAt(i)
                if (childView is CheckBox) {
                    childView.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                        listener.onChange()
                    })
                }
            }
        }
    }

    //-----------------------------------------------------------
    @BindingAdapter(value = ["sIsChecked"])
    @JvmStatic
    fun setCheckBoxIsChecked(view: SuperTextView, value: Boolean) {
        val isChecked = view.cbisChecked
        if (isChecked != value) {
            view.setCbChecked(value)
        }
    }

    @InverseBindingAdapter(attribute = "sIsChecked", event = "sIsCheckedAttrChanged")
    @JvmStatic
    fun getCheckBoxIsChecked(view: SuperTextView): Boolean {
        return view.cbisChecked
    }

    @BindingAdapter(value = ["sIsCheckedAttrChanged"], requireAll = false)
    @JvmStatic
    fun setCheckBoxIsCheckedListener(view: SuperTextView, listener: InverseBindingListener?) {
        if (listener != null) {
            for (i in 0..view.childCount) {
                val childView = view.getChildAt(i)
                if (childView is CheckBox) {
                    childView.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                        listener.onChange()
                    })
                }
            }
        }
    }

    @BindingAdapter(value = ["sLeftTextString"])
    @JvmStatic
    fun setStvLeftTextString(view: SuperTextView, value: String) {
        val leftString = view.leftString
        if (leftString != value) {
            view.setLeftString(value)
        }
    }

    @BindingAdapter(value = ["sRightTextString"])
    @JvmStatic
    fun setStvRightTextString(view: SuperTextView, value: String) {
        val rightString = view.rightString
        if (rightString != value) {
            view.setRightString(value)
        }
    }

    @BindingAdapter(value = ["sTopTextString"])
    @JvmStatic
    fun setStvCenterToptTextString(view: SuperTextView, value: String) {
        val centerTopString = view.centerTopString
        if (centerTopString != value) {
            view.setCenterTopString(value)
        }
    }

    @BindingAdapter(value = ["sCenterTextString"])
    @JvmStatic
    fun setStvCenterTextString(view: SuperTextView, value: String) {
        val centerString = view.centerString
        if (centerString != value) {
            view.setCenterString(value)
        }
    }

    @BindingAdapter(value = ["sBottomTextString"])
    @JvmStatic
    fun setStvCenterBottomTextString(view: SuperTextView, value: String) {
        val centerBottomString = view.centerBottomString
        if (centerBottomString != value) {
            view.setCenterBottomString(value)
        }
    }

    @BindingAdapter(value = ["checkChange"])
    @JvmStatic
    fun checkChange(checkbox: CheckBox, listener: CompoundButton.OnCheckedChangeListener) {
        checkbox.setOnCheckedChangeListener(listener)
    }

    @BindingAdapter(value = ["showPwd"])
    @JvmStatic
    fun showPwd(view: EditText, boolean: Boolean) {
        if (boolean) {
            view.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            view.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        view.setSelection(view.textString().length)
    }

    @BindingAdapter(value = ["imageUrl"])
    @JvmStatic
    fun imageUrl(view: ImageView, url: String) {
        Glide.with(view.context.applicationContext)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(view)
    }

    @BindingAdapter(value = ["circleImageUrl"])
    @JvmStatic
    fun circleImageUrl(view: ImageView, url: String) {
        Glide.with(view.context.applicationContext)
                .load(url)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(view)
    }


    @BindingAdapter(value = ["afterTextChanged"])
    @JvmStatic
    fun EditText.afterTextChanged(action: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                action(s.toString())
            }
        })
    }

    @BindingAdapter(value = ["noRepeatClick"])
    @JvmStatic
    fun setOnClick(view: View, clickListener: () -> Unit) {
        val mHits = LongArray(2)
        view.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()
            if (mHits[0] < SystemClock.uptimeMillis() - 500) {
                clickListener.invoke()
            }
        }
    }


}