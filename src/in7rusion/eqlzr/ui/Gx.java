package in7rusion.eqlzr.ui;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;

public final class Gx {

    private final Canvas _canvas;
    public final Canvas canvas;
    public final Bitmap canvasBitmap;
    public final Matrix matrix = new Matrix();
    public final int width;
    public final int height;


    public Gx(final Canvas c, final int w, final int h) {
        _canvas = c;
        canvasBitmap = Bitmap.createBitmap(c.getWidth(), c.getHeight(), Config.ARGB_8888);
        canvas = new Canvas(canvasBitmap);
        width = w;
        height = h;
    }

    public void flush() {
        _canvas.drawBitmap(canvasBitmap, matrix, null);
    }

}
