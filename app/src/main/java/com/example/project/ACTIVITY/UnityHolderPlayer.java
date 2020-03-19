package com.example.project.ACTIVITY;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.DefaultCompany.Avatar_AR.UnityPlayerActivity;
import com.example.project.R;

public class UnityHolderPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Intent i = new Intent(this, UnityPlayerActivity.class);
        startActivity(i);
    }
}
