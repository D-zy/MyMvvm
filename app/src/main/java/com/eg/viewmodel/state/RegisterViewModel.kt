package com.eg.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.databind.StringObservableField

class RegisterViewModel : BaseViewModel() {

    var username = StringObservableField()

    var sms = StringObservableField()
    var pwd = StringObservableField()
    var pwd2 = StringObservableField()


}