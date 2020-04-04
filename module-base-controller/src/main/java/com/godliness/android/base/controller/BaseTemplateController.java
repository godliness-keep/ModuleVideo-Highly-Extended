package com.godliness.android.base.controller;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

/**
 * Created by godliness on 2020-03-30.
 *
 * @author godliness
 */
public abstract class BaseTemplateController<TitleBar extends BaseControllerBar, BottomBar extends BaseControllerBar, StateBar extends BaseStateBar> extends BaseController {

    private TitleBar mTitleBar;
    private BottomBar mBottomBar;
    private StateBar mStateBar;

    private boolean mShowing;

    protected BaseTemplateController(ViewGroup host) {
        super(host);
    }

    /**
     * Return controller title bar
     *
     * @return {@link BaseControllerBar}
     */
    @Nullable
    protected abstract TitleBar createTitleBar();

    /**
     * Return controller bottom bar
     *
     * @return {@link BaseControllerBar}
     */
    @Nullable
    protected abstract BottomBar createBottomBar();

    /**
     * Return controller state bar
     *
     * @return {@link BaseStateBar}
     */
    @Nullable
    protected abstract StateBar createStateBar();

    /**
     * Initialization template
     */
    protected abstract void initTemplate();

    /**
     * Return controller layout id
     *
     * @return Layout resource id
     */
    @Override
    protected int getControllerLayoutId() {
        return R.layout.modulecontroller_base_layout;
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    protected boolean switchTitleBar() {
        return false;
    }

    protected boolean switchBottomBar() {
        return false;
    }

    @Override
    public void show(boolean openAnim) {
        initTitleBar();
        initBottomBar();
        showBar(openAnim);
        this.mShowing = true;
    }

    @Override
    public void hide(boolean openAnim) {
        hideBar(openAnim);
        this.mShowing = false;
    }

    @Override
    public void release() {
        super.release();
        if (mTitleBar != null) {
            mTitleBar.release();
        }
        if (mBottomBar != null) {
            mBottomBar.release();
        }
        if (mStateBar != null) {
            mStateBar.release();
        }
    }

    protected void initTitleBar() {
        if (mTitleBar == null || switchTitleBar()) {
            mTitleBar = createTitleBar();
        }
        if (mTitleBar != null) {
            mTitleBar.initBarFromController(getControllerView());
        }
    }

    protected void initBottomBar() {
        if (mBottomBar == null || switchBottomBar()) {
            mBottomBar = createBottomBar();
        }
        if (mBottomBar != null) {
            mBottomBar.initBarFromController(getControllerView());
        }
    }

    @Nullable
    protected final TitleBar getTitleBar() {
        return mTitleBar;
    }

    @Nullable
    protected final BottomBar getBottomBar() {
        return mBottomBar;
    }

    @Nullable
    protected final StateBar getStateBar() {
        return mStateBar;
    }

    @Override
    public final boolean isShowing() {
        return mShowing;
    }

    @Override
    protected final void initController() {
        initStateBar();
        initTemplate();
    }

    private void showBar(boolean openAnim) {
        if (mTitleBar != null) {
            mTitleBar.show(openAnim);
        }
        if (mBottomBar != null) {
            mBottomBar.show(openAnim);
        }
        if (mStateBar != null) {
            mStateBar.show(openAnim);
        }
    }

    private void hideBar(boolean openAnim) {
        if (mTitleBar != null) {
            mTitleBar.hide(openAnim);
        }
        if (mBottomBar != null) {
            mBottomBar.hide(openAnim);
        }
        if (mStateBar != null) {
            mStateBar.hide(openAnim);
        }
    }

    private void initStateBar() {
        if (mStateBar == null) {
            mStateBar = createStateBar();
        }
        if (mStateBar != null) {
            mStateBar.initBarFromController(getControllerView());
        }
    }
}
