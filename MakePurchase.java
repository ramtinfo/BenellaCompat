package com.example.ram.benellacompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MakePurchase extends AppCompatActivity {
    Button confirm;
    TextView username;
    EditText name,street,city,state,pincode,mobile;
    ProgressDialog dialog;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_purchase);
        confirm=findViewById(R.id.confirm);
        username=findViewById(R.id.ram);
        name=findViewById(R.id.name);
        street=findViewById(R.id.street);
        city=findViewById(R.id.city);
        state=findViewById(R.id.state);
        pincode=findViewById(R.id.pincode);
        pincode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    confirm.performClick();
                    return true;
                }
                    return false;
            }
        });
        mobile=findViewById(R.id.mobile);
        dialog=new ProgressDialog(this);
        dialog.setMessage("PLACING BID...");
        dialog.setCancelable(false);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });
    }

    private void sendDataToServer() {
        dialog.show();
        String mName,mStreet,mCity,mState,mPincode,mMobile;
        mName=name.getText().toString();
        mStreet=street.getText().toString();
        mCity=city.getText().toString();
        mState=state.getText().toString();
        mPincode=pincode.getText().toString();
        mMobile=mobile.getText().toString();
        if(mName.isEmpty()&&mState.isEmpty()&&mCity.isEmpty()&&mStreet.isEmpty()&&mPincode.isEmpty()&&mMobile.isEmpty()){
            showAlert("Make sure to fill all detail");
            return;
        }
        Map<String,String> map=new HashMap<>();
        map.put("name",mName);
        map.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail()+"");
        map.put("street",mStreet);
        map.put("city",mCity);
        map.put("state",mState);
        map.put("pincode",mPincode);
        map.put("bid",getIntent().getStringExtra("BID"));
        map.put("mobile",mMobile);
        databaseReference.child("BID_DETAIL").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                Toast.makeText(MakePurchase.this, "Bid placed succesfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        databaseReference.child("scorer").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).push().setValue(getIntent().getStringExtra("BID"));
    }
    private void showAlert(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(s);
        builder.setMessage(s);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
