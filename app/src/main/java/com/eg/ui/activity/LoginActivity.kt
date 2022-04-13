package com.eg.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.aries.ui.view.title.TitleBarView
import com.eg.ui.MainActivity2
import com.eg.app.base.BaseActivity
import com.eg.app.util.CacheUtil
import com.eg.databinding.ActivityLoginBinding
import com.eg.viewmodel.request.RequestLoginRegisterViewModel
import com.eg.viewmodel.state.LoginViewModel
import me.hgj.jetpackmvvm.ext.parseState

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun initTitleBar(titleBar: TitleBarView) {
        titleBar.setTitleMainText("登录").setLeftTextDrawable(null)
    }

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestLoginRegisterViewModel)
        mDatabind.vm = mViewModel

        mViewModel.username.set("dev-auto")
        mViewModel.pwd.set("1234qwer")
        mViewModel.otp.set("123456")

    }

    override fun initListener() {
        setOnClickListener(mDatabind.btLogin, mDatabind.tvSkipRegister) {
            when (this) {
                mDatabind.btLogin -> {
//                    val pwd = "1234qwer"
//                    val time = System.currentTimeMillis().toString() + ""
//                    val password = EncryptUtils.encryptSHA256ToString(EncryptUtils.encryptSHA256ToString(pwd).lowercase() + time).lowercase()
//                    requestLoginRegisterViewModel.loginReq(time, mViewModel.username.get(), password, mViewModel.otp.get())

                    CacheUtil.isLogin = true
                    launchActivity(MainActivity2::class.java)
                }
                mDatabind.tvSkipRegister -> {
                    launchActivity(RegisterActivity::class.java)
                }
            }
        }
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.loginResult.observe(this, { bean ->
            parseState(bean, {
                //登录成功
                showToast(it.toString())
            }, {
                //登录失败
                showToast(it.errCode)
            })
        })
    }

}