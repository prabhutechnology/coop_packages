package com.prabhutech.prabhupackages.wallet.views;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prabhutech.prabhupackages.R;

public class Toast {
    public static void show(Activity activity, String message) {

        View layout = activity.getLayoutInflater()
                .inflate(R.layout.toast_layout, (ViewGroup)activity.findViewById(R.id.root));
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        android.widget.Toast toast = new android.widget.Toast(activity);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void show(Context context, String message,Boolean quick){
        View layout = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.toast_layout, ((Activity)context).findViewById(R.id.root));
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setGravity(Gravity.BOTTOM,0, 100);
        if (quick){
            toast.setDuration(android.widget.Toast.LENGTH_SHORT);
        }else {
            toast.setDuration(android.widget.Toast.LENGTH_LONG);
        }
        toast.setView(layout);
        toast.show();
    }


    public static void show(Context context, String message) {

        View layout = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.toast_layout, ((Activity)context).findViewById(R.id.root));
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
