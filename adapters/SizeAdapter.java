package com.example.ram.benellacompat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ram.benellacompat.R;

import java.util.ArrayList;

/**
 * Created by ram on 3/9/18.
 */

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder>{
    ArrayList<String> sizelist=new ArrayList<>();
    public SizeAdapter(ArrayList<String> list){
        sizelist.addAll(list);
    }
    @Override
    public SizeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.size_row,parent,false);
        SizeViewHolder viewHolder=new SizeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SizeViewHolder holder, int position) {
        holder.size.setText(sizelist.get(position)+"");
    }

    @Override
    public int getItemCount() {
        return sizelist.size();
    }

    public static class SizeViewHolder extends RecyclerView.ViewHolder {
        TextView size;
        public SizeViewHolder(View itemView) {
            super(itemView);
            size=itemView.findViewById(R.id.size_text);
        }
    }
}
