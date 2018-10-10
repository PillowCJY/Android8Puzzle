package com.example.austin.fun8puzzle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView extends GridView {

    MyGestureListener myGestureListener;
    GestureDetector gestureDetector;
    public MyGridView(Context context) {
        super(context);
    }

    public void setGestureListener(MyGestureListener listener){
        myGestureListener = listener;
        gestureDetector = new GestureDetector(getContext(), myGestureListener);
    }

    public MyGridView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

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
