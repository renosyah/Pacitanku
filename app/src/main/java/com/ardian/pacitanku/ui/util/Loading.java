package com.ardian.pacitanku.ui.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ardian.pacitanku.R;

public class Loading {
    private Context c;
    private View v;
    private TextView message;

    public Loading(Context c, View v, String message) {
        this.c = c;
        this.v = v;
        this.message = v.findViewById(R.id.loading_message);
        this.message.setText(message);
    }

    public void setMessage(String m) {
        message.setText(m);
    }

    public void  setVisibility(Boolean b) {
        v.setVisibility((b ? View.VISIBLE : View.GONE));
    }
}
