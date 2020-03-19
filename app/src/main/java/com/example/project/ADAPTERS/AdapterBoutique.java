package com.example.project.ADAPTERS;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.DATAS.BoutiqueUser;
import com.example.project.R;

import java.util.ArrayList;

public class AdapterBoutique  extends RecyclerView.Adapter<AdapterBoutique.holder> {


    private int selectedPos = RecyclerView.NO_POSITION;
    private ArrayList<BoutiqueUser> data = null;
    public void swapAdapter(ArrayList<BoutiqueUser> boutiques){
        if (data == boutiques) return;
        data = boutiques;
        this.notifyDataSetChanged();
    }

    public interface click{
        void slect(BoutiqueUser b);
    }

    private final click c;

    public AdapterBoutique(click cc){
        c = cc;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.boutique_item, parent, false));
    }

    private int index = -1;

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        BoutiqueUser boutique = data.get(position);
        if (boutique == null) return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.slect(data.get(position));
                index=position;
                notifyDataSetChanged();
            }
        });
        if(index==position){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFD740"));
        }else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.name.setText(boutique.getDes());

    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder{

        TextView name;
        public holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.boutique_name);
        }
    }

}
