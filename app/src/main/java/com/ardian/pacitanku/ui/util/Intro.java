package com.ardian.pacitanku.ui.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardian.pacitanku.R;
import com.squareup.picasso.Picasso;

public class Intro {
    private Context context;
    private View view;

    public Intro(Context context, View view) {
        this.context = context;
        this.view = view;
        initView();
    }

    public void setContent(String url,String title,String content){
        Picasso.get().load(url).into(this.image);
        this.title.setText(title);
        this.content.setText(content);
    }

    private ImageView image;
    private TextView title,content;

    private void initView(){
        this.image = view.findViewById(R.id.image_imageview);
        this.title =  view.findViewById(R.id.title_textview);
        this.content =  view.findViewById(R.id.content_textview);
    }

    public void setVisibility(int flag){
        this.view.setVisibility(flag);
    }
}
