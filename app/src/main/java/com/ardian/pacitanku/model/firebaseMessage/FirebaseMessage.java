package com.ardian.pacitanku.model.firebaseMessage;

import com.ardian.pacitanku.model.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class FirebaseMessage extends BaseModel {

    @SerializedName("Message")
    public Message message;

    public FirebaseMessage(Message message) {
        this.message = message;
    }

    public FirebaseMessage clone(){
        return new FirebaseMessage(this.message.clone());
    }

    public static class Message {

        @SerializedName("condition")
        public String condition;

        @SerializedName("data")
        public Map<String,String> data;

        public Message(String condition, Map<String, String> data) {
            this.condition = condition;
            this.data = data;
        }

        public Message clone(){
            return new Message(this.condition,this.data);
        }
    }

}
