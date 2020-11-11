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

public class EventLayout {
    private Context context;
    private View view;

    public EventLayout(Context context, View view) {
        this.context = context;
        this.view = view;
        initView();
    }

    private EventAdapter adapter;

    public void setContent(ArrayList<EventModel> eventModels,Unit<EventModel> onEventClick,Unit<EventModel> onEdit,Unit<EventModel> onDelete){
        adapter = new EventAdapter(context, eventModels, onEventClick,onEdit,onDelete);
        event.setAdapter(adapter);
        event.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
    }

    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

    public void showSmallTittle(){
        this.bigTitle.setVisibility(View.GONE);
        this.smallTitle.setVisibility(View.VISIBLE);
    }
    public void showBigTittle(){
        this.bigTitle.setVisibility(View.VISIBLE);
        this.smallTitle.setVisibility(View.GONE);
    }

    private TextView bigTitle,smallTitle;
    private RecyclerView event;

    private void initView(){
        this.bigTitle = view.findViewById(R.id.big_title_textview);
        this.smallTitle = view.findViewById(R.id.small_title_textview);
        this.event = view.findViewById(R.id.events_recylcleview);
    }
    public void setVisibility(int flag){
        this.view.setVisibility(flag);
    }
}
