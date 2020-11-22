package com.ardian.pacitanku.ui.activity.register;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses bisnis aplikasi
// seperti query ke db
public class RegisterActivityPresenter implements RegisterActivityContract.Presenter {

    // deklarasi view
    private RegisterActivityContract.View view;

    // deklarasi firebase
    private FirebaseAuth mAuth;

    // fungsi register
    @Override
    public void register(@NonNull String email, String password,Boolean enableLoading) {

        // jika loading digunakan maka
        if (enableLoading) {

            // tampilkan
            view.showProgressRegister(true);
        }

        // buat user baru dengan email dan password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // jika loading digunakan maka
                        if (enableLoading) {

                            // matikan
                            view.showProgressRegister(false);
                        }

                        // jika task sukses maka
                        if (task.isSuccessful()) {

                            // dan jika user berhasil login
                            // check user yang saat ini login
                            FirebaseUser user = mAuth.getCurrentUser();

                            // jika tidak kosong maka user
                            // berhasil register
                            if (user != null) view.onRegister(user);

                        } else {
                            // user gagal register
                           view.showErrorRegister(((Context) view).getString(R.string.register_fail));
                        }
                    }
                });
    }

    @Override
    public void update( @NonNull String name,Boolean enableLoading) {

        // jika loading digunakan maka
        if (enableLoading) {

            // tampilkan
            view.showProgressUpdate(true);
        }
        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        if (user != null) user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // jika loading digunakan maka
                        if (enableLoading) {

                            // matikan
                            view.showProgressUpdate(true);
                        }

                        // jika task sukses maka
                        if (task.isSuccessful()) {

                            // dan jika user berhasil login
                            // check user yang saat ini login
                            FirebaseUser user = mAuth.getCurrentUser();

                            // jika tidak kosong maka user
                            // berhasil diupdate
                            if (user != null) view.onUpdate(user);

                        } else {

                            // gagal update user
                            view.showErrorUpdate(((Context) view).getString(R.string.update_fail));
                        }
                    }
                });
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
    public void attach(RegisterActivityContract.View view) {
        this.view = view;
    }
}
