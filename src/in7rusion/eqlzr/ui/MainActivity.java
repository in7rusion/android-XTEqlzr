package in7rusion.eqlzr.ui;

import in7rusion.eqlzr.Logger;
import in7rusion.eqlzr.Properties;
import in7rusion.eqlzr.R;
import in7rusion.eqlzr.Toaster;
import in7rusion.eqlzr.ui.Accelerometer.OnParticleTouch;
import in7rusion.eqlzr.ui.gest.AbstractFlipperActivity;
import in7rusion.eqlzr.ui.render.BarGraphRenderer;
import in7rusion.eqlzr.ui.render.LedRenderer;
import in7rusion.eqlzr.ui.render.LightsRenderer;
import in7rusion.eqlzr.ui.render.Renderer;
import in7rusion.lights.Lights;
import in7rusion.lights.RestLights;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends OptionsMenuActivity implements Toaster {

    private final String _defaultUrl = "http://192.168.1.66:9666/";
    private final Renderer[] _defaultRenderers = new Renderer[]{
            new BarGraphRenderer(Color.argb(200, 56, 138, 252)),
            new LedRenderer(),
    };
    private final Accelerometer _accelero = new Accelerometer();
    private Renderer _lightsRenderer;
    private MediaPlayer _player;
    private EqualizerView _equalizerView;
    private SharedPreferences _preferences;



    public MainActivity() {
        super(R.id.flipper, R.menu.main);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        _accelero.onResume();
    }

    @Override
    protected void onPause() {
        _accelero.onPause();
        cleanUp();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        cleanUp();
        super.onDestroy();
    }

    private void init() {
        _player = MediaPlayer.create(this, R.raw.test);
        _player.setLooping(true);
        _player.start();

        _equalizerView = (EqualizerView) findViewById(R.id.visualizerView);
        _equalizerView.link(_player);
        _equalizerView.setCaptureRate(Properties.initialCaptureRate);

        for (Renderer renderer : _defaultRenderers) {
            _equalizerView.addRenderer(renderer);
        }

        bindSeekBar(R.id.seekBarRate, R.id.textRate, R.string.rate,
                Properties.maxCaptureRate, Properties.initialCaptureRate, new SeekBarListener() {
            @Override
            protected void onChange(final int value) {
                _equalizerView.setCaptureRate(value);
            }
        });
        bindSeekBar(R.id.seekBarSensitivity, R.id.textSensitivity, R.string.sensitivity,
                Properties.maxSensitivity, Properties.initialSensitivity, new SeekBarListener() {
            @Override
            protected void onChange(final int value) {
                _equalizerView.setSensitivity(value);
            }
        });
        bindSeekBar(R.id.seekBarBars, R.id.textBars, R.string.bars,
                Properties.maxBars, Properties.initialBars, new SeekBarListener() {
            @Override
            public void onChange(final int value) {
                _equalizerView.setBars(value);
            }
        });

        final List<String> log = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.test_list_item, android.R.id.text1, log);

        ListView list = (ListView) findViewById(R.id.Log);
        list.setAdapter(adapter);

        final Logger logger = new Logger() {
            @Override
            public void info(final String msg) {
                adapter.add(msg);
            }
        };
        logger.info(getUrl());
        toast(getUrl());

        final Lights lights = new RestLights(getUrl(), this, logger);
        _lightsRenderer = new LightsRenderer(lights);

        View simulationView = _accelero.create(this);
        _accelero.setOnParticleTouch(new OnParticleTouch() {
            @Override
            public void onTouch(final int i) {
                lights.set(i);
            }
        });
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.Accelero);
        ll.addView(simulationView);
    }

    private void bindSeekBar(final int id, final int textId, final int nameId,
            final int max, final int initial,
            final OnSeekBarChangeListener listener) {
        final String name = getString(nameId);
        final TextView text = (TextView) findViewById(textId);
        text.setText(name + initial);
        final SeekBar seek = (SeekBar) findViewById(id);
        seek.setMax(max);
        seek.setProgress(initial);
        seek.setOnSeekBarChangeListener(new SeekBarListener() {
            @Override
            protected void onChange(final int value) {
                text.setText(name + value);
                listener.onProgressChanged(seek, value, true);
            }
        });
    }

    private void cleanUp() {
        if (_player != null) {
            _equalizerView.release();
            _player.release();
            _player = null;
        }
    }
    
    @Override
    public boolean onItemSelected(final MenuItem item, final Menu menu) {
        switch (item.getItemId()) {
            case R.id.menu_connect :
                toggleConnection(true, menu);
                return true;
            case R.id.menu_disconnect :
                toggleConnection(false, menu);
                return true;
            case R.id.menu_preferences :
                startActivity(new Intent(getBaseContext(), Preferences.class));
                return true;
            default :
                return false;
        }
    }

    private void toggleConnection(final boolean state, final Menu menu) {
        menu.findItem(R.id.menu_connect).setEnabled(!state);
        menu.findItem(R.id.menu_disconnect).setEnabled(state);
        if (state) {
            _equalizerView.addRenderer(_lightsRenderer);
        } else {
            _equalizerView.removeRenderer(_lightsRenderer);
        }
    }

    private String getUrl() {
        String key = getString(R.string.preferences_url);
        String none = "?";
        String url = _preferences.getString(key, none);
        if (!url.equals(url.trim())) {
            _preferences.edit().putString(key, url.trim()).apply();
        }
        if (url == none) {
            url = _defaultUrl;
            _preferences.edit().putString(key, url).apply();
        }
        return url.endsWith("/") ? url : url + '/';
    }

    @Override
    public void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

abstract class SeekBarListener implements OnSeekBarChangeListener {

    protected abstract void onChange(int value);

    @Override
    public final void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        onChange(progress);
    }

    @Override
    public final void onStartTrackingTouch(final SeekBar seekBar) {}

    @Override
    public final void onStopTrackingTouch(final SeekBar seekBar) {}

}

abstract class OptionsMenuActivity extends AbstractFlipperActivity {

    private final int _menuId;

    private Menu _menu;


    public OptionsMenuActivity(final int flipperId, final int menuId) {
        super(flipperId);
        _menuId = menuId;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(_menuId, menu);
        _menu = menu;
        return true;
    }

    protected abstract boolean onItemSelected(MenuItem item, Menu menu);

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        return onItemSelected(item, _menu) ? true : super.onOptionsItemSelected(item);
    }

}
