package com.exercise.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_LINK = "extra_link";
    public static final String EXTRA_IMAGE_TITLE = "extra_title";

    private String link;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        link = getIntent().getStringExtra(EXTRA_IMAGE_LINK);
        imageView = (ImageView) findViewById(R.id.image);

        String title = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
        if (!TextUtils.isEmpty(title)) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Glide.with(getApplicationContext())
                .load(link)
                .dontAnimate()
                .into(imageView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Glide.clear(imageView);
    }
}
