package com.example.kouveepetshoppers.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kouveepetshoppers.R;
import com.example.kouveepetshoppers.produk.ListProdukFragment;
import com.example.kouveepetshoppers.tentang.TentangFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenu extends AppCompatActivity {
    private BottomNavigationView csBottomNavigationView;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        csBottomNavigationView = findViewById(R.id.bn_main);

        if (getIntent().getStringExtra("from")!=null){
            if(getIntent().getStringExtra("from").equalsIgnoreCase("produk"))
            {
                loadFragment(new ListProdukFragment());
                csBottomNavigationView.setSelectedItemId(R.id.produk);
            } else if(getIntent().getStringExtra("from").equalsIgnoreCase("tentang"))
            {
                loadFragment(new TentangFragment());
                csBottomNavigationView.setSelectedItemId(R.id.tentang);
            }
        }else{
            loadFragment(new ListProdukFragment());
        }

        csBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.produk:
                        fragment = new ListProdukFragment();
                        break;
                    case R.id.tentang:
                        fragment = new TentangFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
