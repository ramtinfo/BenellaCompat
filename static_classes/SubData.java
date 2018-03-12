package com.example.ram.benellacompat.static_classes;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ram.benellacompat.MySingleton;
import com.example.ram.benellacompat.pojo.SubCategoryData;
import com.example.ram.benellacompat.pojo.SubCategoryPojo;

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

public class SubData {
    private static SubData ourInstance;
    private static Context mCtx;
    public static SubDataRetrive listnerData;
    private static final String TAG = "Volley";
    private final static String url = "http://www.benella.in/android/subcat.php?act=subcat&id=";

    public static SubData getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SubData(context);
        }
        return ourInstance;
    }

    private SubData(Context context) {
        this.mCtx = context;
    }

    public void getSubCategory(String header, String id) {
        SubCategory(header, id);
    }

    private static void SubCategory(final String header, String id) {
        String murl = url + id;
        Log.d("URL", murl);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, murl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<SubCategoryPojo> pojoArrayList = new ArrayList<>();
                InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                try {
                    Reader reader = new InputStreamReader(inputStream, "UTF-8");
                    JsonReader jsonReader = new JsonReader(reader);
                    jsonReader.beginObject();
                    SubCategoryPojo pojo = new SubCategoryPojo();
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
                            ArrayList<SubCategoryData> subCategoryDataArrayList = new ArrayList<>();
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()) {
                                SubCategoryData pojo1 = new SubCategoryData();
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String name2 = jsonReader.nextName();
                                    if (name2.equals("id")) {
                                        String id = jsonReader.nextString();
                                        pojo1.setId(id);
                                    } else if (name2.equals("title")) {
                                        String title = jsonReader.nextString();
                                        pojo1.setTitle(title);
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                subCategoryDataArrayList.add(pojo1);
                                jsonReader.endObject();
                            }
                            pojo.setData(subCategoryDataArrayList);
                            jsonReader.endArray();
                        }
                    }
                    pojoArrayList.add(pojo);
                    listnerData.onSubDataRetrival(header, pojoArrayList);
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
                Log.d(TAG, "" + error.toString());
            }
        });
        MySingleton.getInstance(mCtx).addToRequestQueue(stringRequest);
    }

    public static void setSubDataRetrival(SubDataRetrive listner) {
        listnerData = listner;
    }

    public interface SubDataRetrive {
        public void onSubDataRetrival(String header, ArrayList<SubCategoryPojo> subCategoryPojos);
    }
}
