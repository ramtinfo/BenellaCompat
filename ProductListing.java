package com.example.ram.benellacompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ram.benellacompat.fragments.PrevFragment;
import com.example.ram.benellacompat.pojo.Bidmodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProductListing extends AppCompatActivity {
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    TextView start_text, stop_text, bid_amount,bid_var;
    ViewPager viewPager;
    TextView estimation;
    LinearLayout sliderDotspanel;
    private int dotscount;
    int start=0;
    int stop=0;
    Button confirm;
    private ImageView[] dots;
    ArrayList<String> previewimg = new ArrayList<>();
    DatabaseReference mref;
    PagerAdapter pagerAdapter;
    DatabaseReference mroot = FirebaseDatabase.getInstance().getReference();
     ArrayList<String> urilist = new ArrayList<>();
     ProgressDialog dialog;
     TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel =findViewById(R.id.SliderDots);
        bid_amount = findViewById(R.id.bid_amount);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loding data..");
        dialog.show();
        estimation=findViewById(R.id.estimation);
        confirm = findViewById(R.id.submit);
        pagerAdapter=new PagerAdapter(getSupportFragmentManager(),urilist);
        bid_var=findViewById(R.id.bid_var);
        viewPager.setOffscreenPageLimit(6);
        textView=findViewById(R.id.description);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(bid_var.getText().toString());
            }
        });
        start_text=findViewById(R.id.start);
        stop_text=findViewById(R.id.stop);

        mref = mroot.child("ADMIN_POST");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                Bidmodel bidmodel = dataSnapshot.getValue(Bidmodel.class);
                start=Integer.parseInt(bidmodel.getFrom());
                stop=Integer.parseInt(bidmodel.getTo());
                estimation.setText(bidmodel.getFrom()+" ~ "+bidmodel.getTo());
                textView.setText(bidmodel.getDescription());
                getImagesList(bidmodel.getImages());
                pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    dialog.dismiss();
            }
        });
        final AppCompatSeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float difference=stop-start;
                float unit=difference/100;
                bid_var.setText(""+ (int)(start + (progress *unit)));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        start_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value=Integer.valueOf(bid_var.getText().toString());
                if(value>start&&value<stop){
                    bid_var.setText((value-1)+"");
                }
            }
        });
        stop_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value=Integer.valueOf(bid_var.getText().toString());
                if(value>start&&value<stop){
                    bid_var.setText((value+1)+"");
                }
            }
        });
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("TAG",position+" item");
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                try {
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }
               catch (Exception e){
                    e.printStackTrace();
               }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void getImagesList(ArrayList<String> imglist) {
        sliderDotspanel.removeAllViews();
        urilist.clear();
        dotscount=0;
        dotscount = imglist.size();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(getApplicationContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        try {
            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < imglist.size(); i++) {
            storageReference.child("images").child(imglist.get(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(final Uri uri) {
                    urilist.add(uri.toString());
                    pagerAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "connection error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showAlert(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(s);
        builder.setMessage("Do You want to Bid?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
                    Intent i = new Intent(getApplicationContext(), MakePurchase.class);
                    i.putExtra("BID", s);
                    startActivity(i);
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
