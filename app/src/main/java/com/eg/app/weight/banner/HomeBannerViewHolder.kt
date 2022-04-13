package com.eg.app.weight.banner

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/20
 * 描述　:
 */

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.eg.R
import com.eg.data.model.bean.BannerResponse
import com.zhpan.bannerview.BaseViewHolder
import me.hgj.jetpackmvvm.base.appContext

class HomeBannerViewHolder(view: View) : BaseViewHolder<BannerResponse>(view) {
    override fun bindData(data: BannerResponse?, position: Int, pageSize: Int) {
        val img = itemView.findViewById<ImageView>(R.id.bannerhome_img)
        data?.let {
            Glide.with(appContext)
                .load(it.imagePath)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(img)
        }
    }

}
