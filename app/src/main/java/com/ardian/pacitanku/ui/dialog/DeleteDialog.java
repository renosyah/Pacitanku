package com.ardian.pacitanku.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.util.Unit;

// class untuk menampilkan dialog
// apakah user iingin menghapus event atau tidak
public class DeleteDialog {

    // konteks yang dipakai
    private Context context;

    // alert dialog
    private AlertDialog dialog;

    // callback pada saat yes dipilih
    private Unit<String> onYes;

    // konstruktor
    public DeleteDialog(Context context, String id, Unit<String> onYes) {
        this.context = context;
        this.onYes = onYes;

        // init view
        initView(id);
    }

    // fungsi init view
    private void initView(String id){

        // inisialisasi dialog
        dialog = new AlertDialog.Builder(context).create();

        // buat view
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_delete,null);

        // buat tombol yes dan no
        Button yes = v.findViewById(R.id.yes_button), no = v.findViewById(R.id.no_button);

        // saat ditekan
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // invoke callback
                onYes.invoke(id);

                // tutup dialog
                dialog.dismiss();
            }
        });

        // saat diklik
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // tutup dialog
                dialog.dismiss();
            }
        });

        // set view dialog
        dialog.setView(v);

        // tidak boleh tutup otomatis
        dialog.setCancelable(false);

        // tampilkan
        dialog.show();
    }
}
