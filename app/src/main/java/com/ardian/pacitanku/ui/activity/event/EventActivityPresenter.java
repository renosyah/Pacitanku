package com.ardian.pacitanku.ui.activity.event;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.BuildConfig;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.responseModel.ResponseModel;
import com.ardian.pacitanku.model.upload.UploadResponse;
import com.ardian.pacitanku.service.RetrofitService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class EventActivityPresenter implements EventActivityContract.Presenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();
    private RetrofitService api  = RetrofitService.create();
    private EventActivityContract.View view;
    private FirebaseDatabase database;

    @Override
    public void setEvent(@NonNull EventModel event) {
        DatabaseReference ref = database.getReference(BuildConfig.DB);
        ref.child("events").child(event.id).setValue(event.clone());
        view.onSetEvent();
    }

    @Override
    public void upload(MultipartBody.Part file, Boolean enableLoading) {
        if (enableLoading){
            view.showProgressUpload(true);
        }
        Disposable subscribe = api.upload(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseModel<UploadResponse>>() {
                    @Override
                    public void accept(ResponseModel<UploadResponse> result) throws Exception {
                        if (enableLoading) {
                            view.showProgressUpload(false);
                        }
                        if (result != null){
                            if (result.Error != null && !result.Error.isEmpty()){
                                view.showErrorUpload(result.Error);
                            }

                            view.onUploaded(result.Data);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (enableLoading) {
                            view.showProgressUpload(false);
                        }
                        view.showErrorUpload(throwable.getMessage());
                    }
                });

        subscriptions.add(subscribe);
    }

    @Override
    public void subscribe() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void attach(EventActivityContract.View view) {
        this.view = view;
    }
}
