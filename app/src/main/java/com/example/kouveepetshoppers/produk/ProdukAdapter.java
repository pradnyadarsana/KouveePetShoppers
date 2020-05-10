package com.example.kouveepetshoppers.produk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kouveepetshoppers.R;
import com.example.kouveepetshoppers.model.ProdukDAO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<ProdukDAO> result;
    public List<ProdukDAO> resultFiltered;

    public ProdukAdapter(Context context, List<ProdukDAO> result){
        this.context = context;
        this.result = result;
        this.resultFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_produk, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ProdukDAO produk = resultFiltered.get(position);
        System.out.println(resultFiltered.get(position).getNama()+" "+position);
        holder.nama.setText(produk.getNama());
        holder.harga.setText(Integer.toString(produk.getHarga()));
        if(produk.getJumlah_stok()<=0){
            holder.jumlah_stok.setTextColor(Color.parseColor("#FF0000"));
            holder.satuan.setTextColor(Color.parseColor("#FF0000"));
            holder.textTersedia.setTextColor(Color.parseColor("#FF0000"));
            holder.labelHabis.setVisibility(View.VISIBLE);
        }else{
            holder.jumlah_stok.setTextColor(ContextCompat.getColor(context, R.color.colorAccentGrey));
            holder.satuan.setTextColor(ContextCompat.getColor(context, R.color.colorAccentGrey));
            holder.textTersedia.setTextColor(ContextCompat.getColor(context, R.color.colorAccentGrey));
            holder.labelHabis.setVisibility(View.INVISIBLE);
        }
        holder.jumlah_stok.setText(String.valueOf(produk.getJumlah_stok()));
        holder.satuan.setText(produk.getSatuan());

        if(!produk.getGambar().equalsIgnoreCase("default.jpg") && produk.getGambar()!=null){
            String photo_url = "http://kouveepetshopapi.smithdev.xyz/upload/produk/"+produk.getGambar();
            System.out.println(photo_url);
            Glide.with(context).load(photo_url).into(holder.gambar);
        }else {
            holder.gambar.setImageResource(R.drawable.ic_image_black_24dp);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(produk, TampilDetailProdukActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    resultFiltered = result;
                } else {
                    List<ProdukDAO> filteredList = new ArrayList<>();
                    for (ProdukDAO row : result) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || Integer.toString(row.getId_produk()).contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    resultFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = resultFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                resultFiltered = (ArrayList<ProdukDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nama, satuan, jumlah_stok, textTersedia, harga;
        private ImageView gambar;
        private RelativeLayout labelHabis;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.tvNamaProduk);
            harga = itemView.findViewById(R.id.tvHargaProduk);
            jumlah_stok = itemView.findViewById(R.id.tvJumlahStokProduk);
            satuan = itemView.findViewById(R.id.tvSatuanProduk);
            textTersedia = itemView.findViewById(R.id.textTersedia);
            gambar = itemView.findViewById(R.id.ivGambarProduk);
            parent = itemView.findViewById(R.id.ParentProduk);
            labelHabis = itemView.findViewById(R.id.labelHabis);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void startIntent(ProdukDAO hasil, Class nextView){
        Intent view = new Intent(context, nextView);
        Gson gson = new Gson();
        String json = gson.toJson(hasil);
        view.putExtra("produk", json);
        view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(view);
    }
}
