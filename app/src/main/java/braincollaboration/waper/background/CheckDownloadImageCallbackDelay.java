package braincollaboration.waper.background;



/**
 * Use this callbackDelay to notify downloadingTask status
 */
public interface CheckDownloadImageCallbackDelay {

    /**
     * Check DownloadImageTask status
     */
    Boolean onDownloadImageTaskCheck();

    /**
     * If background work not finished write about a time.
     * In case of user ain't turn off internet connection, but has no money so no internet broadcast
     * Or connect to router with no internet connection
     * errorMessage showing that time delay is over the limit
     */
    void onDelayDownloadingError();

}
