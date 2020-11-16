package com.ardian.pacitanku.service;

import com.ardian.pacitanku.BuildConfig;
import com.ardian.pacitanku.model.firebaseMessage.FirebaseMessage;
import com.ardian.pacitanku.model.responseModel.ResponseModel;
import com.ardian.pacitanku.model.upload.UploadResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitService {

        @Multipart
        @POST("/api/upload_file.php")
        public Observable<ResponseModel<UploadResponse>> upload(@Part MultipartBody.Part file);

        // fungsi static yg nantinya akan
        // dipanggil, sama seperti koneksi ke db
        // namun ini untuk api
        public static RetrofitService create()  {

            // deklarasi gson builder
            // fungsinya agar dapat
            // melakukan parsing json
            // meskipun json kurang valid
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // membuat instance retrofit
            // yg nantinya ini yg akan digunakan untuk
            // melakukan request ke api
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BuildConfig.HOSTING_URL)
                    .build();

            // balikan instance
            // sebagai nilai balik
            return retrofit.create(RetrofitService.class);
        }
}
