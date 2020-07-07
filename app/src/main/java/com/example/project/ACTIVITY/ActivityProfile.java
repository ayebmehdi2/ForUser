package com.example.project.ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.project.DATAS.User;
import com.example.project.R;
import com.example.project.databinding.ProfileLayoutBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class ActivityProfile extends AppCompatActivity implements View.OnClickListener {

    ProfileLayoutBinding binding;

    private String nom, prenom, email, tele, date, photo = "Non disponible";
    private String adress = "1";

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private String uid;
    private ValueEventListener eventListener;

    String pathImage = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profile_layout);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", null);


         eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);
                if (us == null) return;
                if (us.getName() != null) nom = us.getName();
                if (us.getPhotoUrl() != null) photo = us.getPhotoUrl();
                if (us.getPrenom() != null) prenom = us.getPrenom();
                if (us.getEmail() != null) email = us.getEmail();
                if (us.getTele() != null) tele = us.getTele();
                if (us.getDateBirth() != null) date = us.getDateBirth();
                if (us.getLocation() != null) adress = us.getLocation();
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        reference.child("USERS").child(uid).addValueEventListener(eventListener);


        binding.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });


        binding.nom.setOnClickListener(this);
        binding.prenom.setOnClickListener(this);
        binding.email.setOnClickListener(this);
        binding.tele.setOnClickListener(this);
        binding.date.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reference.child("USERS").child(uid).child("location").setValue(binding.spinner1.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                reference.child("USERS").child(uid).child("location").setValue("Choisissez adress");
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            if (data.getData() == null) return;
            pathImage = data.getData().toString();
            binding.photo.setImageURI(data.getData());
            reference.child("USERS").child(uid).child("photoUrl").setValue(pathImage);
            reference.child("USERS").child(uid).removeEventListener(eventListener);
            reference.child("USERS").child(uid).addValueEventListener(eventListener);
        }
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public void updateUi(){
        binding.nameT.setText(nom);
        binding.prenomT.setText(prenom);
        binding.emailT.setText(email);
        binding.teleT.setText(tele);
        binding.dateT.setText(date);
        binding.spinner1.setSelection(getIndex(binding.spinner1, adress));

        try {
            Glide.with(ActivityProfile.this).load(photo).into(binding.photo);
        }catch (Exception e){ }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nom : {
                updateValeur("Nom", nom, "name");
                break;
            }
            case R.id.prenom : {
                updateValeur("Prenom", prenom, "prenom");
                break;
            }
            case R.id.email : {
                updateValeur("Email", email, "email");
                break;
            }
            case R.id.tele : {
                updateValeur("Telephone", tele, "tele");
                break;
            }

            case R.id.date : {
                updateValeur("Date de naissance", date, "dateBirth");
                break;
            }


            case R.id.logout : {
                AuthUI.getInstance().signOut(this);
                SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(ActivityProfile.this).edit();
                preferences.putString("uid", null);
                preferences.apply();

                FirebaseMessaging.getInstance().unsubscribeFromTopic(uid).addOnSuccessListener(aVoid ->
                        Toast.makeText(getApplicationContext(),"Unsubscribe Success",Toast.LENGTH_LONG).show());

                startActivity(new Intent(ActivityProfile.this, SplachScreen.class));
                break;
            }
        }
    }

    public void updateValeur(String key, String value, String keyFirebase){
        binding.up.setVisibility(View.VISIBLE);
        binding.updateTitle.setText(key);
        binding.edt.setText(value);
        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt = binding.edt.getText().toString();
                if (edt.length()>0 && !(edt.equals(value))){
                    reference.child("USERS").child(uid).child(keyFirebase).setValue(edt);
                    binding.up.setVisibility(View.GONE);
                    reference.child("USERS").child(uid).removeEventListener(eventListener);
                    reference.child("USERS").child(uid).addValueEventListener(eventListener);
                }else {
                    Toast.makeText(ActivityProfile.this, "Le valeur n'a pas changé", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.updateTitle.setText("");
                binding.edt.setText("");
                binding.up.setVisibility(View.GONE);
            }
        });
    }


}
