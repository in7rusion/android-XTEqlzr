/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package in7rusion.eqlzr.ui;

import in7rusion.eqlzr.Properties;
import in7rusion.eqlzr.ui.render.Adjustables;
import in7rusion.eqlzr.ui.render.Renderer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.util.AttributeSet;
import android.view.View;

public class EqualizerView extends View implements OnDataCaptureListener {

    private final List<Renderer> _renderers = new ArrayList<Renderer>();

    private final Paint _flashPaint = new Paint();
    private final Paint _fadePaint = new Paint();

    private Gx _gx;
    private Visualizer _visualizer;

    private boolean _flash = false;

    private final Adjustables _adjustables = new Adjustables();


    public EqualizerView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs);

        _flashPaint.setColor(Color.argb(122, 255, 255, 255));
        _fadePaint.setColor(Color.argb(238, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
        _fadePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));

        _adjustables.bars = Properties.initialBars;
        _adjustables.sensitivity = Properties.initialSensitivity;
    }

    public EqualizerView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EqualizerView(final Context context) {
        this(context, null, 0);
    }

    public void link(final MediaPlayer player) {
        if (player == null) {
            throw new NullPointerException("Cannot link to null MediaPlayer");
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(final MediaPlayer mediaPlayer) {
                _visualizer.setEnabled(false);
            }
        });

        // Create the Visualizer object and attach it to our media player.
        _visualizer = new Visualizer(player.getAudioSessionId());
        _visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
    }

    public void setCaptureRate(final int captureRate) {
        _visualizer.setEnabled(false);
        _visualizer.setDataCaptureListener(this, captureRate, true, true);
        _visualizer.setEnabled(true);
    }

    public void addRenderer(final Renderer renderer) {
        if (renderer != null) {
            _renderers.add(renderer);
        }
    }

    public void removeRenderer(final Renderer renderer) {
        _renderers.remove(renderer);
    }

    public void clearRenderers() {
        _renderers.clear();
    }

    public void release() {
        _visualizer.release();
    }

    @Override
    public void onWaveFormDataCapture(final Visualizer visualizer, final byte[] bytes, final int samplingRate) {
        _adjustables.audio = bytes;
        invalidate();
    }

    @Override
    public void onFftDataCapture(final Visualizer visualizer, final byte[] bytes, final int samplingRate) {
        _adjustables.fft = bytes;
        invalidate();
    }

    public void flash() {
        _flash = true;
        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (_gx == null) {
            _gx = new Gx(canvas, getWidth(), getHeight());
        }

        if (_adjustables.audio != null || _adjustables.fft != null) {
            for (Renderer renderer : _renderers) {
                renderer.render(_gx, _adjustables);
            }
        }

        // Fade out old contents
        _gx.canvas.drawPaint(_fadePaint);

        if (_flash) {
            _flash = false;
            _gx.canvas.drawPaint(_flashPaint);
        }

        _gx.flush();
    }

    public void setBars(final int bars) {
        _adjustables.bars = bars;
    }

    public void setSensitivity(final int sensitivity) {
        _adjustables.sensitivity = sensitivity;
    }

}
