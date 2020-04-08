package com.godliness.android.modulevideodemo2.controller;

import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.godliness.android.modulevideo.base.BaseVideoControllerBar;
import com.godliness.android.modulevideodemo2.ConfigOptions;
import com.godliness.android.modulevideodemo2.R;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 * 竖屏方向下视频控制栏
 */
public final class PortraitBar extends BaseVideoControllerBar<LandscapeBar.OnControllerBarListener, ConfigOptions> implements View.OnClickListener {

    private ImageView mPlay;
    private TextView mCurrent;
    private TextView mEnd;
    private SeekBar mProgress;
    private View mSwitcher;

    private LandscapeBar.OnControllerBarListener mCallback;

    @Override
    protected int getBarLayoutId() {
        return R.layout.modulevideo_video_controller_portrait;
    }

    @Override
    protected void initBar() {
        mPlay = findViewById(R.id.modulevideo_controller_bar_iv_play_portrait);
        mCurrent = findViewById(R.id.modulevideo_controller_bar_tv_current_portrait);
        mEnd = findViewById(R.id.modulevideo_controller_bar_tv_end_portrait);
        mProgress = findViewById(R.id.modulevideo_controller_bar_progress_portrait);
        mSwitcher = findViewById(R.id.modulevideo_controller_bar_iv_orientation_portrait);
    }

    @Override
    public void regEvent(boolean event) {
        mPlay.setOnClickListener(event ? this : null);
        mSwitcher.setOnClickListener(event ? this : null);

        // Intercept events
        getBarView().setOnClickListener(event ? this : null);
    }

    @Override
    public void updatePlayWidgetState(boolean isPlaying) {
        mPlay.setImageResource(isPlaying ? R.drawable.modulevideo_selector_iv_pause : R.drawable.modulevideo_selector_iv_play);
    }

    @Override
    public void updateCurrentDragProgress(String position) {
        mCurrent.setText(String.format("%s/", position));
    }

    @Override
    public void onProgressChanged(int position, int percent, String current, String end) {
        mCurrent.setText(current);
        mEnd.setText(end);
        mProgress.setProgress(position);
        mProgress.setSecondaryProgress(percent);
    }

    @Override
    public void bindSeekBarChangeListener(SeekBar.OnSeekBarChangeListener callback) {
        mProgress.setOnSeekBarChangeListener(callback);
    }

    @Override
    public void setVideoControllerBarListener(LandscapeBar.OnControllerBarListener callback) {
        this.mCallback = callback;
    }

    @Override
    public int getScreenOrientation() {
        return Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public void updateConfigurationOptions(ConfigOptions options) {
        mSwitcher.setVisibility(options.mDirectionSwitch ? View.VISIBLE : View.INVISIBLE);
        mProgress.setEnabled(options.mDragProgress);
    }

    @Override
    public void onClick(View v) {
        if (mCallback == null) {
            return;
        }
        final int id = v.getId();
        if (id == R.id.modulevideo_controller_bar_iv_play_portrait) {
            mCallback.onControllerBarPlay();
        } else if (id == R.id.modulevideo_controller_bar_iv_orientation_portrait) {
            mCallback.onControllerBarDirection();
        }
    }
}
