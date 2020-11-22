package com.ardian.pacitanku.util;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

// ini adalah class check service
// yg memiliki fungsi-fungsi yg
// dapat digunakan untuk
// mengecheck status service pada device
public class CheckService {

    // melakukan cek status koneksi
    // dengan parameter yg dibutuhkan adalah
    // context yg didapat dari activity
    public static Boolean isInternetConnected(Context c) {

        // membuat instance konsi manajer
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        // check lagi berhasil dibuat
        // dan hasilnya bukan null maka
        if (connectivityManager !=null)

            // jika koneksi internet yg digunakan adalah
            // data atau wifi aktif maka
            // balikkan nilai true
            if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) {
                return true;
        }

        // jika tidak
        // maka false
        return false;
    }

    // simpel fungsi untuk mengecek apakah
    // service sedang running
    // dengan memberikan nama class service
    public static boolean isMyServiceRunning(Context c,Class<?> s) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (s.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
