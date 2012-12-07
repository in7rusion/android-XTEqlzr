package in7rusion.rest;

import com.google.gson.Gson;

public final class Rest {

    private static final Gson GSON = new Gson();


    public static void get(final String url, 
            final GetCallback callback, final RestErrorCallback error) {
        new GetTask(url, new RestTaskCallback() {
            @Override
            public void onTaskComplete(final String response) {
                if (callback != null)
                    callback.onDataReceived(response);
            }
        }, error).execute();
    }

    public static void put(final String url, final String data, 
            final PutCallback callback, final RestErrorCallback error) {
        new PutTask(url, new RestTaskCallback() {
            @Override
            public void onTaskComplete(final String response) {
                if (callback != null)
                    callback.onPostSuccess();
            }
        }, error).execute();
    }

    public static void post(final String url, final String data, 
            final PostCallback callback, RestErrorCallback error) {
        new PostTask(url, data, new RestTaskCallback() {
            @Override
            public void onTaskComplete(final String response) {
                if (callback != null)
                    callback.onPostSuccess();
            }
        }, error).execute();
    }

    public static void postJson(final String url, final Object data, 
            final PostCallback callback, RestErrorCallback error) {
        new PostTask(url, GSON.toJson(data), new RestTaskCallback() {
            @Override
            public void onTaskComplete(final String response) {
                if (callback != null)
                    callback.onPostSuccess();
            }
        }, error).execute();
    }

}
