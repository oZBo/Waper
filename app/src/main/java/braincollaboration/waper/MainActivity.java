package braincollaboration.waper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import braincollaboration.waper.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    static ImageView mainContentImageView;
    static FloatingActionButton fab;
    static ProgressBar progressBar;
    static Boolean asyncTaskIsRunning;
    static ArrayList<ImageView> imageViewArrayList;
    private DownloadImageTask dtm;
    static long asyncTaskTimeFinished;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_as_wallpaper_menu_button:
                return true;
            case R.id.about_menu_button:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureWidgets();
        restoreViewsInstanceState(savedInstanceState);
    }

    private void restoreViewsInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bitmap bitmap = savedInstanceState.getParcelable(Constants.KEY_ONSAVE_ROTATED_IMAGE);
            if (bitmap != null) { // restores ImageView
                mainContentImageView.setImageBitmap(bitmap);
            }
            asyncTaskIsRunning = savedInstanceState.getBoolean(Constants.KEY_ASYNCTASK_RUN_STATE);

            if (asyncTaskIsRunning) { //if AsyncTask was started and not finished
                fab.setEnabled(false);
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
        }
    }

    private void configureWidgets() {
        imageViewArrayList = new ArrayList<ImageView>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mainContentImageView = (ImageView) findViewById(R.id.main_image);
        asyncTaskIsRunning = false;


        mainContentImageView.setOnClickListener(this);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        mainContentImageView.setOnTouchListener(gestureListener);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                if (!asyncTaskIsRunning && currentTime.getTimeInMillis() > asyncTaskTimeFinished + 2000) {
                    dtm = new DownloadImageTask(getApplicationContext());
                    dtm.execute();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener { // reacting on display swipe
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > Constants.SWIPE_MAX_OFF_PATH)
                    return false;
                if (e1.getX() - e2.getX() > Constants.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(MainActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();

                    if (!asyncTaskIsRunning && !imageViewArrayList.isEmpty() && imageViewArrayList.indexOf(mainContentImageView) + 1 < imageViewArrayList.size()) {
                        ImageView imageView = imageViewArrayList.get(imageViewArrayList.indexOf(mainContentImageView) + 1); // takes the next picture in arrayList
                        Drawable drawable = imageView.getDrawable();
                        mainContentImageView.setImageDrawable(drawable); // and set it to the current mainImage
                    }

                } else if (e2.getX() - e1.getX() > Constants.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(MainActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();

                    if (!asyncTaskIsRunning && !imageViewArrayList.isEmpty() && imageViewArrayList.indexOf(mainContentImageView) - 1 >= 0) {
                        ImageView imageView = imageViewArrayList.get(imageViewArrayList.indexOf(mainContentImageView) - 1); // takes the previous picture in arrayList
                        Drawable drawable = imageView.getDrawable();
                        mainContentImageView.setImageDrawable(drawable); // and set it to the current mainImage
                    }

                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }


    public static void toastMakeNoPictureErrorText(Context context) {
        Toast imageLoadingErrorToast = Toast.makeText(context,
                R.string.image_loading_error, Toast.LENGTH_SHORT);
        imageLoadingErrorToast.setGravity(Gravity.CENTER, 0, 0);
        imageLoadingErrorToast.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BitmapDrawable drawable = (BitmapDrawable) mainContentImageView.getDrawable();
        if (drawable != null) { //saves mainContentImageView if it's not consist in null or only link on empty xml object
            Bitmap bitmap = drawable.getBitmap();
            outState.putParcelable(Constants.KEY_ONSAVE_ROTATED_IMAGE, bitmap);
        }
        outState.putBoolean(Constants.KEY_ASYNCTASK_RUN_STATE, asyncTaskIsRunning);
    }

    //TODO features list:
    // 1. Add menu to the ActionBar on the right side. Add 2 options on it. Set as Wallpaper button and About button.
    // 2. In some reasons API for downloading images have restriction on call number. So it would be nice to add timer (3 seconds would be enough) between calls.
    // 3. Add possibility refresh image by swipe image from left to right and vice versa.
    // 4. Add padding for FloatingActionButton in activity_main.xml.
    // 5. Implement case when user cant click on refresh button while image is still loading.
}