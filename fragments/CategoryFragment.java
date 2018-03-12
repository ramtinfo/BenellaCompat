package com.example.ram.benellacompat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ram.benellacompat.R;
import com.example.ram.benellacompat.adapters.MostViewedAdapter;
import com.example.ram.benellacompat.adapters.ProductAdapter;
import com.example.ram.benellacompat.pojo.ProductHeaderPojo;
import com.example.ram.benellacompat.pojo.RetriveData;
import com.example.ram.benellacompat.pojo.RetrivePojo;
import com.example.ram.benellacompat.static_classes.RetriveProduct;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    RecyclerView recyclerView;
    ProductAdapter adapter;
    ArrayList<RetriveData> pojos = new ArrayList<>();
    TextView textView,nodata;
    ContentLoadingProgressBar progressBar;
    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        textView=view.findViewById(R.id.texttile);
        nodata=view.findViewById(R.id.nodata);
        progressBar=view.findViewById(R.id.progress_bar);
        progressBar.show();
        nodata.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        adapter = new ProductAdapter(pojos);
        recyclerView.setAdapter(adapter);
        Bundle bundle=getArguments();
        if(bundle!=null){
            Log.d("CAT","bundle");
            RetriveProduct.getInstance(view.getContext()).getData(bundle.getString("id"));
            RetriveProduct.setProductRetriveListner(new RetriveProduct.GetProduct() {
                @Override
                public void onGetProduct(ArrayList<RetrivePojo> retrivePojos) {
                    progressBar.hide();
                    nodata.setVisibility(View.GONE);
                    for (RetrivePojo pojo : retrivePojos) {
                        Log.d("CAT",pojo.getStatus()+" DD "+pojos.size());
                        if (pojo.getStatus().equals("ok")) {
                            pojos.clear();
                            pojos=pojo.getData();
                            adapter = new ProductAdapter(pojos);
                            recyclerView.setAdapter(adapter);
                            recyclerView.scrollToPosition(0);
                            adapter.notifyDataSetChanged();
                        }else {
                            // failed
                            nodata.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            textView.setText(bundle.getString("CATEGORY"));
        }else{
            Log.d("CAT","null");
        }

        return view;
    }

}
