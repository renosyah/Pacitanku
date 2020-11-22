package com.ardian.pacitanku.ui.activity.home;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.base.BaseContract;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.userType.UserType;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
public class HomeActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    public interface View extends BaseContract.View {
        void showProgressGetEvents(Boolean show);
        void showErrorGetEvents(String error);
        void onGetEvents(@NonNull ArrayList<EventModel> event);

        void showProgressDeleteEvents(Boolean show);
        void showErrorDeleteEvents(String error);
        void onDeleteEvents();

        void onGetUserType(@NonNull UserType userType);
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    public interface Presenter extends BaseContract.Presenter<View> {
        void getEvents(@NonNull int limit);
        void deleteEvents(@NonNull String id);
        void getUserType(@NonNull String uid);
    }
}
