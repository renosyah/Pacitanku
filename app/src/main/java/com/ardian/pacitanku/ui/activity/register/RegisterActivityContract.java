package com.ardian.pacitanku.ui.activity.register;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.base.BaseContract;
import com.google.firebase.auth.FirebaseUser;

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
public class RegisterActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    public interface View extends BaseContract.View {
        void showProgressRegister(Boolean show);
        void showErrorRegister(String error);
        void onRegister(@NonNull FirebaseUser user);


        void showProgressUpdate(Boolean show);
        void showErrorUpdate(String error);
        void onUpdate(@NonNull FirebaseUser user);
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    public interface Presenter extends BaseContract.Presenter<View> {
        void register(@NonNull String email, String password, Boolean enableLoading);
        void update( @NonNull String name, Boolean enableLoading);
    }
}
