package com.example.ram.benellacompat;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.ram.benellacompat.adapters.MostViewedAdapter;
import com.example.ram.benellacompat.pojo.ProductHeaderPojo;
import com.example.ram.benellacompat.static_classes.MostViewedProduct;

import java.util.ArrayList;
import java.util.Collection;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, MostViewedProduct.GetHeader {
    //    CardView cardView;
    ImageView bid_image;
    Handler mHandler = new Handler();
    Context context;
    RecyclerView recyclerView, recyclerView1, recyclerView2;
    MostViewedAdapter adapter, adapter1, adapter2;
    ArrayList<ProductHeaderPojo> arrayList = new ArrayList<>();
    ArrayList<ProductHeaderPojo> arrayList1 = new ArrayList<>();
    ArrayList<ProductHeaderPojo> arrayList2 = new ArrayList<>();
    Bundle bundle = new Bundle();
    View view1;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        bundle = getArguments();
        context = view.getContext();
        view1 = view;
//        RefreshLayout();
        FrameLayout search = view.findViewById(R.id.search);
        final Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(view.getContext(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        MostViewedProduct.getInstance(view.getContext()).getData("random");
        MostViewedProduct.getInstance(view.getContext()).getData("most");
        MostViewedProduct.getInstance(view.getContext()).getData("latest");
        MostViewedProduct.setHeaderListner(this);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SearchActivity.class);
                startActivity(intent, bundle);
            }
        });
        recyclerView = view1.findViewById(R.id.recycler_view);
        recyclerView1 = view1.findViewById(R.id.recycler_view1);
        recyclerView2 = view1.findViewById(R.id.recycler_view2);
        recyclerView1.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView2.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);
//        MainActivity.setCallbackListner(new MainActivity.SetRefreshLayout() {
//            @Override
//            public void onCallback() {
//                RefreshLayout();
//            }
//        });
//        if(bundle!=null){
//            if(bundle.getParcelableArrayList("MOST")!=null){
//                arrayList=getArguments().getParcelableArrayList("MOST");
//            }
//            if(bundle.getParcelableArrayList("LATEST")!=null){
//                arrayList1=bundle.getParcelableArrayList("LATEST");
//            }
//            if(bundle.getParcelableArrayList("RANDOM")!=null){
//                arrayList2=bundle.getParcelableArrayList("RANDOM");
//            }
//        }

//        cardView = view.findViewById(R.id.card);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Product_list.class);
//                Pair<View, String> p1 = Pair.create((View) view.findViewById(R.id.img), "img");
//                Pair<View, String> p3 = Pair.create((View) view.findViewById(R.id.price), "price");
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(HomeFragment.this.getActivity(), p1, p3);
//                startActivity(intent, options.toBundle());
//            }
//        });
        bid_image = view.findViewById(R.id.bid_image);
        bid_image.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bid_image:
                bid_image.setScaleX(.99f);
                bid_image.setScaleY(.99f);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bid_image.setScaleX(1);
                        bid_image.setScaleY(1);
                    }
                }, 100);
//                startActivity(new Intent(context, ProductListing.class));
                startActivity(new Intent(context, Product_list.class));
                break;
        }
    }

    @Override
    public void onGettingHeader(ArrayList<ProductHeaderPojo> pojoArrayList) {
//        homeFragment.RefreshLayout();
//        for (ProductHeaderPojo pojo : pojoArrayList) {
//            Log.d("TAG", pojo.getName() + pojo.getId()); //okay
//        }
        arrayList2.clear();
        arrayList2 = pojoArrayList;
        adapter2 = new MostViewedAdapter(arrayList2);
        recyclerView2.setAdapter(adapter2);
    }

    @Override
    public void onGettingRandom(ArrayList<ProductHeaderPojo> pojoArrayList) {
//        homeFragment.RefreshLayout();
        arrayList1.clear();
        arrayList1 = pojoArrayList;
        adapter1 = new MostViewedAdapter(arrayList1);
        recyclerView1.setAdapter(adapter1);
//        for (ProductHeaderPojo pojo : pojoArrayList) {
//            Log.d("RTAG", pojo.getName() + pojo.getId()); //okay
//        }
    }

    @Override
    public void onGettingMost(ArrayList<ProductHeaderPojo> pojos) {
        arrayList.clear();
        arrayList = pojos;
        adapter = new MostViewedAdapter(arrayList);
        recyclerView.setAdapter(adapter);
//        homeFragment.RefreshLayout();
//        fragmentTransaction.commit();
//        for (ProductHeaderPojo pojo : pojos) {
//            Log.d("MTAG", pojo.getName() + pojo.getId()); //okay
//        }
    }

}
