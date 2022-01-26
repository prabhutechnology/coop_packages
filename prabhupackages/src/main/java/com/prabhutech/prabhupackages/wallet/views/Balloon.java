package com.prabhutech.prabhupackages.wallet.views;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.prabhutech.prabhupackages.R;

public class Balloon {
    public static Animation down(Context context) {
        Animation balloonDown = AnimationUtils.loadAnimation(context, R.anim.balloon_down);
        balloonDown.setFillAfter(true);
        return balloonDown;
    }

    public static Animation up(Context context) {
        Animation balloonUp = AnimationUtils.loadAnimation(context, R.anim.balloon_up);
        balloonUp.setFillAfter(true);
        return balloonUp;
    }
}
