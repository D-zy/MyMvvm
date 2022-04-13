package com.eg.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.eg.R
import com.eg.app.base.BaseActivity
import com.eg.databinding.ActivityMain2Binding
import com.eg.ui.fragment.DealFragment
import com.eg.ui.fragment.HomeFragment
import com.eg.ui.fragment.MeFragment
import com.eg.ui.fragment.MsgFragment
import com.eg.viewmodel.state.MainViewModel

class MainActivity2 : BaseActivity<MainViewModel, ActivityMain2Binding>() {

    private var fragment1: Fragment? = null
    private var fragment2: Fragment? = null
    private var fragment3: Fragment? = null
    private var fragment4: Fragment? = null

    override fun initView(savedInstanceState: Bundle?) {
//        mDatabind.vm = mViewModel
//        mDatabind.tabViewMsg.showMsg(10)
        displayFragment(b = true)
    }

    override fun initListener() {
        mDatabind.apply {
            setOnClickListener(tabViewMain, tabViewDeal, tabViewMsg, tabViewMine) {
                when (this) {
                    tabViewMain -> displayFragment(b = true)
                    tabViewDeal -> displayFragment(1, b1 = true)
                    tabViewMsg -> displayFragment(2, b2 = true)
                    tabViewMine -> displayFragment(3, b3 = true)
                }
            }
        }
    }

    private fun displayFragment(position: Int = 0, b: Boolean = false, b1: Boolean = false, b2: Boolean = false, b3: Boolean = false) {
        mDatabind.apply {
            if (b) tabViewMain.selected() else tabViewMain.unSelected()
            if (b1) tabViewDeal.selected() else tabViewDeal.unSelected()
            if (b2) tabViewMsg.selected() else tabViewMsg.unSelected()
            if (b3) tabViewMine.selected() else tabViewMine.unSelected()
        }
        fragment1?.let { supportFragmentManager.beginTransaction().hide(it).commit() }
        fragment2?.let { supportFragmentManager.beginTransaction().hide(it).commit() }
        fragment3?.let { supportFragmentManager.beginTransaction().hide(it).commit() }
        fragment4?.let { supportFragmentManager.beginTransaction().hide(it).commit() }
        when (position) {
            0 -> fragment1?.let { supportFragmentManager.beginTransaction().show(it).commit() } ?: let {
                fragment1 = HomeFragment()
                supportFragmentManager.beginTransaction().add(R.id.fl_main, fragment1!!).commit()
            }
            1 -> fragment2?.let { supportFragmentManager.beginTransaction().show(it).commit() } ?: let {
                fragment2 = DealFragment()
                supportFragmentManager.beginTransaction().add(R.id.fl_main, fragment2!!).commit()
            }
            2 -> fragment3?.let { supportFragmentManager.beginTransaction().show(it).commit() } ?: let {
                fragment3 = MsgFragment()
                supportFragmentManager.beginTransaction().add(R.id.fl_main, fragment3!!).commit()
            }
            3 -> fragment4?.let { supportFragmentManager.beginTransaction().show(it).commit() } ?: let {
                fragment4 = MeFragment()
                supportFragmentManager.beginTransaction().add(R.id.fl_main, fragment4!!).commit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (supportFragmentManager.fragments.size > 0) {
            val fragments = supportFragmentManager.fragments
            for (f in fragments) {
                f.onActivityResult(requestCode,resultCode,data)
            }
        }
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