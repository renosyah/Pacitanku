package com.ardian.pacitanku.ui.activity.event;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.BuildConfig;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.notifPayload.NotifPayload;
import com.ardian.pacitanku.model.responseModel.ResponseModel;
import com.ardian.pacitanku.model.upload.UploadResponse;
import com.ardian.pacitanku.service.RetrofitService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

public class EventActivityPresenter implements EventActivityContract.Presenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();
    private EventActivityContract.View view;
    private FirebaseDatabase database;

    @Override
    public void setEvent(@NonNull EventModel event) {
        HashMap<String ,String> data = new HashMap<>();
        data.put("name",event.name);
        data.put("address",event.address);

        NotifPayload notifPayload = new NotifPayload(
                "AAAAsldiFMw:APA91bEXW9ukrALB1OiZSInImXAIJAqESdbJwX1B-oS1AFn5vocrW2WK27IcR0alEG3RylJ_tyWKzO_SEYASz2VKf0p8JdZLOJFDfckwOUP08c7x3vr8zaCGIyu-fwtOP8kM0LJlZQ2b",
                BuildConfig.TOPIC,
                data
        );

        Disposable subscribe = RetrofitService.create("https://go-firebase-notif-sender.herokuapp.com/").push(notifPayload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody result) throws Exception {
                        DatabaseReference ref = database.getReference(BuildConfig.DB);
                        ref.child("events").child(event.id).setValue(event.clone());
                        view.onSetEvent();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("error push",throwable.toString());
                        view.showErrorSetEvent(throwable.getMessage());
                    }
                });

        subscriptions.add(subscribe);
    }

    @Override
    public void upload(MultipartBody.Part file, Boolean enableLoading) {
        if (enableLoading){
            view.showProgressUpload(true);
        }
        Disposable subscribe = RetrofitService.create("").upload(file)
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
