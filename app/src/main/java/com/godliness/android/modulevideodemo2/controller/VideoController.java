package com.godliness.android.modulevideodemo2.controller;

import android.support.annotation.Nullable;

import com.godliness.android.modulevideo.base.BaseVideoController;
import com.godliness.android.modulevideo.base.BaseVideoControllerBar;
import com.godliness.android.modulevideo.config.BaseOptions;
import com.godliness.android.modulevideodemo2.ConfigOptions;
import com.godliness.android.modulevideodemo2.OnVideoControllerListener;
import com.godliness.android.modulevideodemo2.VideoBuilder;

/**
 * Created by godliness on 2020-03-31.
 *
 * @author godliness
 */
public final class VideoController extends BaseVideoController<TitleBar, BaseVideoControllerBar, StateBar> {

    private TitleBar mTitleBar;
    private BaseVideoControllerBar mControllerBar;

    private LandscapeBar.OnControllerBarListener mControllerBarCallback;
    private TitleBar.OnTitleBarListener mTitleBarCallback;
    private StateBar.OnVideoStateBarListener mStateBarCallback;

    private final OnVideoControllerListener mCallback;

    public VideoController(VideoBuilder builder) {
        super(builder);
        this.mCallback = builder.mVideoControllerCallback;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <Options extends BaseOptions> Options createOptions() {
        return (Options) new ConfigOptions();
    }

    @Nullable
    @Override
    protected TitleBar createTitleBar() {
        if (!screenOrientationFromPortrait()) {
            if (mTitleBar == null) {
                mTitleBar = new TitleBar();
                mTitleBar.setTitlebarListener(getTitleBarCallback());
            }
            return mTitleBar;
        }
        return null;
    }

    @Override
    protected boolean switchTitleBar() {
        return true;
    }

    @Override
    protected BaseVideoControllerBar createBottomBar() {
        return getControllerBar();
    }

    @Override
    protected boolean switchBottomBar() {
        return true;
    }

    @Override
    protected StateBar createStateBar() {
        final StateBar stateBar = new StateBar();
        stateBar.setStateBarListener(getStateBarCallback());
        return stateBar;
    }

    private BaseVideoControllerBar getControllerBar() {
        if (mControllerBar == null) {
            mControllerBar = createControllerBar();
        } else {
            if (!mControllerBar.isSameOrientation(getConfigurationOrientation())) {
                mControllerBar = createControllerBar();
            }
        }
        return mControllerBar;
    }

    @SuppressWarnings("unchecked")
    private BaseVideoControllerBar createControllerBar() {
        BaseVideoControllerBar controllerBar;
        if (screenOrientationFromPortrait()) {
            controllerBar = new PortraitBar();
        } else {
            controllerBar = new LandscapeBar();
        }
        controllerBar.setVideoControllerBarListener(getControllerBarCallback());
        return controllerBar;
    }

    private LandscapeBar.OnControllerBarListener getControllerBarCallback() {
        if (mControllerBarCallback == null) {
            mControllerBarCallback = new LandscapeBar.OnControllerBarListener() {
                @Override
                public void onControllerBarPlay() {
                    doPlayState();
                }

                @Override
                public void onControllerBarDirection() {
                    notifyScreenToSwitchOrientation(false);
                }

                @Override
                public void onControllerBarSpeed() {
                    hide();
                    if (mCallback != null) {
                        mCallback.onSwitchSpeed();
                    }
                }

                @Override
                public void onControllerBarDefinition() {
                    hide();
                    if (mCallback != null) {
                        mCallback.onSwitchDefinition();
                    }
                }

                @Override
                public void onControllerBarCatalog() {
                    hide();
                    if (mCallback != null) {
                        mCallback.onCatalog();
                    }
                }
            };
        }
        return mControllerBarCallback;
    }

    private TitleBar.OnTitleBarListener getTitleBarCallback() {
        if (mTitleBarCallback == null) {
            mTitleBarCallback = new TitleBar.OnTitleBarListener() {
                @Override
                public void onTitleBarBack() {
                    notifyScreenToSwitchOrientation(true);
                }

                @Override
                public void onTitleBarMore() {
                    hide();
                    if (mCallback != null) {
                        mCallback.onMore();
                    }
                }
            };
        }
        return mTitleBarCallback;
    }

    private StateBar.OnVideoStateBarListener getStateBarCallback() {
        if (mStateBarCallback == null) {
            mStateBarCallback = new StateBar.OnVideoStateBarListener() {
                @Override
                public void onStateBarBack() {
                    notifyScreenToSwitchOrientation(true);
                }

                @Override
                public void onStateBarMore() {
                    if (mCallback != null) {
                        mCallback.onMore();
                    }
                }

                @Override
                public void onStateBarSwitch() {
                    notifyScreenToSwitchOrientation(false);
                }

                @Override
                public void onStateBarRetry() {
                    if (mCallback != null) {
                        mCallback.onRetry();
                    }
                }
            };
        }
        return mStateBarCallback;
    }
}
