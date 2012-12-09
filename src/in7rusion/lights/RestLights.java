package in7rusion.lights;

import in7rusion.eqlzr.Logger;
import in7rusion.eqlzr.Toaster;
import in7rusion.rest.DeleteTask;
import in7rusion.rest.PutTask;
import in7rusion.rest.Rest;
import in7rusion.rest.RestErrorCallback;

import java.util.Arrays;

public final class RestLights implements Lights, RestErrorCallback {

    private static final String TREE = "tree";

    private static final String LINE = "line/";

    private final String _url;

    private final Toaster _toaster;

    private final Logger _logger;

    public RestLights(final String url, final Toaster toaster, final Logger logger) {
        _url = url;
        _toaster = toaster;
        _logger = logger;
    }

    @Override
    public void set(final int line) {
        String url = _url + LINE + line;
        _logger.info("PUT " + url);
        new PutTask(url, null, this).execute();
    }

    @Override
    public void set(final int[] lines) {
        String url = _url + TREE;
        _logger.info("DELETE " + url);
        new DeleteTask(url, null, this).execute();
        if (lines.length > 0) {
            _logger.info("POST " + url + " " + Arrays.toString(lines));
            Rest.postJson(url, lines, null, this);
        }
    }

    @Override
    public void onError(final String error) {
        _toaster.toast(error);
    }

}
