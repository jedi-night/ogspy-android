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

    public static void showCreateAccountDialog(final Pair user, Context context) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_account);

        final EditText editTextUser = (EditText) dialog.findViewById(R.id.newAccountUserName);

        Button buttonSave = (Button) dialog.findViewById(R.id.buttonNewAccountSave);
        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonNewAccountCancel);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user.value = editTextUser.getText().toString();
                if (user.value.length() > 0) {
                    dialog.dismiss();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean showException(Activity act, OgspyException ogse) {
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