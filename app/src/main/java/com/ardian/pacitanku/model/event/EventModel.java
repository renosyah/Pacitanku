package com.ardian.pacitanku.model.event;

import com.ardian.pacitanku.model.BaseModel;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.Calendar;

// ini adalah class model event
// yang akan menentukan struktur data event
@IgnoreExtraProperties
public class EventModel extends BaseModel {

    // id event
    @PropertyName("id")
    public String id = "";

    // nama event
    @PropertyName("name")
    public String name = "";

    // tanggal event dilaksanankan
    @PropertyName("date")
    public long date = 0L;

    // tanggal event dalam bentuk string untuk
    // kebutuhan query
    @PropertyName("date_string")
    public String dateString = "";

    // alamat yang akan digunakan
    // untuk menuju ke goggle map
    @PropertyName("address")
    public String address = "";

    // url image event
    @PropertyName("image_url")
    public String imageUrl = "";

    // reminder
    // saat ini masih belum terpakai
    @PropertyName("reminder")
    public long reminder = 0L;

    // deksripsi event
    @PropertyName("description")
    public String description = "";

    // tanggal dibuatnya data event
    @PropertyName("created_at")
    public long createdAt = Calendar.getInstance().getTime().getTime();

    public EventModel() {
        super();
    }

    public EventModel(String id, String name, long date, String dateString, String address, String imageUrl, long reminder, String description, long createdAt) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.dateString = dateString;
        this.address = address;
        this.imageUrl = imageUrl;
        this.reminder = reminder;
        this.description = description;
        this.createdAt = createdAt;
    }

    public EventModel(String id, String name, long date, String image, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.imageUrl = image;
        this.description = description;
    }

    public EventModel clone(){
        return new EventModel(this.id,
            this.name,
            this.date,
            this.dateString,
            this.address,
            this.imageUrl,
            this.reminder,
            this.description,
            this.createdAt
        );
    }
}
