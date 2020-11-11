package com.ardian.pacitanku.ui.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.util.Unit;

public class NavMenu {
    public static final int HOME_MENU = 0;
    public static final int EVENT_MENU = 1;

    private int currentMenu = HOME_MENU;

    private Context context;
    private View view;

    private Unit<Integer> onMenuClick;

    public NavMenu(Context context, View view, Unit<Integer> onMenuClick) {
        this.context = context;
        this.view = view;
        this.onMenuClick = onMenuClick;
        initView();
    }

    private CardView home,event;
    private TextView labelHome,labelEvent;
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clear();
            switch (v.getId()){
                case R.id.home_cardview:
                    home.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
                    labelEvent.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
                    currentMenu = HOME_MENU;
                    break;
                case R.id.event_menu_cardview:
                    event.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
                    labelHome.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
                    currentMenu = EVENT_MENU;
                    break;
                default:
                    break;
            }
            onMenuClick.invoke(currentMenu);
        }
    };

    private void clear() {
        this.home.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        this.event.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        this.labelEvent.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        this.labelHome.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
    }

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

    public void setVisibility(int flag){
        this.view.setVisibility(flag);
    }
}
