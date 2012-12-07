package in7rusion.eqlzr.ui.render;

import in7rusion.eqlzr.ui.Gx;


public abstract class AdjustableRenderer implements Renderer {

    protected abstract void onFFTRender(Gx gx, Adjustables adjustables, double[] magnitudes);

    @Override
    public void render(final Gx gx, final Adjustables adjustables) {
        double[] magnitudes = new double[adjustables.bars];
        for (int i = 0; i < adjustables.bars; i++) {
            int pos = 2 * i;
            magnitudes[i] = (adjustables.fft[pos] ^ 2) + (adjustables.fft[pos + 1] ^ 2);
        }

        onFFTRender(gx, adjustables, magnitudes);
    }

}
