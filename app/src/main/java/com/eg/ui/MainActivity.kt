package com.eg.ui

import android.os.Bundle
import com.eg.R
import com.eg.app.base.BaseActivity
import com.eg.app.ext.*
import com.eg.app.util.StatusBarUtil
import com.eg.databinding.ActivityMainBinding
import com.eg.viewmodel.state.MainViewModel
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.apply {
            bnve.setIconsMarginTop(BottomNavigationViewEx.dp2px(mActivity, 12f));
            //初始化viewpager2
            mainViewpager.initMain(this@MainActivity)
            //初始化 bottomBar
            bnve.init {
                when (it) {
                    R.id.menu_main -> mDatabind.mainViewpager.setCurrentItem(0, false)
                    R.id.menu_me -> mDatabind.mainViewpager.setCurrentItem(1, false)
                }
            }
        }
        StatusBarUtil.setTransparentForImageView(mActivity, null)
    }

    var exitTime = 0L
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            showToast("再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}