package robustgametools.util;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import robustgametools.guide.GuideFactory;
import robustgametools.model.TrophyGuide;

public class GuideDownloader {

    private static GuideDownloader mDownloader;
    private static Storage mStorage;
    private Context mContext;

    private GuideDownloader(Context context) {
        mContext = context.getApplicationContext();
        mStorage = Storage.getInstance(mContext);
    }

    public static GuideDownloader getInstance(Context context) {
        if (mDownloader == null) {
            mDownloader = new GuideDownloader(context);
        }
        return mDownloader;
    }

    public void downloadGuide(final String title, final AsyncHttpResponseHandler handler) {
        HttpClient.getGameGuide(title, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] guideBytes) {
                String guideData = new String(guideBytes);
                mStorage.persistGuideData(title, guideData);
                TrophyGuide guide = new Gson().fromJson(guideData, TrophyGuide.class);
                GuideFactory factory = GuideFactory.getInstance(mContext);
                ArrayList<String> imageUrls = factory.extractImageUrl(guide);
                downloadImages(imageUrls, title, handler);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                  Throwable error) {
                handler.onFailure(statusCode, headers, responseBody, error);
            }
        });
    }

    private void downloadImages(final ArrayList<String> urls, final String title,
                                final AsyncHttpResponseHandler handler) {
        final ArrayList<Integer> completed = new ArrayList<Integer>();
        for (int i = 0; i < urls.size(); i++) {
            final String url = urls.get(i);
            HttpClient.getImage(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    completed.add(1);
                    mStorage.persistGuideImage(url, title, responseBody);
                    if (completed.size() == urls.size()) {
                        handler.onSuccess(200, null, null);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    handler.onFailure(statusCode, headers, responseBody, error);
                }
            });
        }
    }

    public void cancelOngoingDownload() {
        HttpClient.cancelGuideRequest();
        HttpClient.cancelGuideRequest();
    }

}
