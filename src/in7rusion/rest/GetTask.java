package in7rusion.rest;

import org.apache.http.client.methods.HttpGet;

public class GetTask extends AbstractTask {

    public GetTask(final String url, final RestTaskCallback success, final RestErrorCallback error) {
        super(url, success, error);
    }

    @Override
    protected String doInBackground(final String... params) {
        return call(new HttpGet(_url));
    }

}
