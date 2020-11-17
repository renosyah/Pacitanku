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

public class DetailEventActivity extends AppCompatActivity {

    private Context context;
    private Intent intent;
    private EventModel event;

    private ImageView back,image,push;
    private TextView title,date,description;
    private CardView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        initWidget();
    }

    private void initWidget(){
        this.context = this;
        this.intent = getIntent();

        if (!intent.hasExtra("event")){
            finish();
            return;
        }

        event = (EventModel) intent.getSerializableExtra("event");

        back = findViewById(R.id.back_imageview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image = findViewById(R.id.image_imageview);
        Picasso.get().load(event.imageUrl).into(image);

        title = findViewById(R.id.title_textview);
        title.setText(event.name);

        date = findViewById(R.id.date_textview);
        date.setText(DateFormat.fullDate(new Date(event.date)));

        description = findViewById(R.id.description_textview);
        description.setText(event.description);

        address = findViewById(R.id.address_cardview);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + event.address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

    }
}