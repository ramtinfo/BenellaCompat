package com.example.ram.benellacompat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ram.benellacompat.GetFullDetailOfWinner;
import com.example.ram.benellacompat.R;
import com.example.ram.benellacompat.pojo.WinnerPojo;
import com.example.ram.benellacompat.pojo.WinnerUID;

import java.util.ArrayList;

/**
 * Created by ram on 2/21/18.
 */

public class Winneradapter extends RecyclerView.Adapter<Winneradapter.WinnerViewHolder> {
    static ArrayList<WinnerUID> winnerPojos = new ArrayList<>();
   static Context context;
    public  Winneradapter(){

    }
    public void AddWinnerPojo(ArrayList<WinnerUID> pojos){
        winnerPojos.addAll(pojos);
    }
    @Override
    public WinnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.winner_layout, parent, false);
        context=view.getContext();
        WinnerViewHolder viewHolder = new WinnerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WinnerViewHolder holder, int position) {
        holder.name.setText(winnerPojos.get(position).getUid() + "");
        holder.email.setText(winnerPojos.get(position).getBid() + "");
//        holder.mobile_no.setText(winnerPojos.get(position).getMobile() + "");
//        holder.bid.setText(winnerPojos.get(position).getBid() + "");
//        holder.address.setText(winnerPojos.get(position).getStreet()
//                + " " + winnerPojos.get(position).getCity() + " " +
//                winnerPojos.get(position).getState()
//                + " " + winnerPojos.get(position).getPincode());
    }

    @Override
    public int getItemCount() {
        return winnerPojos.size();
    }

    public static class WinnerViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, email, bid, mobile_no;
        RelativeLayout linearLayout;
        public WinnerViewHolder(View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.linearLayout);
            name = itemView.findViewById(R.id.name);
//            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.email);
//            bid = itemView.findViewById(R.id.bid);
//            mobile_no = itemView.findViewById(R.id.mobile);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,GetFullDetailOfWinner.class);
                    intent.putExtra("id",winnerPojos.get(getAdapterPosition()).getUid()+"");
                    intent.putExtra("bid",winnerPojos.get(getAdapterPosition()).getBid()+"");
                    context.startActivity(intent);
                }
            });
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,GetFullDetailOfWinner.class);
                    intent.putExtra("id",winnerPojos.get(getAdapterPosition()).getUid()+"");
                    intent.putExtra("bid",winnerPojos.get(getAdapterPosition()).getBid()+"");
                    context.startActivity(intent);
                }
            });
        }
    }
}
