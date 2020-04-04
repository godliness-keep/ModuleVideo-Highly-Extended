package com.godliness.android.base.controller;

import android.view.View;

/**
 * Created by godliness on 2020-03-26.
 *
 * @author godliness
 */
public abstract class BaseStateBar extends BaseBar implements View.OnClickListener {

    /**
     * Return layout id of current bar
     *
     * @return layout id
     */
    @Override
    public abstract int getBarLayoutId();

    /**
     * Initialize current bar
     */
    @Override
    public abstract void initBar();

    /**
     * Callbacks when an load error occurs
     *
     * @param extra extra params
     */
    public abstract void onError(int extra);

    /**
     * Callbacks when switch load state
     *
     * @param loading state
     */
    public abstract void onLoading(boolean loading);

    @Override
    protected final boolean attachToRoot() {
        return true;
    }

    public static final class Status {

        /**
         * Network error
         */
        public static final int STATUS_NETWORK_ERROR = -1;
        /**
         * Unknown state
         */
        public static final int STATUS_UNKNOWN = 0;
    }

}
