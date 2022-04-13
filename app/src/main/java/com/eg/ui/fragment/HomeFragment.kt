package com.eg.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.eg.R
import com.eg.app.base.BaseFragment
import com.eg.app.util.StatusBarUtil
import com.eg.data.model.bean.HomeBean
import com.eg.data.model.bean.HomeBean1
import com.eg.data.model.bean.HomeBean2
import com.eg.databinding.FragmentHomeBinding
import com.eg.ui.activity.CalculateActivity
import com.eg.ui.adapter.HomeAdapter
import com.eg.ui.adapter.*
import com.eg.viewmodel.state.HomeViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 描述　: 首页
 */

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), OnRefreshLoadMoreListener {

    private var adapter = BaseBinderAdapter()

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.vm = mViewModel
        initStatus()

        adapter.addItemBinder(HomeBean1::class.java, HomeItemBinder1())
                .addItemBinder(HomeBean2::class.java, HomeItemBinder2())

        val item = creatItem()
        val item2 = creatItem2()
        mDatabind.apply {
            recyclerView.layoutManager = LinearLayoutManager(mActivity)
            val homeAdapter = HomeAdapter()
//            recyclerView.adapter = homeAdapter
            recyclerView.adapter = adapter
            adapter.addData(item2)
//            val headerLayout1 = LayoutInflater.from(mActivity).inflate(R.layout.layout_header_title, smartRefreshLayout, false)
//            homeAdapter.addHeaderView(headerLayout1)

            homeAdapter.addData(item)
            homeAdapter.setOnItemClickListener { adapter, view, position ->

                launchActivity(CalculateActivity::class.java)
            }
            homeAdapter.setOnItemChildClickListener(object : OnItemChildClickListener {
                override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                    val homeBean = adapter.data[position] as HomeBean
                    when (view.id) {
                        R.id.tv1 -> {
                            showToast(homeBean.tv1)
                        }
                        R.id.tv2 -> {
                            showToast(homeBean.tv2)
                        }
                    }
                }
            })

        }
        val statusBarHeight = com.aries.ui.util.StatusBarUtil.getStatusBarHeight()
        mDatabind.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percent = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
            val minTranslationY = statusBarHeight + SizeUtils.dp2px(0f)
            val maxTranslationY = statusBarHeight + SizeUtils.dp2px(50f)
            val targetTranslationY = maxTranslationY + verticalOffset

            // 1. logo的alpha处理
            mDatabind.mainTopLogo.alpha = 1 - percent

            // 2. 搜索框位移调整
            mDatabind.searchLayout.translationY = if (targetTranslationY < minTranslationY)
                minTranslationY.toFloat() else targetTranslationY.toFloat()

            // 3. 搜索框大小调整
            val maxMarginRight = SizeUtils.dp2px(92f)
            var marginRight = (maxMarginRight * percent * 2).toInt()
            if (marginRight >= maxMarginRight) {
                marginRight = maxMarginRight
            }
            val layoutParams = mDatabind.searchLayout.layoutParams as FrameLayout.LayoutParams
            layoutParams.setMargins(0, 0, marginRight, 0)
            //            layoutParams.setMargins(0, 0, (maxMarginRight * percent).toInt(), 0)
            mDatabind.searchLayout.layoutParams = layoutParams
        })
//        mDatabind.titleBar.setTitleMainText("首页")


    }

    /**
     * 初始化状态栏位置
     */
    private fun initStatus() {
        //注意了，这里使用了第三方库 StatusBarUtil，目的是改变状态栏的alpha
        StatusBarUtil.setTransparentForImageView(mActivity, null)
        //这里是重设我们的title布局的topMargin，StatusBarUtil提供了重设的方法，但是我们这里有两个布局
        //TODO 关于为什么不把Toolbar和@layout/layout_uc_head_title放到一起，是因为需要Toolbar来占位，防止AppBarLayout折叠时将title顶出视野范围
        val statusBarHeight: Int = com.aries.ui.util.StatusBarUtil.getStatusBarHeight()
        val lp1: CoordinatorLayout.LayoutParams = mDatabind.toolbarLayout.layoutParams as CoordinatorLayout.LayoutParams
        lp1.topMargin = statusBarHeight
        mDatabind.toolbarLayout.layoutParams = lp1
        val lp2: CollapsingToolbarLayout.LayoutParams = mDatabind.toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
        lp2.topMargin = statusBarHeight
        mDatabind.toolbar.layoutParams = lp2
    }

    private lateinit var mHomeList: MutableList<HomeBean>
    private lateinit var mHomeItemList: MutableList<Any>

    private fun creatItem(): MutableList<HomeBean> {
        mHomeList = mutableListOf()
        for (i in 'A'..'Z') {
            val homeBean = HomeBean()
            homeBean.tv1 = i.toString()
            homeBean.tv2 = i.toString().lowercase()
            mHomeList.add(homeBean)
        }
        return mHomeList
    }

    private fun creatItem2(): MutableList<Any> {
        mHomeItemList = mutableListOf()
        for (i in 'A'..'Z') {
            if (i == 'C' || i == 'F' || i == 'k' || i == 'G') {
                mHomeItemList.add(HomeBean2(0, i.toString(), i.toString().lowercase(), null))
            } else
                mHomeItemList.add(HomeBean1(0, i.toString(), i.toString().lowercase(), null))
        }
        return mHomeItemList
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {


    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

}