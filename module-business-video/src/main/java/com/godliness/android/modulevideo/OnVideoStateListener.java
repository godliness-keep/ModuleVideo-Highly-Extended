package com.godliness.android.modulevideo;

import com.godliness.android.modulevideo.config.BaseOptions;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by godliness on 2020-04-04.
 *
 * @author godliness
 */
public interface OnVideoStateListener<Options extends BaseOptions> {

    String onCompleted();

    boolean onError(IMediaPlayer mediaPlayer);

    boolean onPrepared(IMediaPlayer mediaPlayer);

    void onBack();

    void onSwitchDirection(int orientation);

    void onConfigurationOptions(Options options);
}
