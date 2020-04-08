package com.godliness.android.modulevideodemo2.controller;

import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.godliness.android.modulevideo.base.BaseVideoStateBar;
import com.godliness.android.modulevideodemo2.ConfigOptions;
import com.godliness.android.modulevideodemo2.R;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 * <p>
 * 状态栏
 */
public final class StateBar extends BaseVideoStateBar<ConfigOptions> {

    private View mErrorView;
    private TextView mInfo;
    private View mRetry;

    private View mBack;
    private View mMore;
    private View mLoadingView;
    private View mShadow;
    private View mSwitcher;

    private OnVideoStateBarListener mStateBarCallback;
    private ConfigOptions mOptions;

    @Override
    public int getBarLayoutId() {
        return R.layout.moduledemo_controller_statebar;
    }

    @Override
    public void initBar() {
        mBack = findViewById(R.id.modulevideo_shadow_iv_back);
        mMore = findViewById(R.id.modulevideo_shadow_iv_more);
        mLoadingView = findViewById(R.id.modulevideo_shadow_ll_loading);
        mShadow = findViewById(R.id.modulevideo_video_controller_shadow);
        mSwitcher = findViewById(R.id.modulevideo_shadow_iv_orientation);
    }

    @Override
    public void regEvent(boolean event) {
        mBack.setOnClickListener(event ? this : null);
        mSwitcher.setOnClickListener(event ? this : null);
        mMore.setOnClickListener(event ? this : null);
    }

    /**
     * Configuration options of current controller bar
     *
     * @param options {@link Configuration}
     */
    @Override
    public void updateConfigurationOptions(ConfigOptions options) {
        this.mOptions = options;
        if (screenOrientationFromPortrait()) {
            mSwitcher.setVisibility(options.mDirectionSwitch ? View.VISIBLE : View.GONE);
        } else {
            mSwitcher.setVisibility(View.GONE);
            mBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void show(boolean openAnim) {
        if (screenOrientationFromPortrait()) {
            mBack.setVisibility(View.VISIBLE);

            if (mOptions.mShowMore) {
                if (openAnim) {
                    mMore.startAnimation(getShowAnimation());
                }
                mMore.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void hide(boolean openAnim) {
        final boolean portrait = screenOrientationFromPortrait();
        final boolean showMore = mOptions.mShowMore;
        if (portrait) {
            if (showMore) {
                if (openAnim) {
                    mMore.startAnimation(getHideAnimation());
                }
                mMore.setVisibility(View.GONE);
            }
        } else {
            if (showMore) {
                mMore.setVisibility(View.GONE);
            }
            mBack.setVisibility(View.GONE);
        }
    }

    void setStateBarListener(OnVideoStateBarListener callback) {
        this.mStateBarCallback = callback;
    }

    @Override
    public void showShadow() {
        if (mShadow != null) {
            mShadow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideShadow(boolean openAnim) {
        hideErrorBar();
        showOrHideShadowBack(screenOrientationFromPortrait());
        showOrHideShadowOrientation(false);
        if (openAnim) {
            mShadow.startAnimation(getHideAnimation());
        }
        mShadow.setVisibility(View.GONE);
    }

    @Override
    public void onError(int extra) {
        showShadow();
        onLoading(false);
        final String info = extra == Status.STATUS_NETWORK_ERROR ? getResources().getString(R.string.modulevideo_string_network_disconnected) : getResources().getString(R.string.modulevideo_string_play_error) + extra;
        showErrorBar(true, info);
    }

    @Override
    public void onLoading(boolean loading) {
        hideErrorBar();
        mLoadingView.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRestore() {
        onLoading(true);
        hideErrorBar();
        showOrHideShadowBack(true);
        showOrHideShadowOrientation(screenOrientationFromPortrait() && mOptions.mDirectionSwitch);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        final boolean portrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
        if(!getParent().isEnabled()){
            showOrHideShadowBack(true);
            showOrHideShadowOrientation(mOptions.mDirectionSwitch && portrait);
        }else{
            showOrHideShadowBack(portrait);
        }
    }

    @Override
    public void onClick(View v) {
        if (mStateBarCallback == null) {
            return;
        }
        final int id = v.getId();
        if (id == R.id.modulevideo_loading_error_btn) {
            if (hasNetwork()) {
                hideErrorBar();
                mStateBarCallback.onStateBarRetry();
            }
        } else if (id == R.id.modulevideo_shadow_iv_more) {
            mStateBarCallback.onStateBarMore();
        } else if (id == R.id.modulevideo_shadow_iv_back) {
            mStateBarCallback.onStateBarBack();
        } else if (id == R.id.modulevideo_shadow_iv_orientation) {
            mStateBarCallback.onStateBarSwitch();
        }
    }

    @Override
    public void onCompletion(String completion) {
        onLoading(false);
        showShadow();
        showErrorBar(false, completion);
    }

    @Override
    public void onPrepared() {
        onLoading(false);
        hideErrorBar();
        hideShadow(true);
    }

    public interface OnVideoStateBarListener extends OnStateBarListener {

        /**
         * back
         */
        void onStateBarBack();

        /**
         * 更多内容
         */
        void onStateBarMore();

        /**
         * 方向切换
         */
        void onStateBarSwitch();
    }

    private void showOrHideShadowBack(boolean show) {
        if (mBack != null) {
            mBack.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showOrHideShadowOrientation(boolean show) {
        if (mSwitcher != null) {
            mSwitcher.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showErrorBar(boolean retry, String errorText) {
        initErrorView();
        mRetry.setVisibility(retry ? View.VISIBLE : View.GONE);
        mInfo.setVisibility(TextUtils.isEmpty(errorText) ? View.GONE : View.VISIBLE);
        mInfo.setText(errorText);
    }

    private void initErrorView() {
        if (mErrorView == null) {
            final ViewStub errorStub = findViewById(R.id.modulevideo_vs_error);
            mErrorView = errorStub.inflate();
            mInfo = mErrorView.findViewById(R.id.modulevideo_loading_error_info);
            mRetry = mErrorView.findViewById(R.id.modulevideo_loading_error_btn);
            mRetry.setOnClickListener(this);
        } else {
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    private void hideErrorBar() {
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }
}
