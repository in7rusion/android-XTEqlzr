package in7rusion.eqlzr.ui.render;

import in7rusion.eqlzr.ui.Gx;
import android.graphics.Color;
import android.graphics.Paint;

public class LedRenderer extends AdjustableRenderer {

    private final Paint _paint;


    public LedRenderer() {
        _paint = new Paint();
        _paint.setStrokeWidth(100f);
        _paint.setAntiAlias(true);
        _paint.setColor(Color.argb(255, 255, 255, 255));
    }

    @Override
    protected void onFFTRender(final Gx gx, final Adjustables adjustables, final double[] magnitudes) {
        int w = gx.width / adjustables.bars;
        int dx = w / 2;
        int r = (int) (w * .3666);
        int threshold = adjustables.sensitivity / 2;
        for (int i = 0; i < adjustables.bars; i++) {
            if (magnitudes[i] > threshold) {
                gx.canvas.drawCircle((i * w) + dx, dx, r, _paint);
            }
        }
    }

}
