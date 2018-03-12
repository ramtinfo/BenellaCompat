package com.example.ram.benellacompat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ram.benellacompat.HomeFragment;
import com.example.ram.benellacompat.Product_list;
import com.example.ram.benellacompat.R;
import com.example.ram.benellacompat.pojo.ProductHeaderPojo;
import com.example.ram.benellacompat.static_classes.MostViewedProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ram on 2/17/18.
 */

public class MostViewedAdapter extends RecyclerView.Adapter<MostViewedAdapter.MostViewedViewHolder> {
   static  ArrayList<ProductHeaderPojo> productHeaderPojoArrayList=new ArrayList<>();
    static Context context;
    public MostViewedAdapter(ArrayList<ProductHeaderPojo> arrayList){
        if(productHeaderPojoArrayList.size()!=0){
            productHeaderPojoArrayList.clear();
        }
        productHeaderPojoArrayList.addAll(arrayList);
    }
    @Override
    public MostViewedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_prev_row,parent,false);
        context=view.getContext();
        MostViewedViewHolder vieweholder=new MostViewedViewHolder(view);
        return vieweholder;
    }

    @Override
    public void onBindViewHolder(MostViewedViewHolder holder, int position) {
        Picasso.with(context).
                load("http://www.benella.in/uploads/product/"+productHeaderPojoArrayList.get(position).getImage())
                .resize(400,600).into(holder.dress);
        holder.title.setText(productHeaderPojoArrayList.get(position).getName());
        holder.price.setText("Rs "+productHeaderPojoArrayList.get(position).getSpacel_price());
        holder.mrp.setText("Rs "+productHeaderPojoArrayList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return productHeaderPojoArrayList.size();
    }

    public static class MostViewedViewHolder extends RecyclerView.ViewHolder{
        ImageView dress;
        TextView title;
        TextView price;
        TextView mrp;
        public MostViewedViewHolder(final View itemView) {
            super(itemView);
            dress=itemView.findViewById(R.id.img);
            title=itemView.findViewById(R.id.title);
            price=itemView.findViewById(R.id.price5);
            mrp=itemView.findViewById(R.id.mrp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Product_list.class);
                    intent.putExtra("id",productHeaderPojoArrayList.get(getAdapterPosition()).getId());
                    Log.w("TAG1"," lo "+productHeaderPojoArrayList.get(getAdapterPosition()).getId());
//                    Pair<View, String> p1 = Pair.create((View) dress, "img");
//                Pair<View, String> p3 = Pair.create((View) price, "price");
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(HomeFragment.this, p1, p3);
                    context.startActivity(intent);
                }
            });
        }
    }
}
