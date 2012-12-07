package in7rusion.eqlzr.ui.render;

import in7rusion.eqlzr.ui.Gx;

public abstract class DelayedRenderer extends AdjustableRenderer {

    private final long _delay;

    private long _next = 0;


    public DelayedRenderer(final long miliseconds) {
        _delay = miliseconds;
    }

    @Override
    public void render(final Gx gx, final Adjustables adjustables) {
        long t = System.currentTimeMillis();
        if (t > _next) {
            _next = t + _delay;
            super.render(gx, adjustables);
        }
    }

}
