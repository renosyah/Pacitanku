package com.ardian.pacitanku.ui.activity.login;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivityPresenter implements LoginActivityContract.Presenter {

    private LoginActivityContract.View view;
    private FirebaseAuth mAuth;

    @Override
    public void login(@NonNull String email, String password,Boolean enableLoading) {
        if (enableLoading) {
            view.showProgressLogin(true);
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (enableLoading) {
                            view.showProgressLogin(false);
                        }
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) view.onLogin(user);
                        } else {
                           view.showErrorLogin(((Context) view).getString(R.string.login_fail));
                        }
                    }
                });
    }

    @Override
    public void subscribe() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void attach(LoginActivityContract.View view) {
        this.view = view;
    }
}
