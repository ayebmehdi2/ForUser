package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.DefaultCompany.ARKIDS.UnityPlayerActivity;

public class UnityHolderPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Intent i = new Intent(this, UnityPlayerActivity.class);
        startActivity(i);

    }
}
