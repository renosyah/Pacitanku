package com.ardian.pacitanku.ui.activity.register;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.base.BaseContract;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivityContract {

    public interface View extends BaseContract.View {
        public void showProgressRegister(Boolean show);
        public void showErrorRegister(String error);
        void onRegister(@NonNull FirebaseUser user);


        public void showProgressUpdate(Boolean show);
        public void showErrorUpdate(String error);
        void onUpdate(@NonNull FirebaseUser user);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void register(@NonNull String email, String password, Boolean enableLoading);
        void update( @NonNull String name, Boolean enableLoading);
    }
}
