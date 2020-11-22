package com.ardian.pacitanku.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ardian.pacitanku.BuildConfig;
import com.ardian.pacitanku.R;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.ui.activity.detailEvent.DetailEventActivity;
import com.ardian.pacitanku.util.SerializableSave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;
import static com.ardian.pacitanku.util.DateFormat.ISO_8601_FORMAT;
import static com.ardian.pacitanku.util.Util.getDate;


// firebase service yang bertugas menangani notifkasi
// saat aplikasi aktif maupun tidak
public class FirebaseNotificationService extends FirebaseMessagingService {

    // context yg digunakan oleh service
    private Context context;

    // instance firebase database untuk keperluan
    // mengakses database
    private FirebaseDatabase firebaseDatabase;

    // instance firebase messaging untuk keperluan
    // meneriman notifikasi base dari topik yang
    // disubscibe
    private FirebaseMessaging firebaseMessaging;

    // filter intent untuk broadcast receiver
    // untuk memfilter intent2 apa saja yang bisa diterima
    private IntentFilter s_intentFilter = new IntentFilter();

    // instance broadcast receiver
    // untuk mmenerima broadcast dari proses lain
    private BroadcastReceiver timeChangedReceiver;

    // konstruksi
    public FirebaseNotificationService() {
        super();
    }

    // akan dipanggil saat token baru diberikan
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onCreate() {

        // inisialisai konteks
        context = this;

        // inisialisasi firebase messaging
        firebaseMessaging = FirebaseMessaging.getInstance();

        // subscibe ke topik
        firebaseMessaging.subscribeToTopic(BuildConfig.TOPIC)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    // dan jika berhasil
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e("sub","event subscribe");
                        }
                    }
                });

        // inisialisasi firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();

        // tambahkan filter untuk setiap menit berlalu
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);

        // inisialisasi broadcast receiver
        timeChangedReceiver = new BroadcastReceiver() {

            // pada saat menerima
            @Override
            public void onReceive(Context ctx, Intent intent) {

                // jika intent null
                if (intent.getAction() == null){

                    // berhenti
                    return;
                }

                // jika itu intent event setiap menit yang berlalu maka
                if (intent.getAction().equals(Intent.ACTION_TIME_TICK)){

                    // query database
                    DatabaseReference ref = firebaseDatabase.getReference(BuildConfig.DB);
                    ref.child("events")
                            .orderByChild("date_string")
                            .startAt(ISO_8601_FORMAT.format(getDate(6)))
                            .endAt(ISO_8601_FORMAT.format(getDate(8)))
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                // pada saat data didapat dapat
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    // iterasi array
                                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                                        // dapatkan data event
                                        EventModel event = postSnapshot.getValue(EventModel.class);

                                        // check jika tidak null dan belum disimpan
                                        // notif cache
                                        if ((event != null) && (new SerializableSave(ctx,event.id).load() == null)){

                                            // simpan cache berdasakan id
                                            new SerializableSave(ctx,event.id).save(new SerializableSave.SimpleCache(event.id));

                                            // kirim notifikasi
                                            sendNotification(event);

                                            // paksa selesai loop
                                            break;
                                        }

                                    }
                                }

                                // pada saat batal
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }
        };

        // register broadcast receiver
        registerReceiver(timeChangedReceiver, s_intentFilter);
        super.onCreate();
    }

    // fungsi menghandle pesan yang datang
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // jika pesan tidak kosong
        if (remoteMessage.getData().size() > 0) {

            // dapatkan id eventnya
            String id = remoteMessage.getData().get("id");

            // query dari database
            DatabaseReference ref = firebaseDatabase.getReference(BuildConfig.DB);
            ref.child("events")
                    .orderByChild("id")
                    .equalTo(id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // iterasi array
                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                                // dapatkan data event
                                EventModel event = postSnapshot.getValue(EventModel.class);

                                // check jika tidak null dan belum disimpan
                                // notif cache
                                if ((event != null) ){

                                    // kirim notifikasi
                                    sendNotification(event);

                                    // paksa selesai loop
                                    break;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    // pada saat service jalan
    @Override
    protected Intent getStartCommandIntent(Intent intent) {

        // register broadcast receiver
        registerReceiver(timeChangedReceiver, s_intentFilter);
        return super.getStartCommandIntent(intent);
    }

    // const variabel untuk id notifikasi channel
    private static final int ONGOING_NOTIFICATION_ID = new Random(System.currentTimeMillis()).nextInt(100);

    // fungsi mengirim notifikasi
    private void sendNotification(EventModel eventModel) {

        // persiapkan intent
        Intent mapIntent = new Intent(context, DetailEventActivity.class);

        // letakan payload
        mapIntent.putExtra("event",eventModel);

        // tambahkan flag 1
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // tambahkan flag 2
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // tambahkan flag 3
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // tambahkan ke pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mapIntent,
                PendingIntent.FLAG_ONE_SHOT);

        // tentukan chanel id
        // dan notikasi
        String channelId = BuildConfig.APPLICATION_ID + ".NOTIFICATION_CHANNEL";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(eventModel.name)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        // check service notifikasi
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // check versi jika oreo keatas maka
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // buat notifikasi channel
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // notikasi tampikan
        notificationManager.notify(ONGOING_NOTIFICATION_ID, notificationBuilder.build());
    }


    // pada saat dihancurkan
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

            // hilangkan reistrasi
            unregisterReceiver(timeChangedReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // pada saat dihilangkan
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // hancurkan
        onDestroy();
    }
}
