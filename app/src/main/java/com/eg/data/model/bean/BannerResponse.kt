package com.eg.data.model.bean

import android.annotation.SuppressLint
import java.io.Serializable

/**
 * 轮播图
 */
@SuppressLint("ParcelCreator")
data class BannerResponse(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) : Serializable


