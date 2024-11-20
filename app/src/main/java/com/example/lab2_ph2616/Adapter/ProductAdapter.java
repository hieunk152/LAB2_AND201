package com.example.lab2_ph2616.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lab2_ph2616.DAO.ProductDao;
import com.example.lab2_ph2616.DTO.Product;
import com.example.lab2_ph2616.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private ProductDao productDao;

    private AdapterCallback callback;
    Context context;

    // Constructor nhận dữ liệu
    public ProductAdapter(Context context, List<Product> productList) {
        if (productList == null) {
            this.productList = new ArrayList<>();
        } else {
            this.productList = productList;
        }
        this.context = context;
    }
    public void setCallback(AdapterCallback callback){
        this.callback = callback;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout cho từng item trong RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Lấy dữ liệu cho từng item
        Product product = productList.get(position);
        Log.d("aaa", "position: "+position);
        // Gán dữ liệu vào view
        holder.productSTT.setText(String.valueOf(position+1)); // Sửa lỗi này
        holder.productName.setText("Name: "+product.getName());
        holder.productPrice.setText("Price: "+product.getPrice()); // Sửa lỗi này nếu cần
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                productDao = new ProductDao(context);
                new AlertDialog.Builder(context)
                        .setTitle("Xóa mục")
                        .setMessage("Bạn có chắc chắn muốn xóa mục này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("aaa", "Id: "+product.getId());
                                boolean res = productDao.deleteProduct(product.getId());
                                if (res) {
                                    productList.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Lỗi không xóa được", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert) // Thêm icon nếu cần
                        .show();


                return false;
            }
        });
        // Bạn có thể gán hình ảnh cho ImageView nếu có
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null){
                    callback.onEditButtonClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder cho từng item trong RecyclerView
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productSTT, productName, productPrice;
        ImageView editButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            // Khởi tạo các view trong item layout
            productSTT = itemView.findViewById(R.id.productSTT);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
    public interface AdapterCallback {
        void onEditButtonClick(int position);
    }
}



