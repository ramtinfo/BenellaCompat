package com.example.ram.benellacompat.static_classes;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ram.benellacompat.MySingleton;
import com.example.ram.benellacompat.pojo.FuckProductPojo;
import com.example.ram.benellacompat.pojo.ProductPojo;
import com.example.ram.benellacompat.pojo.ProductDataPojo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by ram on 2/19/18.
 */

public class ProductDetails {
    private static ProductDetails instance;
    private static final String TAG = "TAG1";
    private final static String url = "http://www.benella.in/android/detail.php?act=Details&id=";
    static Context mCtx;
    static GetDetails listner;

    public static synchronized ProductDetails getInstance(Context context) {
        if (instance == null) {
            instance = new ProductDetails(context);
        }
        return instance;
    }

    private ProductDetails(Context context) {
        mCtx = context;
    }

    public void getData(String id) {
        getProduct(id);
    }

    private static void getProduct(String id) {
        String mUrl = url + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response");
                ArrayList<ProductPojo> productPojos = new ArrayList<>();
                InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                try {
                    Reader reader = new InputStreamReader(inputStream, "UTF-8");
                    JsonReader jsonReader = new JsonReader(reader);
                    jsonReader.beginObject();
                    ProductPojo pojo = new ProductPojo();
                    while (jsonReader.hasNext()) {
                        String name = jsonReader.nextName();
                        if (name.equals("status")) {
                            String sttus = jsonReader.nextString();
                            if (sttus.equals("ok")) {
                                pojo.setStatus(sttus);
                            } else {
                                pojo.setStatus(sttus);
                                break;
                            }
                        } else if (name.equals("0")) {
                            jsonReader.beginObject();
                            FuckProductPojo dataPojo = new FuckProductPojo();
                            while (jsonReader.hasNext()) {
                                String name2 = jsonReader.nextName();
                                if (name2.equals("data")) {
                                    jsonReader.beginObject();
                                    ProductDataPojo productDataPojo = new ProductDataPojo();
                                    while (jsonReader.hasNext()) {
                                        String name3 = jsonReader.nextName();
                                        if (name3.equals("id")) {
                                            productDataPojo.setId(jsonReader.nextString());
                                        } else if (name3.equals("name")) {
                                            productDataPojo.setName(jsonReader.nextString());
                                            Log.d("DATE", productDataPojo.getName() + " t challa");
                                        } else if (name3.equals("sku")) {
                                            productDataPojo.setSku(jsonReader.nextString());
                                        } else if (name3.equals("sortdesc")) {
                                            productDataPojo.setSortdesc(jsonReader.nextString());
                                        } else if (name3.equals("price")) {
                                            productDataPojo.setPrice(jsonReader.nextString());
                                        } else if (name3.equals("product_size")) {
                                            productDataPojo.setProduct_size(jsonReader.nextString());
                                        } else if (name3.equals("product_stock")) {
                                            productDataPojo.setProduct_stock(jsonReader.nextString());
                                        }else if(name3.equals("spacel_price")){
                                            productDataPojo.setSpacel_prize(jsonReader.nextString());
                                        }else if (name3.equals("sku")){
                                            productDataPojo.setSku(jsonReader.nextString());
                                        }else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    dataPojo.setData(productDataPojo);
                                    jsonReader.endObject();
                                } else if (name2.equals("product_image")) {
                                    jsonReader.beginArray();
                                    ArrayList<String> imageslist = new ArrayList<>();
                                    while (jsonReader.hasNext()) {
                                        String imgname = jsonReader.nextString();
                                        imageslist.add(imgname);
                                        Log.w("T CHALLA", imgname + " eric");
                                    }
                                    dataPojo.setProduct_image(imageslist);
                                    jsonReader.endArray();
                                } else {
                                    jsonReader.skipValue();
                                }

                            }
                            pojo.setData(dataPojo);
                            jsonReader.endObject();

                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    productPojos.add(pojo);
                    listner.onGetDetails(productPojos);
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
                Log.d(TAG, error.toString() + " error");
            }
        });
        MySingleton.getInstance(mCtx).addToRequestQueue(stringRequest);
    }

    public static void setProductRetriveList(GetDetails list) {
        listner = list;
    }

    public interface GetDetails {
        void onGetDetails(ArrayList<ProductPojo> retrivepojo);
    }
}
