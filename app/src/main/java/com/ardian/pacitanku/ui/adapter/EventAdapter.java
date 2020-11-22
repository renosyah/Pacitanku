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
import com.ardian.pacitanku.util.DateFormat;
import com.ardian.pacitanku.util.Unit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


// adapter untuk tampilan list event
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Holder> {

    // konteks yg dipakai
    private Context context;

    // list data
    private ArrayList<EventModel> eventModels;

    // callback klik edit dan hapus
    private Unit<EventModel> onClick,onEdit,onDelete;

    // konstruktor
    public EventAdapter(Context context, ArrayList<EventModel> eventModels, Unit<EventModel> onClick,Unit<EventModel> onEdit,Unit<EventModel> onDelete) {
        this.context = context;
        this.eventModels = eventModels;
        this.onClick = onClick;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    // apakah opsi diperbolehkan
    // ke false
    private Boolean enableOpt = false;

    // fungsi untuk set opsi
    public void setEnableOpt(Boolean enableOpt) {
        this.enableOpt = enableOpt;
    }

    // pada saat view akan dibuat
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // buat view
        return new Holder(((Activity) context).getLayoutInflater().inflate(R.layout.adapter_event,parent,false));
    }

    // pada saat di binding dengan data
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        // ambil data dari posisi
        EventModel item = eventModels.get(position);

        // set image url ke image
        Picasso.get().load(item.imageUrl).into(holder.image);

        // set text url ke nama
        holder.title.setText(item.name);

        // set text url ke deskripsi
        holder.content.setText(item.description);

        // set text url ke tanggal
        holder.date.setText(DateFormat.simpleDate(new Date(item.date)));

        // set visibilitas opsi layout
        holder.opt.setVisibility(View.GONE);

        // pada saat diklik
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.invoke(item);
            }
        });

        // jika opsi di true maka
        // pada saat di tahan maka
        if (enableOpt) holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // tampilkan layout opsi
                holder.opt.setVisibility(holder.opt.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                return true;
            }
        });

        // pada saat text edit diklik maka
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // invoke callback fungsi edit
                onEdit.invoke(item);
            }
        });

        // pada saat text hapus diklik maka
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // invoke callback fungsi hapus
                onDelete.invoke(item);
            }
        });
    }

    // panjang list
    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    // holder untuk menahan view
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
