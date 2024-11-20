package com.example.lab2_ph2616.DB_Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        super(context, "Product.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCategory = "CREATE TABLE tb_cat (id INTEGER PRIMARY KEY NOT NULL, name TEXT UNIQUE NOT NULL)";
        db.execSQL(sqlCategory);

        String sqlProduct = "CREATE TABLE tb_product (id INTEGER PRIMARY KEY NOT NULL , name TEXT NOT NULL UNIQUE, price REAL DEFAULT (0.0), id_cat INTEGER REFERENCES tb_cat (id) )";
        db.execSQL(sqlProduct);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > newVersion){ //i1 là phiên bản mới , i là phiên bản cũ
            db.execSQL("DROP TABLE IF EXISTS tb_cat");
            db.execSQL("DROP TABLE IF EXISTS tb_product");
            onCreate(db);
        }
    }
}
