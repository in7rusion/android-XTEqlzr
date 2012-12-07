package in7rusion.eqlzr.ui.gest;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GestureFilter extends SimpleOnGestureListener {

    public final static int SWIPE_UP = 1;
    public final static int SWIPE_DOWN = 2;
    public final static int SWIPE_LEFT = 3;
    public final static int SWIPE_RIGHT = 4;

    public final static int MODE_TRANSPARENT = 0;
    public final static int MODE_SOLID = 1;
    public final static int MODE_DYNAMIC = 2;

    private final static int ACTION_FAKE = -13; // just an unlikely number
    private int _swipe_Min_Distance = 100;
    private int _swipe_Max_Distance = 350;
    private int _swipe_Min_Velocity = 100;

    private int _mode = MODE_DYNAMIC;
    private boolean _running = true;
    private boolean _tapIndicator = false;

    private Activity _context;
    private GestureDetector _detector;
    private GestureListener _listener;


    public GestureFilter(final Activity context, final GestureListener sgl) {
        _context = context;
        _detector = new GestureDetector(_context, this);
        _listener = sgl;
    }

    public void onTouchEvent(final MotionEvent event) {
        if (!_running)
            return;
        boolean result = _detector.onTouchEvent(event);
        if (_mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
        else if (_mode == MODE_DYNAMIC) {

            if (event.getAction() == ACTION_FAKE)
                event.setAction(MotionEvent.ACTION_UP);
            else if (result)
                event.setAction(MotionEvent.ACTION_CANCEL);
            else if (_tapIndicator) {
                event.setAction(MotionEvent.ACTION_DOWN);
                _tapIndicator = false;
            }
        }
    }

    public void setMode(final int m) {
        _mode = m;
    }

    public int getMode() {
        return _mode;
    }

    public void setEnabled(final boolean status) {
        _running = status;
    }

    public void setSwipeMaxDistance(final int distance) {
        _swipe_Max_Distance = distance;
    }

    public void setSwipeMinDistance(final int distance) {
        _swipe_Min_Distance = distance;
    }

    public void setSwipeMinVelocity(final int distance) {
        _swipe_Min_Velocity = distance;
    }

    public int getSwipeMaxDistance() {
        return _swipe_Max_Distance;
    }

    public int getSwipeMinDistance() {
        return _swipe_Min_Distance;
    }

    public int getSwipeMinVelocity() {
        return _swipe_Min_Velocity;
    }

    @Override
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, float velocityX, float velocityY) {
        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());

        if (xDistance > _swipe_Max_Distance || yDistance > _swipe_Max_Distance)
            return false;

        velocityX = Math.abs(velocityX);
        velocityY = Math.abs(velocityY);
        boolean result = false;

        if (velocityX > _swipe_Min_Velocity && xDistance > _swipe_Min_Distance) {
            if (e1.getX() > e2.getX()) // right to left
                _listener.onSwipe(SWIPE_LEFT);
            else
                _listener.onSwipe(SWIPE_RIGHT);
            result = true;
        } else if (velocityY > _swipe_Min_Velocity && yDistance > _swipe_Min_Distance) {
            if (e1.getY() > e2.getY()) // bottom to up
                _listener.onSwipe(SWIPE_UP);
            else
                _listener.onSwipe(SWIPE_DOWN);
            result = true;
        }
        return result;
    }

    @Override
    public boolean onSingleTapUp(final MotionEvent e) {
        _tapIndicator = true;
        return false;
    }

    @Override
    public boolean onDoubleTap(final MotionEvent arg0) {
        _listener.onDoubleTap();;
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(final MotionEvent arg0) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent arg0) {
        if (_mode == MODE_DYNAMIC) { // we owe an ACTION_UP, so we fake an
            arg0.setAction(ACTION_FAKE); // action which will be converted to an ACTION_UP later.
            _context.dispatchTouchEvent(arg0);
        }
        return false;
    }

}
