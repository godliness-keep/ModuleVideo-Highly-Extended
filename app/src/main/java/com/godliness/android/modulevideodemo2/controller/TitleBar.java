package com.godliness.android.modulevideodemo2.controller;


import android.view.View;
import android.widget.TextView;

import com.godliness.android.modulevideo.base.BaseVideoTitleBar;
import com.godliness.android.modulevideodemo2.ConfigOptions;
import com.godliness.android.modulevideodemo2.R;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 * 标题栏
 */
public final class TitleBar extends BaseVideoTitleBar<ConfigOptions> implements View.OnClickListener {

    private View mBack;
    private TextView mTitle;
    private View mMore;

    private OnTitleBarListener mCallback;

    /**
     * Return layout id of current bar
     *
     * @return layout id
     */
    @Override
    protected int getBarLayoutId() {
        return R.layout.modulevideo_controller_titlebar;
    }

    /**
     * Initializes current bar resource
     */
    @Override
    protected void initBar() {
        mBack = findViewById(R.id.modulevideo_titlebar_iv_back);
        mTitle = findViewById(R.id.modulevideo_titlebar_tv_title);
        mMore = findViewById(R.id.modulevideo_titlebar_iv_more);
    }

    /**
     * Register event
     *
     * @param event state
     */
    @Override
    public void regEvent(boolean event) {
        mBack.setOnClickListener(event ? this : null);
        mMore.setOnClickListener(event ? this : null);

        // Intercept events
        getBarView().setOnClickListener(event ? this : null);
    }

    @Override
    public void setVideoTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void updateConfigurationOptions(ConfigOptions options) {
        mMore.setVisibility(options.mShowMore ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.modulevideo_titlebar_iv_back) {
            if (mCallback != null) {
                mCallback.onTitleBarBack();
            }
        } else if (id == R.id.modulevideo_titlebar_iv_more) {
            if (mCallback != null) {
                mCallback.onTitleBarMore();
            }
        }
    }

    void setTitlebarListener(OnTitleBarListener callback) {
        this.mCallback = callback;
    }

    public interface OnTitleBarListener {

        /**
         * On back event of {@link TitleBar}
         */
        void onTitleBarBack();

        /**
         * More content event of {@link TitleBar}
         */
        void onTitleBarMore();
    }

}
