package com.codepath.simpletodo;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Utils {

    /*database Priority 1,2,3 correspond to High, Medium and Low respectively
    determining the priority*/
    public static String getPriority(int priority){
        switch (priority){
            case 1:
                return Constant.PRIORITY_HIGH;
            case 2:
                return Constant.PRIORITY_MEDIUM;
            case 3:
                return Constant.PRIORITY_LOW;
        }
        return null;
    }

    /*Changing Priority from High, Medium and Low to 1,2,3*/

    public static int getPriorityInt(String priority){
        switch (priority){
            case Constant.PRIORITY_HIGH:
                return 1;
            case Constant.PRIORITY_MEDIUM:
                return 2;
            case Constant.PRIORITY_LOW:
                return 3;
        }
        return 0;
    }

    /*Display error dialog */
    public static void displayDialog(Context context, String message){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Error Dialog")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
