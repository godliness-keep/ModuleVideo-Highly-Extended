package com.godliness.android.modulevideo.base;

import com.godliness.android.modulevideo.OnVideoStateListener;
import com.godliness.android.modulevideo.ijk.IjkVideoView;

/**
 * Created by godliness on 2020-04-03.
 *
 * @author godliness
 */
public abstract class Builder<T extends Builder, Controller extends BaseVideoController> {

    IjkVideoView mHost;
    OnVideoStateListener mVideoStateCallback;
    Switcher mSwitcher;

    protected Builder() {

    }

    public T host(IjkVideoView videoView) {
        this.mHost = videoView;
        return (T) this;
    }

    public T videoStateListener(OnVideoStateListener videoStateListener) {
        this.mVideoStateCallback = videoStateListener;
        return (T) this;
    }

    public T directionSwitcher(Switcher switcher) {
        this.mSwitcher = switcher;
        switcher.onConfiguartionCreated(mHost);
        return (T) this;
    }

    public Controller build() {
        if (mHost == null) {
            throw new NullPointerException("mHost == null");
        }
        if (mVideoStateCallback == null) {
            throw new NullPointerException("mVideoStateCallback == null");
        }
        if (mSwitcher == null) {
            mSwitcher = new Switcher();
        }
        mSwitcher.onConfiguartionCreated(mHost);
        return createController();
    }

    protected abstract Controller createController();

}

