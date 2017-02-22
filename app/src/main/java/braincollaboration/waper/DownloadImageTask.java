package braincollaboration.waper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import braincollaboration.waper.utils.Constants;

public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

    Context context;

    DownloadImageTask (Context context) { //to use for static method toastMakeNoPictureText in MainActivity.class
        this.context = context;
    }

    void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        MainActivity.asyncTaskIsRunning = true;
        MainActivity.fab.setEnabled(false);
        MainActivity.progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap mImage = null;
        try {
            InputStream in = new java.net.URL(Constants.IMAGE_URL).openStream();
            mImage = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(Constants.IMAGE_LOAD_ERROR_LOG_TAG, e.getMessage());
        }

        return mImage;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            MainActivity.mainContentImageView.setImageBitmap(result);
            MainActivity.imageViewArrayList.add(MainActivity.mainContentImageView);
            Calendar currentTime = Calendar.getInstance();
            MainActivity.asyncTaskTimeFinished = currentTime.getTimeInMillis();
        } else {
            MainActivity.toastMakeNoPictureErrorText(context);
        }
        MainActivity.progressBar.setVisibility(ProgressBar.INVISIBLE); //progressBar hides.
        MainActivity.fab.setEnabled(true);  // makes the button available & visible.
        MainActivity.asyncTaskIsRunning = false;
    }

}
