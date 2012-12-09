package in7rusion.eqlzr.ui.render;

import in7rusion.eqlzr.ui.Gx;
import in7rusion.lights.Lights;

public class LightsRenderer extends DelayedRenderer {

    private static final int N = 8;

    private final Lights _lights;


    public LightsRenderer(final Lights lights) {
        super(999);
        _lights = lights;
    }

    @Override
    protected void onFFTRender(final Gx gx, final Adjustables adjustables, final double[] magnitudes) {
        int threshold = adjustables.sensitivity / 2;
        int n = 0;
        for (int i = 0; i < N; i++) {
            if (magnitudes[i] > threshold)
                ++n;
        }
        int[] lines = new int[n];
        if (n > 0) {
            n = 0;
            for (int i = 0; i < N; i++) {
                if (magnitudes[i] > threshold)
                    lines[n++] = i;
            }
        }
        _lights.set(lines);
    }

}
