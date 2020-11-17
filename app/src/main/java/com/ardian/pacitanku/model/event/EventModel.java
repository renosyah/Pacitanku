package com.ardian.pacitanku.model.event;

import com.ardian.pacitanku.model.BaseModel;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@IgnoreExtraProperties
public class EventModel extends BaseModel {

    @PropertyName("id")
    public String id = "";

    @PropertyName("name")
    public String name = "";

    @PropertyName("date")
    public long date = 0L;

    @PropertyName("date_string")
    public String dateString = "";

    @PropertyName("address")
    public String address = "";

    @PropertyName("image_url")
    public String imageUrl = "";

    @PropertyName("reminder")
    public long reminder = 0L;

    @PropertyName("description")
    public String description = "";

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
