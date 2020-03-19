package com.example.project.ACTIVITY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project.ADAPTERS.AdapterBoutique;
import com.example.project.DATAS.BoutiqueUser;
import com.example.project.DATAS.GIFT;
import com.example.project.R;
import com.example.project.databinding.CommandGiftsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommandGift extends AppCompatActivity implements AdapterBoutique.click {

    CommandGiftsBinding binding;
    AdapterBoutique boutique;
    FirebaseDatabase database;
    DatabaseReference reference;
    private String uid;
    ArrayList<BoutiqueUser> boutiqueUsers = new ArrayList<>();

    BoutiqueUser bou;
    public static GIFT gif;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.command_gifts);

        if (gif == null) {
            startActivity(new Intent(this, MyGifts.class));
        }



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", null);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        boutique = new AdapterBoutique(this);
        binding.boutiquis.setHasFixedSize(true);
        binding.boutiquis.setLayoutManager(new LinearLayoutManager(this));
        binding.boutiquis.setAdapter(boutique);


        binding.etat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boutique.swapAdapter(null);
                if (binding.etat.getItemAtPosition(position).toString().equals("Choisissez etat")){
                    binding.boutiquis.setVisibility(View.GONE);
                    binding.empty.setVisibility(View.VISIBLE);
                }else {
                    reference.child("BOUTIQUES").child(binding.etat.getItemAtPosition(position).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (!(dataSnapshot.getChildrenCount() > 0)){
                                binding.boutiquis.setVisibility(View.GONE);
                                binding.empty.setVisibility(View.VISIBLE);
                                return;
                            }

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                BoutiqueUser user = snapshot.getValue(BoutiqueUser.class);
                                if (user == null) continue;
                                boutiqueUsers.add(user);
                            }


                            binding.empty.setVisibility(View.GONE);
                            binding.boutiquis.setVisibility(View.VISIBLE);
                            boutique.swapAdapter(boutiqueUsers);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bou == null){
                    Toast.makeText(CommandGift.this, "Choose boutique", Toast.LENGTH_SHORT).show();
                    return;
                }
                reference.child("BOUTIQUES").child(bou.getWilaya()).child(bou.getUid()).child("gifts").child(gif.getQr_code()).setValue(gif);
                reference.child("USERS").child(uid).child("myGifts").child(gif.getQr_code()).child("status").setValue("En attente");
                Toast.makeText(CommandGift.this, "Gift command seccus", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CommandGift.this, Home.class));
            }
        });

    }

    @Override
    public void slect(BoutiqueUser b) {
        bou = b;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
