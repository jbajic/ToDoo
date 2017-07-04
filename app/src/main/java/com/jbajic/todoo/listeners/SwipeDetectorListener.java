package com.jbajic.todoo.listeners;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.logging.Logger;

/**
 * Created by jbajic on 14.06.17..
 */

public class SwipeDetectorListener implements View.OnTouchListener {

    public static enum Action {
        LeftToRight,
        RightToLeft,
        TopToBottom,
        BottomToTop,
        None
    };

    private static final int MIN_DISTANCE = 80; // The minimum distance
    private float downX, downY, upX, upY; // Coordinates
    private Action mSwipeDetected = Action.None; // Last action

    public boolean swipeDetected() {
        return mSwipeDetected != Action.None;
    }

    public Action getAction() {
        return mSwipeDetected;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("MOTION", String.valueOf(event.getX()));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                mSwipeDetected = Action.None;
                return false; // allow other events like Click to be processed
            case MotionEvent.ACTION_MOVE:
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // horizontal swipe detection
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        mSwipeDetected = Action.LeftToRight;
                        return true;
                    }
                    if (deltaX > 0) {
                        mSwipeDetected = Action.RightToLeft;
                        return true;
                    }
                } else

                    // vertical swipe detection
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        // top or down
                        if (deltaY < 0) {
                            mSwipeDetected = Action.TopToBottom;
                            return false;
                        }
                        if (deltaY > 0) {
                            mSwipeDetected = Action.BottomToTop;
                            return false;
                        }
                    }
                return true;
        }
        return false;
    }


}
