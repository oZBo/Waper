package braincollaboration.waper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
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
    private braincollaboration.waper.utils.Dialog dialog;

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

        if (!Constants.isInternetOn((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            dialog = new braincollaboration.waper.utils.Dialog(getApplicationContext());
            dialog.showDialog(Constants.DIALOG_INTERNET_NOT_AVIABLE);
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
                Bitmap bitmap = ((BitmapDrawable)mainContentImageView.getDrawable()).getBitmap();
                if (bitmap != null) {
                    dialog = new braincollaboration.waper.utils.Dialog(getApplicationContext(), bitmap);

                    //mainBitmap = bitmap;
                    dialog.showDialog(Constants.DIALOG_SET_WALLPAPER_QUESTION);
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