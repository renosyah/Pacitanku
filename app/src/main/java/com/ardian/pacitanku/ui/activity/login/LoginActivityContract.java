package com.ardian.pacitanku.ui.activity.login;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.base.BaseContract;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivityContract {

    public interface View extends BaseContract.View {
        public void showProgressLogin(Boolean show);
        public void showErrorLogin(String error);
        void onLogin(@NonNull FirebaseUser user);
        void onLoginSession(@NonNull FirebaseUser user);

    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void login(@NonNull String email,String password,Boolean enableLoading);
        void checkSession();
    }
}
