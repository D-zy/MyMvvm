package com.eg.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.chad.library.adapter.base.binder.QuickDataBindingItemBinder
import com.eg.R
import com.eg.data.model.bean.HomeBean
import com.eg.data.model.bean.HomeBean1
import com.eg.data.model.bean.HomeBean2
import com.eg.databinding.ItemRvHomeBinding
import com.eg.databinding.ItemRvHomeItem1Binding
import com.eg.databinding.ItemRvHomeItem2Binding

class HomeItemBinder1 : QuickDataBindingItemBinder<HomeBean1, ItemRvHomeItem1Binding>() {

    override fun convert(holder: BinderDataBindingHolder<ItemRvHomeItem1Binding>, data: HomeBean1) {
        holder.dataBinding.apply {
            homeBean = data
            addChildClickViewIds(R.id.tv1)
            addChildClickViewIds(R.id.tv2)
            executePendingBindings()
        }
    }

    override fun onCreateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): ItemRvHomeItem1Binding {
        return ItemRvHomeItem1Binding.inflate(layoutInflater, parent, false)
    }

}

class HomeItemBinder2 : QuickDataBindingItemBinder<HomeBean2, ItemRvHomeItem2Binding>() {

    override fun convert(holder: BinderDataBindingHolder<ItemRvHomeItem2Binding>, data: HomeBean2) {

    }

    override fun onCreateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): ItemRvHomeItem2Binding {
        return ItemRvHomeItem2Binding.inflate(layoutInflater, parent, false)
    }

}