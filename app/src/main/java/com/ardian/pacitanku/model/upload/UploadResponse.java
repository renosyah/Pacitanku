package com.ardian.pacitanku.model.upload;

import com.ardian.pacitanku.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class UploadResponse extends BaseModel {
    @SerializedName("file_name")
    public String fileName = "";

    @SerializedName("url")
    public String url = "";

    public UploadResponse(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
