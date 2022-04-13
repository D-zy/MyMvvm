package com.eg.app.util

import android.app.Activity
import android.content.Intent
import com.eg.R
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import me.hgj.jetpackmvvm.util.LogUtils
import java.util.*

/**
 * @Function: 图片选择帮助类-演示控制三方库状态栏及导航栏效果
 * @Description:
 */
class ImagePickerUtil(activity: Activity?) : BaseHelper(activity!!) {
    private var mRequestCode = 0
    private var mOnImageSelect: OnImageSelect? = null

    interface OnImageSelect {
        fun onImageSelect(requestCode: Int, list: List<String>)
    }

    fun selectPicture(requestCode: Int, onImageSelect: OnImageSelect) {
        mOnImageSelect = onImageSelect
        mRequestCode = requestCode
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .isCompress(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .selectionMode(PictureConfig.SINGLE)
                .imageEngine(GlideUtil.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .forResult(mRequestCode)
    }

    fun selectCamera(requestCode: Int, onImageSelect: OnImageSelect) {
        mOnImageSelect = onImageSelect
        mRequestCode = requestCode

        PictureSelector.create(mContext)
                .openCamera(PictureMimeType.ofImage())
                .isCompress(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .selectionMode(PictureConfig.SINGLE)
                .imageEngine(GlideUtil.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .forResult(mRequestCode)

//        PictureSelector.create(mContext)
//                .openCamera(PictureMimeType.ofImage())
//                .imageEngine(GlideUtil.createGlideEngine())// 外部传入图片加载引擎，必传项
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
////                .theme(R.style.picture_white_style)
////                .theme(if (StatusBarUtil.isSupportStatusBarFontChange()) R.style.PicturePickerStyle else R.style.PicturePickerStyle_White)
//                .selectionMode(PictureConfig.SINGLE)
//                .forResult(mRequestCode)
    }

    fun selectPicture(requestCode: Int, count: Int, onImageSelect: OnImageSelect) {
        mOnImageSelect = onImageSelect
        mRequestCode = requestCode
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .minSelectNum(1)
                .isCompress(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .maxSelectNum(count)
                .selectionMode(PictureConfig.TYPE_PICTURE)
                .imageEngine(GlideUtil.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .forResult(mRequestCode)
    }

    fun selectVideo(requestCode: Int, count: Int, onImageSelect: OnImageSelect) {
        mOnImageSelect = onImageSelect
        mRequestCode = requestCode
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofVideo())
                .isCompress(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .minVideoSelectNum(1)
                .maxVideoSelectNum(count)
                .selectionMode(PictureConfig.TYPE_VIDEO)
                .imageEngine(GlideUtil.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .forResult(mRequestCode)
    }

    fun selectAudio(requestCode: Int, count: Int, onImageSelect: OnImageSelect) {
        mOnImageSelect = onImageSelect
        mRequestCode = requestCode
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofAudio())
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .minSelectNum(1)
                .maxSelectNum(count)
                .selectionMode(PictureConfig.TYPE_ALL)
                .imageEngine(GlideUtil.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .forResult(mRequestCode)
    }

    fun selectFile(requestCode: Int, count: Int, onImageSelect: OnImageSelect) {
        mOnImageSelect = onImageSelect
        mRequestCode = requestCode
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .maxSelectNum(count)
                .selectionMode(PictureConfig.TYPE_ALL)
                .imageEngine(GlideUtil.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .forResult(mRequestCode)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LogUtils.debugInfo("onActivityResult", "path:")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode != mRequestCode) return
            // 图片、视频、音频选择结果回调
            val selectList = PictureSelector.obtainMultipleResult(data)
            LogUtils.debugInfo("onActivityResult", "selectList:" + Gson().toJson(selectList))
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            val list: MutableList<String> = ArrayList()
            for (item in selectList) {
//                if (item.isCompressed) {
//                    list.add(item.compressPath)
//                } else if (!item.realPath.isNullOrEmpty()) {
//                    list.add(item.realPath)
//                } else {
                    list.add(item.path)
//                }
                LogUtils.debugInfo("onActivityResult", "path:" + item.path)
            }
            mOnImageSelect?.onImageSelect(mRequestCode, list)
        }
    }

    companion object {
        const val IMG = 10000
    }
}