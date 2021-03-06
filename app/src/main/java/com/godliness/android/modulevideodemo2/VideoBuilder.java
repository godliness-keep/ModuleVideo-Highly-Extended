package com.godliness.android.modulevideodemo2;

import com.godliness.android.modulevideo.base.Builder;
import com.godliness.android.modulevideodemo2.controller.VideoController;

/**
 * Created by godliness on 2020-04-01.
 *
 * @author godliness
 * <p>
 * 通过{@link Builder} 构建视频控制器
 */
public final class VideoBuilder extends Builder<VideoBuilder, VideoController> {

    public OnVideoControllerListener mVideoControllerCallback;

    public VideoBuilder videoControllerListener(OnVideoControllerListener callback) {
        this.mVideoControllerCallback = callback;
        return this;
    }

    @Override
    protected VideoController createController() {
        return new VideoController(this);
    }
}
