package com.busradeniz.nightswatch.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class RecyclerItemTouchToClickListener implements RecyclerView.OnItemTouchListener {
    private final GestureDetector gestureDetector;

    public RecyclerItemTouchToClickListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public final boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && gestureDetector.onTouchEvent(e)) {
            onClick(view, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public final void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public final void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        };
    }

    public abstract void onClick(View view, int position);
}
