package com.ardian.pacitanku.model.userType;

import com.ardian.pacitanku.model.BaseModel;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

// model untuk menyimpan data
// sesi user yang sedang menggunakan
// aplikasi
@IgnoreExtraProperties
public class UserType extends BaseModel {

    // const tipe admin
    public static final String ADMIN = "admin";

    // id user
    @PropertyName("id")
    public String id;

    // type user privilige
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
