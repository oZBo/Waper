package braincollaboration.waper.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constants {

    public static final String IMAGE_URL = "https://source.unsplash.com/random";
    public static final String KEY_ONSAVE_ROTATED_IMAGE = "braincollaboration.waper.mainContentImageView";
    public static final String KEY_ASYNCTASK_RUN_STATE = "braincollaboration.waper.asyncTaskIsRunning";
    public static final int DIALOG_INTERNET_NOT_AVIABLE = 1;
    public static final int DIALOG_SET_WALLPAPER_QUESTION = 2;

    public static boolean isInternetOn(ConnectivityManager cm) {
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
