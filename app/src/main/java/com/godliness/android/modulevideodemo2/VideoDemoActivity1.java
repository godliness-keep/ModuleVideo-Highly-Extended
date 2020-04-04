package com.godliness.android.modulevideodemo2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.godliness.android.modulevideo.OnVideoStateListener;
import com.godliness.android.modulevideo.ijk.IjkVideoView;
import com.godliness.android.modulevideodemo2.controller.VideoController;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by godliness on 2020-04-01.
 *
 * @author godliness
 * <p>
 * 2、StateBar
 */
public final class VideoDemoActivity1 extends AppCompatActivity implements OnVideoControllerListener, OnVideoStateListener<ConfigOptions> {

    private final String mVideo = "http://download.yxybb.com/bbvideo/2017/9/hlwbxxqyfxq1_3.mp4";
    private VideoController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);
        final IjkVideoView videoView = findViewById(R.id.videoview);

        mController = new VideoBuilder().host(videoView).videoStateListener(this).videoControllerListener(this).build();

        mController.setVideoPath(mVideo, 0);
        mController.setVideoTitle("这里设置视频标题");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mController.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mController != null) {
            if (mController.onKeyDown(keyCode)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public void onSwitchOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

    @Override
    public void onCatalog() {
        Toast.makeText(this, "onCatalog", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwitchSpeed() {
        Toast.makeText(this, "onSwitchSpeed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwitchDefinition() {
        Toast.makeText(this, "onSwitchDefinition", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRetry() {
        mController.setVideoPath(mVideo, 0);
        Toast.makeText(this, "onRetry", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String onCompleted() {
        return "播放完成了";
    }

    @Override
    public boolean onError(IMediaPlayer mediaPlayer) {
        return false;
    }

    @Override
    public boolean onPrepared(IMediaPlayer mediaPlayer) {
        return true;
    }

    @Override
    public void onMore() {
        Toast.makeText(this, "onMore", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationOptions(ConfigOptions options) {
        options.mDirectionSwitch = true;
        options.mDragProgress = true;
        options.mShowCatalog = true;
        options.mShowDefinit = true;
        options.mShowMore = true;
        options.mShowSpeed = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.release();
    }
}
