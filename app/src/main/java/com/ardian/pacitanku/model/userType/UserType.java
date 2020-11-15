package com.ardian.pacitanku.model.userType;

import com.ardian.pacitanku.model.BaseModel;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class UserType extends BaseModel {

    public static final String ADMIN = "admin";

    @PropertyName("id")
    public String id;

    @PropertyName("type")
    public String type;

    public UserType() {
        super();
    }

    public UserType(String id, String type) {
        this.id = id;
        this.type = type;
    }
}
