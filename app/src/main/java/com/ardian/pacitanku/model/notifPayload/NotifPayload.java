package com.ardian.pacitanku.model.notifPayload;

import com.ardian.pacitanku.model.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

// model yang akan digunakan untuk mengirim notif
// ke server custom lalu ke firebase
public class NotifPayload extends BaseModel {

    // api key server
    @SerializedName("api_key")
    public String apiKey = "";

    // topic notifikasi yang akan di kirimkan
    @SerializedName("topic")
    public String topic = "";

    // data payload berupa map
    @SerializedName("data")
    public Map<String,String> data;

    public NotifPayload(String apiKey, String topic, Map<String, String> data) {
        this.apiKey = apiKey;
        this.topic = topic;
        this.data = data;
    }

    public NotifPayload clone(){
        return new NotifPayload(
                this.apiKey,
                this.topic,
                this.data
        );
    }
}
