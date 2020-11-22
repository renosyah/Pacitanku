package com.ardian.pacitanku.ui.activity.login;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.base.BaseContract;
import com.google.firebase.auth.FirebaseUser;

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
public class LoginActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    public interface View extends BaseContract.View {
        void showProgressLogin(Boolean show);
        void showErrorLogin(String error);
        void onLogin(@NonNull FirebaseUser user);
        void onLoginSession(@NonNull FirebaseUser user);
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    public interface Presenter extends BaseContract.Presenter<View> {
        void login(@NonNull String email,String password,Boolean enableLoading);
        void checkSession();
    }
}
