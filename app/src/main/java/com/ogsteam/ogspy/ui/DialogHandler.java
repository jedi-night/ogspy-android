package com.ogsteam.ogspy.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.ogsteam.ogspy.OgspyException;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.utils.helpers.Pair;

public class DialogHandler {
    public static Runnable ans_true = null;
    public static Runnable ans_false = null;

    public static boolean confirm(Activity act, String title, String confirmText, String cancelBtn, String okBtn, Runnable okProcedure, Runnable koProcedure) {
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
        if(koProcedure != null){
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ans_false.run();
                    }
                });
        }
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();
        return true;
    }

    public boolean showException(Context act, OgspyException ogse) {
        ans_true = new Runnable() {public void run() {}};
        AlertDialog dialog = new AlertDialog.Builder(act).create();
        dialog.setTitle(ogse.getTypeException().value);
        dialog.setMessage(ogse.getMessage());
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Fermer",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ans_true.run();
                    }
                });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();
        return true;
    }
}