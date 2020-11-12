package com.ardian.pacitanku.ui.activity.home;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.base.BaseContract;
import com.ardian.pacitanku.model.event.EventModel;

import java.util.ArrayList;

public class HomeActivityContract {
    public interface View extends BaseContract.View {
        public void showProgressGetEvents(Boolean show);
        public void showErrorGetEvents(String error);
        public void onGetEvents(@NonNull ArrayList<EventModel> event);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        public void getEvents(@NonNull int limit);
    }
}
