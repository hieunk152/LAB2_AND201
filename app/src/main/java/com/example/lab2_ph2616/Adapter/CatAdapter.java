package com.example.lab2_ph2616.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2_ph2616.DAO.CatDao;
import com.example.lab2_ph2616.DAO.ProductDao;
import com.example.lab2_ph2616.DTO.Categoty;
import com.example.lab2_ph2616.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CategoryViewHolder> {
    private ArrayList<Categoty> list_cat;
    private CatDao catDao;
    private ProductDao productDao;
    Context context;

    public CatAdapter(Context context, ArrayList<Categoty> list_cat) {
        this.context = context;
        this.list_cat = list_cat;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cart,parent,false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Categoty categoty = list_cat.get(position);

        holder.tvSTT.setText((position+1)+"");
        holder.tv_name.setText(categoty.getName());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                catDao = new CatDao(context);
                new AlertDialog.Builder(context)
                        .setTitle("Xóa mục")
                        .setMessage("Bạn có chắc chắn muốn xóa này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean found = false;
                                ArrayList<Integer> id_categoryFromTbProduct = catDao.getAllProductCategoryIds();
                                for (int id_cat:id_categoryFromTbProduct) {
                                    Log.d("zzz", "id_tb_cat: "+list_cat.get(position).getId() +" and "+ "id_tb_product "+id_cat);
                                    if(list_cat.get(position).getId() == id_cat){
                                        Toast.makeText(context, "Danh muc đang thuộc một sản phẩm, Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                                        found = true;
                                        break;
                                    }
                                }
                                if(!found){
                                    boolean res = catDao.DeleteCat(list_cat.get(position).getId());
                                    if (res) {
                                        list_cat.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "Lỗi không xóa được, có thể trùng dữ liệu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert) // Thêm icon nếu cần
                        .show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list_cat!=null){
            return list_cat.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tvSTT,tv_name;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSTT = itemView.findViewById(R.id.tv_stt);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
