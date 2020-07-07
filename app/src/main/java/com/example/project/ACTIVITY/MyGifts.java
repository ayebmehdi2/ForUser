package com.example.project.ACTIVITY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project.ADAPTERS.AdapterGifts;
import com.example.project.DATAS.GIFT;
import com.example.project.R;
import com.example.project.databinding.MyGiftsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyGifts extends AppCompatActivity implements AdapterGifts.click {

    MyGiftsBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    String uid;
    private AdapterGifts giftsAdapter;
    private  ValueEventListener valueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.my_gifts);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", null);


         giftsAdapter = new AdapterGifts(this);
        binding.rec.setLayoutManager(new LinearLayoutManager(this));
        binding.rec.setHasFixedSize(true);
        binding.rec.setAdapter(giftsAdapter);
        ArrayList<GIFT> gifts = new ArrayList<>();
         valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                giftsAdapter.swapAdapter(null);
                gifts.clear();

                if (!(dataSnapshot.getChildrenCount() > 0)){
                    binding.rec.setVisibility(View.GONE);
                    binding.empty.setVisibility(View.VISIBLE);
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GIFT g = snapshot.getValue(GIFT.class);
                    gifts.add(g);
                }

                binding.empty.setVisibility(View.GONE);
                binding.rec.setVisibility(View.VISIBLE);
                giftsAdapter.swapAdapter(gifts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };



    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.child("USERS").child(uid).child("myGifts").addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.child("USERS").child(uid).child("myGifts").removeEventListener(valueEventListener);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void command(GIFT g) {
        Intent i = new Intent(this, CommandGiftMap.class);
        CommandGiftMap.gif = g;
        startActivity(i);
    }

    @Override
    public void viewQr(GIFT g) {
        Intent i = new Intent(this, ShowImage.class);
        i.putExtra("url", g.getQr_img());
        startActivity(i);
    }
}
