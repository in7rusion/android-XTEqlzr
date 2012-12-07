package in7rusion.eqlzr.ui.gest;

import android.app.Activity;

public class SwipeFilter extends GestureFilter {

    public SwipeFilter(final Activity context, final SwipeCallback callback) {
        super(context, new GestureListener() {
            @Override
            public void onSwipe(final int direction) {
                switch (direction) {
                    case GestureFilter.SWIPE_RIGHT :
                        callback.onSwipeRight();
                        break;
                    case GestureFilter.SWIPE_LEFT :
                        callback.onSwipeLeft();
                        break;
                    case GestureFilter.SWIPE_DOWN :
                        break;
                    case GestureFilter.SWIPE_UP :
                        break;
                }
            }
            @Override
            public void onDoubleTap() {}
        });
    }

}