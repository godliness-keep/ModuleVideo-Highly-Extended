package com.godliness.android.modulevideo.base;

import com.godliness.android.base.controller.BaseStateBar;
import com.godliness.android.modulevideo.config.BaseOptions;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 * 视频状态栏基础类
 */
public abstract class BaseVideoStateBar<Options extends BaseOptions> extends BaseStateBar {

    /**
     * Completion state of current player
     *
     * @param completion tips when completion state
     */
    public abstract void onCompletion(String completion);

    /**
     * Prepared state of current player
     */
    public abstract void onPrepared();

    /**
     * restore to the original state
     */
    public abstract void onRestore();

    /**
     * Show state bar shadow
     */
    public abstract void showShadow();

    /**
     * Hide state bar shadow
     *
     * @param openAnim animation
     */
    public abstract void hideShadow(boolean openAnim);

    /**
     * Configuration from user custom
     *
     * @param options {@link BaseOptions}
     */
    public abstract void updateConfigurationOptions(Options options);

    public interface OnStateBarListener {

        /**
         * Retry when state bar fails
         */
        void onStateBarRetry();
    }
}
