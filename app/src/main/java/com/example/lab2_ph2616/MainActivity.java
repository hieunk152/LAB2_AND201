package com.example.lab2_ph2616;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2_ph2616.Adapter.CatAdapter;
import com.example.lab2_ph2616.Adapter.ProductAdapter;
import com.example.lab2_ph2616.DAO.CatDao;
import com.example.lab2_ph2616.DAO.ProductDao;
import com.example.lab2_ph2616.DTO.Categoty;
import com.example.lab2_ph2616.DTO.Product;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProductAdapter.AdapterCallback {
    Button btnAddCat,btnAddPro;
    RecyclerView rycCat,rycPro;
    CatAdapter catAdapter;
    ProductAdapter productAdapter;
    CatDao catDao;
    Categoty categoty;
    ProductDao productDao;
    ArrayList<Categoty> list_Category;
    ArrayList<Product> list_Product;
    String taskAdd = "AddPro";
    String taskUpdate = "UpdatePro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnAddCat = findViewById(R.id.btnAddCategory);
        btnAddPro = findViewById(R.id.btnAddProduct);
        rycCat = findViewById(R.id.recyclerViewCategory);
        rycPro = findViewById(R.id.recyclerViewProduct);
        LinearLayoutManager horizontal = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rycCat.setLayoutManager(horizontal);
        rycPro.setLayoutManager(linearLayoutManager);
        catDao = new CatDao(MainActivity.this);
        productDao = new ProductDao(MainActivity.this);
        list_Category = catDao.listCat();
        Log.d("zzz", "list: "+list_Category.size());
        list_Product = productDao.getListProduct();
        catAdapter = new CatAdapter(MainActivity.this,list_Category);
        productAdapter = new ProductAdapter(MainActivity.this,list_Product);
        rycCat.setAdapter(catAdapter);
        rycPro.setAdapter(productAdapter);
        productAdapter.setCallback(this);
        ActivityResultLauncher<Intent> launcherTool = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()==RESULT_OK && result.getData()!=null){
                            //Lấy tên danh mục từ CategoryActivity
                            String categoryName = result.getData().getStringExtra("CategoryName");
                            if(categoryName != null){
                                categoty = new Categoty(categoryName);
                                //Thêm danh mục vaof database
                                int isAdded = catDao.AddCat(categoty);
                                if(isAdded>0){
                                    list_Category.clear();
                                    list_Category.addAll(catDao.listCat());
                                    catAdapter.notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this, "Category added successfully", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(MainActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
        );
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        // trả ngược dữ liệu lại khi activity này bị hủy
        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherTool.launch(intent);
            }
        });
        //Product
        btnAddPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_Category.size()>0){
                    showProductInputDialog(taskAdd,0);
                }else{
                    Toast.makeText(MainActivity.this, "Vui lòng thêm the loại trước.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void showProductInputDialog(String task, int position) {
        // Tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate layout tùy chỉnh vào Dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_enter_data_product, null);

        // Tìm các thành phần trong layout
        final EditText etProductName = dialogView.findViewById(R.id.etProductName);
        final EditText etProductPrice = dialogView.findViewById(R.id.etProductPrice);
        final Spinner spinnerProductCategory = dialogView.findViewById(R.id.spinnerProductCategory);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // Kiểm tra list_Category và thiết lập Adapter cho Spinner nếu có dữ liệu
        if (list_Category != null && !list_Category.isEmpty()) {
            ArrayAdapter<Categoty> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_Category) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setText(list_Category.get(position).getName());
                    return view;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                    view.setText(list_Category.get(position).getName());
                    return view;
                }
            };
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProductCategory.setAdapter(catAdapter);
        } else {
            Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
        }

        // Thiết lập View cho Dialog
        builder.setView(dialogView);

        // Tạo AlertDialog
        final AlertDialog dialog = builder.create();

        if (task.equals(taskUpdate)) {
            // Kiểm tra xem position có hợp lệ không trước khi lấy sản phẩm
            if (position >= 0 && position < list_Product.size()) {
                Product productToEdit = list_Product.get(position);

                // fill dữ liệu khi update
                etProductName.setText(productToEdit.getName());
                etProductPrice.setText(String.valueOf(productToEdit.getPrice()));

                // Tìm id_cat từ sản phẩm
                int productCategoryId = productToEdit.getId_cat();
                int categoryPosition = -1;
                for (int i = 0; i < list_Category.size(); i++) {
                    if (list_Category.get(i).getId() == productCategoryId) {
                        categoryPosition = i;
                        break;
                    }
                }

                if (categoryPosition != -1) {
                    spinnerProductCategory.setSelection(categoryPosition);
                }
            } else {
                Toast.makeText(this, "Invalid product position", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            Categoty obj_catSpin = (Categoty) spinnerProductCategory.getSelectedItem();
            String productName = etProductName.getText().toString();
            String productPriceStr = etProductPrice.getText().toString();

            if (!productName.isEmpty() && !productPriceStr.isEmpty()) {
                double productPrice = Double.parseDouble(productPriceStr);
                Product productTaskinDialog = new Product(productPrice, productName, obj_catSpin.getId());

                if (task.equals(taskAdd)) {
                    // Kiểm tra trùng lặp trong list_Product trước khi thêm mới
                    boolean isDuplicate = false;
                    for (Product p : list_Product) {
                        if (p.getName().equalsIgnoreCase(productTaskinDialog.getName())) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        // Thêm sản phẩm mới nếu không trùng lặp
                        int res = productDao.AddProduct(productTaskinDialog);
                        if (res > 0) {
                            list_Product.add(productTaskinDialog);
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi thêm, hãy kiểm tra trùng lặp", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Sản phẩm đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }

                } else if (task.equals(taskUpdate)) {
                        // Cập nhật sản phẩm
                        productTaskinDialog.setId(list_Product.get(position).getId());  // Đảm bảo ID không đổi để tránh tạo mới
                        boolean res = productDao.updateProduct(list_Product.get(position).getId(), productTaskinDialog);
                        if (res) {
                            list_Product.clear();
                            list_Product.addAll(productDao.getListProduct());
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi sửa, hãy kiểm tra trùng lặp", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Sản phẩm đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }

            } else {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });


        dialog.show();
    }

    @Override
    public void onEditButtonClick(int position) {
        showProductInputDialog(taskUpdate,position);
    }

}
