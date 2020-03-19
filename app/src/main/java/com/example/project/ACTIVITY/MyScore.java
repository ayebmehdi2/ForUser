package com.example.project.ACTIVITY;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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

import com.example.project.DATAS.GIFT;
import com.example.project.DATAS.Statistique;
import com.example.project.DATAS.User;
import com.example.project.R;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MyScore extends AppCompatActivity {

    private MyScoreBinding binding;
    private  FirebaseDatabase database;
    private DatabaseReference reference;
    private String uid;

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


        reference.child("USERS").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);
                if (us == null) return;
                binding.score.setText(String.valueOf(us.getScore()));
                binding.gifts.setText(String.valueOf(us.getGifts()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });



        binding.gift1.setOnClickListener(v -> {
            reference.child("USERS").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) return;
                    Integer score = user.getScore();
                    Integer gift1 = user.getGift1();
                    Integer gifts = user.getGifts();
                    if (score >= 200){ score = score - 200;
                    gift1 = gift1 + 1;
                    gifts = gifts + 1;
                    reference.child("USERS").child(uid).child("score").setValue(score);
                    reference.child("USERS").child(uid).child("gift1").setValue(gift1);
                    reference.child("USERS").child(uid).child("gifts").setValue(gifts);
                    binding.score.setText(String.valueOf(score));
                    binding.gifts.setText(String.valueOf(gifts));
                        @SuppressLint("SimpleDateFormat") DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
                        Date result = new Date(System.currentTimeMillis());
                        String time = simple.format(result);
                        String qrCode = "gift1--" + new Random().nextInt(1000000000);
                        GIFT g = new GIFT(user.getName(), "Gift 1", "Non commande", qrCode, "", time);
                        new AsyncT().execute(g);
                    }else {
                        Toast.makeText(MyScore.this, "Le score insuffisant !", Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            reference.child("STATISTIQUE").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Statistique sta = dataSnapshot.getValue(Statistique.class);
                    if (sta == null) return;
                    Integer g1 = 0;
                    if (sta.getGift1() != null) g1 = sta.getGift1();
                    g1 = g1 + 1;
                    reference.child("STATISTIQUE").child("gift1").setValue(g1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        });

        binding.gift2.setOnClickListener(v -> {

            reference.child("USERS").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) return;
                    Integer score = user.getScore();
                    Integer gift2 = user.getGift2();
                    Integer gifts = user.getGifts();
                    if (score >= 150){
                        score = score - 150;
                        gift2 = gift2 + 1;
                        gifts = gifts + 1;
                        reference.child("USERS").child(uid).child("score").setValue(score);
                        reference.child("USERS").child(uid).child("gift2").setValue(gift2);
                        reference.child("USERS").child(uid).child("gifts").setValue(gifts);
                        binding.score.setText(String.valueOf(score));
                        binding.gifts.setText(String.valueOf(gifts));

                        @SuppressLint("SimpleDateFormat") DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
                        Date result = new Date(System.currentTimeMillis());
                        String time = simple.format(result);
                        String qrCode = "gift2--" + new Random().nextInt(1000000000);
                        GIFT g = new GIFT(user.getName(), "Gift 2", "Non commande", qrCode, "", time);
                        new AsyncT().execute(g);
                    }else {
                        Toast.makeText(MyScore.this, "Le score insuffisant !", Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            reference.child("STATISTIQUE").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Statistique sta = dataSnapshot.getValue(Statistique.class);
                    if (sta == null) return;
                    Integer g2 = 0;
                    if (sta.getGift2() != null) g2 = sta.getGift2();
                    g2 = g2 + 1;
                    reference.child("STATISTIQUE").child("gift2").setValue(g2);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });



        });

        binding.gift3.setOnClickListener(v -> {

            reference.child("USERS").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) return;
                    Integer score = user.getScore();
                    Integer gift3 = user.getGift3();
                    Integer gifts = user.getGifts();
                    if (score >= 100){
                        score = score - 100;
                        gift3 = gift3 + 1;
                        gifts = gifts + 1;
                        reference.child("USERS").child(uid).child("score").setValue(score);
                        reference.child("USERS").child(uid).child("gift3").setValue(gift3);
                        reference.child("USERS").child(uid).child("gifts").setValue(gifts);
                        binding.score.setText(String.valueOf(score));
                        binding.gifts.setText(String.valueOf(gifts));

                        @SuppressLint("SimpleDateFormat") DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
                        Date result = new Date(System.currentTimeMillis());
                        String time = simple.format(result);
                        String qrCode = "gift3--" + new Random().nextInt(1000000000);
                        GIFT g = new GIFT(user.getName(), "Gift 3", "Non commande", qrCode, "", time);
                        new AsyncT().execute(g);
                    }else {
                        Toast.makeText(MyScore.this, "Le score insuffisant !", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            reference.child("STATISTIQUE").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Statistique sta = dataSnapshot.getValue(Statistique.class);
                    if (sta == null) return;
                    Integer g3= 0;
                    if (sta.getGift3() != null) g3 = sta.getGift3();
                    g3 = g3 + 1;
                    reference.child("STATISTIQUE").child("gift3").setValue(g3);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

        });

    }

    @SuppressLint("StaticFieldLeak")
    class AsyncT extends AsyncTask<GIFT, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.l.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(GIFT... s) {
            Bitmap bmp = null;
            GIFT gift = s[0];
            if(!(gift.getQr_code().length() > 0)) return null;
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(gift.getQr_code() , BarcodeFormat.QR_CODE, 200, 200);
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

                uploadBitmap(bmp, gift);
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void no) {
                binding.l.setVisibility(View.GONE);
                Toast.makeText(MyScore.this, "Fin", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadBitmap(final Bitmap bitmap, final GIFT gift) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("GIFTS/"+ gift.getQr_code());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyScore.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        gift.setQr_img(uri.toString());
                        reference.child("USERS").child(uid).child("myGifts").child(gift.getQr_code()).setValue(gift);
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
