package com.example.project.ACTIVITY;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.project.R;

public class ShowImage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image);

        String url = getIntent().getStringExtra("url");

        if (url == null) return;

        ImageView view = findViewById(R.id.img_show);

        Glide.with(this).load(url).into(view);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}