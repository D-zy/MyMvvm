package com.eg.ui.fragment

import android.os.Bundle
import com.eg.app.base.BaseFragment
import com.eg.databinding.FragmentMsgBinding
import com.eg.viewmodel.state.MsgViewModel

/**
 * 描述　: 消息
 */

class MsgFragment : BaseFragment<MsgViewModel, FragmentMsgBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.titleBar.setTitleMainText("消息")
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {


    }

}