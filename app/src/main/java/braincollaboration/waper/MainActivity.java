package braincollaboration.waper;

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

import braincollaboration.waper.background.DownloadImageCallback;
import braincollaboration.waper.background.DownloadImageTask;
import braincollaboration.waper.utils.Constants;

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

    private void configureWidgets() {
        imageLoadingProgress = (ProgressBar) findViewById(R.id.progressBar);
        mainContentImageView = (ImageView) findViewById(R.id.main_image);
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
                imageLoadingProgress.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                mainContentImageView.setImageBitmap(bitmapResult);
            }

            @Override
            public void onDownloadingError(String errorMessage) {
                imageLoadingProgress.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        downloadImageTask.execute();
    }

    private void restoreViewsInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bitmap bitmap = savedInstanceState.getParcelable(Constants.KEY_ONSAVE_ROTATED_IMAGE);
            if (bitmap != null) { // restores ImageView
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