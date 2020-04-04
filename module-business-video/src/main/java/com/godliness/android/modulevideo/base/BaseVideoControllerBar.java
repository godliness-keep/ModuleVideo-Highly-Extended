package com.godliness.android.modulevideo.base;

import android.widget.SeekBar;

import com.godliness.android.base.controller.BaseControllerBar;
import com.godliness.android.modulevideo.config.BaseOptions;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 */
public abstract class BaseVideoControllerBar<Listener, Options extends BaseOptions> extends BaseControllerBar {

    public boolean isSameOrientation(int currentOrientation) {
        return currentOrientation == getScreenOrientation();
    }

    public abstract void updatePlayWidgetState(boolean isPlaying);

    public abstract void updateCurrentDragProgress(String position);

    public abstract void onProgressChanged(int position, int percent, String current, String end);

    public abstract void bindSeekBarChangeListener(SeekBar.OnSeekBarChangeListener callback);

    public abstract void setVideoControllerBarListener(Listener callback);

    public abstract int getScreenOrientation();

    public abstract void updateConfigurationOptions(Options options);

}
