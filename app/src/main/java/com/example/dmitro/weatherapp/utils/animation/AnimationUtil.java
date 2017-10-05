package com.example.dmitro.weatherapp.utils.animation;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by dmitro on 05.10.17.
 */

public class AnimationUtil {

    public interface Callback {
        public void animationEnd();
    }

    public Callback callback;

//    private LinkedList<Object> animations;
//    private LinkedList<View> viewsAnim;
//    private int currentAnim = 0;

//    public AnimationUtil() {
//        animations = new LinkedList<>();
//        viewsAnim = new LinkedList<>();
//    }

    public  static void moveToCenterView(int[] outPointCoordinates, ViewGroup container, View view, Context context, Callback callback) {
        ViewGroup root = container;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int positionView[] = new int[2];
        view.getLocationOnScreen(positionView);
        int halfScreenX = displayMetrics.widthPixels / 2 - view.getWidth() / 2;
        int halfScreenY = (root.getMeasuredHeight() / 2) + view.getMeasuredHeight() / 2;
        TranslateAnimation anim = new TranslateAnimation(0, halfScreenX - positionView[0], 0, halfScreenY - positionView[1] - view.getHeight() / 2);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        outPointCoordinates[0] = root.getWidth() / 2;
        outPointCoordinates[1] = displayMetrics.heightPixels / 2 - view.getHeight() / 2;
        view.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callback != null) {
                    callback.animationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

//
//    public AnimationUtil splashInCoordinate(final View container, int x, int y, int color, int duration) {
//        container.setBackgroundColor(color);
//        int cx = x;
//        int cy = y;
//        int finalRadius = Math.max(container.getWidth(), container.getHeight());
//        Animator anim = ViewAnimationUtils.createCircularReveal(container, cx, cy, 0, finalRadius);
//        anim.setDuration(15000);
//        animations.add(anim);
//        viewsAnim.add(container);
//        return this;
//    }

    public static void splashInCoordinate(final View container, int x, int y, int color, int duration, float transparencyWith, float transparencyTo, Callback callback) {
        container.setBackgroundColor(color);
        int cx = x;
        int cy = y;
        int finalRadius = Math.max(container.getWidth(), container.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(container, cx, cy, 0, finalRadius);
        anim.setDuration(duration);

        AlphaAnimation animation1 = new AlphaAnimation(transparencyWith, transparencyTo);
        animation1.setDuration(duration);
        animation1.setFillAfter(true);
        anim.start();
        container.startAnimation(animation1);
        animation1.start();

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callback != null) {
                    callback.animationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public static  void useTransparency(View view, int duration, float from, float to, Callback callback) {
        AlphaAnimation animation1 = new AlphaAnimation(from, to);
        animation1.setDuration(duration);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callback != null) {
                    callback.animationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
//
//    public void apply() {
//        listener(currentAnim);
//    }
//
//    private void listener(int anim) {
//        if (animations.get(anim) instanceof DoubleAnimation) {
//            DoubleAnimation doubleAnimation = (DoubleAnimation) animations.get(anim);
//            doubleAnimation.getAnimation().setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    int d = 3;
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//
//            doubleAnimation.getAnimator().addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    currentAnim++;
//                    if (animations.size() - 1 < currentAnim) {
//                        if (callback != null) {
//                            callback.animationEnd();
//                        }
//                        return;
//                    } else {
//                        listener(currentAnim);
//                    }
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            });
//
//
//            doubleAnimation.getAnimator().start();
//            doubleAnimation.getView().startAnimation(doubleAnimation.getAnimation());
//
//
//        } else if (animations.get(anim) instanceof Animator) {
//            viewsAnim.get(anim).setVisibility(View.VISIBLE);
//            ((Animator) animations.get(anim)).addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    currentAnim++;
//                    if (animations.size() - 1 < currentAnim) {
//                        if (callback != null) {
//                            callback.animationEnd();
//                        }
//                        return;
//                    } else {
//                        listener(currentAnim);
//                    }
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            });
//            ((Animator) animations.get(anim)).start();
//
//        } else {
//            ((Animation) animations.get(anim)).setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    currentAnim++;
//                    if (animations.size() - 1 < currentAnim) {
//                        if (callback != null) {
//                            callback.animationEnd();
//                        }
//                        return;
//                    } else {
//                        listener(currentAnim);
//                    }
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            viewsAnim.get(anim).startAnimation((Animation) animations.get(anim));
//        }
//    }
//

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
