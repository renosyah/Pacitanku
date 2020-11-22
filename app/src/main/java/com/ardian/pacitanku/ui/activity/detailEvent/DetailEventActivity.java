package com.ardian.pacitanku.ui.activity.detailEvent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.util.DateFormat;
import com.squareup.picasso.Picasso;

import java.util.Date;

// aktivitas untuk halaman detail event
public class DetailEventActivity extends AppCompatActivity {

    // konteks yang digunankan
    private Context context;

    // intent yang digunakan
    private Intent intent;

    // data model event
    private EventModel event;

    // image vent dan tombol kembali
    private ImageView back,image;

    // text label datnggal dan deskripsi
    private TextView title,date,description;

    // layout lokasi
    private CardView address;

    // fungsi yg dipanggil saat activity
    // dibuat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        // panggil fungsi init widget
        initWidget();
    }
    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    private void initWidget(){
        this.context = this;

        // inisialisasi intent
        this.intent = getIntent();

        // jika tidak ada data event maka
        if (!intent.hasExtra("event")){

            // hancurkan aktivitas saat ini
            finish();
            return;
        }

        // inisialisasi event model
        event = (EventModel) intent.getSerializableExtra("event");

        // inisialisasi tombol kembali
        back = findViewById(R.id.back_imageview);

        // saat diklik
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // hancurkan aktivitas saat ini
                finish();
            }
        });

        // inisialisasi gambar
        image = findViewById(R.id.image_imageview);

        // set url gambar ke image event
        Picasso.get().load(event.imageUrl).into(image);

        // inisialisasi nama event
        title = findViewById(R.id.title_textview);

        // set text ke mana event
        title.setText(event.name);

        // inisialisasi tanggal
        date = findViewById(R.id.date_textview);

        // set text ke tanggal
        date.setText(DateFormat.fullDate(new Date(event.date)));

        // inisialisasi deskripsi
        description = findViewById(R.id.description_textview);

        // set text ke deskripsi
        description.setText(event.description);

        // inisialisasi alamat
        address = findViewById(R.id.address_cardview);

        // pada saat diklik
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // buka google map berdasarkan alamat
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + event.address);

                // buat intent
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // package app yg dituju
                mapIntent.setPackage("com.google.android.apps.maps");

                // mulai aktivitas
                startActivity(mapIntent);
            }
        });

    }
}