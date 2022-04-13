package com.eg.app.behavior;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;


/**
 * Created by gjm on 2017/5/24.
 * 目前包括的事件：
 * 图片放大回弹  https://www.jianshu.com/p/bb3fe452e1f5?from=singlemessage
 * 个人信息布局的top和botoom跟随图片位移
 * toolbar背景变色
 */
public class AppBarLayoutOverScrollViewBehavior extends AppBarLayout.Behavior {
    private static final String TAG_OVERSCROLL = "overScroll";
    private static final String TAG_TOOLBAR = "toolbar";
    private static final String TAG_MIDDLE = "middle";
    private static final String TAG_NAME = "name";
    private static final float TARGET_HEIGHT = 1500;
    private View mTargetView;
    private int mParentHeight;
    private int mTargetViewHeight;
    private float mTotalDy;
    private float mLastScale;
    private int mLastBottom;
    private boolean isAnimate;
    private Toolbar mToolBar;
    private ViewGroup middleLayout;//个人信息布局
    private TextView mName; //姓名
    private int mMiddleHeight;
    private boolean isRecovering = false;//是否正在自动回弹中

    private final float MAX_REFRESH_LIMIT = 0.3f;//达到这个下拉临界值就开始刷新动画

    public AppBarLayoutOverScrollViewBehavior() {
    }

    public AppBarLayoutOverScrollViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);

        if (mToolBar == null) {
            mToolBar = (Toolbar) parent.findViewWithTag(TAG_TOOLBAR);
        }
        if (middleLayout == null) {
            middleLayout = (ViewGroup) parent.findViewWithTag(TAG_MIDDLE);
        }

        if (mName == null) {
            mName = (TextView) parent.findViewWithTag(TAG_NAME);
        }
        // 需要在调用过super.onLayoutChild()方法之后获取
        if (mTargetView == null) {
            mTargetView = parent.findViewWithTag(TAG_OVERSCROLL);
            if (mTargetView != null) {
                initial(abl);
            }
        }

        abl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                mToolBar.setAlpha(Float.valueOf(Math.abs(i)) / Float.valueOf(appBarLayout.getTotalScrollRange()));
//                setViewAlpha(appBarLayout, i);
            }

        });
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        isAnimate = true;
        if (target instanceof DisInterceptNestedScrollView) return true;//这个布局就是middleLayout
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        System.out.println("1234dy_:  " + dy + "     child.getBottom()_ :" + child.getBottom() + "   mParentHeight_:" + mParentHeight+ "   mToolBar:" + mToolBar.getBottom());
        if (!isRecovering) {
            // 1.mTargetView不为null
            // 2.dy>0表示向上滑动
            // 3.AppBarLayout尚未恢复到原始高度child.getBottom() > mParentHeight

            // 2.dy<0表示向下滑动
            // 3.AppBarLayout已经完全展开，child.getBottom() >= mParentHeight
            if (mTargetView != null && ((dy < 0 && child.getBottom() >= mParentHeight)
                    || (dy > 0 && child.getBottom() > mParentHeight))) {
                scale(child, target, dy);
                return;
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        if (velocityY > 100) {//当y速度>100,就秒弹回
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
    }

    private void initial(AppBarLayout abl) {
        abl.setClipChildren(false);
        mParentHeight = abl.getHeight();
        mTargetViewHeight = mTargetView.getHeight();
        mMiddleHeight = middleLayout.getHeight();
    }

    private void scale(AppBarLayout abl, View target, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
        ViewCompat.setScaleX(mTargetView, mLastScale);
        ViewCompat.setScaleY(mTargetView, mLastScale);
        mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
        abl.setBottom(mLastBottom);
        target.setScrollY(0);

        middleLayout.setTop(mLastBottom - mMiddleHeight);
        middleLayout.setBottom(mLastBottom);

        if (onProgressChangeListener != null) {
            float progress = Math.min((mLastScale - 1) / MAX_REFRESH_LIMIT, 1);//计算0~1的进度
            onProgressChangeListener.onProgressChange(progress, false);
        }
    }

    private void setViewAlpha(AppBarLayout appBarLayout, int dy) {
//        float percent = (float) Math.abs(child.getY()) / mLimitHeight;
//        float percent = Math.abs(child.getY() / mLimitHeight);
        float percent = (float) Math.abs(dy) / (float) appBarLayout.getTotalScrollRange();

        if (percent >= 1) {
            percent = 1f;
        }
//        //设置toolbar的透明度
//        mToolBar.setAlpha(percent);

        //设置名字缩放
//        mName.setScaleX(Math.max(0.5f, 1 - percent));
        mName.setScaleY(Math.max(0.8f, 1 - percent));
        mName.setTextScaleX(Math.max(0.8f, 1 - percent));

        //设置名字平移
//        int offset = mName.getTop() - mToolBar.getTop();
        int i = mToolBar.getBottom() - mToolBar.getTop();
//        int offset = Math.min((mName.getTop() - mToolBar.getTop()), i);
//        int offset = 20;
        int offset = (mName.getTop() + 48) / 2;

        mName.setTranslationY(-offset * percent);
    }

    public interface onProgressChangeListener {
        /**
         * 范围 0~1
         *
         * @param progress
         * @param isRelease 是否是释放状态
         */
        void onProgressChange(float progress, boolean isRelease);
    }

    public void setOnProgressChangeListener(AppBarLayoutOverScrollViewBehavior.onProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    onProgressChangeListener onProgressChangeListener;

    private void recovery(final AppBarLayout abl) {
        if (isRecovering) return;
        if (mTotalDy > 0) {
            isRecovering = true;
            mTotalDy = 0;
            if (isAnimate) {
                ValueAnimator anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
                anim.addUpdateListener(
                        new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {

                                float value = (float) animation.getAnimatedValue();
                                ViewCompat.setScaleX(mTargetView, value);
                                ViewCompat.setScaleY(mTargetView, value);
                                abl.setBottom((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction()));
                                middleLayout.setTop((int) (mLastBottom -
                                        (mLastBottom - mParentHeight) * animation.getAnimatedFraction() - mMiddleHeight));

                                if (onProgressChangeListener != null) {
                                    float progress = Math.min((value - 1) / MAX_REFRESH_LIMIT, 1);//计算0~1的进度
                                    onProgressChangeListener.onProgressChange(progress, true);
                                }
                            }
                        }
                );
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRecovering = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                anim.start();
            } else {
                ViewCompat.setScaleX(mTargetView, 1f);
                ViewCompat.setScaleY(mTargetView, 1f);
                abl.setBottom(mParentHeight);
                middleLayout.setTop(mParentHeight - mMiddleHeight);
//                middleLayout.setBottom(mParentHeight);
                isRecovering = false;

                if (onProgressChangeListener != null)
                    onProgressChangeListener.onProgressChange(0, true);
            }
        }
    }


}