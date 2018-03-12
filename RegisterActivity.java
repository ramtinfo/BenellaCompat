package com.example.ram.benellacompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText name,email,password,mobile,state,country;
    String url="http://www.benella.in/android/register.php";
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        mobile=findViewById(R.id.mobile);
        state=findViewById(R.id.state);
        country=findViewById(R.id.country);
        signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResgisterUser();
            }
        });
    }

    private void ResgisterUser() {
        final String username=name.getText().toString();
        final String useremail=email.getText().toString();
        final String pwd=password.getText().toString();
        final String usermobile=mobile.getText().toString();
        final String userState=state.getText().toString();
        final String usercontry=country.getText().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG",""+error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",username);
                params.put("email",useremail);
                params.put("password",pwd);
                params.put("mobile",usermobile);
                params.put("state",userState);
                params.put("country",usercontry);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
