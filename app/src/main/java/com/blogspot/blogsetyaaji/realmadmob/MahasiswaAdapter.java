package com.blogspot.blogsetyaaji.realmadmob;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by AJISETYA on 8/19/2017.
 */

class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {
    // deklarasi komponen
    Context context;
    List<ModelMahasiswa> data;
    LayoutInflater layoutInflater;

    public MahasiswaAdapter(MainActivity mainActivity, RealmResults<ModelMahasiswa> data_mhs) {
        //masukan data ke dalam variabel milik MahasiswaAdapter
        context = mainActivity;
        data = data_mhs;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(
                R.layout.item_mahasiswa
                , parent
                , false
        );
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtlistnama)
        TextView txtlistnama;
        @BindView(R.id.txtlistjurusan)
        TextView txtlistjurusan;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
