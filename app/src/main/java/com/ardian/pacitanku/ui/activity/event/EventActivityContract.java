package com.ardian.pacitanku.ui.activity.event;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.base.BaseContract;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.upload.UploadResponse;

import okhttp3.MultipartBody;

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
public class EventActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    public interface View extends BaseContract.View {
        void showProgressSetEvent(Boolean show);
        void showErrorSetEvent(String error);
        void onSetEvent();

        void showProgressUpload(Boolean show);
        void showErrorUpload(String error);
        void onUploaded(UploadResponse response);
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    public interface Presenter extends BaseContract.Presenter<View> {
        void setEvent(@NonNull EventModel event, Boolean enableLoading);
        void upload(MultipartBody.Part file, Boolean enableLoading);
    }
}
