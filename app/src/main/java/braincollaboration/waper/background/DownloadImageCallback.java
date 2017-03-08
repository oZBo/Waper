package braincollaboration.waper.background;


import android.graphics.Bitmap;

/**
 * Use this callback to notify downloading status
 */
public interface DownloadImageCallback {

    /**
     * Signaling when image download started.
     * And starts another asyncTask to be convict that main asyncTask not out of time limit
     * Here you can do whatever you want with your Views.
     */
    void onDownloadingStart();

    /**
     * Signaling when image downloaded.
     * Background work finished. Do something with your Views.
     */
    void onImageDownloaded(Bitmap bitmapResult);

    /**
     * Saves downloaded bitmap on sd card
     */
    void onImageSaved(Bitmap bitmapResult);

    /**
     * Signaling when something went wrong.
     * Background work finished. Do something with your Views.
     * errorMessage showing what went wrong
     */
    void onDownloadingError();

}
