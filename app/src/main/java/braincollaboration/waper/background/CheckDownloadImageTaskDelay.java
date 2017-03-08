package braincollaboration.waper.background;

import android.os.AsyncTask;

/**
 * Checking and canceling downloadImageTask if there is delay load time
 */
public class CheckDownloadImageTaskDelay extends AsyncTask<Void, Void, Void> {

    private CheckDownloadImageCallbackDelay checkDelay; //Callback that can signaling about downloadingTask status
    private long delayTime = 8000; //Default delay time AsyncTask. 8 seconds

    /**
     * You can use this asyncTask to check and delete another task.
     * @param callback signaling callback
     */
    public CheckDownloadImageTaskDelay(CheckDownloadImageCallbackDelay callback) {
        this.checkDelay = callback;
    }

    /**
     * You can use this asyncTask to check and delete another task and set your own delay time.
     * @param callback signaling callback
     * @param delayTime asyncTask delayTime
     */
    public CheckDownloadImageTaskDelay(CheckDownloadImageCallbackDelay callback, long delayTime){
        this.checkDelay = callback;
        this.delayTime = delayTime;
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(delayTime);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * checkDelay.onDownloadImageTaskCheck() returns DownloadImageTask status after delayTime pause
     */

    @Override
    protected void onPostExecute(Void aVoid) {
        if (checkDelay.onDownloadImageTaskCheck()) {
            checkDelay.onDelayDownloadingError();
        }
    }

    /**
     * @return true if currentTask is running
     */
    public boolean isAsyncTaskRunning(){
        return getStatus() == Status.RUNNING;
    }

}
