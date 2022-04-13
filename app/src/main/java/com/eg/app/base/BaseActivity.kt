package com.eg.app.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.databinding.ViewDataBinding
import com.eg.R
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import com.eg.app.ext.dismissLoadingExt
import com.eg.app.ext.showLoadingExt
import com.hjq.toast.ToastUtils

/**
 * 时间　: 2019/12/21
 * 作者　: hegaojian
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}


    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(this,message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

    /* */
    /**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     *//*
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }*/

    fun showToast(msg: String?) {
        msg?.let {
            ToastUtils.setView(R.layout.toast_custom_view)
            ToastUtils.setGravity(Gravity.BOTTOM, 0, 120)
            ToastUtils.show(it)
        }
    }

    /**
     * 页面跳转
     * @param clz 要跳转的Activity
     */
    fun launchActivity(clz: Class<*>, isFinish: Boolean = false) {
        startActivity(Intent(this, clz))
        if (isFinish) finish()
    }

    /**
     * 页面跳转
     * @param clz    要跳转的Activity
     * @param intent intent
     */
    fun launchActivity(clz: Class<*>, intent: Intent, isFinish: Boolean = false) {
        intent.setClass(this, clz)
        startActivity(intent)
        if (isFinish) finish()
    }

    /**
     * 批量设置控件点击事件。
     *
     * @param v 点击的控件
     * @param block 处理点击事件回调代码块
     */
    protected fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
        val listener = View.OnClickListener {
            if (doNotQuickClick(it)) {
                it.block()
            }
        }
        v.forEach { it?.setOnClickListener(listener) }
    }

    /**
     * 批量设置控件点击事件。
     *
     * @param v 点击的控件
     * @param listener 处理点击事件监听器
     */
    protected fun setOnClickListener(vararg v: View?, listener: View.OnClickListener) {
        v.forEach { it?.setOnClickListener(listener) }
    }

    /**
     * 连续双击view间隔
     */
    open fun doNotQuickClick(view: View?): Boolean {
        view?.let {
            val obj = it.getTag(-1)
            val current = System.currentTimeMillis()
            if (obj is Long) {
                val distance = current - obj
                if (distance < 500) return false else it.setTag(-1, current)
            } else it.setTag(-1, current)
            return true
        }
        return false
    }

}