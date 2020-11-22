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

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses bisnis aplikasi
// seperti query ke db
public class EventActivityPresenter implements EventActivityContract.Presenter {

    // deklarasi subscribtion composite
    private CompositeDisposable subscriptions = new CompositeDisposable();

    // deklarasi view
    private EventActivityContract.View view;

    // deklarasi firebase
    private FirebaseDatabase database;

    // fungsi set event
    @Override
    public void setEvent(@NonNull EventModel event,@NonNull Boolean enableLoading) {

        // check jika loading dipakai maka
        if (enableLoading){

            // tampilkan loading
            view.showProgressUpload(true);
        }


        // siapkan data berupa map dan
        // isi nama serta alamat event
        HashMap<String ,String> data = new HashMap<>();
        data.put("name",event.name);
        data.put("address",event.address);


        // set payload setting
        NotifPayload notifPayload = new NotifPayload(
                "AAAAsldiFMw:APA91bEXW9ukrALB1OiZSInImXAIJAqESdbJwX1B-oS1AFn5vocrW2WK27IcR0alEG3RylJ_tyWKzO_SEYASz2VKf0p8JdZLOJFDfckwOUP08c7x3vr8zaCGIyu-fwtOP8kM0LJlZQ2b",
                BuildConfig.TOPIC,
                data
        );

        // tambahkan event ke database firebase
        DatabaseReference ref = database.getReference(BuildConfig.DB);
        ref.child("events").child(event.id).setValue(event.clone());

        // kirimkan notifikasi by server
        Disposable subscribe = RetrofitService.create("https://go-firebase-notif-sender.herokuapp.com/").push(notifPayload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody result) throws Exception {

                        // check jika loading dipakai maka
                        if (enableLoading) {

                            // matikan loading
                            view.showProgressUpload(false);
                        }

                        // invoke event telah di set
                        view.onSetEvent();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        // check jika loading dipakai maka
                        if (enableLoading) {

                            // matikan loading
                            view.showProgressUpload(false);
                        }

                        // invoke event telah di set
                        view.onSetEvent();
                    }
                });

        // kirimkan
        subscriptions.add(subscribe);
    }

    @Override
    public void upload(MultipartBody.Part file, Boolean enableLoading) {

        // check jika loading dipakai maka
        if (enableLoading){

            // tampilkan loading
            view.showProgressUpload(true);
        }

        // kirimkan gambar ke server
        Disposable subscribe = RetrofitService.create("").upload(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseModel<UploadResponse>>() {
                    @Override
                    public void accept(ResponseModel<UploadResponse> result) throws Exception {

                        // check jika loading dipakai maka
                        if (enableLoading) {

                            // matikan loading
                            view.showProgressUpload(false);
                        }

                        // check jika result tidak null
                        if (result != null){

                            // check jika error tidak kosong
                            if (result.Error != null && !result.Error.isEmpty()){

                                // tampilkan pesan error
                                view.showErrorUpload(result.Error);
                            }

                            // invoke gambar telah di upload
                            view.onUploaded(result.Data);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        // check jika loading dipakai maka
                        if (enableLoading) {

                            // matikan loading
                            view.showProgressUpload(false);
                        }

                        // tampilkan error
                        view.showErrorUpload(throwable.getMessage());
                    }
                });

        // kirimkan
        subscriptions.add(subscribe);
    }

    // fungsi subscribe
    @Override
    public void subscribe() {

        // inisialisasi firebase instance
        database = FirebaseDatabase.getInstance();
    }

    // untuk saat ini kosong
    // belum dibutuhkan
    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    // fungsi yg akan menrima data view
    // yg nantinya akan digunakan oleh viewmodel
    // atau untuk keperluhan bisnis aplikasi
    // lainya
    @Override
    public void attach(EventActivityContract.View view) {
        this.view = view;
    }
}
