package braincollaboration.waper.background;


import android.graphics.Bitmap;

/**
 * Use this callback to notify downloading status
 */
public interface DownloadImageCallback {

    /**
     * Signaling when image download started.
     * Here you can do whatever you want with your Views.
     */
    void onDownloadingStart();

    /**
     * Signaling when image downloaded.
     * Background work finished. Do something with your Views.
     */
    void onImageDownloaded(Bitmap bitmapResult);

    /**
     * Signaling when something went wrong.
     * Background work finished. Do something with your Views.
     * @param errorMessage showing what went wrong
     */
    void onDownloadingError(String errorMessage);

}
