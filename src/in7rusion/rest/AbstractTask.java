package in7rusion.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public abstract class AbstractTask extends AsyncTask<String, String, String> {

    private static final String REST = "REST";

    protected final String _url;

    protected final RestTaskCallback _callback;

    protected final RestErrorCallback _error;


    public AbstractTask(final String url, final RestTaskCallback success, final RestErrorCallback error) {
        _url = url;
        _callback = success;
        _error = error != null ? error : new RestErrorCallback() {
            @Override
            public void onError(final String error) {
                Log.e(REST, error);
            }
        };
    }

    protected final String call(final HttpUriRequest httpRequest) {
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = client.execute(httpRequest);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                StringBuilder response = new StringBuilder();
                HttpEntity entity = httpResponse.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                _error.onError("" + statusCode + ": " + statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            _error.onError(e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(final String response) {
        if (_callback != null)
            _callback.onTaskComplete(response);
        super.onPostExecute(response);
    }

}
