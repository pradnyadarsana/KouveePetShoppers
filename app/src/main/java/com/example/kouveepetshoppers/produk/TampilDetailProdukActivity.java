package com.example.kouveepetshoppers.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kouveepetshoppers.R;
import com.example.kouveepetshoppers.model.ProdukDAO;
import com.google.gson.Gson;

public class TampilDetailProdukActivity extends AppCompatActivity {
    private RelativeLayout labelHabis;
    private TextView nama, satuan, jumlah_stok, harga, textTersedia;
    private ImageView gambar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_produk);

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("produk");
        System.out.println(json);
        ProdukDAO produk = gson.fromJson(json, ProdukDAO.class);

        nama = findViewById(R.id.viewNamaProduk);
        satuan = findViewById(R.id.viewSatuanProduk);
        jumlah_stok = findViewById(R.id.viewJumlahStokProduk);
        harga = findViewById(R.id.viewHargaProduk);
        gambar = findViewById(R.id.viewGambarProduk);
        textTersedia = findViewById(R.id.textViewTersedia);
        labelHabis = findViewById(R.id.labelHabisDetail);

        if(produk.getJumlah_stok()<=0){
            jumlah_stok.setTextColor(Color.parseColor("#FF0000"));
            satuan.setTextColor(Color.parseColor("#FF0000"));
            textTersedia.setTextColor(Color.parseColor("#FF0000"));
            labelHabis.setVisibility(View.VISIBLE);
        }else{
            jumlah_stok.setTextColor(ContextCompat.getColor(this, R.color.colorAccentGrey));
            satuan.setTextColor(ContextCompat.getColor(this, R.color.colorAccentGrey));
            textTersedia.setTextColor(ContextCompat.getColor(this, R.color.colorAccentGrey));
            labelHabis.setVisibility(View.INVISIBLE);
        }

        setData(produk);
    }

    public void setData(ProdukDAO produk){
        nama.setText(produk.getNama());
        satuan.setText(produk.getSatuan());
        jumlah_stok.setText(Integer.toString(produk.getJumlah_stok()));
        harga.setText(Integer.toString(produk.getHarga()));

        if(!produk.getGambar().equalsIgnoreCase("default.jpg") && produk.getGambar()!=null) {
            String photo_url = "http://kouveepetshopapi.smithdev.xyz/upload/produk/" + produk.getGambar();
            Glide.with(TampilDetailProdukActivity.this).load(photo_url).into(gambar);
        }else{
            gambar.setImageResource(R.drawable.ic_image_black_24dp);
        }

    }
}
