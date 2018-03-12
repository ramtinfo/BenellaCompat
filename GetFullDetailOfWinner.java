package com.example.ram.benellacompat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ram.benellacompat.pojo.WinnerPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetFullDetailOfWinner extends AppCompatActivity {
    TextView name, email, mobile, bids, street, city, pincode, state;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_full_detail_of_winner);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();
        bids = findViewById(R.id.bid);
        street = findViewById(R.id.street);
        city = findViewById(R.id.city);
        pincode = findViewById(R.id.pincode);
        state = findViewById(R.id.state);
        String id = getIntent().getStringExtra("id");
        final String bid = getIntent().getStringExtra("bid");
        FirebaseDatabase.getInstance().getReference().child("BID_DETAIL").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    WinnerPojo pojo=snapshot.getValue(WinnerPojo.class);
                    Log.d("KEY", snapshot.getKey() + " " + pojo.getEmail());
                    if (snapshot.getValue(WinnerPojo.class).getBid().equals(bid)) {
                        name.setText(pojo.getName()+"");
                        email.setText(pojo.getEmail()+"");
                        mobile.setText(pojo.getMobile()+"");
                        bids.setText(pojo.getBid()+"");
                        street.setText(pojo.getStreet()+"");
                        city.setText(pojo.getCity()+"");
                        pincode.setText(pojo.getPincode()+"");
                        state.setText(pojo.getState()+"");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }
}
