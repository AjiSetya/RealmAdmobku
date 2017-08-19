package com.blogspot.blogsetyaaji.realmadmob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;

public class TambahActivity extends AppCompatActivity {

    @BindView(R.id.txttambahnama)
    EditText txttambahnama;
    @BindView(R.id.txttambahjurusan)
    EditText txttambahjurusan;
    @BindView(R.id.btntambahsimpan)
    Button btntambahsimpan;

    Realm realm;
    RealmAsyncTask realmAsyncTask;
    RealmConfiguration realmConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        ButterKnife.bind(this);

        inisialisasiRealm(this);
    }

    private void inisialisasiRealm(TambahActivity tambahActivity) {
        realm.init(tambahActivity);
        realmConfiguration = new RealmConfiguration.Builder().build();
        realm = Realm.getDefaultInstance();
    }

    @OnClick(R.id.btntambahsimpan)
    public void onViewClicked() {
        final String nama = txttambahnama.getText().toString();
        final String jurusan = txttambahjurusan.getText().toString();
        if (TextUtils.isEmpty(nama)) {
            txttambahnama.setError("Nama tidak boleh kosong");
            txttambahnama.requestFocus();
        } else if (TextUtils.isEmpty(jurusan)) {
            txttambahjurusan.setError("Jurusan tidak boleh kosong");
            txttambahjurusan.requestFocus();
        } else {
            // simpan data
            realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ModelMahasiswa model = realm.createObject(ModelMahasiswa.class);
                    model.setIdmahasiswa(String
                            .valueOf((int) (System.currentTimeMillis() / 100)));
                    model.setNamamahasiswa(nama);
                    model.setJurusanmahasiswa(jurusan);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(TambahActivity.this, "Data disimpan", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TambahActivity.this, MainActivity.class));
                    finish();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(TambahActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    Log.e("error simpan : ", error.getMessage());
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // cek keberadaan transaksi pengolahan data
        if (realmAsyncTask != null && !realmAsyncTask.isCancelled()){
            realmAsyncTask.cancel();
        }
    }
}
