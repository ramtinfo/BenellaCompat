package com.example.ram.benellacompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ram.benellacompat.pojo.UserDetail;
import com.example.ram.benellacompat.pojo.UserPojo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BenellaLoginActivity extends AppCompatActivity {
    EditText email, password;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_account);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredential(email.getText().toString(), password.getText().toString());
            }
        });
    }

    //    public boolean isEmailValid(String email){
//        return !email.isEmpty()&&
//    }
    private void validateCredential(final String uname, final String pwd) {
//        if(!uname.isEmpty()&&)
        String url = "http://www.benella.in/android/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                try {
                    Reader reader = new InputStreamReader(inputStream, "UTF-8");
                    JsonReader jsonreader = new JsonReader(reader);
                    jsonreader.beginObject();
                    UserPojo pojo=new UserPojo();
                    while (jsonreader.hasNext()){
                        String type=jsonreader.nextName();
                        if(type.equals("status")){
                            String status=jsonreader.nextString();
                            if(status.equals("ok")){
                                pojo.setStatus(status);
                            }else{
                                //set status variable
                                pojo.setStatus(status);
                                jsonreader.close();
                                break;
                            }
                        }else if(type.equals("data")){
                            jsonreader.beginArray();
                            ArrayList<UserDetail>  userDetails=new ArrayList<>();
                            while (jsonreader.hasNext()){
                                jsonreader.beginObject();
                                UserDetail userDetail=new UserDetail();
                                while (jsonreader.hasNext()){
                                    String name=jsonreader.nextName();
                                    switch (name){
                                        case "id":
                                            userDetail.setId(jsonreader.nextString());
                                            break;
                                        case "name":
                                            userDetail.setName(jsonreader.nextString());
                                            break;
                                        case "image":
                                            userDetail.setImage(jsonreader.nextString());
                                            break;
                                        case "email":
                                            userDetail.setEmail(jsonreader.nextString());
                                            break;
                                        case "password":
                                            userDetail.setPassword(jsonreader.nextString());
                                            break;
                                        case "gender":
                                            userDetail.setGender(jsonreader.nextString());
                                            break;
                                        case "contact_no":
                                            userDetail.setContact_no(jsonreader.nextString());
                                            break;
                                        case "address":
                                            userDetail.setAddress(jsonreader.nextString());
                                            break;
                                        case "city":
                                            userDetail.setCity(jsonreader.nextString());
                                            break;
                                        case "state":
                                            userDetail.setState(jsonreader.nextString());
                                            break;
                                        case "country":
                                            userDetail.setCountry(jsonreader.nextString());
                                            break;
                                        case "pincode":
                                            userDetail.setPincode(jsonreader.nextString());
                                            break;
                                        case "status":
                                            userDetail.setStatus(jsonreader.nextString());
                                            break;
                                        case "first_name":
                                            userDetail.setFirst_name(jsonreader.nextString());
                                            break;
                                        case "last_name":
                                            userDetail.setLast_name(jsonreader.nextString());
                                            break;
                                        case "locale":
                                            userDetail.setLocale(jsonreader.nextString());
                                            break;
                                        case "picture_url":
                                            userDetail.setPicture_url(jsonreader.nextString());
                                            break;
                                            default:
                                                jsonreader.skipValue();
                                                break;
                                    }
                                }
                                userDetails.add(userDetail);
                                jsonreader.endObject();
                            }
                            pojo.setData(userDetails);
                            jsonreader.endArray();
                        }else{
                            jsonreader.skipValue();
                        }
                    }
                    jsonreader.endObject();
                    jsonreader.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG",error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", uname);
                params.put("password", pwd);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
