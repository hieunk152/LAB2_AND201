package com.example.lab2_ph2616.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lab2_ph2616.DB_Helper.MyDBHelper;
import com.example.lab2_ph2616.DTO.Categoty;

import java.util.ArrayList;

public class CatDao {
    MyDBHelper dbHelper;
    SQLiteDatabase db;
    public CatDao (Context context){
        dbHelper = new MyDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public int AddCat(Categoty categoty){
        ContentValues values = new ContentValues();
        values.put("name",categoty.getName());
        int kq = (int) db.insert("tb_cat",null,values);
        return kq;
    }

    public boolean UpdateCat(int id,String name){
        ContentValues values = new ContentValues();
        values.put("name",name);
        int kq = db.update("tb_cat",values,"id=?",new String[]{String.valueOf(id)});
        return kq >0;
    }
    public boolean DeleteCat(int id){
        int kq = db.delete("tb_cat","id=?",new String[]{String.valueOf(id)});
        return kq>0;
    }
    public ArrayList<Categoty> listCat(){
        ArrayList<Categoty> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM tb_cat ORDER BY id ASC" , null);
        if(c!=null && c.getCount()>0){
            c.moveToFirst();
            do{
                Categoty categoty = new Categoty();
                categoty.setId(c.getInt(0));
                categoty.setName(c.getString(1));
                list.add(categoty);
            }while (c.moveToNext());
        }else{
            Log.d("zzzzzz", "CatDAO::getList: Khong lay duoc du lieu ");
        }
        return list;
    }
    public ArrayList<Integer> getAllProductCategoryIds() {
        ArrayList<Integer> categoryIds = new ArrayList<>();
        Cursor cursor = null;
        try {
            // Truy vấn chỉ lấy ra cột id_cat từ bảng tb_product
            cursor = db.query(
                    "tb_product",               // Tên bảng
                    new String[]{"id_cat"},      // Các cột cần lấy (ở đây chỉ lấy cột id_cat)
                    null,                        // Mệnh đề WHERE (không có điều kiện lọc)
                    null,                        // Tham số cho WHERE (không cần vì không có điều kiện)
                    null,                        // GROUP BY
                    null,                        // HAVING
                    null                         // ORDER BY
            );
            // Duyệt qua các kết quả và thêm vào danh sách categoryIds
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idCat = cursor.getInt(Integer.valueOf(cursor.getColumnIndex("id_cat")));
                    categoryIds.add(idCat);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoryIds;
    }

}
