package com.godliness.android.modulevideo.base;

import com.godliness.android.base.controller.BaseControllerBar;
import com.godliness.android.modulevideo.config.BaseOptions;

/**
 * Created by godliness on 2020-04-02.
 *
 * @author godliness
 * 视频控制器标题栏基础类
 */
public abstract class BaseVideoTitleBar<Options extends BaseOptions> extends BaseControllerBar {

    /**
     * Configuration from user custom
     *
     * @param options {@link BaseOptions}
     */
    public abstract void updateConfigurationOptions(Options options);

    /**
     * title of current resource
     *
     * @param title resource title
     */
    public abstract void setVideoTitle(String title);
}
