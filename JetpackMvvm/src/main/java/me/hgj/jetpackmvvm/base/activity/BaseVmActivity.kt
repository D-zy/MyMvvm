package me.hgj.jetpackmvvm.base.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aries.ui.util.StatusBarUtil
import com.aries.ui.view.title.TitleBarView
import com.parfoismeng.slidebacklib.SlideBack
import me.hgj.jetpackmvvm.R
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getVmClazz
import me.hgj.jetpackmvvm.ext.util.notNull
import me.hgj.jetpackmvvm.network.manager.NetState
import me.hgj.jetpackmvvm.network.manager.NetworkStateManager

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModelActivity基类，把ViewModel注入进来了
 */
abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var mViewModel: VM

    lateinit var mActivity: Activity

    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        initDataBind().notNull({
            setContentView(it)
        }, {
            setContentView(layoutId())
        })
        init(savedInstanceState)
        setTitleBar()
        initListener()
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observeInActivity(this, Observer {
            onNetworkStateChanged(it)
        })
        //设置类全面屏手势滑动返回
        SlideBack.with(this)
                .haveScroll(true)
                .edgeMode(SlideBack.EDGE_BOTH)
                .callBack { onBackPressed() }
                .register()
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingChange.showDialog.observeInActivity(this, Observer {
            showLoading(it)
        })
        //关闭弹窗
        mViewModel.loadingChange.dismissDialog.observeInActivity(this, Observer {
            dismissLoading()
        })
    }

    /**
     * 将非该Activity绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            //显示弹窗
            viewModel.loadingChange.showDialog.observeInActivity(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.loadingChange.dismissDialog.observeInActivity(this, Observer {
                dismissLoading()
            })
        }
    }

    /**
     * 供子类BaseVmDbActivity 初始化Databinding操作
     */
    open fun initDataBind(): View? {
        return null
    }

    open fun initListener() {}

    open fun initTitleBar(titleBar: TitleBarView) {

    }

    private var titleBar: TitleBarView? = null
    private fun setTitleBar() {
        titleBar = findViewById(R.id.titleBar)
        titleBar?.let {
            //是否支持状态栏白色
            val isSupport = StatusBarUtil.isSupportStatusBarFontChange()
            it.setStatusBarLightMode(!isSupport) //不支持黑字的设置白透明
                    .setStatusAlpha(if (isSupport) 0 else 102)
                    .setLeftTextDrawable(R.drawable.ic_back)
                    .setOnLeftTextClickListener { onBackPressed() }
            initTitleBar(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SlideBack.unregister(this)
    }
}