package com.godliness.android.modulevideo.base;

import com.godliness.android.base.controller.BaseStateBar;
import com.godliness.android.modulevideo.config.BaseOptions;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 */
public abstract class BaseVideoStateBar<Options extends BaseOptions> extends BaseStateBar {

    public abstract void onCompletion(String completion);

    public abstract void onPrepared();

    public abstract void showShadow();

    public abstract void hideShadow(boolean openAnim);

    public abstract void updateConfigurationOptions(Options options);

    public interface OnStateBarListener {

        void onStateBarRetry();
    }
}
