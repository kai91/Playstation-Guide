package robustgametools.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * HttpClient to process all http requests for the application
 */
public class HttpClient {

    private static AsyncHttpClient asyncHttpClient = null;

    // This is the domain name/ip address of the server
    private static String serverUrl = "http://boiling-bastion-9577.herokuapp.com/";

    private static void init() {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }
    }

    /**
     * This sends a GET request with username. Error 'No such PSN id will
     * be returned in case of incorrect id. If the PSN id is valid user's profile
     * data will be returned.
     * @param username
     * @param responseHandler
     */
    public static void signIn(String username, AsyncHttpResponseHandler responseHandler) {
        init();
        String url = serverUrl + "psn/" + username;
        asyncHttpClient.get(url, null, responseHandler);
    }

    /**
     * Cancel all ongoing requests
     */
    public static void cancelRequests() {
        if (asyncHttpClient != null) {
            asyncHttpClient.cancelAllRequests(true);
        }
    }
}
