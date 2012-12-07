package in7rusion.eqlzr.ui.gest;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public abstract class AbstractFlipperActivity extends Activity {

    private final int _flipperId;

    private GestureFilter _gestureFilter;


    public AbstractFlipperActivity(final int flipperId) {
        _flipperId = flipperId;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _gestureFilter = new SwipeFilter(this, new SwipeCallback() {
            private ViewFlipper flipper() {
                return (ViewFlipper) findViewById(_flipperId);
            }
            @Override
            public void onSwipeRight() {
                flipper().showPrevious();
            }
            @Override
            public void onSwipeLeft() {
                flipper().showNext();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        _gestureFilter.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

}
