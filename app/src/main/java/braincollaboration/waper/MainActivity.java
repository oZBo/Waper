package braincollaboration.waper;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import braincollaboration.waper.background.DownloadImageCallback;
import braincollaboration.waper.background.DownloadImageTask;
import braincollaboration.waper.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mainContentImageView;
    private FloatingActionButton floatingActionButton;
    private ProgressBar imageLoadingProgress;
    private Bitmap mainBitmap;
    private DownloadImageTask downloadImageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureWidgets();
        restoreViewsInstanceState(savedInstanceState);
        if (!isOnline()) {
            showDialog(Constants.DIALOG_INTERNET_NOT_AVIABLE);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_as_wallpaper_menu_button:
                ImageView imageView = mainContentImageView;
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                if (bitmap != null) {
                    mainBitmap = bitmap;
                    showDialog(Constants.DIALOG_SET_WALLPAPER_QUESTION);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.image_not_chosen, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.about_menu_button:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected Dialog onCreateDialog (int id) {
        if (id == Constants.DIALOG_INTERNET_NOT_AVIABLE) { //internet check dialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.notification_title);
        adb.setMessage(R.string.internet_connection_text);
        adb.setIcon(android.R.drawable.ic_dialog_info);
        adb.setNeutralButton(R.string.ok, myClickListener);
        return adb.create();
    } else if (id == Constants.DIALOG_SET_WALLPAPER_QUESTION) { //Set wallpaper dialog confirm
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.confirm_title);
            adb.setMessage(R.string.set_as_wallpaper_question);
            adb.setIcon(R.drawable.ic_question_answer_white_48dp);
            adb.setPositiveButton(R.string.yes, myClickListener);
            adb.setNegativeButton(R.string.cancel, myClickListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    setAsWallpaper();
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
                // нейтральная кнопка
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };

    private void setAsWallpaper() {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            wallpaperManager.setBitmap(mainBitmap);
            Toast.makeText(getApplicationContext(), R.string.wallpaper_set, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void configureWidgets() {
        mainContentImageView = (ImageView) findViewById(R.id.main_image);
        imageLoadingProgress = (ProgressBar) findViewById(R.id.progressBar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                initDownloadImageTask();
                break;
        }
    }

    private void initDownloadImageTask() {
        downloadImageTask = new DownloadImageTask(new DownloadImageCallback() {
            @Override
            public void onDownloadingStart() {
                imageLoadingProgress.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);
            }

            @Override
            public void onImageDownloaded(Bitmap bitmapResult) {
                imageLoadingProgress.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                mainContentImageView.setImageBitmap(bitmapResult);
            }

            @Override
            public void onDownloadingError() {
                imageLoadingProgress.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, R.string.image_loading_error, Toast.LENGTH_SHORT).show();
            }
        });
        downloadImageTask.execute();
    }

    private void restoreViewsInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.KEY_ONSAVE_ROTATED_IMAGE)) {
                Bitmap bitmap = savedInstanceState.getParcelable(Constants.KEY_ONSAVE_ROTATED_IMAGE);
                // restores ImageView
                mainContentImageView.setImageBitmap(bitmap);
            }
            if (savedInstanceState.containsKey(Constants.KEY_ASYNCTASK_RUN_STATE)) {
                if (savedInstanceState.getBoolean(Constants.KEY_ASYNCTASK_RUN_STATE)) {
                    imageLoadingProgress.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BitmapDrawable drawable = (BitmapDrawable) mainContentImageView.getDrawable();
        if (drawable != null) { //saves mainContentImageView if it's not consist in null or only link on empty xml object
            Bitmap bitmap = drawable.getBitmap();
            outState.putParcelable(Constants.KEY_ONSAVE_ROTATED_IMAGE, bitmap);
        }
        if (downloadImageTask != null) {
            outState.putBoolean(Constants.KEY_ASYNCTASK_RUN_STATE, downloadImageTask.isAsyncTaskRunning());
        }
    }
}