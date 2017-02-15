package braincollaboration.waper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "https://source.unsplash.com/random";
    private ImageView mainContentImageView;
    private FloatingActionButton fab;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureWidgets();
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
            progressBar.setVisibility(ProgressBar.VISIBLE); //показываем шкалу прогресса
            fab.setVisibility(View.INVISIBLE); // прячем кнопку
        }

        protected Bitmap doInBackground(Void... params) {
            Bitmap mImage = null;
            try {
                InputStream in = new java.net.URL(IMAGE_URL).openStream();
                mImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Image transfer error", e.getMessage());
            }

            return mImage;
        }

        protected void onPostExecute(Bitmap result) {
            progressBar.setVisibility(ProgressBar.INVISIBLE); //прячем шкалу прогресса

            if (result!=null) mainContentImageView.setImageBitmap(result);
            else Toast.makeText(getApplicationContext(), "Ошибка загрузки изображения", Toast.LENGTH_LONG).show();

            fab.setVisibility(View.VISIBLE);  // показываем кнопку

        }
    }
}
