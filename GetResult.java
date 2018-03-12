package com.example.ram.benellacompat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ram.benellacompat.adapters.Winneradapter;
import com.example.ram.benellacompat.pojo.WinnerUID;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class GetResult extends AppCompatActivity {
    RecyclerView recyclerView;
    Winneradapter winneradapter;
    DatabaseReference mroot = FirebaseDatabase.getInstance().getReference();
    ArrayList<WinnerUID> scores = new ArrayList<>();
    ProgressDialog dialog;
    TextView winner;
    int price = -1;
    String winnerarray[] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_result);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        winneradapter = new Winneradapter();
        winner = findViewById(R.id.title);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Retriving People Biding Amount..");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
        recyclerView.setAdapter(winneradapter);
        mroot = mroot.child("scorer");
        DatabaseReference adminbid = FirebaseDatabase.getInstance().getReference().child("ADMIN_POST").child("mrp");
        adminbid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    price = Integer.parseInt(dataSnapshot.getValue(String.class));
                Log.d("PRice", price + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mroot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scores.clear();
                dialog.dismiss();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                        WinnerUID obj = new WinnerUID();
                        obj.setBid(snapshot.getValue(String.class));
                        obj.setUid(dataSnapshot1.getKey() + "");
                        scores.add(obj);
                    }
                }
//                Collections.sort(scores);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
        winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WinnerUID> tempPojo=new ArrayList<>();
                if (!scores.isEmpty()) {
                    if (price >= 0) {
                        long length = scores.size();
//                        getTopThreeScrorerThroughBinarySearch(scores, 0, scores.size(), price);
                        for (WinnerUID uiud : scores) {
                            int bidobj = Integer.parseInt(uiud.getBid());
                            int difference = 0;
                            if (price > bidobj) {
                                difference = price - Integer.parseInt(uiud.getBid());
                            } else {
                                difference = Integer.parseInt(uiud.getBid()) - price;
                            }
                            WinnerUID temp=new WinnerUID();
                            temp.setUid(uiud.getUid());
                            temp.setBid(uiud.getBid());
                            temp.setDifference(difference);
                            tempPojo.add(temp);
                        }
                        Collections.sort(tempPojo);
                        winneradapter.AddWinnerPojo(tempPojo);
                        winneradapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(GetResult.this, "Plz wait we are retriving actual price...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GetResult.this, "No result found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private ArrayList<WinnerUID> getTopThreeScrorerThroughBinarySearch(ArrayList<WinnerUID> pojotosort, int low, int high, int price) {
//        int array[]=new int[3];
//        while (low < high) {
//            int mid = (low + high) / 2;
//            Log.w("Process", low + " : " + mid + " : " + high);
//            if (Integer.parseInt(pojotosort.get(mid).getBid()) > price) {
//                high = mid;
//                array[2]=Integer.parseInt(pojotosort.get(mid).getBid());
//            } else if (Integer.parseInt(pojotosort.get(mid).getBid()) == price) {
//                Log.d("TAG", "found");
//                array[1]=price;
//                break;
//            } else {
//                array[0]=Integer.parseInt(pojotosort.get(mid).getBid());
//                low = mid + 1;
//            }
//        }
//        return null;
//    }
}
