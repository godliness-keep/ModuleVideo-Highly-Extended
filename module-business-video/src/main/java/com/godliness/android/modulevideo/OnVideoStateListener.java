package com.godliness.android.modulevideo;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by godliness on 2020-04-04.
 *
 * @author godliness
 */
public interface OnVideoStateListener<Options> {

    String onCompleted();

    boolean onError(IMediaPlayer mediaPlayer);

    boolean onPrepared(IMediaPlayer mediaPlayer);

    void onBack();

    void onSwitchOrientation(int orientation);

    void onConfigurationOptions(Options options);
}
