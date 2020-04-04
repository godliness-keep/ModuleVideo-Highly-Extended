package com.godliness.android.base.controller;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.godliness.android.base.controller.utils.NetUtils;

/**
 * Created by godliness on 2020-03-26.
 *
 * @author godliness
 */
public abstract class BaseBar {

    private ViewGroup mParent;
    private View mBar;

    private Animation mShowAnimation;
    private Animation mHideAnimation;

    public BaseBar() {
    }

    /**
     * Return layout id of current bar
     *
     * @return layout id
     */
    protected abstract int getBarLayoutId();

    /**
     * Initializes current bar resource
     */
    protected abstract void initBar();

    /**
     * Register event
     *
     * @param event state
     */
    public abstract void regEvent(boolean event);

    /**
     * Show bar from controller
     */
    public abstract void show(boolean openAnim);

    /**
     * Hide bar form controller
     */
    public abstract void hide(boolean openAnim);

    /**
     * @see LayoutInflater#inflate(int, ViewGroup, boolean)
     */
    protected boolean attachToRoot() {
        return false;
    }

    /**
     * Destroy lifecycle of bar
     */
    protected void release() {
        regEvent(false);
    }

    /**
     * Create show animation when call {@link #getShowAnimation()},Only once
     */
    protected Animation createShowAnimation() {
        final Animation showAnimation = new AlphaAnimation(0, 1);
        showAnimation.setDuration(300);
        return showAnimation;
    }

    /**
     * Return show animation listener when call {@link #getShowAnimation()}
     */
    protected Animation.AnimationListener getShowAnimationCallback() {
        return null;
    }

    /**
     * Create hide animation when call {@link #getHideAnimation()},Only once
     */
    protected Animation createHideAnimation() {
        final Animation hideAnimation = new AlphaAnimation(1, 0);
        hideAnimation.setDuration(300);
        return hideAnimation;
    }

    /**
     * Return hide animation listener when call {@link #getHideAnimation()}
     */
    protected Animation.AnimationListener getHideAnimationCallback() {
        return null;
    }

    /**
     * Return show animation, If you need to customize it {@link #createShowAnimation()}
     */
    protected final Animation getShowAnimation() {
        if (mShowAnimation == null) {
            mShowAnimation = createShowAnimation();
            mShowAnimation.setAnimationListener(getShowAnimationCallback());
        }
        return mShowAnimation;
    }

    /**
     * Return hide animation, If you need to customize it {@link #createHideAnimation()}
     */
    protected final Animation getHideAnimation() {
        if (mHideAnimation == null) {
            mHideAnimation = createHideAnimation();
            mHideAnimation.setAnimationListener(getHideAnimationCallback());
        }
        return mHideAnimation;
    }

    protected final boolean hasNetwork() {
        return NetUtils.hasNetwork(getContext());
    }

    protected final Context getContext() {
        return mParent.getContext();
    }

    protected final Resources getResources() {
        return getContext().getResources();
    }

    protected final Configuration getConfiguration() {
        return getResources().getConfiguration();
    }

    protected final int getConfigurationOrientation() {
        return getConfiguration().orientation;
    }

    protected final boolean screenOrientationFromPortrait() {
        return getConfigurationOrientation() == Configuration.ORIENTATION_PORTRAIT;
    }

    protected final View getBarView() {
        return mBar;
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        return mBar.findViewById(id);
    }

    protected final void attachToController() {
        attachToController(mBar);
    }

    protected final void attachToController(View childView) {
        final ViewParent parent = childView.getParent();
        if (parent == null) {
            mParent.addView(childView);
        }
    }

    protected final void detachFromController(View childView) {
        final ViewParent parent = childView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(childView);
        }
    }

    protected final void detachFromController() {
        detachFromController(mBar);
    }

    final void initBarFromController(ViewGroup parent) {
        if (mBar != null) {
            return;
        }
        this.mParent = parent;
        this.mBar = LayoutInflater.from(parent.getContext()).inflate(getBarLayoutId(), parent, attachToRoot());
        initBar();
        regEvent(true);
    }
}
