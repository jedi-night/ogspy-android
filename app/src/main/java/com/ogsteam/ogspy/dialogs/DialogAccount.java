package com.ogsteam.ogspy.dialogs;

import android.os.Bundle;
import android.preference.ListPreference;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyException;
import com.ogsteam.ogspy.OgspyPreferencesActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.network.download.DownloadServerConnection;
import com.ogsteam.ogspy.network.download.DownloadTask;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.ui.DialogHandler;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;

/**
 * Created by Breizh on 15/10/2014.
 */
public class DialogAccount extends DialogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);
        this.setFinishOnTouchOutside(false);
        showWaiting(false);

        final DatabaseAccountHandler accountHandler = OgspyActivity.activity.getHandlerAccount();
        final String creation = b.getString("creation");
        final String accountId = b.getString("accountId");

        final EditText user = (EditText) findViewById(R.id.newAccountUserName);
        final EditText password = (EditText) findViewById(R.id.newAccountPassword);
        final EditText serverOgspy = (EditText) findViewById(R.id.newAccountServerOgspy);

        final Spinner serversUniversList = (Spinner) findViewById(R.id.newAccountServers);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(OgspyPreferencesActivity.activity, R.array.servers, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if (serversUniversList != null && adapter != null) {
            serversUniversList.setAdapter(adapter);
        }


        Button buttonDelete = (Button) findViewById(R.id.buttonNewAccountDelete);
        if (ACCOUNT_NEW.equals(creation)) {
            setTitle("Nouveau compte");
            buttonDelete.setVisibility(Button.INVISIBLE);
        } else {
            Account account = accountHandler.getAccountById(Integer.parseInt(accountId));
            setTitle(account.getUsername() + " - " + OgspyUtils.getUniversNameFromUrl(account.getServerUnivers()));

            user.setText(account.getUsername());
            password.setText(account.getPassword());
            serverOgspy.setText(account.getServerUrl());
            serversUniversList.setSelection(OgspyUtils.getUniversPositionFromUrl(account.getServerUnivers()));

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHandler.confirm(DialogActivity.activity, "Suppression de compte", "Voulez-vous réellement supprimer le compte ?", "Annuler", "Ok",
                            new Runnable() {
                                public void run() {
                                    deleteAccount(accountHandler, accountId);
                                }
                            },
                            new Runnable() {
                                public void run() {
                                }
                            }
                    );

                }
            });
        }
        Button buttonSave = (Button) findViewById(R.id.buttonNewAccountSave);
        Button buttonCancel = (Button) findViewById(R.id.buttonNewAccountCancel);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OgspyUtils.checkUserAccount(user.getText().toString(), password.getText().toString(), serverOgspy.getText().toString())) {
                    Account account;
                    if (ACCOUNT_NEW.equals(creation)) {
                        account = new Account(user.getText().toString(), password.getText().toString(), serverOgspy.getText().toString(), getServerUrlFromSelectedPosition(serversUniversList.getSelectedItemPosition()));
                    } else {
                        account = new Account(Integer.parseInt(accountId), user.getText().toString(), password.getText().toString(), serverOgspy.getText().toString(), getServerUrlFromSelectedPosition(serversUniversList.getSelectedItemPosition()));
                    }
                    DownloadServerConnection serverConnection = new DownloadServerConnection(OgspyActivity.activity, DialogActivity.activity, account, creation);
                    DownloadTask.executeDownload(OgspyActivity.activity, serverConnection);
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteAccount(DatabaseAccountHandler accountHandler, String accountId) {
        if (accountHandler.deleteAccountById(accountId) != -1) {
            CommonUtilities.displayMessage(OgspyActivity.activity, "Le compte a été supprimé");
            OgspyPreferencesActivity.activity.refreshAcountsList((ListPreference) OgspyPreferencesActivity.activity.findPreference("prefs_accounts"));
        } else {
            new DialogHandler().showException(OgspyActivity.activity, new OgspyException("Le compte n'a pu être supprimé", Constants.EXCEPTION_DATA_DELETE));
        }
        finish();
    }

    private String getServerUrlFromSelectedPosition(int positionSelected) {
        return OgspyActivity.activity.getResources().getStringArray(R.array.servers_values)[positionSelected];
    }

}
