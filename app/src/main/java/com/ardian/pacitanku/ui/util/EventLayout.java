package com.ardian.pacitanku.ui.util;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ardian.pacitanku.R;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.ui.adapter.EventAdapter;
import com.ardian.pacitanku.util.Unit;

import java.util.ArrayList;


// class untuk menampilkan layout list event
public class EventLayout {

    // konteks yg dipakai
    private Context context;

    // vie yg dipakai
    private View view;

    // konstruktor
    public EventLayout(Context context, View view) {
        this.context = context;
        this.view = view;

        // panggil fungsi init view
        initView();
    }

    // adapter yang digunakan
    private EventAdapter adapter;

    // flag apakah boleh mengaktifkan layout
    // fungsi edit dan hapus
    private Boolean enableOpt = false;

    // fungsi untuk set apakah boleh mengaktifkan layout
    // fungsi edit dan hapus
    public void setEnableOpt(Boolean enableOpt) {
        if (adapter != null) adapter.setEnableOpt(enableOpt);
    }

    // fungsi untuk set content
    public void setContent(ArrayList<EventModel> eventModels,Unit<EventModel> onEventClick,Unit<EventModel> onEdit,Unit<EventModel> onDelete){

        // inisialisasi adapter
        adapter = new EventAdapter(context, eventModels, onEventClick,onEdit,onDelete);

        // inisialisasi adapter
        adapter.setEnableOpt(enableOpt);

        // inisialisasi adapter
        event.setAdapter(adapter);

        // inisialisasi adapter
        event.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
    }

    // fungsi untuk notify
    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

    // fungsi untuk menampilkan judul yg kecil
    public void showSmallTittle(){
        this.bigTitle.setVisibility(View.GONE);
        this.smallTitle.setVisibility(View.VISIBLE);
    }

    // fungsi untuk menampilkan judul yg besar
    public void showBigTittle(){
        this.bigTitle.setVisibility(View.VISIBLE);
        this.smallTitle.setVisibility(View.GONE);
    }

    // deklarasi judul kecil dan besar
    private TextView bigTitle,smallTitle;

    // dekalrasi recycleview event
    private RecyclerView event;

    // fungsi untuk inisialisasi view
    private void initView(){
        this.bigTitle = view.findViewById(R.id.big_title_textview);
        this.smallTitle = view.findViewById(R.id.small_title_textview);
        this.event = view.findViewById(R.id.events_recylcleview);
    }

    // fungsi untuk set apakah layout
    // akan tampil atau tidak
    public void setVisibility(int flag){
        this.view.setVisibility(flag);
    }
}
