package com.example.ram.benellacompat;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ram.benellacompat.adapters.EpandedeListAdapter;
import com.example.ram.benellacompat.fragments.CategoryFragment;
import com.example.ram.benellacompat.pojo.CategoryMainPojo;
import com.example.ram.benellacompat.pojo.CategoryPojo;
import com.example.ram.benellacompat.pojo.ProductHeaderPojo;
import com.example.ram.benellacompat.pojo.RetriveData;
import com.example.ram.benellacompat.pojo.RetrivePojo;
import com.example.ram.benellacompat.pojo.SubCategoryData;
import com.example.ram.benellacompat.pojo.SubCategoryPojo;
import com.example.ram.benellacompat.static_classes.CategoryData;
import com.example.ram.benellacompat.static_classes.MostViewedProduct;
import com.example.ram.benellacompat.static_classes.RetriveProduct;
import com.example.ram.benellacompat.static_classes.SubData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        SubData.SubDataRetrive, CategoryData.CategoryRetrive {

    private FirebaseAuth mAuth;
    ExpandableListView expandedlistView;
    ImageView hamburger;
    HomeFragment homeFragment;
    FragmentTransaction fragmentTransaction;
    ArrayList<String> headerlist = new ArrayList<>();
    HashMap<String, ArrayList<SubCategoryData>> childdata = new HashMap<>();
    EpandedeListAdapter expandableListAdapter;
    ArrayList<SubCategoryData> sample = new ArrayList<>();
    Bundle bundle = new Bundle();
    static SetRefreshLayout listner;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        homeFragment = new HomeFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, homeFragment);
        fragmentTransaction.commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hamburger = toolbar.findViewById(R.id.hamburger);
        hamburger.setOnClickListener(this);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        headerlist.add("Indian Wear");
//        headerlist.add("Bottom Wear");
//        headerlist.add("Western Wear");

        CategoryData.getInstance(this).getData();

        SubData.setSubDataRetrival(this);
        CategoryData.setCategory(this);
//        sample.add("Kurta Kurtis");
//        sample.add("Jegging");sample.add("Legging");sample.add("Plazzo");sample.add("Women Wear");
//        sample.add("Women Capris");sample.add("Top Shirt");sample.add("Skirt");sample.add("Women tees");
//        childdata.put(headerlist.get(0), sample); // Header, Child data
//        childdata.put(headerlist.get(1), sample);
//        childdata.put(headerlist.get(2), sample);
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView home=navigationView.findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,homeFragment);
                fragmentTransaction.commit();
            }
        });
        expandedlistView = navigationView.findViewById(R.id.expanded_menu);
        expandableListAdapter = new EpandedeListAdapter(this, headerlist, childdata);
        expandedlistView.setAdapter(expandableListAdapter);
        expandableListAdapter.setOnRecordSelect(new EpandedeListAdapter.OnRecordSelect() {
            @Override
            public void SetrecordSelect(String id,String tiname) {
                Toast.makeText(MainActivity.this, "id : " + id, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("CATEGORY",tiname);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ragmentTransaction = getSupportFragmentManager().beginTransaction();
                ragmentTransaction.replace(R.id.fragment_container, categoryFragment);
                ragmentTransaction.commit();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

    }
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Log.d("CHECK", "Using.." + currentUser.getEmail() + " : " + currentUser.getUid());
            if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
                getSupportActionBar().setTitle(currentUser.getEmail());
            } else {
                getSupportActionBar().setTitle("Benella");
            }
        } else {
            //sign in user annonmsly
            Log.d("CHECK", "Logging");
            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //sign in successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "welcome : " + user.getUid(), Toast.LENGTH_SHORT).show();

                        updateUI(user);
                    } else {
                        //sign in failed get  exception
                        Log.d("Sign in Error", task.getException() + " .");
                        //update ui
                        updateUI(null);
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.hamburger:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onSubDataRetrival(String header, ArrayList<SubCategoryPojo> subCategoryPojos) {
        sample.clear();
        for (SubCategoryPojo pojo : subCategoryPojos) {
            if (pojo.getStatus().equals("ok")) {
//                for (SubCategoryData data:pojo.getData()){
//                    sample.add(data.getTitle());
//                    Log.d("SUBTAG",data.getTitle()+data.getId()+" why"); // okay
//                }
                sample.addAll(pojo.getData());
                if (childdata.get(header) == null) {
                    childdata.put(header, new ArrayList<SubCategoryData>());
                }
                childdata.get(header).addAll(sample);
                expandableListAdapter = new EpandedeListAdapter(this, headerlist, childdata);
                expandedlistView.setAdapter(expandableListAdapter);
                expandableListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCategoryRetrive(ArrayList<CategoryMainPojo> categoryMainPojoArrayList) {
        for (CategoryMainPojo pojo : categoryMainPojoArrayList) {
            if (pojo.getStatus().equals("ok")) {
                for (CategoryPojo data : pojo.getData()) {
                    headerlist.add(data.getTitle());
                    SubData.getInstance(this).getSubCategory(data.getTitle(), data.getId());
                    Log.d("CATTAG", data.getTitle() + data.getId() + data.getImage());  //ok
                }
            }
        }
    }

    public static void setCallbackListner(SetRefreshLayout refreshlistner) {
        listner = refreshlistner;
    }

    public interface SetRefreshLayout {
        public void onCallback();
    }
}
