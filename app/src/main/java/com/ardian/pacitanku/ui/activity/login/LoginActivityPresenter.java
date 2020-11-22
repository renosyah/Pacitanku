package com.ardian.pacitanku.ui.activity.login;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.BuildConfig;
import com.ardian.pacitanku.R;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.userType.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses bisnis aplikasi
// seperti query ke db
public class LoginActivityPresenter implements LoginActivityContract.Presenter {

    // deklarasi view
    private LoginActivityContract.View view;

    // deklarasi firebase
    private FirebaseAuth mAuth;

    // fungsi untuk login
    @Override
    public void login(@NonNull String email, String password,Boolean enableLoading) {

        // jika loading digunakan maka
        if (enableLoading) {

            // tampilkan
            view.showProgressLogin(true);
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // jika loading digunakan maka
                        if (enableLoading) {

                            // hilangkan
                            view.showProgressLogin(false);
                        }

                        // jika task sukses maka
                        if (task.isSuccessful()) {

                            // dan jika user berhasil login
                            // check user yang saat ini login
                            FirebaseUser user = mAuth.getCurrentUser();

                            // jika tidak kosong maka user
                            // berhasil login
                            if (user != null) view.onLogin(user);

                        } else {
                            // user gagal login
                            view.showErrorLogin(((Context) view).getString(R.string.login_fail));
                        }
                    }
                });
    }

    // fungsi check sesi
    @Override
    public void checkSession() {

        // buat instance
        FirebaseUser user = mAuth.getCurrentUser();

        // jika ada sesi maka invoke fungsi login dengan sesi
        if (user != null) view.onLoginSession(user);
    }

    // fungsi subscribe
    @Override
    public void subscribe() {

        // inisialisasi firebase instance
        mAuth = FirebaseAuth.getInstance();
    }


    // untuk saat ini kosong
    // belum dibutuhkan
    @Override
    public void unsubscribe() {

    }

    // fungsi yg akan menrima data view
    // yg nantinya akan digunakan oleh viewmodel
    // atau untuk keperluhan bisnis aplikasi
    // lainya
    @Override
    public void attach(LoginActivityContract.View view) {
        this.view = view;
    }
}
