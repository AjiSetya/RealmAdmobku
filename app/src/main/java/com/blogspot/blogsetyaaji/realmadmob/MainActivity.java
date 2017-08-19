package com.blogspot.blogsetyaaji.realmadmob;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lvmahasiswa)
    RecyclerView lvmahasiswa;

    // komponen dasar realm
    Realm realm;
    RealmConfiguration realmConfiguration;
    RealmAsyncTask realmAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // memasukkan komponen realm, membentuk layanan realm
        inisialisasiRealm(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TambahActivity.class));
            }
        });

        // atur model recyclerview
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(this);
        lvmahasiswa.setLayoutManager(layoutManager);

        // tampilkan data
        tampilData();
    }

    private void tampilData() {
        // panggil semua data yang disimpan di dalam model mahasiswa
        RealmResults<ModelMahasiswa> data_mhs =
                realm.where(ModelMahasiswa.class).findAll();
        // check keberadaan data
        if (data_mhs.size() <= 0) {
            Toast.makeText(this, "Data kosong", Toast.LENGTH_SHORT).show();
        } else {
            // tampilkan data
            Log.d("Data :", data_mhs.size()
                    + "" + data_mhs.toString());
            MahasiswaAdapter adapter =
                    new MahasiswaAdapter(MainActivity.this, data_mhs);
            lvmahasiswa.setAdapter(adapter);
        }
    }

    private void inisialisasiRealm(MainActivity mainActivity) {
        realm.init(mainActivity);
        realmConfiguration = new RealmConfiguration.Builder().build();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
