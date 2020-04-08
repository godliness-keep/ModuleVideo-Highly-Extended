package com.godliness.android.modulevideo.base;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.godliness.android.base.controller.BaseStateBar;
import com.godliness.android.base.controller.BaseTemplateController;
import com.godliness.android.modulevideo.OnVideoStateListener;
import com.godliness.android.modulevideo.config.BaseOptions;
import com.godliness.android.modulevideo.ijk.IjkVideoView;

import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 * 视频控制器基础组件
 * Option optimize of IjkPlayer from issues:https://github.com/bilibili/ijkplayer/issues/4069
 * Option optimize of IjkPlayer from jianshu:https://www.jianshu.com/p/843c86a9e9ad
 * Cache optimize of IjkPlayer from jianshu:https://www.jianshu.com/p/9e7ebabf3a3f
 * VideoController:https://github.com/godliness-keep/ModuleVideo-Highly-Extended
 */
@SuppressWarnings({"WeakerAccess", "unchecked", "unused"})
public abstract class BaseVideoController<TitleBar extends BaseVideoTitleBar, BottomBar extends BaseVideoControllerBar, StateBar extends BaseVideoStateBar>
        extends BaseTemplateController<TitleBar, BottomBar, StateBar>
        implements IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnPreparedListener, Handler.Callback, View.OnTouchListener {

    private final int DEFAULT_HIDE_CONTROLLER_TIMEOUT = 3000;
    private final int UPDATE_PROGRESS_MSG = 0;
    private final int HIDE_MSG = 1;
    private final int ENABLE_ON_TOUCH = 2;
    private final int SWITCH_SCREEN_ORIENTATION = 3;

    private IjkVideoView mHost;

    private final StringBuilder mFormatBuilder;
    private final Formatter mFormatter;

    private final Handler mControllerHandler;
    private GestureDetector mGestureDetector;
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeCallback;

    private boolean mShowing;
    private boolean mCompleted;
    private int mCurrentPosition;
    private String mTitle;

    @NonNull
    private final OnVideoStateListener mVideoStateCallback;
    private final Switcher mSwitcher;
    private BaseOptions mOptions;

    protected BaseVideoController(Builder builder) {
        super(builder.mHost);
        this.mVideoStateCallback = builder.mVideoStateCallback;
        this.mSwitcher = builder.mSwitcher;

        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        this.mControllerHandler = new Handler(this);

        updateConfigurationOptionsOfStateBar();
    }

    /**
     * Return options of current controller
     *
     * @return {@link BaseOptions}
     */
    protected abstract <Options extends BaseOptions> Options createOptions();

    public final void notifyControllerToRestoreInitialState() {
        getControllerView().setEnabled(false);
        pause();
        hide(false);
        final StateBar stateBar = getStateBar();
        if (stateBar != null) {
            stateBar.onRestore();
        }
    }

    @Override
    public final void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mSwitcher != null) {
            mSwitcher.onConfigurationChanged(newConfig);
        }
        if (mShowing) {
            hide(false);
            show();
        }
    }

    public final boolean onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (screenOrientationFromPortrait()) {
                return false;
            }
            notifyScreenToSwitchOrientation(true);
            return true;
        }
        return false;
    }

    public void show(int timeOut) {
        if (!mShowing) {
            super.show();
            mShowing = true;

            mControllerHandler.sendEmptyMessageDelayed(ENABLE_ON_TOUCH, 300);
        }

        mControllerHandler.sendEmptyMessage(UPDATE_PROGRESS_MSG);

        final Message msg = mControllerHandler.obtainMessage(HIDE_MSG);
        if (timeOut != 0) {
            mControllerHandler.removeMessages(HIDE_MSG);
            mControllerHandler.sendMessageDelayed(msg, timeOut);
        }

        final BottomBar bottomBar = getBottomBar();
        if (bottomBar != null) {
            bottomBar.updatePlayWidgetState(isPlaying());
        }
    }

    @Override
    public void show() {
        show(DEFAULT_HIDE_CONTROLLER_TIMEOUT);
    }

    @Override
    public void hide(boolean openAnim) {
        if (mShowing) {
            super.hide(openAnim);
            mShowing = false;

            mControllerHandler.sendEmptyMessageDelayed(ENABLE_ON_TOUCH, 300);
        }

        mControllerHandler.removeMessages(HIDE_MSG);
        mControllerHandler.removeMessages(UPDATE_PROGRESS_MSG);
    }

    @Override
    public void release() {
        super.release();
        mControllerHandler.removeCallbacksAndMessages(null);
        final IjkVideoView player = getHostView();
        player.stopPlayback();
        player.release(true);
        player.releaseWithoutStop();
    }

    public final void setVideoPath(String path, int position) {
        this.mCurrentPosition = position;
        getHostView().setVideoPath(path);
    }

    public final void setVideoTitle(String title) {
        final TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setVideoTitle(title);
        } else {
            this.mTitle = title;
        }
    }

    protected final <Options extends BaseOptions> Options getOptions() {
        if (mOptions == null) {
            mOptions = createOptions();
            updateConfigurationOptions(mOptions);
        }
        return (Options) mOptions;
    }

    @Override
    protected final void initTemplate() {
        getControllerView().setEnabled(false);
    }

    @Override
    protected final void regEvent(boolean isClick) {
        final IjkVideoView player = getHost();
        player.setOnPreparedListener(isClick ? this : null);
        player.setOnInfoListener(isClick ? this : null);
        player.setOnErrorListener(isClick ? this : null);
        player.setOnCompletionListener(isClick ? this : null);
        getControllerView().setOnTouchListener(isClick ? this : null);
    }

    protected final void notifyScreenToSwitchOrientation(boolean pressBack) {
        final Message orientation = mControllerHandler.obtainMessage(SWITCH_SCREEN_ORIENTATION);
        orientation.obj = pressBack;
        orientation.sendToTarget();
    }

    @Override
    public final boolean onTouch(View v, MotionEvent event) {
        initHandGesture().onTouchEvent(event);
        return true;
    }

    public final void seekTo(int currentPosition) {
        getHostView().seekTo(currentPosition);
    }

    public final int getDuration() {
        return getHostView().getDuration();
    }

    public final int getCurrentPosition() {
        return getHostView().getCurrentPosition();
    }

    public final int getBufferPercentage() {
        return getHostView().getBufferPercentage();
    }

    public final boolean isPlaying() {
        return getHostView().isPlaying();
    }

    public final void start() {
        getHostView().start();
    }

    public final void pause() {
        getHostView().pause();
    }

    @Override
    protected final void initTitleBar() {
        super.initTitleBar();

        final TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.updateConfigurationOptions(getOptions());
            titleBar.setVideoTitle(mTitle);
        }
    }

    @Override
    protected final void initBottomBar() {
        super.initBottomBar();

        final BottomBar bottomBar = getBottomBar();
        if (bottomBar != null) {
            bottomBar.bindSeekBarChangeListener(getSeekBarChangeCallback());
            bottomBar.updateConfigurationOptions(getOptions());
        }
    }

    @Override
    public final boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HIDE_MSG:
                hide();
                return true;

            case UPDATE_PROGRESS_MSG:
                final int pos = setProgress();
                if (mShowing && isPlaying()) {
                    msg = mControllerHandler.obtainMessage(UPDATE_PROGRESS_MSG);
                    mControllerHandler.sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
                return true;

            case SWITCH_SCREEN_ORIENTATION:
                switchScreenOrientationFromUser((Boolean) msg.obj);
                return true;

            default:
                return false;
        }
    }

    @Override
    public final void onCompletion(IMediaPlayer iMediaPlayer) {
        final StateBar stateBar = getStateBar();
        if (stateBar != null) {
            stateBar.onCompletion(hasCompletion());
        }
        this.mCompleted = true;
    }

    @Override
    public final boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        getControllerView().setEnabled(false);
        hide(false);
        if (!hasError(iMediaPlayer)) {
            final StateBar stateBar = getStateBar();
            if (stateBar != null) {
                stateBar.onError(!hasNetwork() ? BaseStateBar.Status.STATUS_NETWORK_ERROR : extra);
            }
        }
        return true;
    }

    @Override
    public final boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        final StateBar stateBar = getStateBar();
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (stateBar != null) {
                    stateBar.onLoading(true);
                }
                return true;

            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (stateBar != null) {
                    stateBar.onLoading(false);
                }
                return true;

            default:
                return false;
        }
    }

    @Override
    public final void onPrepared(IMediaPlayer mediaPlayer) {
        setMediaOptions(mediaPlayer);
        seekTo(mCurrentPosition);
        if (hasPrepared(mediaPlayer)) {
            start();
        }
        mControllerHandler.removeCallbacks(mInitRunnable);
        mControllerHandler.postDelayed(mInitRunnable, 800);

        this.mCompleted = false;
    }

    private void setMediaOptions(IMediaPlayer iMediaPlayer) {
        IjkMediaPlayer mediaPlayer = null;
        if (iMediaPlayer instanceof IjkMediaPlayer) {
            mediaPlayer = (IjkMediaPlayer) iMediaPlayer;
        }
        if (mediaPlayer != null) {
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024 * 10);
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1);
        }
    }

    protected final void doPlayState() {
        if (mShowing) {
            show();
        }
        final BottomBar bottomBar = getBottomBar();
        final boolean isPlaying = isPlaying();
        if (bottomBar != null) {
            bottomBar.updatePlayWidgetState(!isPlaying);
        }

        if (isPlaying) {
            pause();
        } else {
            start();
        }

        final StateBar stateBar = getStateBar();
        if (stateBar != null) {
            stateBar.hideShadow(false);
        }
        mCompleted = false;
    }

    private int setProgress() {
        int duration = getDuration();
        int position = getCurrentPosition();

        if (mCompleted) {
            if (position < duration) {
                position = duration;
            }
        }

        final String current = stringForTime(position);
        final String end = stringForTime(duration);

        if (duration > 0) {
            final long pos = 1000L * position / duration;
            final BottomBar bottomBar = getBottomBar();
            if (bottomBar != null) {
                bottomBar.onProgressChanged((int) pos, getBufferPercentage(), current, end);
            }
        }
        return position;
    }

    private IjkVideoView getHostView() {
        if (mHost == null) {
            mHost = getHost();
        }
        return mHost;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private GestureDetector initHandGesture() {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(getContext(), new VideoControllerOnGesture());
        }
        return mGestureDetector;
    }

    private final class VideoControllerOnGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mControllerHandler.hasMessages(ENABLE_ON_TOUCH)) {
                return false;
            }
            if (mShowing) {
                hide();
            } else {
                show();
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            doPlayState();
            return false;
        }
    }

    private final Runnable mInitRunnable = new Runnable() {
        @Override
        public void run() {
            //delay 1s 用于屏蔽画面过渡
            final StateBar stateBar = getStateBar();
            if (stateBar != null) {
                stateBar.onPrepared();
            }
            getControllerView().setEnabled(true);
            show();
        }
    };

    private void switchScreenOrientationFromUser(boolean pressBack) {
        if (screenOrientationFromPortrait()) {
            if (pressBack) {
                mVideoStateCallback.onBack();
            } else {
                if (getControllerView().isEnabled()) {
                    mShowing = true;
                }
                mVideoStateCallback.onSwitchDirection(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            if (getOptions().mDirectionSwitch) {
                if (getControllerView().isEnabled()) {
                    mShowing = true;
                }
                mVideoStateCallback.onSwitchDirection(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                mVideoStateCallback.onBack();
            }
        }
    }

    private void updateConfigurationOptionsOfStateBar() {
        final StateBar stateBar = getStateBar();
        if (stateBar != null) {
            stateBar.updateConfigurationOptions(getOptions());
        }
    }

    private boolean hasPrepared(IMediaPlayer mediaPlayer) {
        return mVideoStateCallback.onPrepared(mediaPlayer);
    }

    private String hasCompletion() {
        return mVideoStateCallback.onCompleted();
    }

    private boolean hasError(IMediaPlayer mediaPlayer) {
        return mVideoStateCallback.onError(mediaPlayer);
    }

    private void updateConfigurationOptions(BaseOptions options) {
        mVideoStateCallback.onConfigurationOptions(options);
    }

    private SeekBar.OnSeekBarChangeListener getSeekBarChangeCallback() {
        if (mSeekBarChangeCallback == null) {
            mSeekBarChangeCallback = new SeekBar.OnSeekBarChangeListener() {

                private long mChangePosition;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) {
                        return;
                    }

                    long duration = getDuration();
                    mChangePosition = (duration * progress) / 1000L;

                    final BottomBar bottomBar = getBottomBar();
                    if (bottomBar != null) {
                        bottomBar.updateCurrentDragProgress(stringForTime((int) mChangePosition));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    final BaseOptions options = getOptions();
                    if (!options.mDragProgress) {
                        final int currentPosition = getCurrentPosition();
                        if (currentPosition > options.mMaxDragPosition) {
                            options.mMaxDragPosition = currentPosition;
                        }
                    }
                    show(3600000);
                    mControllerHandler.removeMessages(UPDATE_PROGRESS_MSG);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    final BaseOptions options = getOptions();
                    if (options.mDragProgress) {
                        seekTo((int) mChangePosition);
                    } else if (options.mMaxDragPosition > 0) {
                        if (mChangePosition <= options.mMaxDragPosition) {
                            seekTo((int) mChangePosition);
                        } else {
                            seekTo(options.mMaxDragPosition);
                        }
                    }

                    setProgress();
                    final BottomBar bottomBar = getBottomBar();
                    if (bottomBar != null) {
                        bottomBar.updatePlayWidgetState(isPlaying());
                    }

                    if (mCompleted) {
                        final StateBar stateBar = getStateBar();
                        if (stateBar != null) {
                            stateBar.hideShadow(false);
                        }
                        mCompleted = false;
                    }

                    show(DEFAULT_HIDE_CONTROLLER_TIMEOUT);
                }
            };
        }
        return mSeekBarChangeCallback;
    }
}
