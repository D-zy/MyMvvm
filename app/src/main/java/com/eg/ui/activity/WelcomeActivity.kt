package com.eg.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.eg.ui.MainActivity
import com.eg.R
import com.eg.app.base.BaseActivity
import com.eg.app.util.CacheUtil
import com.eg.app.util.SettingUtil
import com.eg.app.weight.banner.WelcomeBannerAdapter
import com.eg.app.weight.countdown.CountDownView2
import com.eg.databinding.ActivityWelcomeBinding
import com.eg.ui.MainActivity2
import com.zhpan.bannerview.BannerViewPager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.weight.banner.WelcomeBannerViewHolder
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.visible

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/22
 * 描述　:
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WelcomeActivity : BaseActivity<BaseViewModel, ActivityWelcomeBinding>() {

    private var resList = arrayOf("唱", "跳", "r a p")

    private lateinit var mViewPager: BannerViewPager<String, WelcomeBannerViewHolder>

    override fun initView(savedInstanceState: Bundle?) {
        //防止出现按Home键回到桌面时，再次点击重新进入该界面bug
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }


        mDatabind.cdView1.setOnFinishListener(object : CountDownView2.OnFinishListener {
            override fun onFinish() {
                launchActivity(if(CacheUtil.isLogin)MainActivity2::class.java else LoginActivity::class.java, true)
            }
        })

//        mDatabind.welcomeBaseview.setBackgroundColor(SettingUtil.getColor(this))
        mViewPager = findViewById(R.id.banner_view)
        if (CacheUtil.isFirst) {
            //是第一次打开App 显示引导页
//            mDatabind.welcomeImage.gone()
            mDatabind.cdView1.gone()
            mDatabind.bannerView.visible()
            mViewPager.apply {
                adapter = WelcomeBannerAdapter()
                setLifecycleRegistry(lifecycle)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (position == resList.size - 1) {
                            mDatabind.welcomeJoin.visible()
                        } else {
                            mDatabind.welcomeJoin.gone()
                        }
                    }
                })
                create(resList.toList())
            }
        } else {
            //不是第一次打开App
//            mDatabind.welcomeImage.visible()
            mDatabind.bannerView.gone()
            mDatabind.cdView1.visible()
            mDatabind.cdView1.start()

//                //带点渐变动画
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
    }

    private fun startGo() {
        GlobalScope.launch(Dispatchers.IO) {
            when {
                CacheUtil.isLogin -> launchActivity(MainActivity2::class.java, true)
                !CacheUtil.isLogin -> launchActivity(LoginActivity::class.java, true)
            }
        }
    }

    override fun initListener() {
        setOnClickListener(mDatabind.welcomeJoin) {
            when (this) {
                mDatabind.welcomeJoin -> {
                    CacheUtil.isFirst = false
                    launchActivity(LoginActivity::class.java, true)
                    //带点渐变动画
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        }
    }


}