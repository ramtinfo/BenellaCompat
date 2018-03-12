package com.example.ram.benellacompat.static_classes;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ram.benellacompat.MySingleton;
import com.example.ram.benellacompat.pojo.CategoryMainPojo;
import com.example.ram.benellacompat.pojo.CategoryPojo;
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

public class CategoryData {
    public static final String TAG = "Volley";
    private static final String url = "http://www.benella.in/android/category.php?act=cat";
    private static CategoryData ourInstance;
    private static Context mCtx;
    static CategoryRetrive Catlistner;

    public static CategoryData getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new CategoryData(context);
        }
        return ourInstance;
    }

    private CategoryData(Context context) {
        mCtx = context;
    }

    public void getData() {
        Categorydata();
    }

    private static void Categorydata() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d(TAG,response);
                ArrayList<CategoryMainPojo> pojoArrayList = new ArrayList<>();
                InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                try {
                    Reader reader = new InputStreamReader(inputStream, "UTF-8");
                    JsonReader jsonReader = new JsonReader(reader);
                    jsonReader.beginObject();
                    CategoryMainPojo pojo = new CategoryMainPojo();
                    while (jsonReader.hasNext()) {
                        String name = jsonReader.nextName();
                        if (name.equals("status")) {
                            if (jsonReader.nextString().equals("ok")) {
                                pojo.setStatus("ok");
                            } else {
                                pojo.setStatus("false");
                                break;
                            }
                        } else if (name.equals("data")) {
                            ArrayList<CategoryPojo> CategoryDataArrayList = new ArrayList<>();
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()) {
                                CategoryPojo pojo1 = new CategoryPojo();
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String name2 = jsonReader.nextName();
                                    if (name2.equals("id")) {
                                        pojo1.setId(jsonReader.nextString());
                                    } else if (name2.equals("title")) {
                                        pojo1.setTitle(jsonReader.nextString());
                                    } else if (name2.equals("image")) {
                                        pojo1.setImage(jsonReader.nextString());
                                    } else if (name2.equals("description")) {
                                        pojo1.setDescription(jsonReader.nextString());
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                CategoryDataArrayList.add(pojo1);
                                jsonReader.endObject();
                            }
                            pojo.setData(CategoryDataArrayList);
                            jsonReader.endArray();
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    pojoArrayList.add(pojo);
                    Catlistner.onCategoryRetrive(pojoArrayList); //make changes to all
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
        MySingleton.getInstance(mCtx).addToRequestQueue(request);
    }

    public static void setCategory(CategoryRetrive listner) {
        Catlistner = listner;
    }

    public interface CategoryRetrive {
        public void onCategoryRetrive(ArrayList<CategoryMainPojo> categoryMainPojoArrayList);
    }
}
