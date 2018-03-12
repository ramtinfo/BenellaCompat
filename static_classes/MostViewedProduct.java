package com.example.ram.benellacompat.static_classes;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ram.benellacompat.MySingleton;
import com.example.ram.benellacompat.pojo.ProductHeaderPojo;

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

public class MostViewedProduct {
    private static final String url = "http://www.benella.in/android/home.php?act=";
    private static MostViewedProduct ourInstance;
    private static Context mCtx;
    private static String TAG = "PVolley";
    static GetHeader headerlistner;

    public static MostViewedProduct getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MostViewedProduct(context);
        }
        return ourInstance;
    }

    private MostViewedProduct(Context context) {
        mCtx = context;
    }

    public void getData(String type) {
        getProductType(type);
    }

    private static void getProductType(final String type) {
        final String mUrl = url + type;
        StringRequest request = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d(TAG,mUrl+"  "+response);
                ArrayList<ProductHeaderPojo> arrayList = new ArrayList<>();
                InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                try {
                    Reader reader = new InputStreamReader(inputStream, "UTF-8");
                    JsonReader jsonReader = new JsonReader(reader);
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        ProductHeaderPojo pojo = new ProductHeaderPojo();
                        while (jsonReader.hasNext()) {
                            String name = jsonReader.nextName();
                            if (name.equals("id")) {
                                pojo.setId(jsonReader.nextString());
                            } else if (name.equals("sortdesc")) {
                                pojo.setSortdesc(jsonReader.nextString());
                            } else if (name.equals("title")) {
                                pojo.setTitle(jsonReader.nextString());
                            } else if (name.equals("price")) {
                                pojo.setPrice(jsonReader.nextString());
                            } else if (name.equals("product_size")) {
                                pojo.setProduct_size(jsonReader.nextString());
                            } else if (name.equals("name")) {
                                pojo.setName(jsonReader.nextString());
                            } else if (name.equals("image")) {
                                pojo.setImage(jsonReader.nextString());
                            } else if (name.equals("spacel_price")) {
                                pojo.setSpacel_price(jsonReader.nextString());
                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        arrayList.add(pojo);
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    jsonReader.close();
                    switch (type) {
                        case "latest":
                            headerlistner.onGettingHeader(arrayList);
                            break;
                        case "random":
                            headerlistner.onGettingRandom(arrayList);
                            break;
                        case "most":
                            headerlistner.onGettingMost(arrayList);
                            break;
                        default:
//                            headerlistner.onGettingHeader(arrayList);
                            break;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "" + error.toString());
            }
        });
        MySingleton.getInstance(mCtx).addToRequestQueue(request);
    }

    public static void setHeaderListner(GetHeader header) {
        headerlistner = header;
    }

    public interface GetHeader {
        public void onGettingHeader(ArrayList<ProductHeaderPojo> pojo);

        public void onGettingRandom(ArrayList<ProductHeaderPojo> pojoArrayList);

        public void onGettingMost(ArrayList<ProductHeaderPojo> pojos);
    }
}
