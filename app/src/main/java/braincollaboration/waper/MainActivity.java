package braincollaboration.waper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "https://source.unsplash.com/random";
    private ImageView mainContentImageView;
    private FloatingActionButton fab;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureWidgets();
    }

    private void configureWidgets() {
        mainContentImageView = (ImageView) findViewById(R.id.main_image);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mProgressBar = (ProgressBar) findViewById(R.id.google_progress);
        mProgressBar.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(this).build());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                getNewImage();
            }
        });


    }

    private void getNewImage() {
        Picasso.with(WaperApp.getCurrentActivity()).load(IMAGE_URL).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(mainContentImageView, new Callback() {
            @Override
            public void onSuccess() {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        });
    }
}
