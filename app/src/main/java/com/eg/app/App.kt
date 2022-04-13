package com.eg.app

import androidx.multidex.MultiDex
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.eg.BuildConfig
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.base.BaseApp
import com.eg.app.event.AppViewModel
import com.eg.app.event.EventViewModel
import com.eg.app.util.MmkvUtil
import com.eg.app.weight.loadCallBack.EmptyCallback
import com.eg.app.weight.loadCallBack.ErrorCallback
import com.eg.app.weight.loadCallBack.LoadingCallback
import com.eg.ui.activity.LoginActivity
import com.hjq.toast.ToastUtils
import me.hgj.jetpackmvvm.ext.util.jetpackMvvmLog
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig

//Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { App.appViewModelInstance }

//Application全局的ViewModel，用于发送全局通知操作
val eventViewModel: EventViewModel by lazy { App.eventViewModelInstance }

class App : BaseApp() {

    companion object {
        lateinit var instance: App
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)
        MmkvUtil.getInstance(this)
        ToastUtils.init(this) // 初始化Toast
        MultiDex.install(this)
        AutoSizeConfig(); // 屏幕适配
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()

        jetpackMvvmLog = BuildConfig.DEBUG

        //防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
//            .restartActivity(WelcomeActivity::class.java) // 重启的activity
            .errorActivity(LoginActivity::class.java) //发生错误跳转的activity
            .apply()
    }

    private fun AutoSizeConfig() {
        AutoSize.initCompatMultiProcess(this)
        AutoSizeConfig.getInstance()
                .setBaseOnWidth(false).isCustomFragment = true
    }


}
