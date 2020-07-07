package com.example.project.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.DATAS.GIFT;
import com.example.project.R;

import java.util.ArrayList;

public class AdapterGifts extends RecyclerView.Adapter<AdapterGifts.holder> {


    private ArrayList<GIFT> data = null;
    public void swapAdapter(ArrayList<GIFT> gifts){
        if (data == gifts) return;
        data = gifts;
        this.notifyDataSetChanged();
    }

    public interface click{
        void command(GIFT g);
        void viewQr(GIFT g);
    }

    private final click c;

    public AdapterGifts(click cc){
        c = cc;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        GIFT gift = data.get(position);
        if (gift == null) return;

        holder.type.setText(gift.getType());
        holder.status.setText(gift.getStatus());
        holder.time.setText(gift.getTime());

        if (gift.getStatus().equals("Non commande")){
            holder.command.setVisibility(View.VISIBLE);
            holder.qr.setVisibility(View.GONE);
        } else if (gift.getStatus().equals("Recu")){
            holder.command.setVisibility(View.GONE);
            holder.qr.setVisibility(View.GONE);
        } else {
            holder.command.setVisibility(View.GONE);
            holder.qr.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder{

        TextView type, status, time;
        ImageView qr;
        Button command;
        public holder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            qr = itemView.findViewById(R.id.qr);
            command = itemView.findViewById(R.id.commond);

            command.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c.command(data.get(getAdapterPosition()));
                }
            });

            qr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c.viewQr(data.get(getAdapterPosition()));
                }
            });
        }
    }

}
