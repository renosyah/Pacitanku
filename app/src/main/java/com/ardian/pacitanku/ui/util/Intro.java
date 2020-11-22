package com.ardian.pacitanku.ui.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardian.pacitanku.R;
import com.squareup.picasso.Picasso;


// class yang akan digunakan untuk
// menampilkan layout header pada home
public class Intro {

    // konteks yang digunakan
    private Context context;

    // view yang digunakan
    private View view;


    // konstruksi
    public Intro(Context context, View view) {
        this.context = context;
        this.view = view;

        // inisialisasi view
        initView();
    }

    // set kontent
    public void setContent(String url,String title,String content){

        // set gambar ke layout
        Picasso.get().load(url).into(this.image);

        // set text ke title
        this.title.setText(title);

        // set text ke text
        this.content.setText(content);
    }

    // image yang dipakai
    private ImageView image;

    // text yang dipakai
    private TextView title,content;

    // fungsi inisialisasi view
    private void initView(){
        this.image = view.findViewById(R.id.image_imageview);
        this.title =  view.findViewById(R.id.title_textview);
        this.content =  view.findViewById(R.id.content_textview);
    }

    // fungsi untuk set apakah layout
    // akan tampil atau tidak
    public void setVisibility(int flag){
        this.view.setVisibility(flag);
    }
}
