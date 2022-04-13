package com.eg.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.aries.ui.view.title.TitleBarView
import com.blankj.utilcode.util.SpanUtils
import com.eg.R
import com.eg.app.base.BaseActivity
import com.eg.app.weight.preventkeyboardblock.PreventKeyboardBlockUtil
import com.eg.databinding.ActivityRegisterBinding
import com.eg.viewmodel.state.RegisterViewModel

class RegisterActivity : BaseActivity<RegisterViewModel, ActivityRegisterBinding>() {

    override fun initTitleBar(titleBar: TitleBarView) {
        titleBar.setTitleMainText("注册")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel

        SpanUtils.with(mDatabind.tvHint).append("我已阅读并同意").append("《xx用户协议》").setForegroundColor(Color.parseColor("#0076FF"))
                .append("、").append("《隐私政策》").setForegroundColor(Color.parseColor("#0076FF"))
                .append(",并已授权xx使用该app账号信息（如昵称、头像、收货地址）进行统一管理").create()
    }

    override fun initListener() {
        setOnClickListener(mDatabind.cdvSms, mDatabind.btRegister) {
            when (this) {
                mDatabind.cdvSms -> when {
                    mViewModel.username.get().isEmpty() -> mDatabind.xetPhoneNo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_anim))
                    else -> mDatabind.cdvSms.start()
                }
                mDatabind.btRegister -> when {
                    mViewModel.sms.get().length != 4 -> showToast("验证码错误")
                    mViewModel.pwd.get() != mViewModel.pwd2.get() -> showToast("输入密码不一致")
                    else -> goRegister()
                }
            }
        }
    }

    private fun goRegister() {

    }

    override fun onResume() {
        super.onResume()
        //布局上移解决软键盘挡住
//        PreventKeyboardBlockUtil.getInstance(this).setBtnView(mDatabind.preventView).register();
    }

    override fun onPause() {
        super.onPause()
//        PreventKeyboardBlockUtil.getInstance(this).unRegister();
    }

}