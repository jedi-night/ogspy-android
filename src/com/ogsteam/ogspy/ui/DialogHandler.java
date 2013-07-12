package com.ogsteam.ogspy.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogHandler {
    public Runnable ans_true = null;
    public Runnable ans_false = null;

    public boolean confirm(Activity act, String title, String confirmText, String cancelBtn, String okBtn, Runnable okProcedure, Runnable koProcedure) {
        ans_true = okProcedure;
        ans_false= koProcedure;
        AlertDialog dialog = new AlertDialog.Builder(act).create();
        dialog.setTitle(title);
        dialog.setMessage(confirmText);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, okBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ans_true.run();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ans_false.run();
                    }
                });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();
        return true;
    }
}