package com.eg.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.databind.StringObservableField

class LoginViewModel : BaseViewModel() {

    var username = StringObservableField()

    var pwd = StringObservableField()

    var otp = StringObservableField()

    var captchaCode = StringObservableField()

}