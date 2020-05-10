package com.example.kouveepetshoppers.produk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kouveepetshoppers.R;
import com.example.kouveepetshoppers.model.ProdukDAO;
import com.google.gson.Gson;

public class TampilDetailProdukActivity extends AppCompatActivity {
    private TextView id_produk, nama, satuan, jumlah_stok, min_stok, harga, created_at, created_by,
            modified_at, modified_by, delete_at, delete_by;
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
