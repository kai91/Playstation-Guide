package robustgametools.util;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * An AsyncLoader class that utilizes Universal
 * Image Loader for asynchronous image loading
 * and caching.
 */
public class AsyncLoader {

    private static AsyncLoader mAsyncLoader = null;
    private static Context mContext = null;

    private AsyncLoader(Context context) {
        // UNIVERSAL IMAGE LOADER SETUP
        mContext = context;

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheOnDisk(true)/.cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .threadPoolSize(3)
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    public static AsyncLoader getInstance(Context context) {
        if (mAsyncLoader == null) {
            mAsyncLoader = new AsyncLoader(context);
        }
        return mAsyncLoader;
    }

    public void displayImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

}
