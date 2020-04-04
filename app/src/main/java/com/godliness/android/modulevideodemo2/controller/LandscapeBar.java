package com.godliness.android.modulevideodemo2.controller;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewStub;
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
 */
public final class LandscapeBar extends BaseVideoControllerBar<LandscapeBar.OnControllerBarListener, ConfigOptions> implements View.OnClickListener {

    private ImageView mPlay;
    private TextView mCurrent;
    private TextView mEnd;
    private SeekBar mProgress;

    private View mOptionsView;
    private View mSpeed;
    private View mDefinit;
    private View mCatalog;

    private OnControllerBarListener mCallback;

    @Override
    protected int getBarLayoutId() {
        return R.layout.modulevideo_video_controller_landscape;
    }

    @Override
    protected void initBar() {
        mPlay = findViewById(R.id.modulevideo_controller_bar_iv_play_landscape);
        mCurrent = findViewById(R.id.modulevideo_controller_tv_current_landscape);
        mEnd = findViewById(R.id.modulevideo_controller_tv_end_landscape);
        mProgress = findViewById(R.id.modulevideo_video_controller_progress_landscape);
    }

    @Override
    public void regEvent(boolean event) {
        mPlay.setOnClickListener(event ? this : null);
    }

    @Override
    public void updatePlayWidgetState(boolean isPlaying) {
        mPlay.setImageResource(isPlaying ? R.drawable.modulevideo_ic_media_pause : R.drawable.modulevideo_ic_media_play);
    }

    @Override
    public void updateCurrentDragProgress(String position) {
        mCurrent.setText(position);
    }

    @Override
    public void onProgressChanged(int position, int percent, String current, String end) {
        mCurrent.setText(String.format("%s/", current));
        mEnd.setText(end);
        mProgress.setProgress(position);
        mProgress.setSecondaryProgress(percent);
    }

    @Override
    public void bindSeekBarChangeListener(SeekBar.OnSeekBarChangeListener callback) {
        mProgress.setOnSeekBarChangeListener(callback);
    }

    @Override
    public void setVideoControllerBarListener(OnControllerBarListener callback) {
        this.mCallback = callback;
    }

    @Override
    public int getScreenOrientation() {
        return Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void updateConfigurationOptions(ConfigOptions options) {
        final boolean show = options.mShowSpeed || options.mShowDefinit || options.mShowCatalog;
        if (show && mOptionsView == null) {
            initOptionsView();
        }
        mSpeed.setVisibility(options.mShowSpeed ? View.VISIBLE : View.GONE);
        mDefinit.setVisibility(options.mShowDefinit ? View.VISIBLE : View.GONE);
        mCatalog.setVisibility(options.mShowCatalog ? View.VISIBLE : View.GONE);
        mProgress.setEnabled(options.mDragProgress);
    }

    @Override
    public void onClick(View v) {
        if (mCallback == null) {
            return;
        }
        final int id = v.getId();
        if (id == R.id.modulevideo_controller_bar_iv_play_landscape) {
            mCallback.onControllerBarPlay();
        } else if (id == R.id.modulevideo_controller_tv_speed) {
            mCallback.onControllerBarSpeed();
        } else if (id == R.id.modulevideo_controller_tv_definition) {
            mCallback.onControllerBarDefinition();
        } else if (id == R.id.modulevideo_controller_tv_catalog) {
            mCallback.onControllerBarCatalog();
        }
    }

    public interface OnControllerBarListener {

        /**
         * 播放or暂停
         */
        void onControllerBarPlay();

        /**
         * 方向切换
         */
        void onControllerBarDirection();

        /**
         * 倍速
         */
        void onControllerBarSpeed();

        /**
         * 清晰度
         */
        void onControllerBarDefinition();

        /**
         * 目录
         */
        void onControllerBarCatalog();
    }

    private void initOptionsView() {
        final ViewStub optionStub = findViewById(R.id.modulevideo_controller_vs_options_landscape);
        mOptionsView = optionStub.inflate();
        mSpeed = mOptionsView.findViewById(R.id.modulevideo_controller_tv_speed);
        mSpeed.setOnClickListener(this);
        mDefinit = mOptionsView.findViewById(R.id.modulevideo_controller_tv_definition);
        mDefinit.setOnClickListener(this);
        mCatalog = mOptionsView.findViewById(R.id.modulevideo_controller_tv_catalog);
        mCatalog.setOnClickListener(this);
    }
}
