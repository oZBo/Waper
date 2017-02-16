package braincollaboration.waper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_LOAD_ERROR_TAG = "Image transfer error";
    private static final String IMAGE_URL = "https://source.unsplash.com/random";
    private static final String KEY_ONSAVE_ROTATED_IMAGE = "braincollaboration.waper.mainContentImageView";
    private static final String KEY_ASYNCTASK_RUN_STATE = "braincollaboration.waper.asyncTaskIsRunning";
    private ImageView mainContentImageView;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private Boolean asyncTaskIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureWidgets();

        if(savedInstanceState != null) {
            Bitmap bitmap = savedInstanceState.getParcelable(KEY_ONSAVE_ROTATED_IMAGE);
            if (bitmap != null) { // restores ImageView
                mainContentImageView.setImageBitmap(bitmap);
            }
            asyncTaskIsRunning = savedInstanceState.getBoolean(KEY_ASYNCTASK_RUN_STATE);
            if (asyncTaskIsRunning) { //if AsyncTask was started and not finished
                progressBar.setVisibility(ProgressBar.VISIBLE); //progressBar shows.
                fab.setVisibility(View.INVISIBLE);
                fab.setEnabled(false); // makes the button unavailable & invisible.
            }
        }
    }

    private void configureWidgets() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mainContentImageView = (ImageView) findViewById(R.id.main_image);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadImageTask().execute();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(ProgressBar.VISIBLE); //progressBar shows.
            fab.setVisibility(View.INVISIBLE);
            fab.setEnabled(false); // makes the button unavailable & invisible.
            asyncTaskIsRunning = true;
        }

        protected Bitmap doInBackground(Void... params) {
            //TODO check what will happen when you minimize app while asyncTask is working
            // - When the application is minimized or rotated when AsyncTask is working, the AsyncTask code will execute in the background.
            Bitmap mImage;
            try {
                InputStream in = new java.net.URL(IMAGE_URL).openStream();
                mImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(IMAGE_LOAD_ERROR_TAG, e.getMessage());
                mImage = null;
            }

            return mImage;

            //TODO check what will happen when Stream return null. Maybe use some stub picture in this case?
            // - In this case will be used code in the 84th line.
        }

        protected void onPostExecute(Bitmap result) {
            progressBar.setVisibility(ProgressBar.INVISIBLE); //progressBar hides.

            if (result!=null) {
                mainContentImageView.setImageBitmap(result);
            } else {
                Toast imageLoadingErrorToast = Toast.makeText(getApplicationContext(), R.string.image_loading_error, Toast.LENGTH_SHORT);
                imageLoadingErrorToast.setGravity(Gravity.CENTER, 0, 0);
                imageLoadingErrorToast.show();
            }

            fab.setVisibility(View.VISIBLE);
            fab.setEnabled(true);  // makes the button available & visible.
            asyncTaskIsRunning = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        BitmapDrawable drawable = (BitmapDrawable) mainContentImageView.getDrawable();
        if (drawable != null) { //saves mainContentImageView if it's not consist in null or only link on empty xml object
            Bitmap bitmap = drawable.getBitmap();
            outState.putParcelable(KEY_ONSAVE_ROTATED_IMAGE, bitmap);
        }
        outState.putBoolean(KEY_ASYNCTASK_RUN_STATE, asyncTaskIsRunning);

        super.onSaveInstanceState(outState);
    }
}