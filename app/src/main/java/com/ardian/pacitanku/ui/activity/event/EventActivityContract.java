package com.ardian.pacitanku.ui.activity.event;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.base.BaseContract;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.upload.UploadResponse;

import okhttp3.MultipartBody;

public class EventActivityContract {
    public interface View extends BaseContract.View {
        public void showProgressSetEvent(Boolean show);
        public void showErrorSetEvent(String error);
        public void onSetEvent();

        public void showProgressUpload(Boolean show);
        public void showErrorUpload(String error);
        public void onUploaded(UploadResponse response);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        public void setEvent(@NonNull EventModel event);
        public void upload(MultipartBody.Part file, Boolean enableLoading);
    }
}
