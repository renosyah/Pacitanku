package com.ardian.pacitanku.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.util.Unit;

public class DeleteDialog {
    private Context context;
    private AlertDialog dialog;
    private Unit<String> onYes;

    public DeleteDialog(Context context, String id, Unit<String> onYes) {
        this.context = context;
        this.onYes = onYes;
        initView(id);
    }

    private void initView(String id){
        dialog = new AlertDialog.Builder(context).create();
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_delete,null);

        Button yes = v.findViewById(R.id.yes_button), no = v.findViewById(R.id.no_button);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYes.invoke(id);
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setView(v);
        dialog.setCancelable(false);
        dialog.show();
    }
}
