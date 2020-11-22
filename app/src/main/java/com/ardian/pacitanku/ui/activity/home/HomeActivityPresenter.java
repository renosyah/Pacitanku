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

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses bisnis aplikasi
// seperti query ke db
public class HomeActivityPresenter implements HomeActivityContract.Presenter {

    // deklarasi view
    private HomeActivityContract.View view;

    // deklarasi firebase
    private FirebaseDatabase database;

    @Override
    public void getEvents(@NonNull int limit) {

        // query data event berdasarkan nama
        // dengan limit data yang diambil
        DatabaseReference ref = database.getReference(BuildConfig.DB);
        ref.child("events")
                .orderByChild("name")
                .limitToFirst(limit)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    // saat dapat
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // siapkan variabel penahan
                        ArrayList<EventModel> events = new ArrayList<>();

                        // iterasi array
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                            // buat value dari masing2 data
                            EventModel event = postSnapshot.getValue(EventModel.class);

                            // tambahkan ke array
                            events.add(event);
                        }

                        // invoke event telah didaptkan
                        view.onGetEvents(events);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        // tampilkan error
                        view.showErrorGetEvents(error.getMessage());
                    }
                });
    }

    // fungsi hapus event
    @Override
    public void deleteEvents(@NonNull String id) {

        // hapus event berdasarkan
        // key dari database events
        DatabaseReference ref = database.getReference(BuildConfig.DB).child("events");
        ref.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                // invoke fungsi jika selesai
                view.onDeleteEvents();
            }
        });

    }


    // fungsi untuk mendapatkan tipe user
    // yang saat ini sedang login
    @Override
    public void getUserType(@NonNull String uid) {

        // query mendapatkan tipe user
        // yang saat ini sedang login
        database.getReference(BuildConfig.DB)
                .child("users")
                .orderByChild("id").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // iterasi array
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                            // buat value
                            UserType userType = postSnapshot.getValue(UserType.class);

                            // jika tidak null dan id sama dengan user yang sedang login maka
                            if (userType != null && userType.id.equals(uid)){

                                // invoke fungsi user telah didapatkan
                                view.onGetUserType(userType);

                                // break dari iterasi
                                break;
                            }
                        }
                    }

                    // saat batal
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    // fungsi subscribe
    @Override
    public void subscribe() {

        // inisialisasi firebase instance
        database = FirebaseDatabase.getInstance();
    }


    // untuk saat ini kosong
    // belum dibutuhkan
    @Override
    public void unsubscribe() {

    }

    // fungsi yg akan menrima data view
    // yg nantinya akan digunakan oleh viewmodel
    // atau untuk keperluhan bisnis aplikasi
    // lainya
    @Override
    public void attach(HomeActivityContract.View view) {
        this.view = view;
    }

}
