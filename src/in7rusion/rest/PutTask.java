package in7rusion.rest;

import org.apache.http.client.methods.HttpPut;

public class PutTask extends AbstractTask {

    public PutTask(final String url, final RestTaskCallback success, final RestErrorCallback error) {
        super(url, success, error);
    }

    @Override
    protected String doInBackground(final String... arg0) {
        return call(new HttpPut(_url));
    }

}
