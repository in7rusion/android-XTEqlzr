package in7rusion.eqlzr.ui.render;

import in7rusion.eqlzr.ui.Gx;
import android.graphics.Paint;

public class BarGraphRenderer extends AdjustableRenderer {

    private final Paint _paint;


    public BarGraphRenderer(final int color) {
        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setColor(color);
    }

    @Override
    protected void onFFTRender(final Gx gx, final Adjustables adjustables, final double[] magnitudes) {
        float[] points = new float[adjustables.bars * 4];
        int stroke = gx.width / adjustables.bars;
        int dx = stroke / 2;
        int h = gx.height;
        int hh = h / 2;
        for (int i = 0; i < adjustables.bars; i++) {
            int pos = i * 4;
            points[pos] = points[pos + 2] = (i * stroke) + dx;
            points[pos + 1] = hh;
            double norm = magnitudes[i] + adjustables.sensitivity;
            points[pos + 3] = (float) (h - (norm * h / (2 * adjustables.sensitivity)));
        }

        // Log.d("Q", "BARS=" + bars + ", SENS=" + sensitivity + ", W=" + stroke);

        _paint.setStrokeWidth(stroke);
        gx.canvas.drawLines(points, _paint);
    }

}
