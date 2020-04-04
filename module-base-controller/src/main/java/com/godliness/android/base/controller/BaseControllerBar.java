package com.godliness.android.base.controller;

import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;

/**
 * Created by godliness on 2020-03-26.
 *
 * @author godliness
 */
public abstract class BaseControllerBar extends BaseBar {

    private View.OnAttachStateChangeListener mStateChangeCallback;
    private Animation.AnimationListener mHideAnimationCallback;

    /**
     * Show bar
     */
    @Override
    public void show(boolean openAnimation) {
        if (openAnimation) {
            if (mStateChangeCallback == null) {
                getBarView().addOnAttachStateChangeListener(getStateChangeCallback());
            }
        }
        attachToController();
    }

    /**
     * Hide bar
     */
    @Override
    public void hide(boolean openAnimation) {
        if (openAnimation) {
            final ViewParent parent = getBarView().getParent();
            if (parent != null) {
                getBarView().startAnimation(getHideAnimation());
            }
        } else {
            detachFromController();
        }
    }


    @Override
    protected void release() {
        super.release();
        if (mStateChangeCallback != null) {
            getBarView().removeOnAttachStateChangeListener(mStateChangeCallback);
        }
    }

    @Override
    protected final Animation.AnimationListener getHideAnimationCallback() {
        if (mHideAnimationCallback == null) {
            mHideAnimationCallback = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    detachFromController();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };
        }
        return mHideAnimationCallback;
    }

    private View.OnAttachStateChangeListener getStateChangeCallback() {
        if (mStateChangeCallback == null) {
            mStateChangeCallback = new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                    view.startAnimation(getShowAnimation());
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                }
            };
        }
        return mStateChangeCallback;
    }
}
