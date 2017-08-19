package com.blogspot.blogsetyaaji.realmadmob;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

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
    RealmResults<ModelMahasiswa> data_mhs;
    @BindView(R.id.adView)
    AdView adView;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6309516150695970/4012985914");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                startActivity(new Intent(MainActivity.this, TambahActivity.class));
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Log.i("Ads", "onAdClosed");
            }
        });
        // memasukkan komponen realm, membentuk layanan realm
        inisialisasiRealm(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });

        // atur model recyclerview
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(this);
        lvmahasiswa.setLayoutManager(layoutManager);

        // tampilkan data
        tampilData();

        // perintah ketika perintah diklik
        lvmahasiswa.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gesture = new GestureDetector(getApplicationContext()
                    , new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                // cek apakah posisi dan gesture ada
                if (child != null && gesture.onTouchEvent(e)) {
                    final int posisi = rv.getChildAdapterPosition(child);
                    // dialog menampilkan aksi untuk data yang dipilih
                    final ModelMahasiswa model = data_mhs.get(posisi);
                    final Dialog dialog = new Dialog(MainActivity.this);
                    //tampilkan layout aksi ke dalam dialog
                    dialog.setContentView(R.layout.aksi_mahasiswa);
                    // deklarasi dan inisialisasi komponen pada layout
                    final EditText txtnama = (EditText) dialog.findViewById(R.id.txtaksinama);
                    final EditText txtjurusan = (EditText) dialog.findViewById(R.id.txtaksijurusan);
                    txtnama.setText(model.getNamamahasiswa());
                    txtjurusan.setText(model.getJurusanmahasiswa());
                    Button btnedit = (Button) dialog.findViewById(R.id.btnaksisimpan);
                    Button btnhapus = (Button) dialog.findViewById(R.id.btnaksihapus);
                    // aksi pada tombol
                    btnedit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // perbarui data
                            Toast.makeText(MainActivity.this, model.getIdmahasiswa(), Toast.LENGTH_SHORT).show();
                            final String nama = txtnama.getText().toString();
                            final String jurusan = txtjurusan.getText().toString();
                            if (TextUtils.isEmpty(nama)) {
                                txtnama.setError("Nama tidak boleh kosong");
                                txtnama.requestFocus();
                            } else if (TextUtils.isEmpty(jurusan)) {
                                txtjurusan.setError("Jurusan tidak boleh kosong");
                                txtjurusan.requestFocus();
                            } else {
                                // simpan data
                                ModelMahasiswa model = realm.where(ModelMahasiswa.class)
                                        .equalTo("idmahasiswa", data_mhs.get(posisi).getIdmahasiswa())
                                        .findFirst();
                                if (model != null) {
                                    realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            ModelMahasiswa model = realm.createObject(ModelMahasiswa.class);
                                            model.setNamamahasiswa(nama);
                                            model.setJurusanmahasiswa(jurusan);
                                            dialog.dismiss();
                                            tampilData();
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(MainActivity.this, "Data disimpan", Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            Toast.makeText(MainActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                                            Log.e("error simpan : ", error.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(MainActivity.this, "data tidak ditemukan", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    btnhapus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, model.getIdmahasiswa(), Toast.LENGTH_SHORT).show();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    ModelMahasiswa mhs = data_mhs.get(posisi);
                                    mhs.deleteFromRealm();
                                    Toast.makeText(MainActivity.this, "data terhapus", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    tampilData();
                                }
                            });
                        }
                    });
                    dialog.show();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    private void tampilData() {
        // panggil semua data yang disimpan di dalam model mahasiswa
        data_mhs =
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
