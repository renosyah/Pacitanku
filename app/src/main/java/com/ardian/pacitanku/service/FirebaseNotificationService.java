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

public class FirebaseNotificationService extends FirebaseMessagingService {

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseMessaging firebaseMessaging;
    private IntentFilter s_intentFilter = new IntentFilter();
    private BroadcastReceiver timeChangedReceiver;

    public FirebaseNotificationService() {
        super();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onCreate() {
        context = this;

        firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic(BuildConfig.TOPIC)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e("sub","event subscribe");
                        }
                    }
                });

        firebaseDatabase = FirebaseDatabase.getInstance();

        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        timeChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction() == null){
                    return;
                }

                if (intent.getAction().equals(Intent.ACTION_TIME_TICK)){

                    DatabaseReference ref = firebaseDatabase.getReference(BuildConfig.DB);
                    ref.child("events")
                            .orderByChild("date_string")
                            .startAt(ISO_8601_FORMAT.format(getDate(6)))
                            .endAt(ISO_8601_FORMAT.format(getDate(8)))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                        EventModel event = postSnapshot.getValue(EventModel.class);
                                        if ((event != null) && (new SerializableSave(ctx,event.id).load() == null)){
                                            new SerializableSave(ctx,event.id).save(new SerializableSave.SimpleCache(event.id));
                                            sendNotification(event);
                                            break;
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }

                Log.e("received broadcast", intent.getAction());
            }
        };

        registerReceiver(timeChangedReceiver, s_intentFilter);

        Log.e("service","on");
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e("data",remoteMessage.getData().toString());

            EventModel e = new EventModel();
            e.name = remoteMessage.getData().get("name");
            e.address = remoteMessage.getData().get("address");
            sendNotification(e);
        }
    }

    @Override
    protected Intent getStartCommandIntent(Intent intent) {
        registerReceiver(timeChangedReceiver, s_intentFilter);
        return super.getStartCommandIntent(intent);
    }

    private static final int ONGOING_NOTIFICATION_ID = new Random(System.currentTimeMillis()).nextInt(100);

    private void sendNotification(EventModel eventModel) {

        Intent mapIntent = new Intent(context, DetailEventActivity.class);
        mapIntent.putExtra("event",eventModel);
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mapIntent,
                PendingIntent.FLAG_ONE_SHOT);

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

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(ONGOING_NOTIFICATION_ID, notificationBuilder.build());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(timeChangedReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        Log.e("service","off");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        onDestroy();
    }
}
