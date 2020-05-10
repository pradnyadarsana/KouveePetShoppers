package com.example.kouveepetshoppers.produk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kouveepetshoppers.R;
import com.example.kouveepetshoppers.api.ApiClient;
import com.example.kouveepetshoppers.api.ApiInterfaceAdmin;
import com.example.kouveepetshoppers.model.ProdukDAO;
import com.example.kouveepetshoppers.response.GetProduk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProdukFragment extends Fragment {
    private List<ProdukDAO> ListProduk;
    private RecyclerView recyclerProduk;
    private ProdukAdapter adapterProduk;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView;

    private RadioGroup sortOptionRG;
    private RadioButton sortAsc, sortDesc;
    private Spinner sortBy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_produk, container, false);

        Toolbar toolbar = view.findViewById(R.id.searchProdukToolbar);
        toolbar.setBackgroundResource(R.color.colorAccentOrange);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // toolbar fancy stuff
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Produk");

        recyclerProduk = view.findViewById(R.id.recycler_view_produk);

        sortOptionRG = view.findViewById(R.id.radioGroup);
        sortAsc = view.findViewById(R.id.sortAscending);
        sortDesc = view.findViewById(R.id.sortDescending);
        loadSortOptionCheckListener();

        sortBy = view.findViewById(R.id.spinSort);
        loadSortBySelectedListener();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListProduk = new ArrayList<>();
        adapterProduk = new ProdukAdapter(getContext(), ListProduk);
        mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerProduk.setLayoutManager(mLayoutManager);
        recyclerProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerProduk.setAdapter(adapterProduk);
        setRecycleView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Nama Produk");

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapterProduk.getFilter().filter(query);
                loadSortOptionCheckListener();
                loadSortBySelectedListener();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapterProduk.getFilter().filter(query);
                loadSortOptionCheckListener();
                loadSortBySelectedListener();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetProduk> produkDAOCall = apiService.getAllProdukAktif();

        produkDAOCall.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                ListProduk.addAll(response.body().getListDataProduk());
                //System.out.println(ListProduk.get(0).getNama());
                adapterProduk.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(getContext(), "Gagal menampilkan Produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadSortOptionCheckListener(){
        sortAsc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (sortBy.getSelectedItem().toString().equalsIgnoreCase("none")){
                        //sortByIdDesc();
                    }else if(sortBy.getSelectedItem().toString().equalsIgnoreCase("nama")){
                        sortByName(true);
                    }else if(sortBy.getSelectedItem().toString().equalsIgnoreCase("harga")){
                        sortByHarga(true);
                    }else if(sortBy.getSelectedItem().toString().equalsIgnoreCase("stok")){
                        sortByJumlahStok(true);
                    }
                }
            }
        });

        sortDesc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (sortBy.getSelectedItem().toString().equalsIgnoreCase("none")){
                        //sortByIdDesc();
                    }else if(sortBy.getSelectedItem().toString().equalsIgnoreCase("nama")){
                        sortByName(false);
                    }else if(sortBy.getSelectedItem().toString().equalsIgnoreCase("harga")){
                        sortByHarga(false);
                    }else if(sortBy.getSelectedItem().toString().equalsIgnoreCase("stok")){
                        sortByJumlahStok(false);
                    }
                }
            }
        });
    }

    public void loadSortBySelectedListener(){
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        sortByIdDesc();
                        break;
                    case 1:
                        if(sortOptionRG.getCheckedRadioButtonId()==sortAsc.getId()){
                            sortByName(true);
                        }else if(sortOptionRG.getCheckedRadioButtonId()==sortDesc.getId()){
                            sortByName(false);
                        }else{
                            sortAsc.setChecked(true);
                            sortByName(true);
                        }
                        break;
                    case 2:
                        if(sortOptionRG.getCheckedRadioButtonId()==sortAsc.getId()){
                            sortByHarga(true);
                        }else if(sortOptionRG.getCheckedRadioButtonId()==sortDesc.getId()){
                            sortByHarga(false);
                        }else{
                            sortAsc.setChecked(true);
                            sortByHarga(true);
                        }
                        break;
                    case 3:
                        if(sortOptionRG.getCheckedRadioButtonId()==sortAsc.getId()){
                            sortByJumlahStok(true);
                        }else if(sortOptionRG.getCheckedRadioButtonId()==sortDesc.getId()){
                            sortByJumlahStok(false);
                        }else{
                            sortAsc.setChecked(true);
                            sortByJumlahStok(true);
                        }
                        break;
                    default:
                        System.out.println("masuk case default");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sortByIdDesc() {
        Collections.sort(adapterProduk.resultFiltered, new Comparator<ProdukDAO>() {
            @Override
            public int compare(ProdukDAO o1, ProdukDAO o2) {
                return o2.getId_produk()-o1.getId_produk();
            }
        });

        adapterProduk.notifyDataSetChanged();
    }

    public void sortByName(boolean ascdesc){
        if(ascdesc){
            Collections.sort(adapterProduk.resultFiltered, new Comparator<ProdukDAO>() {
                @Override
                public int compare(ProdukDAO o1, ProdukDAO o2) {
                    return o1.getNama().compareTo(o2.getNama());
                }
            });
        }else{
            Collections.sort(adapterProduk.resultFiltered, new Comparator<ProdukDAO>() {
                @Override
                public int compare(ProdukDAO o1, ProdukDAO o2) {
                    return o2.getNama().compareTo(o1.getNama());
                }
            });
        }
        adapterProduk.notifyDataSetChanged();
    }

    private void sortByHarga(boolean ascdesc) {
        if(ascdesc){
            Collections.sort(adapterProduk.resultFiltered, new Comparator<ProdukDAO>() {
                @Override
                public int compare(ProdukDAO o1, ProdukDAO o2) {
                    return o1.getHarga()-o2.getHarga();
                }
            });
        }else{
            Collections.sort(adapterProduk.resultFiltered, new Comparator<ProdukDAO>() {
                @Override
                public int compare(ProdukDAO o1, ProdukDAO o2) {
                    return o2.getHarga()-o1.getHarga();
                }
            });
        }
        adapterProduk.notifyDataSetChanged();
    }

    private void sortByJumlahStok(boolean ascdesc) {
        if(ascdesc){
            Collections.sort(adapterProduk.resultFiltered, new Comparator<ProdukDAO>() {
                @Override
                public int compare(ProdukDAO o1, ProdukDAO o2) {
                    return o1.getJumlah_stok()-o2.getJumlah_stok();
                }
            });
        }else{
            Collections.sort(adapterProduk.resultFiltered, new Comparator<ProdukDAO>() {
                @Override
                public int compare(ProdukDAO o1, ProdukDAO o2) {
                    return o2.getJumlah_stok()-o1.getJumlah_stok();
                }
            });
        }
        adapterProduk.notifyDataSetChanged();
    }
}
