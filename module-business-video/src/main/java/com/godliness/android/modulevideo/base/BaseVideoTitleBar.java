package com.godliness.android.modulevideo.base;

import com.godliness.android.base.controller.BaseControllerBar;
import com.godliness.android.modulevideo.config.BaseOptions;

/**
 * Created by godliness on 2020-04-02.
 *
 * @author godliness
 */
public abstract class BaseVideoTitleBar<Options extends BaseOptions> extends BaseControllerBar {

    public abstract void updateConfigurationOptions(Options options);

    public abstract void setVideoTitle(String title);
}
