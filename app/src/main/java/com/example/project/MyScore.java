package com.example.project;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.project.databinding.MyScoreBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.Random;

public class MyScore extends AppCompatActivity {

    MyScoreBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    String uid;
    User user;
    Statistique statistique;
    private String  type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.my_score);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", null);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);
                if (us == null) return;
                user = us;
                binding.score.setText(String.valueOf(user.getScore()));
                binding.gifts.setText(String.valueOf(user.getGifts()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        reference.child("USERS").child(uid).addListenerForSingleValueEvent(valueEventListener);

        reference.child("STATISTIQUE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Statistique sta = dataSnapshot.getValue(Statistique.class);
                if (user == null) return;
                statistique = sta;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        binding.gift1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Integer score = user.getScore();
                        Integer gift1 = user.getGift1();
                        Integer gifts = user.getGifts();
                        if (score >= 200){
                            Integer g1 = statistique.getGift1() + 1;
                            reference.child("STATISTIQUE").child("gift1").setValue(g1);
                            score = score - 200;
                            gift1 = gift1 + 1;
                            gifts = gifts + 1;
                            reference.child("USERS").child(uid).child("score").setValue(score);
                            reference.child("USERS").child(uid).child("gift1").setValue(gift1);
                            reference.child("USERS").child(uid).child("gifts").setValue(gifts);
                            binding.score.setText(String.valueOf(score));
                            binding.gifts.setText(String.valueOf(gifts));


                            type = "Gift 1";
                            new AsyncT().execute("gift1--" + new Random().nextInt(1000000000));
                        }else {
                            Toast.makeText(MyScore.this, "Le score insuffisant !", Toast.LENGTH_LONG).show();
                        }
            }
        });



        binding.gift2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                        if (user == null) return;
                        Integer score = user.getScore();
                        Integer gift2 = user.getGift2();
                        Integer gifts = user.getGifts();
                        if (score >= 150){
                            Integer g2 = statistique.getGift1() + 1;
                            reference.child("STATISTIQUE").child("gift2").setValue(g2);
                            score = score - 150;
                            gift2 = gift2 + 1;
                            gifts = gifts + 1;
                            reference.child("USERS").child(uid).child("score").setValue(score);
                            reference.child("USERS").child(uid).child("gift2").setValue(gift2);
                            reference.child("USERS").child(uid).child("gifts").setValue(gifts);
                            binding.score.setText(String.valueOf(score));
                            binding.gifts.setText(String.valueOf(gifts));

                            type = "Gift 2";
                            new AsyncT().execute("gift2--" + new Random().nextInt(1000000000));
                        }else {
                            Toast.makeText(MyScore.this, "Le score insuffisant !", Toast.LENGTH_LONG).show();
                        }

            }
        });

        binding.gift3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if (user == null) return;
                        Integer score = user.getScore();
                        Integer gift3 = user.getGift3();
                        Integer gifts = user.getGifts();
                        if (score >= 100){
                            Integer g3 = statistique.getGift1() + 1;
                            reference.child("STATISTIQUE").child("gift3").setValue(g3);
                            score = score - 100;
                            gift3 = gift3 + 1;
                            gifts = gifts + 1;
                            reference.child("USERS").child(uid).child("score").setValue(score);
                            reference.child("USERS").child(uid).child("gift3").setValue(gift3);
                            reference.child("USERS").child(uid).child("gifts").setValue(gifts);
                            binding.score.setText(String.valueOf(score));
                            binding.gifts.setText(String.valueOf(gifts));

                            type = "Gift 3";
                            new AsyncT().execute("gift3--" + new Random().nextInt(1000000000));
                        }else {
                            Toast.makeText(MyScore.this, "Le score insuffisant !", Toast.LENGTH_LONG).show();
                        }
                    }
            });

    }

    @SuppressLint("StaticFieldLeak")
    class AsyncT extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.l.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... s) {
            Bitmap bmp = null;
            String content = s[0];
            if(!(content.length() > 0)) return null;
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content , BarcodeFormat.QR_CODE, 200, 200);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

            } catch (WriterException e) {
                e.printStackTrace();
            }

            if (bmp != null){

                uploadBitmap(bmp, content);
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void no) {
                binding.l.setVisibility(View.GONE);
                Toast.makeText(MyScore.this, "Fin", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadBitmap(final Bitmap bitmap, final String imageName) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("GIFTS/"+ imageName);
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyScore.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.child("USERS").child(uid).child("myGifts").child(imageName).setValue(new GIFT(user.getName(),
                        type, "Non commande", imageName, Objects.requireNonNull(taskSnapshot.getUploadSessionUri()).toString()));
                    type = "";
            }
        });


    }
}
