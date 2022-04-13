package com.eg.ui

import android.os.Bundle
import com.eg.app.base.BaseFragment
import com.eg.databinding.FragmentMainBinding
import com.eg.viewmodel.state.MainViewModel

/**
 * 时间　: 2019/12/27
 * 作者　: hegaojian
 * 描述　:项目主页Fragment
 */
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        //初始化viewpager2
//        mDatabind.mainViewpager.initMain(this)
//        //初始化 bottomBar
//        mDatabind.mainBottom.init{
//            when (it) {
//                R.id.menu_main -> mDatabind.mainViewpager.setCurrentItem(0, false)
////                R.id.menu_project -> mainViewpager.setCurrentItem(1, false)
////                R.id.menu_system -> mainViewpager.setCurrentItem(2, false)
////                R.id.menu_public -> mainViewpager.setCurrentItem(3, false)
//                R.id.menu_me -> mDatabind.mainViewpager.setCurrentItem(1, false)
//            }
//        }
////        mDatabind.mainBottom.interceptLongClick(R.id.menu_main,R.id.menu_project,R.id.menu_system,R.id.menu_public,R.id.menu_me)
//        mDatabind.mainBottom.interceptLongClick(R.id.menu_main,R.id.menu_me)
    }

    override fun createObserver() {
//        appViewModel.appColor.observeInFragment(this, Observer {
//            setUiTheme(it, mainBottom)
//        })
    }
}