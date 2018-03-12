package com.example.ram.benellacompat.static_classes;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ram.benellacompat.MySingleton;
import com.example.ram.benellacompat.pojo.RetriveData;
import com.example.ram.benellacompat.pojo.RetrivePojo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by ram on 2/16/18.
 */

public class RetriveProduct {
    private static RetriveProduct instance;
    private static final String TAG = "TAG";
    private final static String url = "http://www.benella.in/android/product.php?act=pro&id=";
    static Context mCtx;
    static GetProduct productlistner;

    public static RetriveProduct getInstance(Context context) {
        if (instance == null) {
            instance = new RetriveProduct(context);
        }
        return instance;
    }

    private RetriveProduct(Context context) {
        mCtx = context;
    }

    public void getData(String id) {
        getProductDetail(id);
    }

    private static void getProductDetail(String id) {
        String murl = url + id;
        StringRequest request = new StringRequest(Request.Method.GET, murl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response);
                ArrayList<RetrivePojo> retrivePojos = new ArrayList<>();
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
                            pojo.setData(dataArrayList);
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    retrivePojos.add(pojo);
                    productlistner.onGetProduct(retrivePojos);
                    jsonReader.endObject();
                    jsonReader.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        MySingleton.getInstance(mCtx).addToRequestQueue(request);
    }

    public static void setProductRetriveListner(GetProduct productlistne) {
        productlistner = productlistne;
    }

    public interface GetProduct {
        void onGetProduct(ArrayList<RetrivePojo> retrivePojos);
    }
}
