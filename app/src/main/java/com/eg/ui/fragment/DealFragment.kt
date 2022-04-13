package com.eg.ui.fragment

import android.os.Bundle
import com.eg.app.base.BaseFragment
import com.eg.databinding.FragmentDealBinding
import com.eg.viewmodel.state.TradeViewModel

/**
 * 描述　: 详情
 */

class DealFragment : BaseFragment<TradeViewModel, FragmentDealBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.titleBar.setTitleMainText("详情")
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {


    }

}