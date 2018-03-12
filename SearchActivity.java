package com.example.ram.benellacompat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ram.benellacompat.adapters.ProductAdapter;
import com.example.ram.benellacompat.pojo.RetriveData;
import com.example.ram.benellacompat.pojo.RetrivePojo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    EditText searchbox;
    ImageView microphone;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    TextView records;
    ProgressDialog dialog;
    String url="http://www.benella.in/android/search.php?keywords=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchbox=findViewById(R.id.searchview);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Searching");
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        microphone=findViewById(R.id.microphone);
        records=findViewById(R.id.result);
        searchbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    searchbenella(v.getText().toString());
                    Toast.makeText(SearchActivity.this, Html.fromHtml("<h1>Submitted</h1>")+" :"+v.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void searchbenella(String s) {
        String keywords=url+s;
        records.setVisibility(View.GONE);
        dialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.GET,keywords, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<RetrivePojo> retrivePojos = new ArrayList<>();
                dialog.dismiss();
                Log.d("Tag12",response);
                InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                try {
                    Reader reader = new InputStreamReader(inputStream, "UTF-8");
                    JsonReader jsonReader = new JsonReader(reader);
                    jsonReader.beginObject();
                    RetrivePojo pojo = new RetrivePojo();
                    while (jsonReader.hasNext()) {
                        String name = jsonReader.nextName();
                        if (name.equals("status")) {
                            String status = jsonReader.nextString();
                            if (status.equals("ok")) {
                                pojo.setStatus(status);
                            } else {
                                records.setVisibility(View.VISIBLE);
                                pojo.setStatus(status);
                                break;
                            }
                        } else if (name.equals("data")) {
                            ArrayList<RetriveData> dataArrayList = new ArrayList<>();
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()) {
                                jsonReader.beginObject();
                                RetriveData retriveData = new RetriveData();
                                while (jsonReader.hasNext()) {
                                    String name2 = jsonReader.nextName();
                                    if (name2.equals("id")) {
                                        retriveData.setId(jsonReader.nextString());
                                    } else if (name2.equals("category_id")) {
                                        retriveData.setCategory_id(jsonReader.nextString());
                                    } else if (name2.equals("name")) {
                                        retriveData.setName(jsonReader.nextString());
                                    } else if(name2.equals("price")) {
                                        retriveData.setPrice(jsonReader.nextString());
                                    } else if(name2.equals("spacel_price")){
                                        retriveData.setSpacel_price(jsonReader.nextString());
                                    }else if(name2.equals("image")){
                                        retriveData.setImage(jsonReader.nextString());
                                    }else {
                                        jsonReader.skipValue();
                                    }
                                }
                                dataArrayList.add(retriveData);
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                            for (RetriveData  data:dataArrayList){
                                Log.d("TAG14",data.getName()+data.getPrice());
                            }
                            adapter=new ProductAdapter(dataArrayList);
                            recyclerView.setAdapter(adapter);
                            pojo.setData(dataArrayList);
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    retrivePojos.add(pojo);

                    jsonReader.endObject();
                    jsonReader.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("TAG",response+"");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG",error.toString());
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    records.setText("Timeout! Server is taking too long to reply to a data request");
                } else if (error instanceof ServerError) {
                    records.setText("The server encountered an internal error or misconfiguration and was unable to complete your request");
                } else if (error instanceof ParseError) {
                    records.setText("Parse Error while parsing data.");
                } else if (error instanceof NetworkError) {
                    records.setText("Network errors can be any of the following: DNS resolution errors, TCP connection timeout/error, or the server closing/resetting the connection with no response.Check your internet connection.");
                } else if (error instanceof AuthFailureError) {
                    records.setText("Authentication Failure error.");
                } else {
                    records.setText(error.toString());
                }
                records.setVisibility(View.VISIBLE);
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        supportFinishAfterTransition();
    }
}
