package com.example.kouveepetshoppers.api;

import com.example.kouveepetshoppers.response.GetProduk;
import com.example.kouveepetshoppers.response.PostUpdateDelete;
import com.example.kouveepetshoppers.response.SearchProduk;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterfaceAdmin {
    //KELOLA PRODUK
    @POST("produk")
    @Multipart
    Call<PostUpdateDelete> tambahProduk(@Part("nama") RequestBody nama,
                                        @Part("satuan") RequestBody satuan,
                                        @Part("jumlah_stok") RequestBody jumlah_stok,
                                        @Part("harga") RequestBody harga,
                                        @Part("min_stok") RequestBody min_stok,
                                        @Part MultipartBody.Part gambar,
                                        @Part("created_by") RequestBody created_by);

    @GET("produk")
    Call<GetProduk> getAllProdukAktif();

    @GET("produk/all")
    Call<GetProduk> getAllProduk();

    @GET("produk/underMinStok")
    Call<GetProduk> getProdukUnderMinStok();

    @GET("produk/search/{id_produk}")
    Call<SearchProduk> searchProduk(@Path("id_produk") String id_produk);

    @POST("produk/update/{id_produk}")
    @Multipart
    Call<PostUpdateDelete> ubahProduk(@Path("id_produk") String id_produk,
                                      @Part("nama") RequestBody nama,
                                      @Part("satuan") RequestBody satuan,
                                      @Part("jumlah_stok") RequestBody jumlah_stok,
                                      @Part("harga") RequestBody harga,
                                      @Part("min_stok") RequestBody min_stok,
                                      @Part MultipartBody.Part gambar,
                                      @Part("modified_by") RequestBody modified_by);

    @POST("produk/delete/{id_produk}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusProduk(@Path("id_produk") String id_supplier,
                                       @Field("delete_by") String delete_by);
}
