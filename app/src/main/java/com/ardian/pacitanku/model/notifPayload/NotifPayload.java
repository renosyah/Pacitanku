package com.ardian.pacitanku.model.notifPayload;

import com.ardian.pacitanku.model.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class NotifPayload extends BaseModel {

    @SerializedName("api_key")
    public String apiKey = "";

    @SerializedName("topic")
    public String topic = "";

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
