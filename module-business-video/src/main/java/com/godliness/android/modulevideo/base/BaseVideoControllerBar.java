package com.godliness.android.modulevideo.base;

import android.widget.SeekBar;

import com.godliness.android.base.controller.BaseControllerBar;
import com.godliness.android.modulevideo.config.BaseOptions;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 * 视频控制栏基础类
 */
public abstract class BaseVideoControllerBar<Listener, Options extends BaseOptions> extends BaseControllerBar {

    public final boolean isSameOrientation(int currentOrientation) {
        return currentOrientation == getScreenOrientation();
    }

    /**
     * Updates the state of the playback control
     *
     * @param isPlaying player state
     */
    public abstract void updatePlayWidgetState(boolean isPlaying);

    /**
     * Update the playback drag progress
     *
     * @param position text progress
     */
    public abstract void updateCurrentDragProgress(String position);

    /**
     * Update playback schedule
     *
     * @param position current play position
     * @param percent  percent
     * @param current  The position to the text
     * @param end      duration
     */
    public abstract void onProgressChanged(int position, int percent, String current, String end);

    /**
     * Bind {@link SeekBar} listener
     *
     * @param callback {@link SeekBar.OnSeekBarChangeListener}
     */
    public abstract void bindSeekBarChangeListener(SeekBar.OnSeekBarChangeListener callback);

    /**
     * Set controller bar listener
     *
     * @param callback The custom
     */
    public abstract void setVideoControllerBarListener(Listener callback);

    /**
     * Return orientation of current controller bar
     *
     * @return screen orientation
     */
    public abstract int getScreenOrientation();

    /**
     * Update configuration options
     *
     * @param options {@link BaseOptions}
     */
    public abstract void updateConfigurationOptions(Options options);

}
