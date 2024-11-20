package com.example.lab2_ph2616;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


public class CategoryActivity extends AppCompatActivity {
    EditText edCategoryName;
    Button btnSave,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        edCategoryName = findViewById(R.id.etCategoryName);
        btnSave = findViewById(R.id.btnSaveCat);
        btnCancel = findViewById(R.id.btnCancelCat);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_category = edCategoryName.getText().toString().trim();
                if (name_category.isEmpty()) {
                    Toast.makeText(CategoryActivity.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
                } else {
                    // Tạo Intent để trả dữ liệu về MainActivity
                    Intent intent = new Intent();
                    intent.putExtra("CategoryName", name_category);
                    setResult(RESULT_OK, intent);  // 111 là mã Result Code mà bạn đã định nghĩa trong MainActivity
                    finish(); // Đóng activity này và quay về MainActivity
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}