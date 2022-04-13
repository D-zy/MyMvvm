package com.eg.app.ext

import android.app.Activity
import android.graphics.Color
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.aries.ui.widget.progress.UIProgressDialog
import com.aries.ui.widget.progress.UIProgressDialog.MaterialBuilder
import com.eg.R
import com.eg.app.base.BaseActivity
import com.eg.app.util.SettingUtil

/**
 * @author : hgj
 * @date : 2020/6/28
 */

//loading框
private var dialog: UIProgressDialog? = null

/**
 * 打开等待框
 */
fun AppCompatActivity.showLoadingExt(activity: Activity, message: String? = "") {
    if (!this.isFinishing) {
        dialog?.let {
            if (it.isShowing) it.dismiss()
        }
        dialog = MaterialBuilder(activity)
                .setBackgroundRadiusResource(R.dimen.dp_radius_loading)
                .setTextColor(Color.BLACK)
                .setMessage(message)
                .setLoadingColor(Color.BLACK)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create()
        dialog!!.show()
    }
}

/**
 * 打开等待框
 */
fun Fragment.showLoadingExt(activity: Activity, message: String? = "") {
    activity.let {
        if (!it.isFinishing) {
            dialog?.let {
                if (it.isShowing) it.dismiss()
            }
            dialog = MaterialBuilder(activity)
                    .setBackgroundRadiusResource(R.dimen.dp_radius_loading)
                    .setTextColor(Color.BLACK)
                    .setMessage(message)
                    .setLoadingColor(Color.BLACK)
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .create()
            dialog!!.show()
        }
    }
}

/**
 * 关闭等待框
 */
fun Activity.dismissLoadingExt() {
    if (dialog != null && dialog!!.isShowing) {
        dialog!!.dismiss()
    }
}

/**
 * 关闭等待框
 */
fun Fragment.dismissLoadingExt() {
    if (dialog != null && dialog!!.isShowing) {
        dialog!!.dismiss()
    }
}
