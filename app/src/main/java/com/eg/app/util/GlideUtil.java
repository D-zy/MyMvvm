package com.eg.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.eg.R;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

import java.security.MessageDigest;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * @author：luck
 * @date：2019-11-13 17:02
 * @describe：Glide加载引擎
 */
public class GlideUtil implements ImageEngine {

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @param callback      网络图片加载回调监听 {link after version 2.5.1 Please use the #OnImageCompleteCallback#}
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView, OnImageCompleteCallback callback) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (callback != null) {
                            callback.onShowLoading();
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(0, new PointF(0, 0), 0));
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                        }
                    }
                });
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @ 已废弃
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(0, new PointF(0, 0), 0));
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                        }
                    }
                });
    }

    /**
     * 加载相册目录
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .override(180, 180)
                .centerCrop()
                .sizeMultiplier(0.5f)
                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(8);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    /**
     * 加载gif
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadAsGifImage(@NonNull Context context, @NonNull String url,
                               @NonNull ImageView imageView) {
        Glide.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
                .into(imageView);
    }


    private GlideUtil() {
    }

    private static GlideUtil instance;

    public static GlideUtil createGlideEngine() {
        if (null == instance) {
            synchronized (GlideUtil.class) {
                if (null == instance) {
                    instance = new GlideUtil();
                }
            }
        }
        return instance;
    }



    private static int sCommonPlaceholder = -1;
    private static int sCirclePlaceholder = -1;
    private static int sRoundPlaceholder = -1;

    private static Drawable sCommonPlaceholderDrawable;
    private static Drawable sCirclePlaceholderDrawable;
    private static Drawable sRoundPlaceholderDrawable;
    @ColorInt
    private static int mPlaceholderColor = Color.LTGRAY;
    private static float mPlaceholderRoundRadius = 4f;

    private static void setDrawable(GradientDrawable gd, float radius) {
        gd.setColor(mPlaceholderColor);
        gd.setCornerRadius(radius);
    }

    /**
     * 设置默认颜色
     *
     * @param placeholderColor
     */
    public static void setPlaceholderColor(@ColorInt int placeholderColor) {
        mPlaceholderColor = placeholderColor;
        sCommonPlaceholderDrawable = new GradientDrawable();
        sCirclePlaceholderDrawable = new GradientDrawable();
        sRoundPlaceholderDrawable = new GradientDrawable();
        setDrawable((GradientDrawable) sCommonPlaceholderDrawable, 0);
        setDrawable((GradientDrawable) sCirclePlaceholderDrawable, 10000);
        setDrawable((GradientDrawable) sRoundPlaceholderDrawable, mPlaceholderRoundRadius);
    }

    /**
     * 设置圆角图片占位背景图圆角幅度
     *
     * @param placeholderRoundRadius
     */
    public static void setPlaceholderRoundRadius(float placeholderRoundRadius) {
        mPlaceholderRoundRadius = placeholderRoundRadius;
        setPlaceholderColor(mPlaceholderColor);
    }

    /**
     * 设置圆形图片的占位图
     *
     * @param circlePlaceholder
     */
    public static void setCirclePlaceholder(int circlePlaceholder) {
        sCirclePlaceholder = circlePlaceholder;
    }

    public static void setCirclePlaceholder(Drawable circlePlaceholder) {
        sCirclePlaceholderDrawable = circlePlaceholder;
    }

    /**
     * 设置正常图片的占位符
     *
     * @param commonPlaceholder
     */
    public static void setCommonPlaceholder(int commonPlaceholder) {
        sCommonPlaceholder = commonPlaceholder;
    }

    public static void setCommonPlaceholder(Drawable commonPlaceholder) {
        sCommonPlaceholderDrawable = commonPlaceholder;
    }

    /**
     * 设置圆角图片的占位符
     *
     * @param roundPlaceholder
     */
    public static void setRoundPlaceholder(int roundPlaceholder) {
        sRoundPlaceholder = roundPlaceholder;
    }

    public static void setRoundPlaceholder(Drawable roundPlaceholder) {
        sRoundPlaceholderDrawable = roundPlaceholder;
    }

    /**
     * 普通加载图片
     *
     * @param obj
     * @param iv
     * @param placeholder
     */
    public static void loadImg(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate())
                .into(iv);
    }


    public static void loadImg(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadImg(obj, iv, drawable != null ? drawable : sCommonPlaceholderDrawable);
    }

    public static void loadImg(Object obj, ImageView iv) {
        loadImg(obj, iv, sCommonPlaceholder);
    }

    /**
     * 加载圆形图片
     *
     * @param obj
     * @param iv
     * @param placeholder 占位图
     */
    public static void loadCircleImg(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .transform(new CircleCrop())).into(iv);
    }

    public static void loadCircleImg(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadCircleImg(obj, iv, drawable != null ? drawable : sCirclePlaceholderDrawable);
    }

    public static void loadCircleImg(Object obj, ImageView iv) {
        loadCircleImg(obj, iv, sCirclePlaceholder);
    }

    /**
     * 加载圆角图片
     *
     * @param obj                 加载的图片资源
     * @param iv
     * @param dp                  圆角尺寸-dp
     * @param placeholder         -占位图
     * @param isOfficial-是否官方模式圆角
     */
    public static void loadRoundImg(Object obj, ImageView iv, float dp, Drawable placeholder, boolean isOfficial) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .transform(isOfficial ? new RoundedCorners(dp2px(dp)) : new GlideUtil.GlideRoundTransform(dp2px(dp)))).into(iv);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp, int placeholderResource, boolean isOfficial) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadRoundImg(obj, iv, dp, drawable != null ? drawable : sRoundPlaceholderDrawable, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp, boolean isOfficial) {
        loadRoundImg(obj, iv, dp, sRoundPlaceholder, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp) {
        loadRoundImg(obj, iv, dp, true);
    }

    public static void loadRoundImg(Object obj, ImageView iv, boolean isOfficial) {
        loadRoundImg(obj, iv, mPlaceholderRoundRadius, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv) {
        loadRoundImg(obj, iv, true);
    }

    private static RequestOptions getRequestOptions() {
        RequestOptions requestOptions = new RequestOptions()
                // 填充方式
                .centerCrop()
                //优先级
                .priority(Priority.HIGH)
                //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }

    private static int dp2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private static Drawable getDrawable(Context context, @DrawableRes int res) {
        Drawable drawable = null;
        try {
            drawable = context.getResources().getDrawable(res);
        } catch (Exception e) {

        }
        return drawable;
    }

    private static class GlideRoundTransform extends BitmapTransformation {
        int radius;

        public GlideRoundTransform(int dp) {
            super();
            this.radius = dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }

}
