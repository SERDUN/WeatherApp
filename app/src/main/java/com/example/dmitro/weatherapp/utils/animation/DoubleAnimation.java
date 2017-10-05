package com.example.dmitro.weatherapp.utils.animation;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by dmitro on 05.10.17.
 */

public class DoubleAnimation {
    public DoubleAnimation(Animation animation, Animator animator,View view) {
        this.animation = animation;
        this.animator = animator;
        this.view=view;
    }

    private Animation animation;
    private Animator animator;
    private View view;

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animator getAnimator() {
        return animator;
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
