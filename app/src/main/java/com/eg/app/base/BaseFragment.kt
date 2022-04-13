package com.eg.app.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.databinding.ViewDataBinding
import com.eg.R
import me.hgj.jetpackmvvm.base.fragment.BaseVmDbFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import com.eg.app.ext.dismissLoadingExt
import com.eg.app.ext.hideSoftKeyboard
import com.eg.app.ext.showLoadingExt
import com.hjq.toast.ToastUtils

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/21
 * 描述　: 你项目中的Fragment基类，在这里实现显示弹窗，吐司，还有自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmFragment例如
 * abstract class BaseFragment<VM : BaseViewModel> : BaseVmFragment<VM>() {
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbFragment<VM, DB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法
     */
    override fun lazyLoadData() {}

    /**
     * 创建LiveData观察者 Fragment执行onViewCreated后触发
     */
    override fun createObserver() {}

    /**
     * Fragment执行onViewCreated后触发
     */
    override fun initData() {

    }


    /**
     * 打开等待框
     */
    override fun showLoading(message: String?) {
        activity?.let { showLoadingExt(it, message) }
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(activity)
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    override fun lazyLoadTime(): Long {
        return 300
    }

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
        startActivity(Intent(mActivity, clz))
        if (isFinish) mActivity.finish()
    }

    /**
     * 页面跳转
     * @param clz    要跳转的Activity
     * @param intent intent
     */
    fun launchActivity(clz: Class<*>, intent: Intent, isFinish: Boolean = false) {
        intent.setClass(mActivity, clz)
        startActivity(intent)
        if (isFinish) mActivity.finish()
    }

}