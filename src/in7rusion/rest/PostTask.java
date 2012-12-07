package in7rusion.rest;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

public class PostTask extends AbstractTask {

    private final String _data;


    public PostTask(final String url, final String data,
            final RestTaskCallback success, final RestErrorCallback error) {
        super(url, success, error);
        _data = data;
    }

    @Override
    protected String doInBackground(final String... arg0) {
        HttpPost httpPost = new HttpPost(_url);
        httpPost.setHeader("Content-Type", "application/json");
        try {
            StringEntity entity = new StringEntity(_data);
            entity.setContentEncoding(HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            return call(httpPost);
        } catch (UnsupportedEncodingException e) {
            _error.onError(e.getMessage());
            return null;
        }
    }

}
