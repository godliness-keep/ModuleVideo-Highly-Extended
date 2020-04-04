package com.godliness.android.base.controller;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.godliness.android.base.controller.utils.NetUtils;

/**
 * Created by godliness on 2020-03-30.
 *
 * @author godliness
 */
public abstract class BaseController {

    private static final String TAG = "BaseController";

    private final Context mCxt;
    private final LayoutInflater mInflater;

    private final ViewGroup mHost;
    private ViewGroup mWormParent;

    private int mCurrentOrientation = -1;

    protected BaseController(ViewGroup host) {
        this.mCxt = host.getContext();
        this.mInflater = LayoutInflater.from(mCxt);
        this.mHost = host;
        initAndAttachController();
    }

    /**
     * Show controller view
     */
    public abstract void show(boolean openAnim);

    /**
     * Hide controller view
     */
    public abstract void hide(boolean openAnim);

    /**
     * Return display state
     *
     * @return isShowing
     */
    public abstract boolean isShowing();

    /**
     * Return controller layout id
     *
     * @return Layout resource id
     */
    protected abstract int getControllerLayoutId();

    /**
     * Initialize the controller view here
     */
    protected abstract void initController();

    /**
     * Register related events
     *
     * @param isClick register/unregister
     */
    protected abstract void regEvent(boolean isClick);

    /**
     * @see LayoutInflater#inflate(int, ViewGroup, boolean)
     */
    protected boolean attachToRoot() {
        return false;
    }

    /**
     * @see android.support.v7.app.AppCompatActivity#onConfigurationChanged(Configuration)
     */
    public void onConfigurationChanged(Configuration newConfig) {
        this.mCurrentOrientation = newConfig.orientation;
    }

    /**
     * Release the controller view
     */
    public void release() {
        if (mWormParent != null) {
            final ViewParent parent = mWormParent.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mWormParent);
            }
        }
        regEvent(false);
    }

    /**
     * @return Return host of current controller
     */
    protected final <T extends ViewGroup> T getHost() {
        return (T) mHost;
    }

    /**
     * @return Return current controller view
     */
    protected final ViewGroup getControllerView() {
        return mWormParent;
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        return mWormParent.findViewById(id);
    }

    protected final LayoutInflater getInflater() {
        return mInflater;
    }

    protected final Context getContext() {
        return mCxt;
    }

    protected final Resources getResources() {
        return mCxt.getResources();
    }

    public final Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    protected final int getColor(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    protected final String getString(@StringRes int strId) {
        return getResources().getString(strId);
    }

    protected final int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    protected final boolean hasNetwork() {
        return NetUtils.hasNetwork(getContext());
    }

    protected final int getConfigurationOrientation() {
        if (mCurrentOrientation != -1) {
            return mCurrentOrientation;
        }
        return getResources().getConfiguration().orientation;
    }

    protected final boolean screenOrientationFromPortrait() {
        return getConfigurationOrientation() == Configuration.ORIENTATION_PORTRAIT;
    }

    private void initAndAttachController() {
        this.mWormParent = (ViewGroup) mInflater.inflate(getControllerLayoutId(), mHost, attachToRoot());
        if (!attachToRoot()) {
            mHost.addView(mWormParent);
        }
        initController();
        Log.e(TAG, "regEvent");
        regEvent(true);
    }
}
