package com.ardian.pacitanku.model.event;

import com.ardian.pacitanku.model.BaseModel;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@IgnoreExtraProperties
public class EventModel extends BaseModel {

    @PropertyName("id")
    public String id;

    @PropertyName("name")
    public String name;

    @PropertyName("date")
    public long date;

    @PropertyName("time")
    public long time;

    @PropertyName("address")
    public String address;

    @PropertyName("image_url")
    public String imageUrl;

    @PropertyName("reminder")
    public long reminder;

    @PropertyName("description")
    public String description;

    public EventModel() {
        super();
    }

    public EventModel(String id, String name, long date, long time, String address, String image,long reminder, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.address = address;
        this.imageUrl = image;
        this.reminder = reminder;
        this.description = description;
    }

    public EventModel(String id, String name,long date,String image, String description) {
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
            this.time,
            this.address,
            this.imageUrl,
            this.reminder,
            this.description
        );
    }
}
