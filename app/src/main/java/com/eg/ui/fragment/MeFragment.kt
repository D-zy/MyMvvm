package com.eg.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.eg.app.base.BaseFragment
import com.eg.app.util.*
import com.eg.databinding.FragmentMeBinding
import com.eg.ui.activity.LoginActivity
import com.eg.viewmodel.state.MeViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions


/**
 * 描述　: 我的
 */

class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {

    private lateinit var imagePicker: ImagePickerUtil

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        imagePicker = ImagePickerUtil(mActivity)

        mDatabind.logout.setOnClickListener {
            val alpha = mDatabind.logout.alpha
            if (alpha > 0.8)
                launchActivity(LoginActivity::class.java, true)
        }
        mDatabind.avater.setOnClickListener {
            XXPermissions.with(this) // 不适配 Android 11 可以这样写
                    //.permission(Permission.Group.STORAGE)
                    // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .permission(Permission.CAMERA)
                    .request { permissions, all ->
                        if (all) {
                            val item = arrayOf("拍照", "相册")
                            AlertDialogUtils.showItems(mActivity, "请选择", item) { position: Int ->
                                when (position) {
                                    0 -> imagePicker.selectCamera(1001, object : ImagePickerUtil.OnImageSelect {
                                        override fun onImageSelect(requestCode: Int, list: List<String>) {
                                            if (list.isNotEmpty() || requestCode == 1001) {
                                                GlideUtil.loadCircleImg(list[0], mDatabind.avater)
                                            }
                                        }
                                    })
                                    1 -> imagePicker.selectPicture(1000, object : ImagePickerUtil.OnImageSelect {
                                        override fun onImageSelect(requestCode: Int, list: List<String>) {
                                            if (list.isNotEmpty() || requestCode == 1000) {
                                                GlideUtil.loadCircleImg(list[0], mDatabind.avater)
                                            }
                                        }
                                    })
                                }
                            }
                        }
                    }

        }

        mDatabind.appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                val percent = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
                mDatabind.llcontent.alpha = 0.7f - percent
                mDatabind.titlebar.alpha = percent
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker.onActivityResult(requestCode, resultCode, data)
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {

    }

}