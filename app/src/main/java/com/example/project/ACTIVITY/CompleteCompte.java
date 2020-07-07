package com.example.project.ACTIVITY;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.databinding.CompleteCompteActivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class CompleteCompte extends AppCompatActivity {

    CompleteCompteActivityBinding binding;
    public static  FirebaseUser user = null;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String pathImage = null;
    private ProgressDialog PD;
    private String uid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.complete_compte_activity);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", "");

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        if (user == null) return;

        if (user.getDisplayName() != null){
            binding.name.setText(user.getDisplayName().split(" ")[0]);
            if (user.getDisplayName().split(" ").length > 1){
                binding.prenom.setText(user.getDisplayName().split(" ")[1]);
            }
        }

        if (user.getEmail() != null)binding.email.setText(user.getEmail());
        if (user.getPhoneNumber() != null)binding.tele.setText(user.getPhoneNumber());
        if (user.getPhotoUrl() != null){
            try {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(binding.photo);
            }catch (Exception ignored){ }
        }


        binding.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD.show();

                String nom = binding.name.getText().toString();
                if (!(nom.length() > 0)){
                    Toast.makeText(CompleteCompte.this, "nom !", Toast.LENGTH_LONG).show();
                    return;
                }

                String prenom = binding.prenom.getText().toString();
                if (!(prenom.length() > 0)){
                    Toast.makeText(CompleteCompte.this, "prenom !", Toast.LENGTH_LONG).show();
                    return;
                }

                reference.child("STATISTIQUE").child("comptes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer comptes = dataSnapshot.getValue(Integer.class);
                        if (comptes == null) return;
                        reference.child("STATISTIQUE").child("comptes").setValue(comptes + 1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });


                if (pathImage != null){
                    uploadImage(pathImage);
                }else {
                    mouve(user.getPhotoUrl().toString());
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ActivityAuth.class));
    }

    public void mouve(String uri){
        reference.child("USERS").child(uid).child("photoUrl").setValue(uri);
        uploadData();
        user = null;
        PD.dismiss();
        FirebaseMessaging.getInstance().subscribeToTopic(uid).addOnSuccessListener(aVoid ->
                Toast.makeText(getApplicationContext(),"Subscribe Success",Toast.LENGTH_LONG).show());
        Intent intent = new Intent(CompleteCompte.this, Home.class);
        startActivity(intent);
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
        }
    }

    public void uploadData(){

        String nom = binding.name.getText().toString();
        reference.child("USERS").child(uid).child("name").setValue(nom);
        String prenom = binding.prenom.getText().toString();
        reference.child("USERS").child(uid).child("prenom").setValue(prenom);

        String email = binding.email.getText().toString();
        String phone = binding.tele.getText().toString();
        String date = binding.dateBirth.getText().toString();
        String adress = binding.spinner1.getSelectedItem().toString();

        if (email.length()>0){
            reference.child("USERS").child(uid).child("email").setValue(email);
        }else {
            reference.child("USERS").child(uid).child("email").setValue("Non disponible");
        }

        if (phone.length()>0){
            reference.child("USERS").child(uid).child("tele").setValue(phone);
        }else {
            reference.child("USERS").child(uid).child("tele").setValue("Non disponible");
        }

        if (date.length()>0){
            reference.child("USERS").child(uid).child("dateBirth").setValue(date);
        } else {
            reference.child("USERS").child(uid).child("dateBirth").setValue("Non disponible");
        }

        if (adress.length()>0){
            reference.child("USERS").child(uid).child("location").setValue(adress);
        } else {
            reference.child("USERS").child(uid).child("location").setValue("Choisissez adress");
        }

        reference.child("USERS").child(uid).child("score").setValue(0);

        reference.child("USERS").child(uid).child("prod1").setValue(0);
        reference.child("USERS").child(uid).child("prod2").setValue(0);
        reference.child("USERS").child(uid).child("prod3").setValue(0);
        reference.child("USERS").child(uid).child("prod4").setValue(0);
        reference.child("USERS").child(uid).child("prod5").setValue(0);

        reference.child("USERS").child(uid).child("gifts").setValue(0);
        reference.child("USERS").child(uid).child("gift1").setValue(0);
        reference.child("USERS").child(uid).child("gift2").setValue(0);
        reference.child("USERS").child(uid).child("gift3").setValue(0);

        @SuppressLint("SimpleDateFormat") DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date result = new Date(System.currentTimeMillis());
        reference.child("USERS").child(uid).child("dateInsc").setValue(simple.format(result));
    }

    private void uploadImage(String filePath) {

        if (filePath == null) return;
        Bitmap bitmap = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(filePath));
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitmap == null){
            Toast.makeText(CompleteCompte.this, "Failed Try again", Toast.LENGTH_SHORT).show();
            return;
        };
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref =  FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mouve(Objects.requireNonNull(user.getPhotoUrl()).toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mouve(uri.toString());
                    }
                });

            }
        });


    }


}
