package com.eg.app.weight.loadCallBack

import com.eg.R
import com.kingja.loadsir.callback.Callback

class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

}