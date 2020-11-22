package com.ardian.pacitanku.ui.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.util.Unit;

// class yang digunakan untuk menampilkan menu navigasi
// antara menu home dan event
public class NavMenu {

    // const variabel untuk menu home
    public static final int HOME_MENU = 0;

    // const variabel untuk menu event
    public static final int EVENT_MENU = 1;

    // flag untuk menu apa yg sekarang digunakan
    private int currentMenu = HOME_MENU;

    // konteks yang dipakai
    private Context context;

    // view yang dipakai
    private View view;

    // callback yang digunakan saat menu dipilih
    private Unit<Integer> onMenuClick;

    // konstruktor
    public NavMenu(Context context, View view, Unit<Integer> onMenuClick) {
        this.context = context;
        this.view = view;
        this.onMenuClick = onMenuClick;

        // inisialisasi view
        initView();
    }

    // layout home dan event yang digunakan
    private CardView home,event;

    // text label
    private TextView labelHome,labelEvent;

    // pada saat sesuatu diklik maka akan
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // bersihkan tampilan
            clear();

            // check view apa yang diklik
            switch (v.getId()){

                // jika itu layout dari menu home
                case R.id.home_cardview:

                    // berikan warna
                    home.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));

                    // berikan warna label
                    labelEvent.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

                    // set menu yang dipakai
                    currentMenu = HOME_MENU;
                    break;

                // jika itu layout dari menu event
                case R.id.event_menu_cardview:

                    // berikan warna
                    event.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));

                    // berikan warna label
                    labelHome.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

                    // set menu yang dipakai
                    currentMenu = EVENT_MENU;
                    break;
                default:
                    break;
            }

            // panggil callback
            onMenuClick.invoke(currentMenu);
        }
    };

    // fungsi set menu
    public void setMenu(int m,Unit<Integer> callBack){

        // bersihkan tampilan
        clear();

        // check menu flag apa yang diklik
        switch (m){

            // jika itu layout dari menu event
            case EVENT_MENU:

                // berikan warna
                event.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));

                // berikan warna label
                labelHome.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

                // set menu yang dipakai
                currentMenu = EVENT_MENU;
                break;

            // jika itu layout dari menu home
            case HOME_MENU:

                // berikan warna
                home.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));

                // berikan warna label
                labelEvent.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

                // set menu yang dipakai
                currentMenu = HOME_MENU;
                break;
            default:
                break;
        }

        // panggil callback
        callBack.invoke(currentMenu);
    }

    // fungsi bersihkan warna
    private void clear() {

        // berikan warna
        this.home.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

        // berikan warna
        this.event.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

        // berikan warna label
        this.labelEvent.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

        // berikan warna label
        this.labelHome.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
    }

    // fungsi inisialisasi view
    private void initView(){
        this.home = view.findViewById(R.id.home_cardview);
        home.setOnClickListener(onClick);
        this.event =  view.findViewById(R.id.event_menu_cardview);
        event.setOnClickListener(onClick);
        this.labelHome =  view.findViewById(R.id.label_home_textview);
        this.labelEvent =  view.findViewById(R.id.label_event_textview);

        home.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        labelEvent.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
        onMenuClick.invoke(currentMenu);
    }

    // fungsi untuk set apakah layout
    // akan tampil atau tidak
    public void setVisibility(int flag){
        this.view.setVisibility(flag);
    }
}
