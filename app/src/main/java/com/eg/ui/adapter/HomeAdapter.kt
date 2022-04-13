package com.eg.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.eg.R
import com.eg.data.model.bean.HomeBean
import com.eg.databinding.ItemRvHomeBinding

class HomeAdapter : BaseQuickAdapter<HomeBean, BaseDataBindingHolder<ItemRvHomeBinding>>(R.layout.item_rv_home) {

//    override fun convert(holder: BaseViewHolder, item: HomeBean) {
//        holder.setText(R.id.tv1, item.tv1)
//                .setText(R.id.tv2, item.tv2)
//    }

    override fun convert(holder: BaseDataBindingHolder<ItemRvHomeBinding>, item: HomeBean) {
//        val dataBinding = holder.dataBinding
//        dataBinding?.let {
//            dataBinding.homeBean = item
//            dataBinding.executePendingBindings()
//        }

        holder.dataBinding?.apply {
            homeBean = item
            addChildClickViewIds(R.id.tv1)
            addChildClickViewIds(R.id.tv2)
            executePendingBindings()
        }

    }

}