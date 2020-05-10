package com.example.kouveepetshoppers.response;

import com.example.kouveepetshoppers.model.ProdukDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProduk {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<ProdukDAO> listDataProduk;

    public GetProduk(){}

    public GetProduk(String error, List<ProdukDAO> listDataProduk) {
        this.error = error;
        this.listDataProduk = listDataProduk;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<ProdukDAO> getListDataProduk() {
        return listDataProduk;
    }

    public void setListDataProduk(List<ProdukDAO> listDataProduk) {
        this.listDataProduk = listDataProduk;
    }
}
