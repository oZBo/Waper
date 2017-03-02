package braincollaboration.waper;

import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import braincollaboration.waper.utils.InternetUtil;
import braincollaboration.waper.views.dialog.CheckInternetAccessDialog;
import braincollaboration.waper.views.dialog.SetAsWallpaperDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mainContentImageView;
    private FloatingActionButton floatingActionButton;
    private ProgressBar imageLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureWidgets();
        restoreViewsInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!InternetUtil.isInternetAvailable(WaperApp.getCurrentActivity())) {
            showInternetNotAvailableDialog();
        }
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
                Bitmap bitmap = ((BitmapDrawable) mainContentImageView.getDrawable()).getBitmap();
                if (bitmap != null) {
                    showWallpaperDialog();
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

    private void showWallpaperDialog() {
        final SetAsWallpaperDialog dialog = new SetAsWallpaperDialog(MainActivity.this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setAsWallpaper();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.showDialog();
    }

    private void showInternetNotAvailableDialog() {
        CheckInternetAccessDialog dialog = new CheckInternetAccessDialog(MainActivity.this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.showDialog();
    }

    private void setAsWallpaper() {
        try {
            Bitmap bitmap = ((BitmapDrawable) mainContentImageView.getDrawable()).getBitmap();
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(WaperApp.getCurrentActivity());
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(WaperApp.getCurrentActivity(), R.string.wallpaper_set, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(WaperApp.getCurrentActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        DownloadImageTask downloadImageTask = new DownloadImageTask(new DownloadImageCallback() {
            @Override
            public void onDownloadingStart() {
                imageLoadingProgress.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);
            }

            @Override
            public void onImageDownloaded(Bitmap bitmapResult) {
                mainContentImageView.setImageBitmap(bitmapResult);
                imageLoadingProgress.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
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
    }
}