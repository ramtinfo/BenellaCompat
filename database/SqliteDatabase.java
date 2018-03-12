package com.example.ram.benellacompat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ram.benellacompat.pojo.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 3/12/18.
 */

public class SqliteDatabase extends SQLiteOpenHelper{
    private    static final int DATABASE_VERSION =    5;
    private    static final String    DATABASE_NAME = "PRODUCT_CART";
    private    static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "_id";
    private static final   String COLUMN_PRODUCTNAME = "productname";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String DROP="DROP TABLE IF EXISTS ";
    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME + " TEXT," + COLUMN_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP + TABLE_PRODUCTS);
        onCreate(db);
    }
    public List<Product> listProducts(){
        String sql = "select * from " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Product> storeProducts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                int quantity = Integer.parseInt(cursor.getString(2));
                storeProducts.add(new Product(id, name, quantity));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeProducts;
    }
    public void addProduct(Product product){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_ID,product.getId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
    }
    public void updateProduct(Product product){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,product.getId());
        values.put(COLUMN_PRODUCTNAME, product.getName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PRODUCTS, values, COLUMN_ID    + "    = ?", new String[] { String.valueOf(product.getId())});
    }
    public Product findProduct(int ids){
        String query = "Select * FROM "    + TABLE_PRODUCTS + " WHERE " + COLUMN_ID + " = " + ids;
        SQLiteDatabase db = this.getWritableDatabase();
        Product mProduct = null;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String productName = cursor.getString(1);
            int productQuantity = Integer.parseInt(cursor.getString(2));
            mProduct = new Product(id, productName, productQuantity);
        }
        cursor.close();
        return mProduct;
    }
    public void deleteProduct(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID    + "    = ?", new String[] { String.valueOf(id)});
    }
}
