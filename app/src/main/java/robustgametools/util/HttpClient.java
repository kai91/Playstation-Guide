package robustgametools.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import java.util.ArrayList;

/**
 * HttpClient to process all http requests for the application
 */
public class HttpClient {

    private static AsyncHttpClient mAsyncHttpClient = null;
    private static ArrayList<RequestHandle> mRequestHandles = new ArrayList<RequestHandle>();

    private static RequestHandle mSignInRequest;
    private static RequestHandle mTrophyRequest;

    // This is the domain name/ip address of the server
    private static String serverUrl = "http://104.236.62.4/";


    private static void init() {
        if (mAsyncHttpClient == null) {
            mAsyncHttpClient = new AsyncHttpClient();
        }
    }

    /**
     * Sends a GET request with username. Error 'No such PSN id will
     * be returned in case of incorrect id. If the PSN id is valid user's profile
     * data will be returned.
     * @param username
     * @param responseHandler
     */
    public static void signIn(String username, AsyncHttpResponseHandler responseHandler) {
        init();
        String url = serverUrl + "psn/" + username;
        mSignInRequest = mAsyncHttpClient.get(url, null, responseHandler);
    }

    /**
     * Sends a GET request with username and returns the JSON file for
     * the first 100 games
     * @param responseHandler
     */
    public static void getRecentlyPlayedGames(String username,
                                              AsyncHttpResponseHandler responseHandler) {
        init();
        String url = serverUrl + "psn/" + username + "/" + "trophies";
        mRequestHandles.add(mAsyncHttpClient.get(url, null, responseHandler));
    }

    public static void getGames(String username, int offset,
                                AsyncHttpResponseHandler responseHandler) {
        init();
        String url = serverUrl + "psn/" + username + "/" + "trophies/offset/" + Integer.toString(offset);
        Log.i("GET request: " + url);
        mRequestHandles.add(mAsyncHttpClient.get(url, null, responseHandler));
    }

    public static void getTrophies(String username, String npId, AsyncHttpResponseHandler handler) {
        init();
        String url = serverUrl + "psn/" + username + "/" + "trophies/" + npId;
        Log.i(url);
        mTrophyRequest = mAsyncHttpClient.get(url, null, handler);
    }

    /**
     * Cancel all ongoing requests
     */
    public static void cancelRequests() {
        if (!mRequestHandles.isEmpty()) {
            int size = mRequestHandles.size();
            for (int j = 0; j < size; j++ ) {
                mRequestHandles.get(j).cancel(true);
            }
        }
    }

    /**
     * Downloads game guide
     */
    public static void getGameGuide(String name, AsyncHttpResponseHandler handler) {
        init();
        String url = serverUrl + "psn/getGuide/" + name;
        Log.i(url);
        mAsyncHttpClient.get(url, null, handler);
    }

    public static void getGuideList(int offset, AsyncHttpResponseHandler handler) {
        init();
        String url = serverUrl + "psn/listGuide/" + offset;
        mAsyncHttpClient.get(url, handler);
    }

    public static void cancelTrophyRequest() {
        if (mTrophyRequest != null) {
            mTrophyRequest.cancel(true);
            mTrophyRequest = null;
        }
    }

    /**
     * Cancel sign in request
     */
    public static void cancelSignInRequest() {
        if (mSignInRequest != null) {
            mSignInRequest.cancel(true);
            mSignInRequest = null;
        }
    }
}
