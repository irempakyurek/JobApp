package com.example.jobapplication;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView adayAdi;
    public TextView adaySoyadi;
    public TextView adayCepTel;
    public TextView adayMezunOkul;
    public TextView optionsButton;


    public AdayViewHolder(@NonNull View itemView) {
        super(itemView);

        adayAdi = itemView.findViewById(R.id.tvAdayAdi);
        adaySoyadi = itemView.findViewById(R.id.tvAdaySoyadi);
        adayCepTel = itemView.findViewById(R.id.tvAdayCepTel);
        adayMezunOkul = itemView.findViewById(R.id.tvAdayMezunOkul);
        optionsButton = itemView.findViewById(R.id.tvOptions);

    }

    @Override
    public void onClick(View v) {

    }
}
