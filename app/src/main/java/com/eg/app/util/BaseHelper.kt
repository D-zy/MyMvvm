package com.eg.app.util

import android.app.Activity
import com.aries.ui.util.ResourceUtil

open class BaseHelper(protected var mContext: Activity) {
    protected var mResourceUtil: ResourceUtil

    /**
     * Activity 关闭onDestroy调用
     */
    open fun onDestroy() {
//        EventBus.getDefault().unregister(this)
    }

    init {
        mResourceUtil = ResourceUtil(mContext)
    }
}