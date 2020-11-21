package com.ardian.pacitanku.model.upload;

import com.ardian.pacitanku.model.BaseModel;
import com.google.gson.annotations.SerializedName;

// model yang akan digunakan untuk
// mendapat response dari mengupload image
// ke server custom
public class UploadResponse extends BaseModel {

    // nama file
    @SerializedName("file_name")
    public String fileName = "";

    // url path yang dapat
    @SerializedName("url")
    public String url = "";

    public UploadResponse(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
