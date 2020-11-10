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

public class RegisterActivityPresenter implements RegisterActivityContract.Presenter {

    private RegisterActivityContract.View view;
    private FirebaseAuth mAuth;

    @Override
    public void register(@NonNull String email, String password,Boolean enableLoading) {
        if (enableLoading) {
            view.showProgressRegister(true);
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (enableLoading) {
                            view.showProgressRegister(false);
                        }
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) view.onRegister(user);
                        } else {
                           view.showErrorRegister(((Context) view).getString(R.string.register_fail));
                        }
                    }
                });
    }

    @Override
    public void update( @NonNull String name,Boolean enableLoading) {
        if (enableLoading) {
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
                        if (enableLoading) {
                            view.showProgressUpdate(true);
                        }
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) view.onUpdate(user);
                        } else {
                            view.showErrorUpdate(((Context) view).getString(R.string.update_fail));
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
    public void attach(RegisterActivityContract.View view) {
        this.view = view;
    }
}
