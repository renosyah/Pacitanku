package com.ardian.pacitanku.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.util.Unit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Holder> {

    private Context context;
    private ArrayList<EventModel> eventModels;
    private Unit<EventModel> onClick,onEdit,onDelete;

    public EventAdapter(Context context, ArrayList<EventModel> eventModels, Unit<EventModel> onClick,Unit<EventModel> onEdit,Unit<EventModel> onDelete) {
        this.context = context;
        this.eventModels = eventModels;
        this.onClick = onClick;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(((Activity) context).getLayoutInflater().inflate(R.layout.adapter_event,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        EventModel item = eventModels.get(position);

        Picasso.get().load(item.imageUrl).into(holder.image);
        holder.title.setText(item.name);
        holder.content.setText(item.description);
        holder.date.setText(new Date(item.date).toString());

        holder.opt.setVisibility(View.GONE);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.invoke(item);
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.opt.setVisibility(holder.opt.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                return true;
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit.invoke(item);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete.invoke(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public LinearLayout layout,opt;
        public ImageView image;
        public TextView date,title,content,edit,delete;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.layout_linearlayout);
            this.image = itemView.findViewById(R.id.image_imageview);
            this.date = itemView.findViewById(R.id.date_textview);
            this.title = itemView.findViewById(R.id.title_textview);
            this.content = itemView.findViewById(R.id.content_textview);
            this.opt = itemView.findViewById(R.id.opt_layout_linearlayout);
            this.edit = itemView.findViewById(R.id.opt_edit_textview);
            this.delete = itemView.findViewById(R.id.opt_delete_textview);
        }
    }
}
