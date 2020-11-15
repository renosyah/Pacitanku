package com.ardian.pacitanku.ui.activity.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ardian.pacitanku.BuildConfig;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.userType.UserType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeActivityPresenter implements HomeActivityContract.Presenter {

    private HomeActivityContract.View view;
    private FirebaseDatabase database;

    @Override
    public void getEvents(@NonNull int limit) {
        DatabaseReference ref = database.getReference(BuildConfig.DB);
        ref.child("events")
                .orderByChild("name")
                .limitToFirst(limit)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<EventModel> events = new ArrayList<>();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            EventModel event = postSnapshot.getValue(EventModel.class);
                            events.add(event);
                        }
                        view.onGetEvents(events);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        view.showErrorGetEvents(error.getMessage());
                    }
                });
    }

    @Override
    public void deleteEvents(@NonNull String id) {
        DatabaseReference ref = database.getReference(BuildConfig.DB).child("events");
        ref.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                view.onDeleteEvents();
            }
        });

    }

    @Override
    public void getUserType(@NonNull String uid) {
        database.getReference(BuildConfig.DB)
                .child("users")
                .orderByChild("id").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            UserType userType = postSnapshot.getValue(UserType.class);
                            if (userType != null && userType.id.equals(uid)){
                                view.onGetUserType(userType);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    @Override
    public void subscribe() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void attach(HomeActivityContract.View view) {
        this.view = view;
    }

}
