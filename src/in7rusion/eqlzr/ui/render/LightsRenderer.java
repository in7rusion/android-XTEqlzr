package in7rusion.eqlzr.ui.render;

import in7rusion.eqlzr.ui.Gx;
import in7rusion.lights.Lights;

public class LightsRenderer extends DelayedRenderer {

    private static final int N = 8;

    private final Lights _lights;


    public LightsRenderer(final Lights lights) {
        super(666);
        _lights = lights;
    }

    @Override
    protected void onFFTRender(final Gx gx, final Adjustables adjustables, final double[] magnitudes) {
        int threshold = adjustables.sensitivity / 2;
        boolean[] line = new boolean[N];
        for (int i = 0; i < N; i++) {
            line[i] = magnitudes[i] > threshold;
        }
        _lights.set(line);
    }

}
