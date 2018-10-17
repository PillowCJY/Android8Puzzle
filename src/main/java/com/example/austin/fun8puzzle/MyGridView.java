package com.example.austin.fun8puzzle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * My own gridview
 * the reason to rewrite this class is because child views will consume on touchEvent which need onIntercept to fix it
 * @author Junyi Chen
 */
public class MyGridView extends GridView {

    MyGestureListener myGestureListener; //Gesture listener
    GestureDetector gestureDetector; //Gesture detector

    //constructor
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    //this method will set gesturelistener
    public void setGestureListener(MyGestureListener listener){
        myGestureListener = listener;
        gestureDetector = new GestureDetector(getContext(), myGestureListener);
    }

    //override on intercept to prevent children views consume onTouch event
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent(event);
    }

}
