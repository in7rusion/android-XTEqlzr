package in7rusion.rest;

import org.apache.http.client.methods.HttpDelete;

public class DeleteTask extends AbstractTask {

    public DeleteTask(final String url, final RestTaskCallback success, final RestErrorCallback error) {
        super(url, success, error);
    }

    @Override
    protected String doInBackground(final String... arg0) {
        return call(new HttpDelete(_url));
    }

}
