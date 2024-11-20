package com.example.lab2_ph2616.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lab2_ph2616.DB_Helper.MyDBHelper;
import com.example.lab2_ph2616.DTO.Product;

import java.util.ArrayList;

public class ProductDao {
    MyDBHelper dbHelper;
    SQLiteDatabase db;

    public ProductDao(Context context) {
        dbHelper = new MyDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public int AddProduct(Product product){
        ContentValues values = new ContentValues();
        values.put("name",product.getName());
        values.put("price",product.getPrice());
        values.put("id_cat",product.getId_cat());
        int kq = (int) db.insert("tb_product",null,values);
        return kq;
    }

    public boolean deleteProduct(int id){
        int productAffected = db.delete("tb_product","id=?",new String[]{String.valueOf(id)});
        return productAffected > 0; // trả về true
    }

    public boolean updateProduct(int id,Product product){
        ContentValues values = new ContentValues();
        values.put("name",product.getName());
        values.put("price",product.getPrice());
        values.put("id_cat",product.getId_cat());
        int UpdateProductAffected = db.update("tb_product",values,"id=?",new String[]{String.valueOf(id)});
        return UpdateProductAffected > 0;
    }
    public ArrayList<Product> getListProduct(){
        ArrayList<Product> listPro = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_product ORDER BY id ASC",null);
        if(cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                Double product_price = cursor.getDouble(2);
                int id_cat = cursor.getInt(3);
                listPro.add(new  Product(id,id_cat,name,product_price));
            }while (cursor.moveToNext());
        }else{
            Log.d("zzzzzz", "Product::getList: Khong lay duoc du lieu ");
        }
        return listPro;
    }
    public Product getProductById(int id){
        Cursor cursor = db.query("tb_product",new String[]{"id,name,price,id_cat"},"id=?",new String[]{String.valueOf(id)},null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            int product_id = cursor.getInt(0);
            String  product_name = cursor.getString(1);
            Double product_price = cursor.getDouble(2);
            int id_cat = cursor.getInt(3);
            cursor.close();
            db.close();
            return new Product(product_id,id_cat,product_name,product_price);
        }
        cursor.close();
        db.close();
        return null;
    }
}
