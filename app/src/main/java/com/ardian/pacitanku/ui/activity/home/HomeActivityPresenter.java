package com.ardian.pacitanku.ui.activity.home;

import androidx.annotation.NonNull;

import com.ardian.pacitanku.BuildConfig;
import com.ardian.pacitanku.model.event.EventModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

                    }
                });
    }

    @Override
    public void addEvent(@NonNull EventModel e) {
        DatabaseReference ref = database.getReference(BuildConfig.DB);
        ref.child("events").child(e.id).setValue(e.clone());
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
