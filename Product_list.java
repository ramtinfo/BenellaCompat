package com.example.ram.benellacompat;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ram.benellacompat.adapters.SizeAdapter;
import com.example.ram.benellacompat.database.SqliteDatabase;
import com.example.ram.benellacompat.pojo.FuckProductPojo;
import com.example.ram.benellacompat.pojo.Product;
import com.example.ram.benellacompat.pojo.ProductDataPojo;
import com.example.ram.benellacompat.pojo.ProductPojo;
import com.example.ram.benellacompat.static_classes.ProductDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Product_list extends AppCompatActivity implements ProductDetails.GetDetails{
    TextView mrp,price,title,stock,descritption;
    ViewPager viewPager;
    PagerAdapter adapter;
    RecyclerView recyclerView;
    SizeAdapter sizeAdapter;
    ArrayList<String> size12=new ArrayList<>();
    ArrayList<String> urilist = new ArrayList<>();
    TextView addtocart,addtowishlist;
    private SqliteDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parrallax_effect);
        Toolbar toolbar=findViewById(R.id.tool_bar);
        mrp=findViewById(R.id.mrp);
        price=findViewById(R.id.price);
        stock=findViewById(R.id.stock);
        title=findViewById(R.id.title);
        mDatabase = new SqliteDatabase(this);
        List<Product> allProducts = mDatabase.listProducts();
        for (Product product:allProducts){
            Log.d("ID",product.getName()+" : "+product.getId());
        }
        addtocart=findViewById(R.id.add_to_cart);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocart.setScaleX(.89f);
                addtocart.setScaleY(.89f);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addtocart.setScaleY(1);
                        addtocart.setScaleX(1);
                    }
                },200);
                //code add to cart
                String product_id=getIntent().getStringExtra("id");
                Log.d("ID",product_id+" ready");
                if(mDatabase.findProduct(Integer.parseInt(getIntent().getStringExtra("id")))!=null){
                    Log.d("ID",product_id+" updated");
                    mDatabase.
                            updateProduct(new Product
                                    (Integer.parseInt(getIntent().getStringExtra("id"))
                                            ,"test",1));
                }else{
                    Log.d("ID",product_id+" created");
                    mDatabase.
                            addProduct(new Product
                                    (Integer.parseInt(getIntent().getStringExtra("id"))
                                            ,"test",1));
                }

            }
        });
        recyclerView=findViewById(R.id.horizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        sizeAdapter=new SizeAdapter(size12);
        recyclerView.setAdapter(sizeAdapter);
        descritption=findViewById(R.id.desc);
        adapter=new PagerAdapter(getSupportFragmentManager(),urilist);
        viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        ProductDetails.getInstance(this).getData(getIntent().getStringExtra("id"));
        Log.d("TAG","ds "+getIntent().getStringExtra("id"));
        ProductDetails.setProductRetriveList(this);
//        descritption.
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void multiLineStrikeThrough(TextView description, String textContent){
        description.setText(textContent, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable)description.getText();
        spannable.setSpan(new StrikethroughSpan(), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    @Override
    public void onGetDetails(ArrayList<ProductPojo> retrivepojo) {
            for (ProductPojo pojo:retrivepojo){
                if(pojo.getStatus().equals("ok")){
                    FuckProductPojo dataPojo=pojo.getData();
                        ProductDataPojo sd=dataPojo.getData();
                        for (String s:dataPojo.getProduct_image()){
                            Log.d("TC",s+" h");
                            urilist.add("http://www.benella.in/uploads/product/"+s);
                        }
                       adapter.notifyDataSetChanged();
                       title.setText(sd.getName()+"");
                       descritption.setText(sd.getSortdesc()+"");
                       mrp.setText(sd.getPrice()+"");
                       price.setText(sd.getSpacel_prize()+"");
                       String[] size=sd.getProduct_size().split(",");
                       Log.d("TAG",size[3]+"");
                       size12.clear();
                       for (int i=0;i<size.length;i++){
                           size12.add(size[i]);
                       }
                       sizeAdapter.notifyDataSetChanged();
                       stock.setText(sd.getProduct_stock()+"");
//                        Log.w("T challa",dataPojo.toString()+" d");
                }else{
                    Log.w("Product","no status");
                }
            }
    }
}
