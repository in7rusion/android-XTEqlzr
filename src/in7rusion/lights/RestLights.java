package in7rusion.lights;

import in7rusion.eqlzr.Logger;
import in7rusion.eqlzr.Toaster;
import in7rusion.rest.PutTask;
import in7rusion.rest.Rest;
import in7rusion.rest.RestErrorCallback;

public final class RestLights implements Lights, RestErrorCallback {

    private static final String PROGRAM = "program";

    private static final String RESET = "reset";

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
    public void set(final boolean[] line) {
        String url = _url + PROGRAM;
        Program program = new Program(line);

        _logger.info("POST " + url + " " + program);

        Rest.postJson(url, program, null, this);
    }

    @Override
    public void onError(final String error) {
        _toaster.toast(error);
    }

}

final class Program {
    public final String author = "Szatan";
    public final String name = "XTEqlzr";
    public final String program;

    public Program(final boolean[] line) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < line.length; i++) {
            if (line[i])
                sb.append(i).append(' ');
        }
        sb.append("; 6666)");
        program = sb.toString();
    }

    @Override
    public String toString() {
        return program;
    }
}
