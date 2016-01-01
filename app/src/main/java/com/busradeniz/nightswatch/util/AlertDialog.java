package com.busradeniz.nightswatch.util;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by busradeniz on 29/12/15.
 */
public class AlertDialog {

    public static   void showAlertWithPositiveButton(Context context , String title ,  String message ){
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }


}
