package braincollaboration.waper.background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

import braincollaboration.waper.R;
import braincollaboration.waper.utils.Constants;

/**
 * Simple download image though Url task.
 */
public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

    private DownloadImageCallback downloadCallback; //Callback that can signaling about downloading status
    private long delayTime = 2000; //Default delay time in AsyncTask. 3 seconds
    private String downloadingUrl = Constants.IMAGE_URL; //Default download Url

    /**
     * You can use this asyncTask to download image.
     * @param callback signaling callback
     */
    public DownloadImageTask (DownloadImageCallback callback) {
        this.downloadCallback = callback;
    }

    /**
     * You can use this asyncTask to download image and set delay time.
     * @param callback signaling callback
     * @param delayTime asyncTask delayTime
     */
    public DownloadImageTask(DownloadImageCallback callback, long delayTime){
        this.downloadCallback = callback;
        this.delayTime = delayTime;
    }

    /**
     * * You can use this asyncTask to download image through customUrl and set delay time.
     * @param callback signaling callback
     * @param downloadUrl custom downloading Url
     * @param delayTime asyncTask delayTime
     */
    public DownloadImageTask(DownloadImageCallback callback, String downloadUrl, long delayTime){
        this.downloadCallback = callback;
        this.downloadingUrl = downloadUrl;
        this.delayTime = delayTime;
    }

    @Override
    protected void onPreExecute() {
        downloadCallback.onDownloadingStart();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap resultBitmap = null;
        try {
            URL downloadImageUrl = new URL(downloadingUrl);
            Thread.sleep(delayTime);
            resultBitmap = BitmapFactory.decodeStream(downloadImageUrl.openConnection().getInputStream());
        } catch (IOException | InterruptedException e) { }
        return resultBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result != null) {
            downloadCallback.onImageSaved(result);
            downloadCallback.onImageDownloaded(result);
        }else{
            downloadCallback.onDownloadingError();
        }
    }

    /**
     * @return true if currentTask is running
     */
    public boolean isAsyncTaskRunning(){
        return getStatus() == Status.RUNNING;
    }

}
